package ChomskyNormalForm;

import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GrammarManipulationTest {

    private GrammarManipulation manipulator;
    private Grammar grammar;
    private Grammar grammarEliminateEpsilon;
    private Grammar grammarEliminateRenaming;
    private Grammar grammarEliminateInaccessible;
    private Grammar grammarEliminateNonProductive;
    private Grammar grammarTransform;
    private Grammar grammarFinalResult;

    @BeforeEach
    void setUp() {
        List<String> vn = Arrays.asList("S", "A", "B", "C");
        List<String> vt = Arrays.asList("a", "b");
        String s = "S";
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
        List<String> pEliminateEpsilon = Arrays.asList(
                "S->abAB",
                "C->AS",
                "B->BA",
                "S->abA",
                "B->abab",
                "A->BS",
                "A->b",
                "B->A",
                "B->b",
                "A->aSab",
                "B->ababB",
                "A->aA",
                "A->S"
        );
        List<String> pEliminateRenaming = Arrays.asList(
                "S->abAB",
                "A->abAB",
                "C->AS",
                "B->aA",
                "B->BA",
                "S->abA",
                "B->abab",
                "A->BS",
                "A->abA",
                "A->b",
                "B->b",
                "A->aSab",
                "B->BS",
                "B->ababB",
                "B->aSab",
                "A->aA"
        );
        List<String> pInaccessible = Arrays.asList(
                "S->abAB",
                "A->abAB",
                "B->aA",
                "B->BA",
                "S->abA",
                "B->abab",
                "A->BS",
                "A->abA",
                "A->b",
                "B->b",
                "A->aSab",
                "B->BS",
                "B->ababB",
                "B->aSab",
                "A->aA"
        );
        List<String> pEliminateNonProductive = Arrays.asList(
                "S->abAB",
                "A->abAB",
                "B->aA",
                "B->BA",
                "S->abA",
                "B->abab",
                "A->BS",
                "A->abA",
                "A->b",
                "B->b",
                "A->aSab",
                "B->BS",
                "B->ababB",
                "B->aSab",
                "A->aA"
        );

        List<String> pFinalResult = Arrays.asList(
                "A->KA",
                "A->JB",
                "B->KK",
                "B->LK",
                "B->BA",
                "L->NS",
                "N->a",
                "M->b",
                "A->BS",
                "A->NA",
                "K->NM",
                "A->b",
                "B->NA",
                "B->b",
                "B->BS",
                "H->KK",
                "A->LK",
                "J->KA",
                "B->HB",
                "S->JB",
                "S->KA"
        );
        grammar = new Grammar(vn, vt, p, s);
        grammarEliminateEpsilon = new Grammar(vn, vt, pEliminateEpsilon, s);
        grammarEliminateRenaming = new Grammar(vn, vt, pEliminateRenaming, s);
        grammarEliminateInaccessible = new Grammar(vn, vt, pInaccessible, s);
        grammarEliminateNonProductive = new Grammar(vn, vt, pEliminateNonProductive, s);
        grammarFinalResult = new Grammar(vn, vt, pFinalResult, s);
        manipulator = new GrammarManipulation();
    }

    @org.junit.jupiter.api.Test
    void convertToCNF() {
        Grammar cnfGrammar = manipulator.convertToCNF(grammar);
        assertNotNull(cnfGrammar);
        assertEquals(grammarFinalResult.getP(), cnfGrammar.getP());

    }

    @org.junit.jupiter.api.Test
    void eliminateNonProductive() {
        manipulator.setGrammar(grammarEliminateInaccessible);
        manipulator.eliminateNonProductive();

        List<String> productions = manipulator.getGrammar().getP();
        assertEquals(grammarEliminateNonProductive.getP(), productions);
    }

    @org.junit.jupiter.api.Test
    void eliminateRenaming() {
        manipulator.setGrammar(grammarEliminateEpsilon);
        manipulator.eliminateRenaming();
        List<String> productions = manipulator.getGrammar().getP();
        assertEquals(grammarEliminateRenaming.getP(), productions);
    }

    @org.junit.jupiter.api.Test
    void eliminateEpsilonProductions() {
        manipulator.setGrammar(grammar);
        manipulator.eliminateEpsilonProductions();
        System.out.println("Grammar #: " + manipulator.getGrammar());
        List<String> productions = manipulator.getGrammar().getP();
        assertEquals(grammarEliminateEpsilon.getP(), productions);
    }

    @org.junit.jupiter.api.Test
    void eliminateInaccessible() {
        manipulator.setGrammar(grammarEliminateRenaming);
        manipulator.eliminateInaccessible();
        List<String> productions = manipulator.getGrammar().getP();
        System.out.println("productions: " + productions);
        assertEquals(grammarEliminateInaccessible.getP(), productions);
    }

    @org.junit.jupiter.api.Test
    void transformToStructuredForm() {
        manipulator.setGrammar(grammarEliminateNonProductive);
        manipulator.transformToStructuredForm();
        List<String> productions = manipulator.getGrammar().getP();
        assertEquals(grammarFinalResult.getP(), productions);
    }
}