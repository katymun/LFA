package lab1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grammar {
    private List<String> Vn;
    private List<String> Vt;
    private List<String> P;
    private String S;

    public Grammar(List<String> Vn, List<String> Vt, List<String> P, String S) {
        this.Vt = Vt;
        this.Vn = Vn;
        this.P = P;
        this.S = S;
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

    public FiniteAutomaton toFiniteAutomaton() {
        List<String> Sigma = new ArrayList<>(Vt);

        List<String> Q = new ArrayList<>(Vn);
        Q.addAll(Vn);
        Q.add("X");

        String q0 = S;
        String F = "X";

        List<Transition> delta = new ArrayList<>();
        for (String production : P) {
            String[] parts = production.split("->");
            String leftSide = parts[0];
            String rightSide = parts[1];

            if (rightSide.length() == 1 && Vt.contains(rightSide)) {
                // A → a
                delta.add(new Transition(leftSide, rightSide, F));
            } else if (rightSide.length() > 1) {
                // A → aB
                delta.add(new Transition(leftSide, String.valueOf(rightSide.charAt(0)), String.valueOf(rightSide.charAt(1))));
            }
        }
        return new FiniteAutomaton(Q, Sigma, delta, q0, F);
    }
}
