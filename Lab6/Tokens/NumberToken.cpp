//
// Created by natallem on 12/10/21.
//

#include "NumberToken.hpp"

NumberToken::NumberToken(int value) : value(value) {

}

std::string NumberToken::toString() const {
    return std::to_string(value);
}

void NumberToken::accept(TokenVisitor &v) {
    v.visit(*this);
}

int NumberToken::getValue() const {
    return value;
}
