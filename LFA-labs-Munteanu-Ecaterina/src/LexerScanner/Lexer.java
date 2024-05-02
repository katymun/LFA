package LexerScanner;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private String input;
    private int position;
    private List<Token> tokens;

    public Lexer(String input) {
        this.input = input;
        this.position = 0;
        this.tokens = new ArrayList<>();
        tokenize();
    }

    public List<Token> tokenize() {
        while (position < input.length()) {
            char currentChar = input.charAt(position);
            if (Character.isLetter(currentChar)) {
                tokenizeWord();
            } else if (Character.isDigit(currentChar)) {
                tokenizeNumber();
            } else if (currentChar == '=') {
                tokens.add(new Token(TokenType.ASSIGN, "="));
                position++;
            } else if (currentChar == ';') {
                tokens.add(new Token(TokenType.SEMI, ";"));
                position++;
            } else if (currentChar == '(') {
                tokens.add(new Token(TokenType.LPAREN, "("));
                position++;
            } else if (currentChar == ')') {
                tokens.add(new Token(TokenType.RPAREN, ")"));
                position++;
            } else if (currentChar == '+') {
                tokens.add(new Token(TokenType.OP, "+"));
                position++;
            } else if (currentChar == '-') {
                tokens.add(new Token(TokenType.OP, "-"));
                position++;
            } else if (currentChar == '*') {
                tokens.add(new Token(TokenType.OP, "*"));
                position++;
            } else if (currentChar == '/') {
                tokens.add(new Token(TokenType.OP, "/"));
                position++;
            } else if (currentChar == ',') {
                tokens.add(new Token(TokenType.COMMA, ","));
                position++;
            } else if (currentChar == '"') {
                tokenizeString();
            } else if (Character.isWhitespace(currentChar)) {
                position++;
            } else {
                // Invalid character
                throw new RuntimeException("Invalid character: " + currentChar);
            }
        }
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }

    private void tokenizeWord() {
        StringBuilder word = new StringBuilder();
        while (position < input.length() && Character.isLetter(input.charAt(position))) {
            word.append(input.charAt(position));
            position++;
        }
        String wordStr = word.toString();
        if (wordStr.equals("var")) {
            tokens.add(new Token(TokenType.VAR, wordStr));
        } else if (wordStr.equals("print")) {
            tokens.add(new Token(TokenType.PRINT, wordStr));
        } else {
            tokens.add(new Token(TokenType.ID, wordStr));
        }
    }



    private void tokenizeNumber() {
        StringBuilder number = new StringBuilder();
        while (position < input.length() && Character.isDigit(input.charAt(position))) {
            number.append(input.charAt(position));
            position++;
        }
        tokens.add(new Token(TokenType.NUMBER, number.toString()));
    }

    private void tokenizeString() {
        StringBuilder str = new StringBuilder();
        position++; // Skip the opening double quote
        while (position < input.length() && input.charAt(position) != '"') {
            str.append(input.charAt(position));
            position++;
        }
        if (position < input.length() && input.charAt(position) == '"') {
            position++; // Skip the closing double quote
            tokens.add(new Token(TokenType.STRING, str.toString()));
        } else {
            throw new RuntimeException("Unterminated string literal");
        }
    }

    public boolean hasMoreTokens() {
        return position < tokens.size();
    }

    public Token getCurrentToken() {
        return tokens.get(position);
    }

    public void advance() {
        position++;
    }
}