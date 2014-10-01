
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

## Install preqrequisites

Install RocksdDB and RocksDB_Protobuf dependencies:

* **Ubuntu 14.04**
  * Install dependencies
```
sudo apt-get install libprotobuf-dev libsnappy-dev zlib1g-dev libbz2-dev libgflags-dev
```
   *You might need to upgrade gcc to get C++ 11 support!

3. Make the library and test it

* This will create the library:
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
