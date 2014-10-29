
#include <iostream>
#include <string>

#include "rocksdb/merge_operator.h"
#include "rocksdb/slice.h"
#include "rocksdb_protobuf/protobuf_merge.h"
#include "rocksdb_protobuf/operator_factory.h"

#include "prototest.pb.h"

/* prepare the data for a merge operation
   and call the merge operator */
template <class T>
T merge(std::shared_ptr<ProtobufMergeOperator> merge_operator,
        T t_existing_value,
        T t_value) {
  std::string s_existing_value;
  std::string s_value;
  t_existing_value.SerializeToString(&s_existing_value);
  t_value.SerializeToString(&s_value);

  rocksdb::Slice key("");
  rocksdb::Slice existing_value(s_existing_value);
  rocksdb::Slice value(s_value);

  std::string *new_value = new std::string();

  merge_operator->Merge(key,
                        &existing_value,
                        value,
                        new_value,
                        NULL);

  T result;
  result.ParseFromString(*new_value);
  return result;
}

/* test a merge operator and verify that the returned list
   has the expected number of elements */
void test_mergecap_operator(std::shared_ptr<ProtobufMergeOperator> merge_operator,
                   int expected_count) {
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

  merged = merge<Type1>(merge_operator,
                              message,
                              delta);
  assert(merged.text12_size() == expected_count);
  assert(merged.subtype11().text112_size() == expected_count);

  std::cout << "result:" << std::endl;
  std::cout << merged.DebugString() << std::endl;
}

/* test a merge operator and verify that the returned sum
   has the expected value*/
void test_mergesum_operator(std::shared_ptr<ProtobufMergeOperator> merge_operator,
                            int expected_sum, bool second_operand) {
  Type1 message;
  Type1 delta;
  Type1 merged;

  message.mutable_subtype11()->set_int113(2);
  if (second_operand) {
    delta.mutable_subtype11()->set_int113(1);
  }

  std::cout << "message:" << std::endl;
  std::cout << message.DebugString() << std::endl;
  std::cout << "delta:" << std::endl;
  std::cout << delta.DebugString() << std::endl;

  merged = merge<Type1>(merge_operator,
                              message,
                              delta);
  assert(merged.subtype11().int113() == expected_sum);

  std::cout << "result:" << std::endl;
  std::cout << merged.DebugString() << std::endl;
}

/* test a merge without loading the protobuf descriptor */
void test_simple_merge() {
  std::cout << "testing a merge without passing the descriptor" << std::endl;
  test_mergecap_operator(CreateProtobufMergeOperator(), 3);
}

/* test a MERGE_CAPPED_LIST-based merge
   test.proto specifies a limit of 2 elements
   for this list, therefore the first element
   will fall through */
void test_mergecap_merge() {
  std::cout << "testing a MERGE_CAPPED_LIST merge with cap 2" << std::endl;
  test_mergecap_operator(get_operator<Type1>(), 2);
}

void test_mergesum_merge() {
 std::cout << "testing a MERGE_SUMMABLE merge" << std::endl;
 test_mergesum_operator(get_operator<Type1>(), 3, true);
}

void test_mergesum2_merge() {
 std::cout << "testing a MERGE_SUMMABLE merge without a second operand" << std::endl;
 test_mergesum_operator(get_operator<Type1>(), 2, false);
}

int main() {
  test_simple_merge();
  test_mergecap_merge();
  test_mergesum_merge();
  test_mergesum2_merge();

  return 0;
}
