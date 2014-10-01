
#Modify this to suit your machine
ifeq ($(ROCKSDB_BASE),)
        ROCKSDB_BASE = ./rocksdb
endif

ifeq ($(PROTOBUF_BASE),)
        PROTOBUF_BASE = /usr/include
endif

NATIVE_JAVA_CLASSES = org.rocksdbprotobuf.ProtobufMergeOperator

JAR = java/rocksdbprotobuf.jar
CLASSPATH =  java/protobuf-java-2.5.0.jar:${ROCKSDB_BASE}/java

SOURCES = src/main/cpp/protobuf_merge.cc \
	      src/main/cpp/protobuf_logger.cc \
	      proto/message_base.pb.cc \
		  proto/prototest.pb.cc \
	      proto/merge.pb.cc

JNI_SOURCES = src/main/cpp/jni/org_rocksdbprotobuf_protobuf_merge_operator.cc

TEST_OPERATOR_SOURCES = src/test/cpp/test_operator.cc
TEST_DATABASE_SOURCES = src/test/cpp/test_database.cc

INCLUDE = -I $(ROCKSDB_BASE) -I$(ROCKSDB_BASE)/include -I$(PROTOBUF_BASE)/include \
	-I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux -I./include \
	-I $(ROCKSDB_BASE)/java  -I./proto

OPT = -std=c++11 -pthread

#Compiler flags
#if mode variable is empty, setting debug build mode
ifeq ($(mode),debug)
	mode = debug
	OPT += -g -DDEBUG
else
	mode = release
endif

PLATFORM_CCFLAGS =
PLATFORM_CXXFLAGS =

WARNING_FLAGS = -Wall -Werror -Wsign-compare
CFLAGS += $(WARNING_FLAGS) -I. $(INCLUDE) $(PLATFORM_CCFLAGS) $(OPT) -fPIC
CXXFLAGS += $(WARNING_FLAGS) -I. $(INCLUDE) $(PLATFORM_CXXFLAGS) $(OPT) -Woverloaded-virtual -fPIC

LIBOBJECTS = $(SOURCES:.cc=.o)

JNILIBOBJECTS = $(LIBOBJECTS)
JNILIBOBJECTS += $(JNI_SOURCES:.cc=.o)

TEST_OPERATOR_OBJECTS = $(TEST_OPERATOR_SOURCES:.cc=.o)
TEST_DATABASE_OBJECTS = $(TEST_DATABASE_SOURCES:.cc=.o)

# The library name is configurable.
ifeq ($(LIBNAME),)
        LIBNAME=librocksdbprotobuf
endif

LIBRARY = ${LIBNAME}.so
JNILIBRARY = ${LIBNAME}jni.so

LDFLAGS = -lpthread -lrocksdb -lprotobuf -lrt -lsnappy -lgflags -lz -lbz2

all: library

protogen:
	protoc -I $(PROTOBUF_BASE) -I proto proto/*.proto --cpp_out=proto
	protoc -I $(PROTOBUF_BASE) -I proto proto/merge.proto --java_out=src/main/java
	protoc -I $(PROTOBUF_BASE) -I proto proto/prototest.proto --java_out=src/main/java

library: ${LIBRARY}

$(LIBRARY): $(LIBOBJECTS)
	rm -f $@
	$(CXX) -shared -fPIC -o $@ $(LIBOBJECTS) $(LDFLAGS)

clean:
	-rm test proto/*.pb.*
	find . -name "*~" -delete
	find . -name "*.o" -delete
	find . -name "*.class" -delete
	-rm *.a *.jar

test_operator: $(TEST_OPERATOR_OBJECTS) library
	$(CXX) $(CXXFLAGS) -o test_operator $(TEST_OPERATOR_OBJECTS) $(LIBRARY) $(LDFLAGS)
	LD_LIBRARY_PATH=. ./test_operator

test_database: $(TEST_DATABASE_OBJECTS) library
	$(CXX) $(CXXFLAGS) -o test_database $(TEST_DATABASE_OBJECTS) $(LIBRARY) $(LDFLAGS)
	LD_LIBRARY_PATH=. ./test_database

test: test_operator test_database

$(JNILIBRARY): $(JNILIBOBJECTS)
	mvn	compile
	javah -classpath target/classes -d ./include/rocksdb_protobuf -jni $(NATIVE_JAVA_CLASSES)
	rm -f $@
	$(CXX) -shared -fPIC -o $@ $(ROCKSDB_BASE)/java/librocksdbjni.so $(JNILIBOBJECTS) $(LDFLAGS)

library-java: $(JNILIBRARY)

java: library-java
	mvn package
