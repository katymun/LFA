package FiniteAutomata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Grammar2 {
    private List<String> Vn;
    private List<String> Vt;
    private List<String> P;
    private String S;

    public Grammar2(List<String> Vn, List<String> Vt, List<String> P, String S) {
        this.Vt = Vt;
        this.Vn = Vn;
        this.P = P;
        this.S = S;
    }

    public Type getType() {
        boolean type1 = true;
        boolean type2 = true;
        boolean undefined = false;

        List<String> productions = getP();
        for (String production : productions) {
            String[] parts = production.split("->");
            if (parts.length == 1) {
                if (parts[0].isEmpty() || countNonTerminals(parts[0]) == 0) {
                    undefined = true;
                }
            }
        }

        if (undefined) {
            return Type.UNDEFINED;
        }

        if (isLeftRightRegularGrammar()) {
            return Type.TYPE_3;
        }

        for (String production : productions) {
            String[] parts = production.split("->");
            String leftSide = parts[0];
            String rightSide = parts[1];

            if (leftSide.isEmpty() || countNonTerminals(leftSide) == 0) {
                return Type.UNDEFINED;
            }

            if (leftSide.length() != 1 || countNonTerminals(leftSide) != 1) {
                type2 = false;
            } else if (leftSide.length() > rightSide.length()) {
                type1 = false;
            }
        }

        if (type2) {
            return Type.TYPE_2;
        } else if (type1) {
            return Type.TYPE_1;
        } else {
            return Type.TYPE_0;
        }
    }

    public boolean isLeftRightRegularGrammar() {
        boolean rightRegular = false;
        boolean leftRegular = false;
        List<String> productions = getP();
        for (String production : productions) {
            String[] parts = production.split("->");
            String leftSide = parts[0];

            if (leftSide.length() != 1 || countNonTerminals(leftSide) != 1) {
                return false;
            }

            String rightSide = parts[1];
            if (rightSide.length() == 1 && countNonTerminals(rightSide) == 1) {
                return false;
            }

            if (rightSide.length() > 1 && countNonTerminals(rightSide) == 1) {
                String nonTerminal = extractNonTerminal(rightSide);
                String[] sides = rightSide.split(nonTerminal);

                if (sides.length == 1) {
                    rightRegular = true;
                } else if (!sides[1].isEmpty() && sides[0].isEmpty()) {
                    leftRegular = true;
                }
            }
        }

        return rightRegular != leftRegular;
    }

    public String extractNonTerminal(String input) {
        for (char c : input.toCharArray()) {
            if (Vn.contains(String.valueOf(c))) {
                return String.valueOf(c);
            }
        }
        return null; // If no non-terminal symbol is found
    }
    
    public int countNonTerminals(String input) {
        int count = 0;
        for (char c : input.toCharArray()) {
            if (Vn.contains(String.valueOf(c))) {
                count++;
            }
        }
        return count;
    }


    public String generateString() {
        StringBuilder result = new StringBuilder();
        generateStringHelper(S, result);
        return result.toString();
    }

    private void generateStringHelper(String symbol, StringBuilder result) {
        if (Vt.contains(symbol)) {
            result.append(symbol);
        } else {
            List<String> productions = getProductions(symbol);

            String selectedProduction = productions.get(new Random().nextInt(productions.size()));
            for (char c : selectedProduction.toCharArray()) {
                generateStringHelper(String.valueOf(c), result);
            }
        }
    }

    private List<String> getProductions(String symbol) {
        List<String> productions = new ArrayList<>();
        for (String production : P) {
            String[] parts = production.split("->");
            if (parts[0].equals(symbol)) {
                productions.add(parts[1]);
            }
        }
        return productions;
    }

    private List<String> getAllProductions() {
        List<String> allProductions = new ArrayList<>();
        for (String production : P) {
            String[] parts = production.split("->");
            if (parts.length == 2) {
                allProductions.add(parts[1]);
            }
        }
        return allProductions;
    }

    public static Grammar2 convertToRegularGrammar(FiniteAutomaton2 automaton) {
        List<String> Vn = new ArrayList<>(automaton.getQ());
        List<String> Vt = new ArrayList<>(automaton.getSigma());
        List<String> P = new ArrayList<>();

        // Iterate through each initial state in q0
        for (String initialState : automaton.getQ0()) {
            String S = initialState; // Start symbol is the initial state

            for (Transition2 transition : automaton.getDelta()) {
                String fromState = transition.getFromState();
                String symbol = transition.getSymbol();
                String toState = transition.getToState();

                if (automaton.getF().contains(toState)) {
                    // If the destination state is a final state, add productions accordingly
                    P.add(fromState + "->" + symbol + toState);
                } else {
                    P.add(fromState + "->" + symbol + toState);
                }
            }
        }

        return new Grammar2(Vn, Vt, P, automaton.getQ0().get(0)); // Return a Grammar2 instance
    }


    public FiniteAutomaton2 convertToFiniteAutomaton() {
        List<String> Sigma = new ArrayList<>(Vt);

        List<String> Q = new ArrayList<>(Vn);
        Q.addAll(Vn);
        Q.add("X");

        List<String> q0 = Arrays.asList(S);
        List<String> F = Arrays.asList("X");

        List<Transition2> delta = new ArrayList<>();
        for (String production : P) {
            String[] parts = production.split("->");
            String leftSide = parts[0];
            String rightSide = parts[1];

            if (rightSide.length() == 1 && Vt.contains(rightSide)) {
                // A → a
                delta.add(new Transition2(leftSide, rightSide, F.get(0)));
            } else if (rightSide.length() > 1) {
                // A → aB
                delta.add(new Transition2(leftSide, String.valueOf(rightSide.charAt(0)),
                        String.valueOf(rightSide.charAt(1))));
            }
        }
        return new FiniteAutomaton2(Q, Sigma, delta, q0, F);
    }

    public List<String> getVn() {
        return Vn;
    }

    public void setVn(List<String> vn) {
        Vn = vn;
    }

    public List<String> getVt() {
        return Vt;
    }

    public void setVt(List<String> vt) {
        Vt = vt;
    }

    public List<String> getP() {
        return P;
    }

    public void setP(List<String> p) {
        P = p;
    }

    public String getS() {
        return S;
    }

    public void setS(String s) {
        S = s;
    }

    @Override
    public String toString() {
        return "Grammar2{" +
                "Vn=" + Vn +
                ", Vt=" + Vt +
                ", P=" + P +
                ", S='" + S + '\'' +
                '}';
    }
}
