//
// Created by natallem on 12/10/21.
//

#ifndef CALCULATOR_NUMBERSTATE_HPP
#define CALCULATOR_NUMBERSTATE_HPP

#include <cctype>
#include "State.hpp"

class NumberState : public State {
public:
    void ProcessChar(char ch, int pos) override;
private:
    int curNumber = 0;
};


#endif //CALCULATOR_NUMBERSTATE_HPP
