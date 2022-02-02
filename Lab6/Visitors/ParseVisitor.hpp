//
// Created by natallem on 12/11/21.
//

#ifndef CALCULATOR_PARSEVISITOR_HPP
#define CALCULATOR_PARSEVISITOR_HPP


#include <stack>
#include <memory>
#include <vector>
#include "TokenVisitor.hpp"
#include "../Tokens/Token.hpp"

class ParseVisitor : TokenVisitor {
public:
    void visit(const NumberToken &token) override;

    void visit(const BraceToken &token) override;

    void visit(const OperationToken &token) override;

    const std::vector<std::unique_ptr<Token>> &GetTokens() const;

    void ParseTokens(const std::vector<std::unique_ptr<Token>> &tokens);

private:
    std::stack<std::unique_ptr<Token>> exprStack;
    std::vector<std::unique_ptr<Token>> output;

    void pushToOutput();
};


#endif //CALCULATOR_PARSEVISITOR_HPP
