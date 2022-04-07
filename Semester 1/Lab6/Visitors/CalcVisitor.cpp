//
// Created by natallem on 12/11/21.
//

#include <unordered_set>
#include "CalcVisitor.hpp"

#include "../Tokens/NumberToken.hpp"
#include "../Tokens/BraceToken.hpp"
#include "../Tokens/OperationToken.hpp"

int CalcVisitor::CalcTokens(const std::vector<std::unique_ptr<Token>> &tokens) {
    for (const auto &token: tokens) {
        token->accept(*this);
    }
    if (exprStack.empty()) {
        throw std::invalid_argument("Cannot calculate result. No values.");
    }
    if (exprStack.size() != 1) {
        throw std::invalid_argument("Cannot calculate result. Incorrect numbers in input.");
    }
    return exprStack.top();
}

void CalcVisitor::visit(const BraceToken &token) {
    throw std::invalid_argument("Cannot calculate result. No braces allowed");
}

void CalcVisitor::visit(const OperationToken &token) {
    if (exprStack.size() < 2) {
        throw std::invalid_argument("Cannot calculate result. Not enough arguments in stack");
    }
    int secondOperand = getOperand();
    int firstOperand = getOperand();
    int res = token.calcResult(firstOperand, secondOperand);
    exprStack.push(res);
}

void CalcVisitor::visit(const NumberToken &token) {
    exprStack.push(token.getValue());
}

int CalcVisitor::getOperand() {
    int res = exprStack.top();
    exprStack.pop();
    return res;
}
