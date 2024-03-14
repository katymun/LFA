package LexerScanner;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String text;
    private int pos;
    private char currentChar;

    public Lexer(String text) {
        this.text = text;
        pos = 0;
        currentChar = text.charAt(pos);
    }

    private void advance() {
        pos++;
        if (pos < text.length()) {
            currentChar = text.charAt(pos);
        } else {
            currentChar = '\0';
        }
    }

    private void skipWhitespace() {
        while (currentChar != '\0' && Character.isWhitespace(currentChar)) {
            advance();
        }
    }

    private String getIdent() {
        StringBuilder result = new StringBuilder();
        while (currentChar != '\0' && Character.isLetterOrDigit(currentChar)) {
            result.append(currentChar);
            advance();
        }
        return result.toString();
    }

    private Token getNextToken() {
        while (currentChar != '\0') {
            if (Character.isWhitespace(currentChar)) {
                skipWhitespace();
                continue;
            }

            if (Character.isLetter(currentChar)) {
                String ident = getIdent();
                switch (ident) {
                    case "var":
                        return new Token(TokenType.VAR, ident);
                    case "print":
                        return new Token(TokenType.PRINT, ident);
                    default:
                        return new Token(TokenType.ID, ident);
                }
            }

            if (Character.isDigit(currentChar)) {
                StringBuilder num = new StringBuilder();
                while (currentChar != '\0' && Character.isDigit(currentChar)) {
                    num.append(currentChar);
                    advance();
                }
                return new Token(TokenType.ID, num.toString());
            }

            switch (currentChar) {
                case '=':
                    advance();
                    return new Token(TokenType.ASSIGN, "=");
                case ';':
                    advance();
                    return new Token(TokenType.SEMI, ";");
                case '(':
                    advance();
                    return new Token(TokenType.LPAREN, "(");
                case ')':
                    advance();
                    return new Token(TokenType.RPAREN, ")");
                case '+':
                case '-':
                case '*':
                case '/':
                    String op = String.valueOf(currentChar);
                    advance();
                    return new Token(TokenType.OP, op);
                case ',':
                    advance();
                    return new Token(TokenType.COMMA, ",");
                case '"':
                    advance();
                    StringBuilder str = new StringBuilder();
                    while (currentChar != '"' && currentChar != '\0') {
                        str.append(currentChar);
                        advance();
                    }
                    advance();
                    return new Token(TokenType.STRING, str.toString());
                default:
                    throw new IllegalArgumentException("Invalid character: " + currentChar);
            }
        }

        return new Token(TokenType.SEMI, null);
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        Token token = getNextToken();
        while (token.type != TokenType.SEMI) {
            tokens.add(token);
            token = getNextToken();
        }
        tokens.add(token);
        return tokens;
    }
}
