
#include "rocksdb_protobuf/protobuf_logger.h"
#include <iostream>

namespace logger{
  void info(std::string message) {
    std::cout << message << std::endl;
  }

  void debug(std::string message) {
#if DEBUG
    std::cout << message << std::endl;
#endif
  }

  void debug(std::string name, google::protobuf::Message *message) {
#if DEBUG
    std::cout << name << " typename:" << message->GetTypeName() << std::endl;
    std::cout << name << " start:" << std::endl;
    std::cout << message->DebugString();
    std::cout << name << " end:" << std::endl;
#endif
  }
}
