package RegularGrammars;

import FiniteAutomata.FiniteAutomaton2;
import FiniteAutomata.Grammar2;
import FiniteAutomata.Transition2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FiniteAutomaton {
    private List<String> Q;
    private List<String> Sigma;
    private List<Transition> delta;
    private String q0;
    private String F;


    public FiniteAutomaton(List<String> Q, List<String> Sigma, List<Transition> delta, String q0, String F) {
        this.Q = Q;
        this.Sigma = Sigma;
        this.delta = delta;
        this.q0 = q0;
        this.F = F;
    }

    public boolean stringBelongToLanguage(final String inputString) {
        Set<String> currentStates = new HashSet<>();
        currentStates.add(q0);

        for (char symbol : inputString.toCharArray()) {
            String inputSymbol = String.valueOf(symbol);

            Set<String> nextStates = new HashSet<>();

            for (String currentState : currentStates) {
                List<Transition> transitions = findTransitions(currentState, inputSymbol);
                for (Transition transition : transitions) {
                    nextStates.add(transition.getToState());
                }
            }

            if (nextStates.isEmpty()) {
                return false;
            }

            currentStates = nextStates;
        }
        for (String currentState : currentStates) {
            if (F.contains(currentState)) {
                return true;
            }
        }
        return false;
    }


    private List<Transition> findTransitions(String currentState, String inputSymbol) {
        List<Transition> result = new ArrayList<>();
        for (Transition transition : delta) {
            if (transition.getFromState().equals(currentState) && transition.getSymbol().equals(inputSymbol)) {
                result.add(transition);
            }
        }
        return result;
    }

    public List<String> getQ() {
        return Q;
    }

    public void setQ(List<String> q) {
        Q = q;
    }

    public List<String> getSigma() {
        return Sigma;
    }

    public void setSigma(List<String> sigma) {
        Sigma = sigma;
    }

    public List<Transition> getDelta() {
        return delta;
    }

    public void setDelta(List<Transition> delta) {
        this.delta = delta;
    }

    public String getQ0() {
        return q0;
    }

    public void setQ0(String q0) {
        this.q0 = q0;
    }

    public String getF() {
        return F;
    }

    public void setF(String f) {
        F = f;
    }
}
