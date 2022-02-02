#include <iostream>
#include <memory>
#include <cassert>
#include <sstream>
#include "Common/Calculator.hpp"

void one_incorrect_test(const std::string &input) {
    bool exceptionThrown = false;
    try {
        std::stringstream ss;
        Calculator d1(ss, input);
        d1.calculate_expression();
    }
    catch (const std::invalid_argument &exp)
    {
        exceptionThrown = true;
    }
    assert(exceptionThrown);
}

void test_incorrect_input() {
    one_incorrect_test("(5+)2d");
    one_incorrect_test("i");
    one_incorrect_test("s");
    one_incorrect_test(" *#");
    one_incorrect_test("в");
    one_incorrect_test("(5");
    one_incorrect_test("(5))");
    one_incorrect_test("5 * ");
}

void one_correct_test(const std::string& input, const std::string & expected_output, int expected_val){
    std::stringstream ss;
    Calculator d1(ss, input);
    int value_result =  d1.calculate_expression();
    std::string parsed_result = ss.str();
    assert(parsed_result == expected_output);
    assert(value_result == expected_val);
}
void test_correct_input(){
    one_correct_test("(5+3 * 4 + 1 ) / 9 - 56", "5 3 4 * 1 + + 9 / 56 - ", -54);
    one_correct_test(" 3", "3 " , 3);
    one_correct_test(" 3     ", "3 " , 3);
    one_correct_test(" 3/1     ", "3 1 / " , 3);
    one_correct_test(" 9 - 79    ", "9 79 - " , -70);
    one_correct_test(" (((5))) ", "5 " , 5);
}

void run_tests(){
    test_correct_input();
    test_incorrect_input();
}

void run_user_input(){
    std::cout << "Input your expression:\n";
    std::string input;
    std::getline(std::cin, input);
    std::cout << "Parsed expression: ";
    Calculator d1(std::cout, input);
    int value_result =  d1.calculate_expression();
    std::cout << "\nResult of expression: " << value_result;
}
int main() {
    run_tests();
    run_user_input();
}