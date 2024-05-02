package ParserASTBuild;

class PrintStatement extends ASTNode {
    ASTNode expression;

    public PrintStatement(ASTNode expression) {
        this.expression = expression;
    }

    @Override
    void printTree(int level) {
        printIndent(level);
        System.out.println("PrintStatement");
        expression.printTree(level + 1);
    }
}