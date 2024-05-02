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
    EOF,    // End of file (program)
    NUMBER,
    STRING,  // String literal
    PROGRAM
}
