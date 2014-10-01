## RocksDB_Protobuf: RocksDB Merge Operator implementation for Protobuf objects

RocksDB_Protobuf is developed by the Turn Platform Runtime Group. It
is an extension for RocksDB, developed at Facebook, which in turn is based on
LevelDB, developed at Google. Protobuf is a data representation
developed at Google.

RocksDB offers the possibility of doing appends to existing key values
efficiently through the use of a merge operator. A merge operator is a
user-provided callback that knows how the merge the old value ("the
message") and the new value ("the delta") in order into a single value
("the merged value").

Protobuf has its own built-in merge operation for combining two values
of the same protobuf type into a single new value in a meaningful
way. As long as your data is stored as protobuf objects, it should be
possible to apply a single predefined merge operator in order to take
advantage of the RocksDB merge operation. For example, let's assume
that you define messages with the following protobuf structure:

```
message Type1 {
  message Subtype11 {
    optional string text111 = 1;
    repeated string text112 = 2;
  }

  optional Subtype11 subtype11 = 1;
  repeated string text12 = 2;
  optional string text13 = 3;
}
```

You should be able to create a new RocksDB database, pass it as an
option the generic Protobuf merge operator and write and read Protobuf
objects while the merging is happening in the background. The
following C++ code snippet does just this:



```C++
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

  /* Open a database and merge two messages */
  DB* db;
  Options options;
  options.merge_operator = get_operator<Type1>();
  Status s = DB::Open(options, kDBPath, &db);

  db->Put(WriteOptions(), "key", message.SerializeAsString());
  db->Merge(WriteOptions(), "key", delta.SerializeAsString());
  std::string value;
  s = db->Get(ReadOptions(), "key", &value);
  merged.ParseFromString(value);
  std::cout << merged.DebugString() << std::endl;
```

You get the following output:

```
subtype11 {
text112: "a"
text112: "b"
text112: "c"
}
text12: "a"
text12: "b"
text12: "c"
```

You might prefer Java, in which case your code should look like:

```Java
	Type1.Subtype11 message_submessage = Type1.Subtype11.newBuilder()
			.addText112("a")
			.addText112("b")
			.build();
	Type1.Subtype11 delta_submessage = Type1.Subtype11.newBuilder()
			  .addText112("c")
			  .build();
    Type1 message = Type1.newBuilder()
			.addText12("a")
			.addText12("b")
			.setSubtype11(message_submessage)
			.build();
	Type1 delta = Type1.newBuilder()
			.addText12("c")
			.setSubtype11(delta_submessage)
			.build();

    Options opt = new Options();
    ProtobufMergeOperator protobufMergeOperator = new ProtobufMergeOperator(Type1.class);
    opt.setMergeOperator(protobufMergeOperator);

    RocksDB db = RocksDB.open(opt, db_path_string);

    db.put("key".getBytes(), message.toByteArray());
    db.merge("key".getBytes(), delta.toByteArray());

    byte[] value = db.get("key".getBytes());

	Type1 new_message = Type1.parseFrom(new String(value);
	System.out.println(new_message.toString());
```

The result should be the same.

In some cases you do not want merged lists to grow unbounded for a
particular key. You can use an extension in the Protobuf descriptor in
order to set a cap on the number of elements preserved for the field:

```
// This is where the merge extensions are defined
import "merge.proto";

message Type1 {
  message Subtype11 {
    optional string text111 = 1;
    repeated string text112 = 2 [(merge_type) = MERGE_CAPPED_LIST, (merge_cap) = 2];
  }

  optional Subtype11 subtype11 = 1;
  repeated string text12 = 2 [(merge_type) = MERGE_CAPPED_LIST, (merge_cap) = 2];
  optional string text13 = 3;
}
```

The merge operator constructor is aware of the type of the object to
be merged. It will fetch the Protobuf descriptor of the object and the
descriptors of all the dependency files and pass them down to the
protobuf merge function. The function starts looking at the field
extensions when procesing objects after merge and, in this case, drops
the oldest entries in order to keep the number of entries under the
cap. The code remains the same, all that differs is the protobuf
object definition.

Runnin the two snippets, we see that for both fields the cap of 2 is
in place. The merged object will be:
```
subtype11 {
  text112: "b"
    text112: "c"
    }
    text12: "b"
    text12: "c"
```
