package ChomskyNormalForm;

import java.util.*;
import java.util.stream.Collectors;

public class Grammar {
    private List<String> Vn;
    private List<String> Vt;
    private List<String> P;
    private String S;

    public Grammar() {
        S = "S";
        Vn = new ArrayList<>();
        Vt = new ArrayList<>();
        P = new ArrayList<>();
    }

    public Grammar(List<String> vn, List<String> vt, List<String> p, String s) {
        Vn = vn;
        Vt = vt;
        P = p;
        S = s;
    }

    public void addNonTerminal(String symbol) {
        Vn.add(symbol);
    }

    public void addTerminal(String symbol) {
        Vt.add(symbol);
    }

    public void addProduction(String from, String to) {
        this.P.add(from + "->" + to);
    }

    // Print the productions
    public void printProductions() {
        for (String production : P) {
            System.out.println(production);
        }
    }

    @Override
    public String toString() {
        String g = "Grammar{" +
                "Vn=" + Vn +
                ", Vt=" + Vt +
                ", S='" + S + '\'' +
                ", P=\n";
        for (String p : this.getP()) {
            g += p + " \n";
        }
        g += "};";
        return g;
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
