package ParserASTBuild;

import LexerScanner.Token;

class VariableDeclaration extends ASTNode {
    Token variableName;
    ASTNode value;

    public VariableDeclaration(Token variableName, ASTNode value) {
        this.variableName = variableName;
        this.value = value;
    }

    @Override
    void printTree(int level) {
        printIndent(level);
        System.out.println("VariableDeclaration: " + variableName.getValue());
        value.printTree(level + 1);
    }
}