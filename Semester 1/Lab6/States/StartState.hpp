//
// Created by natallem on 12/11/21.
//

#ifndef CALCULATOR_STARTSTATE_HPP
#define CALCULATOR_STARTSTATE_HPP

#include "State.hpp"

class StartState : public State {
public:
    void ProcessChar(char ch, int pos) override;
};


#endif //CALCULATOR_STARTSTATE_HPP
