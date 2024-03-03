package FiniteAutomata;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainClass {
    public static void main(String[] args) {

//        Type grammarType;
//        List<String> Vn = Arrays.asList("S", "A", "B", "C");
//        List<String> Vt = Arrays.asList("a", "b");
//        List<String> P = Arrays.asList("S->aA", "A->bS", "S->bC", "A->b",
//                "C->a", "C->bS");
//        Grammar2 grammar2 = new Grammar2(Vn, Vt, P, "S");
//        grammarType = grammar2.getType();
//
//        System.out.println("Grammar Type: " + grammarType);
//
//        FiniteAutomaton2 fa = grammar2.convertToFiniteAutomaton();
//        for (Transition2 transition : fa.getDelta()) {
//            System.out.println(transition);
//        }

        List<String> Q = Arrays.asList("q0", "q1", "q2", "q3");
        List<String> Sigma = Arrays.asList("a", "b");
        List<String> F = Arrays.asList("q3");
        List<String> q0 = Arrays.asList("q0");
        List<Transition2> delta = Arrays.asList(
                new Transition2("q0", "a", "q1"),
                new Transition2("q0", "b", "q0"),
                new Transition2("q1", "b", "q1"),
                new Transition2("q1", "b", "q2"),
                new Transition2("q2", "a", "q2"),
                new Transition2("q2", "b", "q3")
        );
        FiniteAutomaton2 automatonNFA = new FiniteAutomaton2(Q, Sigma, delta, q0, F);

        System.out.println("Is this FA deterministic? - " + automatonNFA.isDeterministic());

        FiniteAutomaton2 automatonDFA = automatonNFA.convertToDFA();
        System.out.println("Is this FA deterministic? - " + automatonDFA.isDeterministic());

        for (Transition2 transition : automatonDFA.getDelta()) {
            System.out.println(transition);
        }
        System.out.println("Starting states: " + automatonDFA.getQ0());
        System.out.println("Final states: " + automatonDFA.getF());

        System.out.println("Grammar from NFA: " + Grammar2.convertToRegularGrammar(automatonNFA));
        System.out.println("Grammar from DFA: " + Grammar2.convertToRegularGrammar(automatonDFA));
//
//        FiniteAutomatonGraph automatonGraph = new FiniteAutomatonGraph();
//        automatonGraph.drawAutomaton(automatonDFA);


//        List<Transition2> DFAdelta = Arrays.asList(
//                new Transition2("q0", "a", "q1"),
//                new Transition2("q0", "b", "q0"),
//                new Transition2("q1", "a", "q1"),
//                new Transition2("q1", "b", "q2"),
//                new Transition2("q2", "a", "q2"),
//                new Transition2("q2", "b", "q3"),
//                new Transition2("q3", "a", "q3")
//        );
//
//        FiniteAutomaton2 dFA = new FiniteAutomaton2(Q, Sigma, DFAdelta, q0, F);
//        Grammar2 grammarDFA = Grammar2.convertToRegularGrammar(automatonDFA);
//        System.out.println("Productions: " + grammarDFA.getP());
    }


}
