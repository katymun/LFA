package ChomskyNormalForm;

import java.util.*;

public class GrammarManipulation {
    private List<String> otherSymbols;
    private Grammar grammar;
    public Grammar getGrammar() {
        return grammar;
    }

    public void setGrammar(Grammar grammar) {
        this.grammar = grammar;
    }

    public Grammar convertToCNF(Grammar grammar) {

        this.grammar = grammar;
        eliminateEpsilonProductions();
        eliminateRenaming();
        eliminateInaccessible();
        eliminateNonProductive();
        transformToStructuredForm();

        return grammar;
    }
    public void transformToStructuredForm() {
        List<String> newSymbols = getNewSymbols();

        Set<String> newVn = new HashSet<>(grammar.getVn());
        Set<String> newP = new HashSet<>();
        int index = 0;
        Set<String> newRightSides = new HashSet<>();
        for (String terminal : grammar.getVt()) {
            newVn.add(newSymbols.get(index));
//            grammar.addProduction(newSymbols.get(index), terminal);
            newP.add(newSymbols.get(index) + "->" + terminal);
            newRightSides.add(terminal);
            index++;
        }

        grammar.setVn(new ArrayList<>(newVn));

        Set<String> copyNewP = new HashSet<>(newP);
        for (String production : grammar.getP()) {
            String[] parts = production.split("->");
            String newPart = parts[1];
            for (String prod : copyNewP) {
                String[] symbols = prod.split("->");
                if (parts[1].contains(symbols[1]) && !parts[0].equals(symbols[0]) && parts[1].length() > 1) {
                    newPart = newPart.replace(symbols[1], symbols[0]);
                }
            }
            newP.add(parts[0] + "->" + newPart);
        }
        grammar.setP(new ArrayList<>(newP));

        while (true) {
            Set<String> P = new HashSet<>();
            String newProd = null;
            for (String production : grammar.getP()) {
                String[] parts = production.split("->");
                if (parts[1].length() > 2) {
                    newProd = String.valueOf(parts[1].charAt(0)) + String.valueOf(parts[1].charAt(1));
                    P.add(newSymbols.get(index) + "->" + newProd);
                    newVn.add(newSymbols.get(index));
                    break;
                }
            }
            if (newProd != null) {
                for (String production : grammar.getP()) {
                    String[] parts = production.split("->");
                    if (parts[1].contains(newProd) && parts[1].length() > 2) {
                        parts[1] = parts[1].replace(newProd, newSymbols.get(index));
                    }
                    P.add(parts[0] + "->" + parts[1]);
                }
                index++;
                grammar.setP(new ArrayList<>(P));
            } else {
                break;
            }
        }
        grammar.setVn(new ArrayList<>(newVn));
    }

    private List<String> getNewSymbols() {
        List<String> newSymbols = new ArrayList<>();
        newSymbols.add("N");
        newSymbols.add("M");
        newSymbols.add("K");
        newSymbols.add("L");
        newSymbols.add("J");
        newSymbols.add("H");
        newSymbols.add("G");
        newSymbols.add("F");
        newSymbols.add("P");
        newSymbols.add("Y");
        newSymbols.add("W");
        newSymbols.add("Q");
        newSymbols.add("T");
        newSymbols.add("O");
        newSymbols.add("E");
        newSymbols.add("X");
        newSymbols.add("Z");
        newSymbols.add("R");
        newSymbols.add("V");
        return newSymbols;
    }

    public void eliminateNonProductive() {
        Set<String> productiveSymbols = new HashSet<>();
        Set<String> nonProductiveSymbols = new HashSet<>();
        for (String symbol : grammar.getVn()) {
            for (String production : grammar.getP()) {
                String[] parts = production.split("->");
                if (parts[0].equals(symbol) && parts[1].length() == 1 && grammar.getVt().contains(parts[1]) ) {
                    productiveSymbols.add(symbol);
                }
            }
        }
        productiveSymbols.add(grammar.getS());
        for (String symbol : grammar.getVn()) {
            if (!productiveSymbols.contains(symbol)) {
                nonProductiveSymbols.add(symbol);
            }
        }
        List<String> newP = grammar.getP();
        for (String symbol : nonProductiveSymbols) {
            for (String production : grammar.getP()) {
                if (production.contains(symbol)) {
                    newP.remove(production);
                }
            }
        }
        grammar.setP(newP);
    }

    public void eliminateRenaming() {
        List<String> P = new ArrayList<>(grammar.getP());

        Set<String> renamingProductions = new HashSet<>();
        for (String production : P) {
            String[] parts = production.split("->");
            String leftHandSide = parts[0];
            String rightHandSide = parts[1];

            if (rightHandSide.length() == 1 && grammar.getVn().contains(rightHandSide)) {
                renamingProductions.add(production);
            }
        }
        P.removeAll(renamingProductions);

        Set<String> newProductions = new HashSet<>();
        for (String renamingProduction : renamingProductions) {
            String[] parts = renamingProduction.split("->");
            String leftHandSide = parts[0];
            String rightHandSide = parts[1];

            newProductions.addAll(generateNonRenamingProductions(leftHandSide, rightHandSide, P));

        }
        grammar.setP(new ArrayList<>(newProductions));
    }

    private Set<String> generateNonRenamingProductions(String leftSymbol, String rightSymbol, List<String> P) {
        Set<String> newProductions = new HashSet<>(P);

        for (String production : grammar.getP()) {
            String[] parts = production.split("->");
            if (parts[0].equals(rightSymbol) && (parts[1].length() > 1 || !grammar.getVn().contains(parts[1]))) {
                newProductions.add(leftSymbol + "->" + parts[1]);
            }
        }

        return newProductions;
    }

    public void eliminateEpsilonProductions() {
        List<String> N = findEpsilonProductions();
        Set<String> newProductions = new HashSet<>();
        List<String> P = new ArrayList<>(grammar.getP());
        for (String production : P) {
            String[] parts = production.split("->");
            String leftHandSide = parts[0];
            String rightHandSide = parts[1];
            List<String> combinations = generateCombinations(rightHandSide, N);
            for (String combination : combinations) {
                newProductions.add(leftHandSide + "->" + combination);
            }
        }
        grammar.setP(new ArrayList<>(newProductions));
    }

    public List<String> findEpsilonProductions() {
        List<String> N = new ArrayList<>();
        List<String> newP = new ArrayList<>();
        for (String production : grammar.getP()) {
            String[] parts = production.split("->");
            if (parts[1].equals(Constants.EPSILON)) {
                N.add(parts[0]);
            } else {
                newP.add(production);
            }
        }
        grammar.setP(newP);
        return N;
    }
    private List<String> generateCombinations(String rightHandSide, List<String> N) {
        List<String> combinations = new ArrayList<>();
        int n = rightHandSide.length();
        for (int i = 0; i < (1 << n); i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    char c = rightHandSide.charAt(j);
                    if (N.contains(String.valueOf(c))) {
                        continue;
                    } else {
                        sb.append(c);
                    }
                } else {
                    sb.append(rightHandSide.charAt(j));
                }
            }
            if (sb.length() > 0) {
                combinations.add(sb.toString());
            }
        }
        return combinations;
    }

    public void eliminateInaccessible() {
        List<String> P = new ArrayList<>(grammar.getP());
        Set<String> leftSideSymbols = new HashSet<>();
        Set<String> rightSideSymbols = new HashSet<>();
        for (String production : P) {
            String[] parts = production.split("->");
            leftSideSymbols.add(parts[0]);
            rightSideSymbols.add(parts[1]);
        }
        Set<String> inaccessibleSymbols = new HashSet<>();
        for (String leftS : leftSideSymbols) {
            boolean found = false;
            for (String rightS : rightSideSymbols) {
                if (rightS.contains(leftS)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                inaccessibleSymbols.add(leftS);
            }
        }
        List<String> newP = new ArrayList<>();
        for (String production : P) {
            String[] parts = production.split("->");
            if (!inaccessibleSymbols.contains(parts[0])) {
                newP.add(production);
            }
        }
        List<String> Vn = new ArrayList<>(grammar.getVn());
        for (String symbol : grammar.getVn()) {
            if (inaccessibleSymbols.contains(symbol)) {
                Vn.remove(symbol);
            }
        }
        grammar.setVn(Vn);
        grammar.setP(newP);

    }
}
