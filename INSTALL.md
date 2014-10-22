
## Getting the source

We use the RocksDB Java merge operator extensions, which
are not yet in the RocksDB master. To get them, you can clone
my RocksDB for with:
```
git clone https://github.com/vladb38/rocksdb.git
```
I assume you have cloned RocksdDB_Protobuf, but just in case
you still need to clone it:
```
git clone https://github.com/vladb38/rocksdb_protobuf.git
```
* Go to the RocksDB_Protobuf folder and Link your RocksDB source folder here
```
cd rocksdb_protobuf 
ln -s <your RocksDB source folder> .
```

## Install prerequisites

Install RocksdDB and RocksDB_Protobuf dependencies:

* **Ubuntu 14.04**
```
sudo apt-get install protobuf-compiler libprotobuf-dev libsnappy-dev zlib1g-dev libbz2-dev libgflags-dev
```
   You might need to upgrade gcc to get C++ 11 support!

GCC 4.9 in Ubuntu 14.04:
```
sudo add-apt-repository ppa:ubuntu-toolchain-r/test
apt-get update -y
apt-get install -y g++-4.9
```
To use g++4.9 by default:
```
export CXX=g++4.9
```

## Compile RocksDB as static

* If your runtime machines do not have a new enough version of the C++ standard library then it can be linked statically.
The correct switches can be set in CXXFLAGS, and both the RocksDB and RocksDB_protobuf Makefiles will use them. RocksDB also
needs to be built with these flags.
```
export CXXFLAGS="-static-libstdc++ -static-libgcc -fPIC"
```

* Go to the RocksDB source folder do
```
make static_lib
make rocksdbjavastatic
```

## Compile

* Go to the RocksDB_Protobuf source folder.
* This will compile the library:
```
make protogen
make library
```
* This will run the C++ tests:
```
make test
```
* This will run the Java tests:
```
make test-java
```
