package FiniteAutomata;

import RegularGrammars.FiniteAutomaton;
import RegularGrammars.Transition;

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

    public Type getType() {
        boolean type0 = true;
        boolean type1 = true;
        boolean type2 = true;
        boolean type3 = true;

        List<String> productions = getAllProductions();
        for (String production : productions) {
            String[] parts = production.split("->");
            String leftSide = parts[0];
            String rightSide = parts[1];
            if (leftSide != null && leftSide != "" && countNonTerminals(leftSide) >= 1) {
                if (rightSide != null && rightSide != "" && leftSide.length() <= rightSide.length()) {
                    if (leftSide.length() == 1 && countNonTerminals(rightSide) >= 2) {
                        if (countNonTerminals(rightSide) != 0 && countNonTerminals(rightSide) != 1) {
                            type3 = false;
                        }
                    } else {
                        type2 = false;
                    }
                } else {
                    type1 = false;
                }
            } else {
                type0 = false;
            }
        }
        if (type3) {
            return Type.TYPE_3;
        } else if (type2) {
            return Type.TYPE_2;
        } else if (type1) {
            return Type.TYPE_1;
        } else if (type0) {
            return Type.TYPE_0;
        } else {
            return Type.UNDEFINED;
        }
    }

    public boolean isLeftRightRegularGrammar() {
        // return false if it isn't only left-regular or right-regular
        boolean rightRegular = false;
        boolean leftRegular = false;
        List<String> productions = getAllProductions();
        for (String production : productions) {
            String[] parts = production.split("->");
            String leftSide = parts[0];
            String rightSide = parts[1];
            if (rightSide.length() == 1 && countNonTerminals(rightSide) == 1) {
                return false;
            }
            if (rightSide.length() > 1 && countNonTerminals(rightSide) == 1) {
                String[] sides = rightSide.split();
                if (Vn.contains(String.valueOf(nonTerminal)) && Vt.contains(String.valueOf(terminal))) {
                    // Found a production in right-regular form
                    return true;
                }
            }
        }
        return false;
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
                delta.add(new Transition(leftSide, String.valueOf(rightSide.charAt(0)),
                        String.valueOf(rightSide.charAt(1))));
            }
        }
        return new FiniteAutomaton(Q, Sigma, delta, q0, F);
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
}
