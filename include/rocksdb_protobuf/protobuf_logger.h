
#pragma once
#include <string>
#include <google/protobuf/message.h>

void info(std::string message);
void debug(std::string message);

void debug(std::string name, google::protobuf::Message *message);
