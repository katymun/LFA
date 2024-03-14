package LexerScanner;

public class Token {
    TokenType type;
    String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s:\'%s\'", type.name(), value != null ? value : "");
    }
}
