package LexerScanner;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your statements (press Enter after each statement), and type 'exit' to end:");
        System.out.print("console> ");

        StringBuilder inputBuilder = new StringBuilder();
        String line;
        while (!(line = scanner.nextLine()).equals("exit")) {
            inputBuilder.append(line).append("\n");

            String text = inputBuilder.toString();
            Lexer lexer = new Lexer(text);
            List<Token> tokens = lexer.tokenize();

            System.out.println("Tokens: " + tokens);

            inputBuilder.setLength(0);

            System.out.print("\nconsole> ");
        }

        scanner.close();
    }
}
