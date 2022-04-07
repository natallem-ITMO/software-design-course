//
// Created by natallem on 12/11/21.
//

#ifndef CALCULATOR_PRINTVISITOR_HPP
#define CALCULATOR_PRINTVISITOR_HPP


#include <memory>
#include <vector>
#include "TokenVisitor.hpp"
#include "../Tokens/Token.hpp"

class PrintVisitor : TokenVisitor {

public:

    explicit PrintVisitor(std::ostream &ostream);

    void PrintTokens(const std::vector<std::unique_ptr<Token>> &tokens);

private:

    void visit(const NumberToken &token) override;

    void visit(const BraceToken &token) override;

    void visit(const OperationToken &token) override;


private:
    std::ostream &ostream;
};


#endif //CALCULATOR_PRINTVISITOR_HPP
