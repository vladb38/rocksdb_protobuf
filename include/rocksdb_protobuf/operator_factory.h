
#pragma once

#include "protobuf_merge.h"
#include <google/protobuf/descriptor.pb.h>
#include <memory>

template <class T>
std::shared_ptr<ProtobufMergeOperator> get_operator() {
  T proto_object;
  const google::protobuf::Descriptor *descriptor = proto_object.GetDescriptor();
  const google::protobuf::FileDescriptor *file_descriptor = descriptor->file();
  google::protobuf::FileDescriptorProto *file_descriptor_proto;
  google::protobuf::FileDescriptorSet file_descriptor_set;

  for (int i = file_descriptor->dependency_count()-1;
         i >= 0;
         i--) {
    const google::protobuf::FileDescriptor *dependency_file_descriptor =
      file_descriptor->dependency(i);
    file_descriptor_proto = file_descriptor_set.add_file();
    file_descriptor_proto->Clear();
    dependency_file_descriptor->CopyTo(file_descriptor_proto);
  }

  file_descriptor_proto = file_descriptor_set.add_file();
  file_descriptor_proto->Clear();
  file_descriptor->CopyTo(file_descriptor_proto);

  std::string serialized_descriptor;
  file_descriptor_set.SerializeToString(&serialized_descriptor);

  std::shared_ptr<ProtobufMergeOperator> op (new ProtobufMergeOperator(serialized_descriptor));
  return op;
}
