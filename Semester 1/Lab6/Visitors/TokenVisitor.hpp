//
// Created by natallem on 12/11/21.
//

#ifndef CALCULATOR_TOKENVISITOR_HPP
#define CALCULATOR_TOKENVISITOR_HPP

class NumberToken;

class BraceToken;

class OperationToken;

class TokenVisitor {
public:
    virtual void visit(const NumberToken &) = 0;

    virtual void visit(const BraceToken &) = 0;

    virtual void visit(const OperationToken &) = 0;

    virtual ~TokenVisitor() = default;
};

#endif //CALCULATOR_TOKENVISITOR_HPP
