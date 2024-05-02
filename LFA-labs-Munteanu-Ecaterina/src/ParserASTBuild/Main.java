package ParserASTBuild;

import LexerScanner.Lexer;
import LexerScanner.Token;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String input = "var x = 4; var y = 7; var sum = x + y; print(sum);";
        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.tokenize();
        Parser parser = new Parser(tokens);
        List<ASTNode> astNodes = parser.parse();

        for (ASTNode node : astNodes) {
            node.printTree(0);
        }
    }
}
