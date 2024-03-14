package FiniteAutomata;

import FiniteAutomata.FiniteAutomaton2;
import FiniteAutomata.Transition2;

import java.util.*;
import java.util.stream.Collectors;

public class NDFAtoDFAConverter {
    private FiniteAutomaton2 nfa;
    private Set<List<String>> dfaStates;
    private List<String> dfaSigma;
    private List<Transition2> dfaTransitions;
    private List<String> dfaStartState;
    private List<String> dfaFinalStates;

    public NDFAtoDFAConverter(FiniteAutomaton2 nfa) {
        this.nfa = nfa;
        this.dfaStates = new HashSet<>();
        this.dfaSigma = new ArrayList<>(nfa.getSigma());
        this.dfaTransitions = new ArrayList<>();
        this.dfaStartState = new ArrayList<>(nfa.getQ0());
        this.dfaFinalStates = new ArrayList<>();
    }

    public FiniteAutomaton2 convert() {
        List<List<String>> powerSetStates = getPowerSet(new ArrayList<>(nfa.getQ()));
        Map<List<String>, List<String>> dfaTransitionsMap = new HashMap<>();

        for (List<String> currentState : powerSetStates) {
            dfaTransitionsMap.put(currentState, new ArrayList<>());
        }

        for (List<String> currentState : powerSetStates) {
            for (String symbol : dfaSigma) {
                List<String> nextStates = new ArrayList<>();

                for (String state : currentState) {
                    List<Transition2> transitions = findTransitions(nfa.getDelta(), state, symbol);

                    for (Transition2 transition : transitions) {
                        nextStates.add(transition.getToState());
                    }
                }

                nextStates.removeIf(s -> s.equals(FiniteAutomaton2.EPSILON));

                if (!dfaTransitionsMap.containsKey(currentState) && !nextStates.isEmpty()) {
                    dfaTransitionsMap.get(currentState).add(nextStates.toString());
                }
            }
        }

        for (Map.Entry<List<String>, List<String>> entry : dfaTransitionsMap.entrySet()) {
            List<String> currentState = entry.getKey();

            for (String symbol : dfaSigma) {
                List<String> transitions = entry.getValue();

                List<String> nextStates = transitions.stream()
                        .map(t -> Arrays.asList(t.substring(1, t.length() - 1).split(", ")))
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());

                nextStates.removeIf(s -> s.equals(FiniteAutomaton2.EPSILON));

                if (!dfaTransitionsMap.containsKey(nextStates) && !nextStates.isEmpty()) {
                    dfaTransitionsMap.get(currentState).add(nextStates.toString());

                    dfaTransitions.add(new Transition2(currentState.toString(), symbol, nextStates.toString()));
                }
            }
        }

        for (List<String> state : powerSetStates) {
            if (!Collections.disjoint(state, nfa.getF())) {
                dfaFinalStates.add(state.toString());
            }
        }

        return new FiniteAutomaton2(transformSetToList(dfaStates), dfaSigma, dfaTransitions, dfaStartState, dfaFinalStates);
    }

    private List<Transition2> findTransitions(List<Transition2> transitions, String state, String symbol) {
        List<Transition2> result = new ArrayList<>();
        for (Transition2 transition : transitions) {
            if (state.equals(transition.getFromState()) && symbol.equals(transition.getSymbol())) {
                result.add(transition);
            }
        }
        return result;
    }

    private List<List<String>> getPowerSet(List<String> list) {
        List<List<String>> powerSet = new ArrayList<>();
        int n = list.size();

        for (int i = 0; i < (1 << n); i++) {
            List<String> subset = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    subset.add(list.get(j));
                }
            }
            powerSet.add(subset);
        }

        return powerSet;
    }

    private List<String> transformSetToList(Set<List<String>> setOfLists) {
        List<String> resultList = new ArrayList<>();

        for (List<String> list : setOfLists) {
            // Join the elements of the list into a single string
            String concatenatedString = String.join(",", list);
            resultList.add(concatenatedString);
        }

        return resultList;
    }
}
