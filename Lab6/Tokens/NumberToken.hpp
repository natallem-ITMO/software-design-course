//
// Created by natallem on 12/10/21.
//

#ifndef CALCULATOR_NUMBERTOKEN_HPP
#define CALCULATOR_NUMBERTOKEN_HPP

#include "Token.hpp"

class NumberToken : public Token {
public:
    explicit NumberToken(int value);

    void accept(TokenVisitor &v) override;

    std::string toString() const override;

    int getValue() const;

private:
    int value;
};


#endif //CALCULATOR_NUMBERTOKEN_HPP
