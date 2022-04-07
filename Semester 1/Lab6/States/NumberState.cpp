//
// Created by natallem on 12/10/21.
//

#include "NumberState.hpp"
#include "../Common/Context.hpp"
#include "../Tokens/NumberToken.hpp"
#include "StartState.hpp"

void NumberState::ProcessChar(char ch, int pos) {
    if (std::isdigit(ch)) {
        curNumber *= 10;
        curNumber += ch - '0';
    } else {
        context->CreateToken(std::make_unique<NumberToken>(curNumber));
        context->TransitionTo(std::make_unique<StartState>());
    }
}
