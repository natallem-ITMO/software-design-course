//
// Created by natallem on 12/11/21.
//

#include <cctype>

#include "StartState.hpp"
#include "NumberState.hpp"
#include "../Common/Context.hpp"
#include "../Tokens/BraceToken.hpp"
#include "../Tokens/OperationToken.hpp"

void StartState::ProcessChar(char ch, int pos) {
    if (std::isblank(ch)) {
        return;
    } else if (std::isdigit(ch)) {
        context->TransitionTo(std::make_unique<NumberState>());
    } else {
        switch (ch) {
            case '\0':
                return;
            case ')':
            case '(':
                context->CreateToken(std::make_unique<BraceToken>(ch));
                return;
            case '*':
            case '/':
            case '-':
            case '+':
                context->CreateToken(std::make_unique<OperationToken>(ch));
                return;
            default: {
                std::string messageError = "Cannot parse char ";
                messageError += ch;
                messageError += " at position" + std::to_string(pos);
                throw std::invalid_argument(messageError);
            }
        }
    }
}
