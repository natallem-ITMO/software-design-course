//
// Created by natallem on 12/10/21.
//

#ifndef CALCULATOR_OPERATIONTOKEN_HPP
#define CALCULATOR_OPERATIONTOKEN_HPP

#include "Token.hpp"


class OperationToken : public Token {
public:
    explicit OperationToken(char operation);

    std::string toString() const override;

    void accept(TokenVisitor &v) override;

    int calcResult(int a, int b) const;

private:
    char operation;
};

#endif //CALCULATOR_OPERATIONTOKEN_HPP
