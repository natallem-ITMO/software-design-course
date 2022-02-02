//
// Created by natallem on 12/11/21.
//

#ifndef CALCULATOR_CALCVISITOR_HPP
#define CALCULATOR_CALCVISITOR_HPP


#include <memory>
#include <vector>
#include <stack>
#include "TokenVisitor.hpp"
#include "../Tokens/Token.hpp"

class CalcVisitor : TokenVisitor {
public:
    int CalcTokens(const std::vector<std::unique_ptr<Token>> &tokens);

private:
    void visit(const NumberToken &token) override;

    void visit(const BraceToken &token) override;

    void visit(const OperationToken &token) override;

    int getOperand();

    std::stack<int> exprStack;
};


#endif //CALCULATOR_CALCVISITOR_HPP
