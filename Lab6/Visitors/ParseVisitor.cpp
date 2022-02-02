//
// Created by natallem on 12/11/21.
//

#include <unordered_set>
#include <unordered_map>
#include "ParseVisitor.hpp"

#include "../Tokens/NumberToken.hpp"
#include "../Tokens/BraceToken.hpp"
#include "../Tokens/OperationToken.hpp"

void ParseVisitor::visit(const NumberToken &token) {
    output.emplace_back(std::make_unique<NumberToken>(token));
}

void ParseVisitor::visit(const BraceToken &token) {
    if (token.isOpen()) {
        exprStack.push(std::make_unique<BraceToken>(token));
    } else {
        bool found_open_brace = false;
        while (!exprStack.empty()) {
            auto &t = exprStack.top();
            if (auto *v = dynamic_cast<BraceToken *>(t.get())) {
                if (v->isOpen()) {
                    found_open_brace = true;
                    exprStack.pop();
                    break;
                }
            }
            pushToOutput();
        }
        if (!found_open_brace) {
            throw std::invalid_argument("Cannot parse input expression. No open brace");
        }
    }
}

void ParseVisitor::visit(const OperationToken &token) {
    static std::unordered_map<std::string, std::unordered_set<std::string>> higher_operations =
            {{"-", {"*", "/"}},
             {"+", {"-", "*", "/"}},
             {"/", {}},
             {"*", {"/"}},
            };
    std::string cur_operation = token.toString();
    while (!exprStack.empty()) {
        auto &t = exprStack.top();
        bool need_to_push = false;
        if (auto *v = dynamic_cast<OperationToken *>(t.get())) {
            std::string stack_opertion = v->toString();
            if (higher_operations[cur_operation].find(stack_opertion) != higher_operations[cur_operation].end()) {
                need_to_push = true;
            }
        }
        if (need_to_push) {
            pushToOutput();
        } else {
            break;
        }
    }
    exprStack.push(std::make_unique<OperationToken>(token));
}

const std::vector<std::unique_ptr<Token>> &ParseVisitor::GetTokens() const {
    return output;
}

void ParseVisitor::pushToOutput() {
    output.push_back(std::move(exprStack.top()));
    exprStack.pop();
}

void ParseVisitor::ParseTokens(const std::vector<std::unique_ptr<Token>> &tokens) {
    for (const auto &token: tokens) {
        token->accept(*this);
    }
    while (!exprStack.empty()) {
        auto &t = exprStack.top();
        if (dynamic_cast<OperationToken *>(t.get())) {
        } else {
            throw std::invalid_argument("Cannot parse input expression. Incorrect braces");
        }
        pushToOutput();
    }
}
