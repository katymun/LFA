package ParserASTBuild;

import java.util.ArrayList;
import java.util.List;

abstract class ASTNode {
    abstract void printTree(int level);
    void printIndent(int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
    }
}