package ParserASTBuild;

import LexerScanner.Lexer;
import LexerScanner.Token;
import LexerScanner.TokenType;

import java.util.ArrayList;
import java.util.List;

class Parser {
    private List<Token> tokens;
    private int position = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<ASTNode> parse() {
        List<ASTNode> statements = new ArrayList<>();
        while (currentToken().getType() != TokenType.EOF) {
            statements.add(parseStatement());
        }
        return statements;
    }

    private ASTNode parseStatement() {
        if (currentToken().getType() == TokenType.VAR) {
            return parseVariableDeclaration();
        } else if (currentToken().getType() == TokenType.PRINT) {
            return parsePrintStatement();
        }
        throw new RuntimeException("Unexpected token: " + currentToken());
    }

    private VariableDeclaration parseVariableDeclaration() {
        advance(); // Skip 'var'
        Token varName = currentToken();
        advance(); // Skip variable name
        advance(); // Skip '='
        ASTNode value = parseExpression();
        advance(); // Skip ';'
        return new VariableDeclaration(varName, value);
    }

    private PrintStatement parsePrintStatement() {
        advance(); // Skip 'print'
        ASTNode expression = parseExpression();
        advance(); // Skip ';'
        return new PrintStatement(expression);
    }

    private ASTNode parseExpression() {
        ASTNode result = parseTerm();
        while (currentToken().getType() == TokenType.OP) {
            Token op = currentToken();
            advance(); // Skip operator
            ASTNode right = parseTerm();
            result = new BinaryOperation(result, op, right);
        }
        return result;
    }

    private ASTNode parseTerm() {
        if (currentToken().getType() == TokenType.NUMBER) {
            Token number = currentToken();
            advance(); // Advance past the number
            return new NumericLiteral(number);
        } else if (currentToken().getType() == TokenType.ID) {
            Token varName = currentToken();
            advance(); // Advance past the ID
            return new VariableReference(varName);
        } else if (currentToken().getType() == TokenType.LPAREN) {
            advance(); // Skip the opening parenthesis
            ASTNode expr = parseExpression(); // Parse the expression inside the parentheses
            if (currentToken().getType() != TokenType.RPAREN) {
                throw new RuntimeException("Missing closing parenthesis");
            }
            advance(); // Skip the closing parenthesis
            return expr;
        }
        throw new RuntimeException("Unexpected token in term: " + currentToken());
    }
    private Token currentToken() {
        return position < tokens.size() ? tokens.get(position) : new Token(TokenType.EOF, "");
    }

    private void advance() {
        if (position < tokens.size()) position++;
    }
}