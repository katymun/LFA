package ParserASTBuild;

import LexerScanner.Token;

class NumericLiteral extends ASTNode {
    Token value;

    public NumericLiteral(Token value) {
        this.value = value;
    }

    @Override
    void printTree(int level) {
        printIndent(level);
        System.out.println("NumericLiteral: " + value.getValue());
    }
}
