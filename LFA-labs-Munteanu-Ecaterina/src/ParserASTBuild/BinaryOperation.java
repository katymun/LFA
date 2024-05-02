package ParserASTBuild;

import LexerScanner.Token;

class BinaryOperation extends ASTNode {
    ASTNode left;
    Token operator;
    ASTNode right;

    public BinaryOperation(ASTNode left, Token operator, ASTNode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    void printTree(int level) {
        printIndent(level);
        System.out.println("BinaryOperation: " + operator.getValue());
        left.printTree(level + 1);
        right.printTree(level + 1);
    }
}
