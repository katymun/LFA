package lab1;

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

            // Find the transition for each current state and input symbol
            for (String currentState : currentStates) {
                List<Transition> transitions = findTransitions(currentState, inputSymbol);
                for (Transition transition : transitions) {
                    nextStates.add(transition.getToState());
                }
            }

            if (nextStates.isEmpty()) {
                // No valid transition found
                return false;
            }

            currentStates = nextStates;
        }

        // Check if any of the final states is in the set of accepting states
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
}
