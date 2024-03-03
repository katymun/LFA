package FiniteAutomata;

import RegularGrammars.Transition;

import java.util.*;

public class FiniteAutomaton2 {
    private List<String> Q;
    private List<String> Sigma;
    private List<Transition2> delta;
    private List<String> q0;
    private List<String> F;
    public static final String EPSILON = "ε";

    public FiniteAutomaton2(List<String> Q, List<String> Sigma, List<Transition2> delta, List<String> q0, List<String> F) {
        this.Q = Q;
        this.Sigma = Sigma;
        this.delta = delta;
        this.q0 = q0;
        this.F = F;
    }

    public Grammar2 convertToRegularGrammar() {
        List<String> Vn = new ArrayList<>(this.getQ());
        List<String> Vt = new ArrayList<>(this.getSigma());
        List<String> P = new ArrayList<>();
        String S = this.getQ().get(0); // Start symbol is the initial state

        List<String> fromStates = new ArrayList<>();
        for (Transition2 transition : this.getDelta()) {
            fromStates.add(transition.getFromState());
        }

        for (Transition2 transition2 : this.getDelta()) {
            String fromState = transition2.getFromState();
            String symbol = transition2.getSymbol();
            String toState = transition2.getToState();

            if (this.getF().contains(toState)) {
                if (!fromStates.contains(toState)) {
                    P.add(fromState + "->" + symbol);
                } else {
                    P.add(fromState + "->" + symbol);
                    P.add(fromState + "->" + symbol + toState);
                }
            } else {
                P.add(fromState + "->" + symbol + toState);
            }
        }

        return new Grammar2(Vn, Vt, P, S);
    }

    public boolean isDeterministic() {
        Set<String> seenTransitions = new HashSet<>();
        for (Transition2 transition : delta) {
            String key = transition.getFromState() + "-" + transition.getSymbol();
            if (seenTransitions.contains(key)) {
                return false;
            }
            seenTransitions.add(key);
        }
        return true;
    }

    public boolean stringBelongToLanguage(final String inputString) {
        Set<String> currentStates = new HashSet<>(q0);

        for (char symbol : inputString.toCharArray()) {
            String inputSymbol = String.valueOf(symbol);
            Set<String> nextStates = new HashSet<>();
            for (String currentState : currentStates) {
                List<Transition2> transitions = findTransitions(currentState, inputSymbol);
                for (Transition2 transition : transitions) {
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

    private List<Transition2> findTransitions(String currentState, String inputSymbol) {
        List<Transition2> result = new ArrayList<>();
        for (Transition2 transition : delta) {
            if (transition.getFromState().equals(currentState) && transition.getSymbol().equals(inputSymbol)) {
                result.add(transition);
            }
        }
        return result;
    }

    public FiniteAutomaton2 convertToDFA() {
        Set<Set<String>> newStates = new HashSet<>();
        List<Transition2> newDelta = new ArrayList<>();
        Set<String> newF = new HashSet<>();

        Set<String> startStateSet = new HashSet<>(q0);
        Queue<Set<String>> unprocessedStates = new LinkedList<>();
        unprocessedStates.add(startStateSet);
        newStates.add(startStateSet);

        while (!unprocessedStates.isEmpty()) {
            Set<String> currentState = unprocessedStates.poll();

            for (String symbol : Sigma) {
                Set<String> nextStateSet = new HashSet<>();
                for (String state : currentState) {
                    for (Transition2 transition : delta) {
                        if (transition.getFromState().equals(state) && transition.getSymbol().equals(symbol)) {
                            nextStateSet.add(transition.getToState());
                        }
                    }
                }

                if (!nextStateSet.isEmpty()) {
                    if (newStates.add(nextStateSet)) {
                        unprocessedStates.add(nextStateSet);
                    }

                    String fromState = stateSetToString(currentState);
                    String toState = stateSetToString(nextStateSet);
                    newDelta.add(new Transition2(fromState, symbol, toState));
                }
            }
        }

        for (Set<String> stateSet : newStates) {
            for (String state : stateSet) {
                if (F.contains(state)) {
                    newF.add(stateSetToString(stateSet));
                    break;
                }
            }
        }

        List<String> newQ = new ArrayList<>();
        for (Set<String> stateSet : newStates) {
            newQ.add(stateSetToString(stateSet));
        }

        return new FiniteAutomaton2(newQ, Sigma, newDelta, new ArrayList<>(Collections.singletonList(stateSetToString(startStateSet))), new ArrayList<>(newF));
    }

    private String stateSetToString(Set<String> stateSet) {
        List<String> sortedStates = new ArrayList<>(stateSet);
        Collections.sort(sortedStates); // Ensure consistent ordering
        return String.join("", sortedStates);
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

    public List<Transition2> getDelta() {
        return delta;
    }

    public void setDelta(List<Transition2> delta) {
        this.delta = delta;
    }

    public List<String> getQ0() {
        return q0;
    }

    public void setQ0(List<String> q0) {
        this.q0 = q0;
    }

    public List<String> getF() {
        return F;
    }

    public void setF(List<String> f) {
        F = f;
    }
}
