//
// Created by natallem on 12/10/21.
//

#ifndef CALCULATOR_CONTEXT_HPP
#define CALCULATOR_CONTEXT_HPP


#include <memory>
#include <vector>
#include "../States/State.hpp"
#include "../Tokens/Token.hpp"

class Context {
public:

    explicit Context(std::string input);

    void TransitionTo(std::unique_ptr<State> &&new_state, bool need_to_continue = true);

    void CreateToken(std::unique_ptr<Token> &&token_ptr);

    void ParseInput();

    const std::vector<std::unique_ptr<Token>> &GetTokens() const;

    Context &operator=(const Context &) = delete;

    Context &operator=(Context &&other) = delete;

    Context(Context &) = delete;

    Context(Context &&) = delete;

private:
    std::unique_ptr<State> state;
    std::string input;
    int pos = 0;
    std::vector<std::unique_ptr<Token>> tokens;

    void ProcessChar(char ch, int pos);

    bool Parse();
};

#endif //CALCULATOR_CONTEXT_HPP
