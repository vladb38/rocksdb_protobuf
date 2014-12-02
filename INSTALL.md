
## Compile using docker

We have discovered that compiling RocksDB_Protobuf can be a tedious
process. To avoid having too many system specific dependencies, the
best way to compile is to link statically many libraries as
possible. This prevents deployment issues, however it is not a
perfect solution. Since libc will remain outside the compiled library,
to prevent issues when starting the container you should compile on
the same operating system version on which you want to run your
database.

To ease the compilation process we have prepared a Dockerfile for
compiling RocksDB_Protobuf for CentOS 6. The Dockerfile uses Protobuf
version 2.4.1 and our own clone of the RocksDB sources and compiles
using Java 6. Feel free to change these choices (for example, switch
to Protobuf 2.6.0, RocksDB master and Java 7) by editing the Dockerfile.

I assume you have cloned RocksdDB_Protobuf, but just in case
you still need to clone it:
```
git clone https://github.com/vladb38/rocksdb_protobuf.git
```

After this step you can go to build/centos and do:
```
docker build -t rocksdb-compile .
docker run --name "rocksdb-compile-instance" rocksdb-compile
docker cp rockdb-build-instance:work/rocksdb_protobuf/target/rocksdb_protobuf-1.0-SNAPSHOT.jar .
```

## Compile from sources on your own system

We expect more issues when compiling from sources on your own system, however here are the
basic instructions:

Clone our for of the RocksDB repository:
```
git clone https://github.com/vladb38/rocksdb.git
```
I assume you have cloned RocksdDB_Protobuf, but just in case
you still need to clone it:
```
git clone https://github.com/vladb38/rocksdb_protobuf.git
```
Go to the RocksDB_Protobuf folder and Link your RocksDB source folder here
```
cd rocksdb_protobuf 
ln -s <your RocksDB source folder> .
```

Install RocksdDB and RocksDB_Protobuf dependencies:

   **Ubuntu 14.04**
```
sudo apt-get install protobuf-compiler libprotobuf-dev libsnappy-dev zlib1g-dev libbz2-dev libgflags-dev
```
   You might need to upgrade gcc to get C++ 11 support!

   GCC 4.9 in Ubuntu 14.04:
```
sudo add-apt-repository ppa:ubuntu-toolchain-r/test
sudo apt-get update -y
sudo apt-get install -y g++-4.9
```
  To use g++-4.9 by default:
```
export CXX=g++-4.9
```

If your runtime machines do not have a new enough version of the C++ standard library then it can be linked statically.
The correct switches can be set in CXXFLAGS, and both the RocksDB and RocksDB_protobuf Makefiles will use them. RocksDB also
needs to be built with these flags.
```
export CXXFLAGS="-static-libstdc++ -static-libgcc -fPIC"
```

Go to the RocksDB source folder and run:
```
make static_lib
make rocksdbjavastatic
```

Now we can compile RocksDB_Protobuf. Go to the RocksDB_Protobuf source folder and run:
```
make protogen
make library
```
   This will run the C++ tests:
```
make test
```
   This will run the Java tests:
```
make test-java
```
