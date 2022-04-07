//
// Created by natallem on 12/10/21.
//

#include <stdexcept>
#include "OperationToken.hpp"
#include "../Visitors/TokenVisitor.hpp"

OperationToken::OperationToken(char operation) : operation(operation) {}

std::string OperationToken::toString() const {
    std::string res;
    res += operation;
    return res;
}

void OperationToken::accept(TokenVisitor &v) {
    v.visit(*this);
}

int OperationToken::calcResult(int a, int b) const {
    switch (operation) {
        case '-':
            return a - b;
        case '+':
            return a + b;
        case '/' :
            return a / b;
        case '*':
            return a * b;
        default:
            throw std::invalid_argument("Unknown operation. Cannot calculate result.");
    }
}
