package RegularGrammars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int nrWords = 5;  //how many words/strings to be generated
        List<String> Vn = Arrays.asList("S", "A", "B", "C");
        List<String> Vt = Arrays.asList("a", "b", "c", "d");
        List<String> P = Arrays.asList("S->dA", "A->aB", "B->bC", "C->cB",
                "A->bA", "B->aB", "B->d");
        Grammar grammar = new Grammar(Vn, Vt, P, "S");

        for (int i = 0; i < nrWords; i++) {
            String generatedWord = grammar.generateString();
            System.out.println("Generated word " + (i + 1) + ": " + generatedWord);
        }

        FiniteAutomaton finiteAutomaton = grammar.toFiniteAutomaton();

        List<String> testCases = new ArrayList<>();
        testCases.add("dbbad");
        testCases.add("daaabcd");
        testCases.add("dabcaad");
        testCases.add("abcd");
        testCases.add("dbacbd");
        testCases.add("daa");

        for (String testCase : testCases) {
            System.out.println("Does " + testCase + " belong to this Language? - " + finiteAutomaton.stringBelongToLanguage(testCase));
        }
    }
}
