

#include "rocksdb_protobuf/protobuf_merge.h"
#include "rocksdb_protobuf/protobuf_logger.h"

#include <iostream>
#include <google/protobuf/dynamic_message.h>
#include <google/protobuf/descriptor.pb.h>

#include "merge.pb.h"

thread_local bool ProtobufMergeOperator::thread_data_initialized;
thread_local google::protobuf::Message *ProtobufMergeOperator::message;
thread_local google::protobuf::Message *ProtobufMergeOperator::delta;

ProtobufMergeOperator::ProtobufMergeOperator() {
  message = new MessageBase();
  delta = new MessageBase();
};

ProtobufMergeOperator::ProtobufMergeOperator(const std::string &file_descriptor_set_proto) {
  google::protobuf::FileDescriptorSet file_descriptor_set;
  file_descriptor_set.ParseFromString(file_descriptor_set_proto);
  const google::protobuf::FileDescriptor *file_descriptor;
  for (auto file_descriptor_proto : file_descriptor_set.file()) {
    file_descriptor = descriptor_pool.BuildFile(file_descriptor_proto);
  }
  /* Check that we have at least one message descriptor loaded
     and create a generic message of the first type described
     in the protobuf file */
  if (file_descriptor->message_type_count() == 0) {
    logger::info("No top-level message found in the descriptor!");
    return;
  }
  const google::protobuf::Descriptor *message_descriptor =
    file_descriptor->message_type(0);
  google::protobuf::DynamicMessageFactory *dynamic_message_factory =
    new google::protobuf::DynamicMessageFactory(&descriptor_pool);
  proto_message = dynamic_message_factory->GetPrototype(message_descriptor);
}

bool ProtobufMergeOperator::Merge(const rocksdb::Slice& key,
                                  const rocksdb::Slice* existing_value,
                                  const rocksdb::Slice& value,
                                  std::string* new_value,
                                  rocksdb::Logger* logger) const {
  if (!thread_data_initialized) {
    InitializeThreadData();
  }
  // Clear the *new_value for writing.
  assert(new_value);
  new_value->clear();
  if (existing_value) {
    message->ParseFromArray(existing_value->data(), existing_value->size());
    delta->ParseFromArray(value.data(), value.size());
    Sum(message, delta);
    message->MergeFrom(*delta);
  } else {
    message->ParseFromArray(value.data(), value.size());
  }
  Trim(message);
  message->AppendToString(new_value);
  return true;
}

void ProtobufMergeOperator::InitializeThreadData() const {
  if (!thread_data_initialized) {
    if (proto_message) {
      message = proto_message->New();
      delta = proto_message->New();
    } else {
      message = new MessageBase();
      delta = new MessageBase();
    }
  }
}

/* Sum summable fields in protobuf messages before merge.
   If the field appears in both messages, we compute its
   sum value and then write it in the new message, since
   the new message entry will overwrite the old message one.
*/
void ProtobufMergeOperator::Sum(google::protobuf::Message *message,
                                google::protobuf::Message *new_message) const {
  std::vector <const google::protobuf::FieldDescriptor * > fields;
  const google::protobuf::Reflection* reflection =
    message->GetReflection();
  const google::protobuf::Reflection* new_reflection =
    new_message->GetReflection();
  reflection->ListFields(*message, &fields);
  for (auto field_descriptor : fields) {
    // Trim first the sub-messages
    if (field_descriptor->type() ==
        google::protobuf::FieldDescriptor::TYPE_MESSAGE) {
      google::protobuf::Message *sub_message =
        reflection->MutableMessage(message, field_descriptor);
      google::protobuf::Message *new_sub_message =
        new_reflection->MutableMessage(new_message, field_descriptor);
      Sum(sub_message, new_sub_message);
    }
    // Sum then the marked fields
    const google::protobuf::FieldOptions field_options =
      field_descriptor->options();
    if (field_options.HasExtension(merge_type)) {
      switch (field_options.GetExtension(merge_type)) {
      case MergeMessage_MergeType_MERGE_SUMMABLE:
        switch (field_descriptor->type()) {
        case google::protobuf::FieldDescriptor::TYPE_DOUBLE:
          reflection->SetDouble(new_message, field_descriptor,
                               reflection->GetDouble(*message, field_descriptor) +
                               reflection->GetDouble(*new_message, field_descriptor));
          break;
        case google::protobuf::FieldDescriptor::TYPE_INT32:
          reflection->SetInt32(new_message, field_descriptor,
                               reflection->GetInt32(*message, field_descriptor) +
                               reflection->GetInt32(*new_message, field_descriptor));
          break;
        case google::protobuf::FieldDescriptor::TYPE_INT64:
          reflection->SetInt64(new_message, field_descriptor,
                               reflection->GetInt64(*message, field_descriptor) +
                               reflection->GetInt64(*new_message, field_descriptor));
          break;
        case google::protobuf::FieldDescriptor::TYPE_FLOAT:
          reflection->SetFloat(new_message, field_descriptor,
                               reflection->GetFloat(*message, field_descriptor) +
                               reflection->GetFloat(*new_message, field_descriptor));
          break;
        case google::protobuf::FieldDescriptor::TYPE_UINT32:
          reflection->SetUInt32(new_message, field_descriptor,
                               reflection->GetUInt32(*message, field_descriptor) +
                               reflection->GetUInt32(*new_message, field_descriptor));
          break;
        case google::protobuf::FieldDescriptor::TYPE_UINT64:
          reflection->SetUInt64(new_message, field_descriptor,
                               reflection->GetUInt64(*message, field_descriptor) +
                               reflection->GetUInt64(*new_message, field_descriptor));
          break;
        default:
          break;
        }
      default:
        break;
      }
    }
  }
}


/* Trim a protobuf message according to per-field caps */
void ProtobufMergeOperator::Trim(google::protobuf::Message *message) const {
  std::vector <const google::protobuf::FieldDescriptor * > fields;
  const google::protobuf::Reflection* reflection =
    message->GetReflection();
  reflection->ListFields(*message, &fields);
  for (auto field_descriptor : fields) {
    // Trim first the sub-messages
    if (field_descriptor->type() ==
        google::protobuf::FieldDescriptor::TYPE_MESSAGE) {
      google::protobuf::Message *sub_message =
        reflection->MutableMessage(message, field_descriptor);
      Trim(sub_message);
    }
    // Trim then the marked fields
    const google::protobuf::FieldOptions field_options =
      field_descriptor->options();
    if (field_options.HasExtension(merge_type)) {
      switch (field_options.GetExtension(merge_type)) {
      case MergeMessage_MergeType_MERGE_CAPPED_LIST:
        // trim a capped list
        if (field_options.HasExtension(merge_cap)) {
          uint32_t cap = field_options.GetExtension(merge_cap);
          uint32_t start_size = reflection->FieldSize(*message, field_descriptor);
          if (start_size > cap) {
            // shift the elements forward, push the old ones back
            for (uint32_t i = 0; i < cap; i++)
              reflection->SwapElements(message, field_descriptor,
                                       i, (start_size-cap)+i);
            // remove the old elements
            for (uint32_t i = cap; i < start_size; i++)
              reflection->RemoveLast(message,
                                     field_descriptor);
          }
        }
        break;
      default:
        break;
      }
    }
  }
}

const char* ProtobufMergeOperator::Name() const {
  return "Protobufrocksdb::MergeOperator";
}

std::shared_ptr<ProtobufMergeOperator> CreateProtobufMergeOperator() {
  return std::make_shared<ProtobufMergeOperator>();
}

std::shared_ptr<ProtobufMergeOperator> CreateProtobufMergeOperator(const std::string &file_descriptor_set_proto) {
  return std::make_shared<ProtobufMergeOperator>(file_descriptor_set_proto);
}
