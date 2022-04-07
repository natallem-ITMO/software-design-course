//
// Created by natallem on 12/10/21.
//

#ifndef CALCULATOR_STATE_HPP
#define CALCULATOR_STATE_HPP


class Context;

class State {
protected:
    Context *context;
public:
    virtual ~State() = default;

    void setContext(Context *context);

    virtual void ProcessChar(char ch, int pos) = 0;
};


#endif //CALCULATOR_STATE_HPP
