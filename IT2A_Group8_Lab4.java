/*  STACK APPLICATION CONVERSION MENU PROGRAM
    Group 8
    Authors: Domingo, Mario Jr. C. (Leader)
             Dela Cruz, Bic Julian (Members)
    Laboratory Exercise #4
    Date: 10/26/2025

*/

import java.util.Scanner;
import java.util.Stack;

public class IT2A_Group8_Lab4 {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;

        while (true) {
            clearConsole();
            System.out.println("-----------------------------------------------------");
            System.out.println("           STACK APPLICATION CONVERSION MENU         ");
            System.out.println("-----------------------------------------------------\n");
            System.out.println("[1] Infix to Postfix");
            System.out.println("[2] Infix to Prefix");
            System.out.println("[3] Postfix to Infix");
            System.out.println("[0] Stop");
            System.out.print("Enter Choice: ");

            // Validate input once
            Integer validatedChoice = getValidatedInput();

            // If validation failed, go back to menu
            if (validatedChoice == null) {
                continue;
            }

            choice = validatedChoice;

            switch (choice) {
                case 1:
                    clearConsole();
                    infixToPostfixMenu();
                    break;
                case 2:
                    clearConsole();
                    infixToPrefixMenu();
                    break;
                case 3:
                    clearConsole();
                    postfixToInfixMenu();
                    break;
                case 0:
                    System.out.println("Exiting the program. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please select from 1, 2, 3, or 0 only.\n");
                    pressEnterToContinue();
                    clearConsole();
            }
        }
    }

    // Input Validation Method
    public static Integer getValidatedInput() {
        String input = scanner.nextLine();

        if (input.isEmpty()) {
            System.out.println("Input cannot be empty. Please enter a valid number.\n");
            pressEnterToContinue();
            clearConsole();
            return null;
        }

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.\n");
            pressEnterToContinue();
            clearConsole();
            return null;
        }
    }

    // Press Enter to Contnue Method
    public static void pressEnterToContinue() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }

    // Clear Console Method
    public static void clearConsole() {
    try {
        // cls command in BlueJ
        if (System.console() == null) {
            System.out.print('\u000c');
            return;
        }

        // cls in windows cmd or vs code
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } else {
            // cls for Linux, macOS, or terminals that support ANSI codes
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }

    } catch (Exception e) {
        System.out.println("Unable to clear console.");
        }
    }


    // Infix to Postfix Menu Method
    public static void infixToPostfixMenu() {
        while (true) {
            System.out.print("Enter the Infix Expression: ");
            String infix = scanner.nextLine();

            String postfix = infixToPostfix(infix);

            System.out.println("\nInfix Expression: " + infix);
            System.out.println("Postfix Expression: " + postfix);
            System.out.println();

            if (!tryAgain()) return;
            clearConsole();
        }
    }

    // Infix to Postfix Method (with validation) - handles actual conversion process
    public static String infixToPostfix(String infix) {
        infix = infix.replaceAll("\\s+", ""); // remove spaces
        Stack<Character> stack = new Stack<>();
        StringBuilder postfix = new StringBuilder();

        // constraints
        if (infix.isEmpty()) return "Error: Expression cannot be empty.";

        if (!Character.isLetter(infix.charAt(0)) && infix.charAt(0) != '(')
            return "Error: Expression must start with an operand or '('";

        if (!Character.isLetter(infix.charAt(infix.length() - 1)) && infix.charAt(infix.length() - 1) != ')')
            return "Error: Expression must end with an operand or ')'";

        int count = 0;
        for (char ch : infix.toCharArray())
            if (Character.isLetter(ch) || isOperator(ch)) count++;
        if (count > 15) return "Error: Expression exceeds 15 operands/operators.";

        char prev = ' ';

        // main scanning loop
        for (int i = 0; i < infix.length(); i++) {
            char ch = infix.charAt(i);

            if (!Character.isLetter(ch) && !isOperator(ch) && ch != '(' && ch != ')')
                return "Error: Invalid character detected.";

            if (Character.isLetter(ch)) {
                if (i > 0 && (Character.isLetter(prev) || prev == ')'))
                    return "Error: Operand cannot follow another operand or ')'.";
                postfix.append(ch);
            }

            else if (isOperator(ch)) {
                if (i == 0 || isOperator(prev) || prev == '(')
                    return "Error: Operator cannot appear at this position.";
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(ch))
                    postfix.append(stack.pop());
                stack.push(ch);
            }

            else if (ch == '(') {
                if (i > 0 && (Character.isLetter(prev) || prev == ')'))
                    return "Error: '(' cannot follow operand or ')'.";
                stack.push(ch);
            }

            else if (ch == ')') {
                if (i > 0 && (isOperator(prev) || prev == '('))
                    return "Error: ')' cannot follow operator or '('.";
                boolean foundOpening = false;
                while (!stack.isEmpty()) {
                    char top = stack.pop();
                    if (top == '(') {
                        foundOpening = true;
                        break;
                    }
                    postfix.append(top);
                }
                if (!foundOpening) return "Error: Unmatched ')'.";
            }

            prev = ch;
        }

        while (!stack.isEmpty()) {
            char top = stack.pop();
            if (top == '(') return "Error: Unmatched '(' found.";
            postfix.append(top);
        }

        // return final postfix string
        return postfix.toString();
    }

    // Helper Methods
    private static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^';
    }

    private static int precedence(char ch) {
        switch (ch) {
            case '+': case '-': return 1;
            case '*': case '/': return 2;
            case '^': return 3;
            default: return -1;
        }
    }

    private static boolean tryAgain() {
        while (true) {
            System.out.print("Try Again (Y/N): ");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("Y")) return true;
            if (input.equals("N")) return false;

            System.out.println("Invalid input. Please enter 'Y' or 'N' only.\n");
        }
    }

    public static void infixToPrefixMenu() {
        while (true) {
            System.out.print("Enter the Infix Expression: ");
            String infix = scanner.nextLine();

            if (infix.trim().isEmpty()) {
                System.out.println("Error: Expression cannot be empty.\n");
                pressEnterToContinue();
                clearConsole();
                continue;
            }

            String prefix = infixToPrefix(infix);
            System.out.println("\nInfix Expression: " + infix);
            System.out.println("Prefix Expression: " + prefix + "\n");

            if (!tryAgain()) return;
            clearConsole();
        }
    }

    public static String infixToPrefix(String infix) {
        infix = infix.replaceAll("\\s+", "");
        if (infix.isEmpty()) return "Error: Expression cannot be empty.";
        String reversed = new StringBuilder(infix).reverse().toString();
        StringBuilder modified = new StringBuilder();

        for (char ch : reversed.toCharArray()) {
            if (ch == '(') modified.append(')');
            else if (ch == ')') modified.append('(');
            else modified.append(ch);
        }

        String postfix = infixToPostfix(modified.toString());
        if (postfix.startsWith("Error")) return postfix;
        return new StringBuilder(postfix).reverse().toString();
    }

    public static void postfixToInfixMenu() {
        while (true) {
            System.out.print("Enter the Postfix Expression: ");
            String postfix = scanner.nextLine();

            if (postfix.trim().isEmpty()) {
                System.out.println("Error: Expression cannot be empty.\n");
                pressEnterToContinue();
                clearConsole();
                continue;
            }

            String infix = postfixToInfix(postfix);
            System.out.println("\nPostfix Expression: " + postfix);
            System.out.println("Infix Expression: " + infix + "\n");

            if (!tryAgain()) return;
            clearConsole();
        }
    }

    public static String postfixToInfix(String postfix) {
        postfix = postfix.replaceAll("\\s+", "");
        if (postfix.isEmpty()) return "Error: Expression cannot be empty.";
        Stack<String> stack = new Stack<>();

        for (char ch : postfix.toCharArray()) {
            if (Character.isLetter(ch)) {
                stack.push(ch + "");
            } else if (isOperator(ch)) {
                if (stack.size() < 2) return "Error: Invalid Postfix Expression.";
                String op2 = stack.pop();
                String op1 = stack.pop();
                String exp = "(" + op1 + ch + op2 + ")";
                stack.push(exp);
            } else {
                return "Error: Invalid character detected.";
            }
        }

        if (stack.size() != 1) return "Error: Invalid Postfix Expression.";
        return stack.pop();
    }

}