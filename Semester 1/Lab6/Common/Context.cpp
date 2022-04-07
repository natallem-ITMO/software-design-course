//
// Created by natallem on 12/10/21.
//

#include "Context.hpp"
#include "../States/StartState.hpp"

Context::Context(std::string input) : input(std::move(input)) {
    this->TransitionTo(std::make_unique<StartState>(), false);
}

void Context::TransitionTo(std::unique_ptr<State> &&new_state, bool need_to_continue) {
    state = std::move(new_state);
    state->setContext(this);
    if (need_to_continue) {
        ProcessChar(input[pos], pos);
    }
}

void Context::CreateToken(std::unique_ptr<Token> &&token_ptr) {
    tokens.push_back(std::move(token_ptr));
}

bool Context::Parse() {
    if (pos >= input.size()) {
        ProcessChar('\0', pos);
        return true;
    }
    unsigned char cur_char = input[pos];
    ProcessChar(cur_char, pos);
    ++pos;
    return false;
}

void Context::ProcessChar(char ch, int pos) {
    state->ProcessChar(ch, pos);
}

const std::vector<std::unique_ptr<Token>> &Context::GetTokens() const {
    return tokens;
}

void Context::ParseInput() {
    while (!Parse()) {}
}

