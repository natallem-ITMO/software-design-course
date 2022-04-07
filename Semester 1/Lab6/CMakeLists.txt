cmake_minimum_required(VERSION 3.21)
project(Calculator)

set(CMAKE_CXX_STANDARD 17)

add_executable(Calculator main.cpp Tokens/NumberToken.cpp Tokens/NumberToken.hpp Tokens/Token.hpp Tokens/BraceToken.cpp Tokens/BraceToken.hpp Tokens/OperationToken.cpp Tokens/OperationToken.hpp States/State.cpp States/State.hpp States/NumberState.cpp States/NumberState.hpp Common/Context.cpp Common/Context.hpp States/StartState.cpp States/StartState.hpp Visitors/TokenVisitor.hpp Visitors/ParseVisitor.cpp Visitors/ParseVisitor.hpp Common/Calculator.cpp Common/Calculator.hpp Visitors/PrintVisitor.cpp Visitors/PrintVisitor.hpp Visitors/CalcVisitor.hpp Visitors/CalcVisitor.cpp)
