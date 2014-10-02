
#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include <string>
#include <memory>

#include "rocksdb_protobuf/org_rocksdbprotobuf_ProtobufMergeOperator.h"
#include "rocksdb_protobuf/protobuf_merge.h"
#include "rocksjni/portal.h"
#include "rocksdb/db.h"
#include "rocksdb/options.h"
#include "rocksdb/statistics.h"
#include "rocksdb/memtablerep.h"
#include "rocksdb/table.h"
#include "rocksdb/slice_transform.h"
#include "rocksdb/merge_operator.h"
#include "utilities/merge_operators.h"

/*
 * Class:     org_rocksdb_ProtobufMergeOperator
 * Method:    newMergeOperatorHandle
 * Signature: ()J
 */
jlong Java_org_rocksdbprotobuf_ProtobufMergeOperator_newMergeOperatorHandleImpl(JNIEnv *env, jobject jobj, jbyteArray file_descriptor_set_bytes) {
  std::shared_ptr<rocksdb::MergeOperator> *op = new std::shared_ptr<rocksdb::MergeOperator>();
  const jbyte *buffer = env->GetByteArrayElements(file_descriptor_set_bytes, nullptr);
  jsize length = env->GetArrayLength(file_descriptor_set_bytes);
  std::string file_descriptor_set_string((const char*) buffer, (size_t) length);
  *op = CreateProtobufMergeOperator(file_descriptor_set_string);
  return reinterpret_cast<jlong>(op);
}

