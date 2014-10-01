#include <iostream>
#include <cstdio>
#include <string>

#include "rocksdb/db.h"
#include "rocksdb/slice.h"
#include "rocksdb/options.h"

#include "rocksdb_protobuf/operator_factory.h"
#include "prototest.pb.h"

using namespace rocksdb;

std::string kDBPath = "/tmp/rocksdbprotobuf_database_test";

int main() {

  std::cout << "Testing rocksdb protobuf_merge_operator integration..." << std::endl;

  /* Create protobuf messages */
  Type1 message;
  Type1 delta;
  Type1 merged;

  message.add_text12("a");
  message.add_text12("b");
  message.mutable_subtype11()->add_text112("a");
  message.mutable_subtype11()->add_text112("b");
  delta.add_text12("c");
  delta.mutable_subtype11()->add_text112("c");

  std::cout << "message:" << std::endl;
  std::cout << message.DebugString() << std::endl;
  std::cout << "delta:" << std::endl;
  std::cout << delta.DebugString() << std::endl;

  /* Open a database and merge two messages */
  DB* db;
  Options options;
  // Optimize RocksDB. This is the easiest way to get RocksDB to perform well
  options.IncreaseParallelism();
  options.OptimizeLevelStyleCompaction();
  // create the DB if it's not already present
  options.create_if_missing = true;
  options.merge_operator = get_operator<Type1>();

  // open DB
  Status s = DB::Open(options, kDBPath, &db);
  assert(s.ok());

  // Put key-value
  s = db->Put(WriteOptions(), "key", message.SerializeAsString());
  assert(s.ok());
  // Merge delta
  s = db->Merge(WriteOptions(), "key", delta.SerializeAsString());
  assert(s.ok());
  std::string value;
  // get value
  s = db->Get(ReadOptions(), "key", &value);
  assert(s.ok());

  delete db;

  merged.ParseFromString(value);
  std::cout << "result:" << std::endl;
  std::cout << merged.DebugString() << std::endl;

  return 0;
}
