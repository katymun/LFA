package ParserASTBuild;

import LexerScanner.Token;

class VariableReference extends ASTNode {
    Token variableName;

    public VariableReference(Token variableName) {
        this.variableName = variableName;
    }

    @Override
    void printTree(int level) {
        printIndent(level);
        System.out.println("VariableReference: " + variableName.getValue());
    }
}