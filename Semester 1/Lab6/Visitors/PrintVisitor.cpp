//
// Created by natallem on 12/11/21.
//

#include <iostream>
#include "PrintVisitor.hpp"
#include "../Tokens/NumberToken.hpp"
#include "../Tokens/BraceToken.hpp"
#include "../Tokens/OperationToken.hpp"

PrintVisitor::PrintVisitor(std::ostream &ostream) : ostream(ostream) {}

void PrintVisitor::PrintTokens(const std::vector<std::unique_ptr<Token>> &tokens) {
    for (const auto &token: tokens) {
        token->accept(*this);
    }
}

void PrintVisitor::visit(const NumberToken &token) {
    ostream << token.toString() << ' ';
}

void PrintVisitor::visit(const BraceToken &token) {
    ostream << token.toString() << ' ';
}

void PrintVisitor::visit(const OperationToken &token) {
    ostream << token.toString() << ' ';
}

