//
// Created by natallem on 12/11/21.
//

#ifndef CALCULATOR_CALCULATOR_HPP
#define CALCULATOR_CALCULATOR_HPP

#include <optional>
#include <utility>
#include <iostream>
#include "Context.hpp"
#include "../Visitors/ParseVisitor.hpp"
#include "../Visitors/PrintVisitor.hpp"
#include "../Visitors/CalcVisitor.hpp"

class Calculator {
public:
    explicit Calculator(std::ostream &ostream, std::string input);

    int calculate_expression();

private:
    Context context;
    ParseVisitor parseVisitor;
    PrintVisitor printVisitor;
    CalcVisitor calcVisitor;
};


#endif //CALCULATOR_CALCULATOR_HPP
