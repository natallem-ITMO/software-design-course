//
// Created by natallem on 12/11/21.
//

#include "Calculator.hpp"

int Calculator::calculate_expression() {
    context.ParseInput();
    parseVisitor = ParseVisitor();
    parseVisitor.ParseTokens(context.GetTokens());
    printVisitor.PrintTokens(parseVisitor.GetTokens());
    return calcVisitor.CalcTokens(parseVisitor.GetTokens());
}

Calculator::Calculator(std::ostream &ostream, std::string input) : context(std::move(input)),
                                                                   printVisitor(ostream) {}
