//
// Created by natallem on 12/10/21.
//

#ifndef CALCULATOR_BRACETOKEN_HPP
#define CALCULATOR_BRACETOKEN_HPP

#include "Token.hpp"

class BraceToken : public Token {
public:
    explicit BraceToken(char brace);

    void accept(TokenVisitor &v) override;

    std::string toString() const override;

    bool isOpen() const;

private:
    char brace;
};


#endif //CALCULATOR_BRACETOKEN_HPP
