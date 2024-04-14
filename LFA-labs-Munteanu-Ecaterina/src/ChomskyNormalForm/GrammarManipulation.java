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
        Set<String> newProductions = new LinkedHashSet<>();
        Set<String> newNonTerminals = new LinkedHashSet<>(grammar.getVn()); // Copy existing non-terminals

        // Handle each production
        for (String production : grammar.getP()) {
            String[] parts = production.split("->");
            String lhs = parts[0].trim();
            String rhs = parts[1].trim();

            // Directly handle terminal productions
            if (rhs.length() == 1 && grammar.getVt().contains(rhs)) {
                newProductions.add(lhs + "->" + rhs);
                continue;
            }

            // Transform productions to X->YZ form
            if (rhs.length() >= 2) {
                String newLhs = lhs;
                String rest = rhs;
                int i = 0;
                while (rest.length() > 2) {
                    if (grammar.getVt().contains(String.valueOf(rest.charAt(0))) && i == 0) {
                        // Handle terminal followed by non-terminal
                        String newNonTerminal = "N" + (newNonTerminals.size() + 1);
                        newNonTerminals.add(newNonTerminal);
                        newProductions.add(newLhs + "->" + rest.charAt(0) + newNonTerminal);
                        newLhs = newNonTerminal;
                        rest = rest.substring(1);
                        i++;
                    } else {
                        // Handle non-terminal followed by non-terminal
                        String newNonTerminal = "N" + (newNonTerminals.size() + 1);
                        newNonTerminals.add(newNonTerminal);
                        newProductions.add(newLhs + "->" + rest.substring(0, 1) + newNonTerminal);
                        newLhs = newNonTerminal;
                        rest = rest.substring(1);
                    }
                }
                // Last two characters should form a valid production
                if (rest.length() == 2 && grammar.getVt().contains(rest.substring(1))) {
                    String newNonTerminal = "N" + (newNonTerminals.size() + 1);
                    newNonTerminals.add(newNonTerminal);
                    newProductions.add(newLhs + "->" + rest.charAt(0) + newNonTerminal);
                    newProductions.add(newNonTerminal + "->" + rest.charAt(1));
                } else {
                    newProductions.add(newLhs + "->" + rest);
                }
            }
        }

        // Update grammar with new productions and non-terminals
        grammar.setVn(new ArrayList<>(newNonTerminals));
        grammar.setP(new ArrayList<>(newProductions));
    }



    public void eliminateNonProductive() {
        List<String> productiveSymbols = new ArrayList<>();
        List<String> nonProductiveSymbols = new ArrayList<>();
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
        List<String> newP = new ArrayList<>();
        for (String symbol : nonProductiveSymbols) {
            for (String production : grammar.getP()) {
                if (!production.contains(symbol)) {
                    newP.add(production);
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
