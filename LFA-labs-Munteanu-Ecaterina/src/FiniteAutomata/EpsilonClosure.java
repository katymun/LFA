package FiniteAutomata;

import java.util.Set;

public class EpsilonClosure {
    String fromState;
    Set<String> states;

    public EpsilonClosure() {
    }

    public EpsilonClosure(String fromState, Set<String> states) {
        this.fromState = fromState;
        this.states = states;
    }

    public String getFromState() {
        return fromState;
    }

    public void setFromState(String fromState) {
        this.fromState = fromState;
    }

    public Set<String> getStates() {
        return states;
    }

    public void setStates(Set<String> states) {
        this.states = states;
    }

    @Override
    public String toString() {
        return "EpsilonClosure{" +
                "fromState='" + fromState + '\'' +
                ", states=" + states +
                '}';
    }
}
