// A test for the merge operator
// @author vbalan
package org.rocksdbprotobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Collections;
import org.rocksdb.*;
import org.rocksdbprotobuf.*;
import org.rocksdbprotobuf.proto.Prototest.*;

public class RocksDBProtobufTest extends TestCase {
  static final String db_path_string = "/tmp/rocksdbprotobuftest";

  static {
    RocksDB.loadLibrary();
	System.loadLibrary("rocksdbprotobufjni");
  }

  public RocksDBProtobufTest(String testName) {
    super(testName);
  }

  public static void testProtobufMergeOperator()
      throws InterruptedException, RocksDBException, InvalidProtocolBufferException {

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

    System.out.println("Testing ProtbufMergeOperator with capped list ===");

    Options opt = new Options();
    ProtobufMergeOperator protobufMergeOperator = new ProtobufMergeOperator(Type1.class);
    opt.setMergeOperator(protobufMergeOperator);
    opt.setCreateIfMissing(true);

    RocksDB db = RocksDB.open(opt, db_path_string);

    System.out.println("Writing message...");
	System.out.println(message.toString());
    db.put("key".getBytes(), message.toByteArray());

    System.out.println("Writing delta...");
	System.out.println(delta.toString());
    db.merge("key".getBytes(), delta.toByteArray());

    byte[] value = db.get("key".getBytes());
    String strValue = new String(value);

	Type1 new_message = Type1.parseFrom(value);

    System.out.println("Retrieved value:");
	System.out.println(new_message.toString());

    db.close();
    opt.dispose();

    System.out.println("ProtobufMergeOperator passed!");
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite()
    {
      return new TestSuite( RocksDBProtobufTest.class );
    }

}
