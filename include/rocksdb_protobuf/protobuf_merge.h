

#pragma once

#include "rocksdb/merge_operator.h"
#include "rocksdb/slice.h"

#include <vector>

#include <google/protobuf/descriptor.h>
#include <google/protobuf/dynamic_message.h>
#include "message_base.pb.h"

class ProtobufMergeOperator : public rocksdb::AssociativeMergeOperator {
public:
  ProtobufMergeOperator();
  ProtobufMergeOperator(const std::string &descriptor);
  virtual bool Merge(const rocksdb::Slice& key,
                     const rocksdb::Slice* existing_value,
                     const rocksdb::Slice& value,
                     std::string* new_value,
                     rocksdb::Logger* logger) const override;
  virtual const char* Name() const override;
  void Sum(google::protobuf::Message *message,
           google::protobuf::Message *new_message) const;
  void Trim(google::protobuf::Message *message) const;

private:
  /* The descriptor pool must outlive all allocate objects */
  google::protobuf::DescriptorPool descriptor_pool;
  /* Prototype message created by the descriptor pool */
  const google::protobuf::Message *proto_message;
  /* Message objects used during merge are declared static
     and initialized when loading the protobuf descriptor.
     This is making the assumption that we will only use
     one instance of ProtobufMergeOperator per thread.
     Otherwise we would have to use ThreadLocalPtr from
     "util/thread_local.h" in the RocksDB source code. */
  void InitializeThreadData() const;
  static thread_local bool thread_data_initialized;
  static thread_local google::protobuf::Message *message;
  static thread_local google::protobuf::Message *delta;
};

std::shared_ptr<ProtobufMergeOperator> CreateProtobufMergeOperator();
std::shared_ptr<ProtobufMergeOperator> CreateProtobufMergeOperator(const std::string &file_descriptor_set_proto);
