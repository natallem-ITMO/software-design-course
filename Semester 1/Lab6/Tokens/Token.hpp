//
// Created by natallem on 12/10/21.
//

#ifndef CALCULATOR_TOKEN_HPP
#define CALCULATOR_TOKEN_HPP

#include <string>
#include "../Visitors/TokenVisitor.hpp"

class Token {
public:
    virtual std::string toString() const = 0;

    virtual void accept(TokenVisitor &v) = 0;

    virtual ~Token() = default;
};

#endif //CALCULATOR_TOKEN_HPP
