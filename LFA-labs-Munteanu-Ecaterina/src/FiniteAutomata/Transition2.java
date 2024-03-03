package FiniteAutomata;

public class Transition2 {
    private String fromState;
    private String symbol;
    private String toState;

    public Transition2(String fromState, String symbol, String toState) {
        this.fromState = fromState;
        this.symbol = symbol;
        this.toState = toState;
    }

    public String getFromState() {
        return fromState;
    }

    public void setFromState(String fromState) {
        this.fromState = fromState;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getToState() {
        return toState;
    }

    public void setToState(String toState) {
        this.toState = toState;
    }

    @Override
    public String toString() {
        return "Transition2{" +
                "fromState='" + fromState + '\'' +
                ", symbol='" + symbol + '\'' +
                ", toState='" + toState + '\'' +
                '}';
    }
}
