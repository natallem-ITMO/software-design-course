//
// Created by natallem on 12/10/21.
//

#include "BraceToken.hpp"

BraceToken::BraceToken(char brace) : brace(brace) {}

std::string BraceToken::toString() const {
    std::string str;
    str += brace;
    return str;
}

void BraceToken::accept(TokenVisitor &v) {
    v.visit(*this);
}

bool BraceToken::isOpen() const {
    return brace == '(';
}
