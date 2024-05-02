package ChomskyNormalForm;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<String> vn = Arrays.asList("S", "A", "B", "C");
        List<String> vt = Arrays.asList("a", "b");
        List<String> p = Arrays.asList(
                "S->abAB",
                "A->aSab",
                "A->BS",
                "A->aA",
                "A->b",
                "B->BA",
                "B->ababB",
                "B->b",
                "B->ε",
                "C->AS"
        );
        String s = "S";

//        List<String> vn = Arrays.asList("S", "A", "B", "C", "D", "X");
//        List<String> vt = Arrays.asList("a", "b");
//        List<String> p = Arrays.asList(
//                "S->A",
//                "A->aX",
//                "A->bX",
//                "X->ε",
//                "X->BX",
//                "X->b",
//                "B->AD",
//                "D->aD",
//                "D->a",
//                "C->Ca"
//        );
//        String s = "S";

        Grammar grammar = new Grammar(vn, vt, p, s);
        System.out.println("Original Grammar:");
        System.out.println(grammar);

        System.out.println("\nGrammar after doing something:");
        GrammarManipulation tool = new GrammarManipulation();
        System.out.println(tool.convertToCNF(grammar));
    }

}
