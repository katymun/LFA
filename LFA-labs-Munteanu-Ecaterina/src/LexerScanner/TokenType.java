package LexerScanner;

public enum TokenType {
    VAR,    // var keyword
    ID,     // Identifier
    ASSIGN, // =
    SEMI,   // ;
    OP,     // Arithmetic operator (+, -, *, /)
    LPAREN, // (
    RPAREN, // )
    PRINT,  // print keyword
    COMMA,  // ,
    STRING  // String literal
}
