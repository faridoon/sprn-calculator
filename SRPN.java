import java.util.EmptyStackException;
import java.util.Stack;

public class SRPN {

    private static final int MAX_INT = 2147483647;
    private static final int MIN_INT = -2147483648;
    private Stack<Integer> mainStack = new Stack<>();
    private String[] allowedOperators = {"+", "-", "*", "/", "%", "^"};
    private Stack<Integer> infixOperandsStack = new Stack<>();
    private Stack<Character> infixOperatorsStack = new Stack<>();
    private Stack<Integer> randomNums = new Stack<>();

    public SRPN() {
        generateRandomNums(randomNums);
    }

    /***
     * Resets Stack and pushes 23 random integers to it
     * @param randStack the stack to be reset and re-populated
     */
    private void generateRandomNums(Stack<Integer> randStack) {
        randStack.clear();
        randStack.push(1804289383);
        randStack.push(846930886);
        randStack.push(1681692777);
        randStack.push(1714636915);
        randStack.push(1957747793);
        randStack.push(424238335);
        randStack.push(719885386);
        randStack.push(1649760492);
        randStack.push(596516649);
        randStack.push(1189641421);
        randStack.push(1025202362);
        randStack.push(1350490027);
        randStack.push(783368690);
        randStack.push(1102520059);
        randStack.push(2044897763);
        randStack.push(1967513926);
        randStack.push(1365180540);
        randStack.push(1540383426);
        randStack.push(304089172);
        randStack.push(1303455736);
        randStack.push(35005211);
        randStack.push(521595368);
        randStack.push(1804289383);
    }

    public void processCommand(String s) {
        //Remove comments from the user inputted string using the regex pattern replacing it with empty string
        s = s.replaceAll("#.*", "");
        //One line of expression may include multiple parts/tokens so we want to
        //split the line/string into an array of Strings, and then iterate over them to perform operations
        String[] userInput = s.split("\\s+");
        String el;
        for (int i = 0; i < userInput.length; i++) {
            el = userInput[i];
            if (!isInteger(el) && isInfix(el.replaceAll("=$", ""))) {
                int result = performInfixOp(el);
                mainStack.push(result);
                if (el.charAt(el.length() - 1) == '=') {
                    System.out.println(result);
                }
            } else if (isInteger(el)) {
                int num = Integer.parseInt(el);
                mainStack.push(num);
            } else {
                String item;
                for (int j = 0; j < el.length(); j++) {
                    item = String.valueOf(el.charAt(j));
                    if (isOperator(item)) {
                        if (mainStack.size() >= 2) {
                            int num1 = mainStack.pop();
                            int num2 = mainStack.pop();
                            int result = performPostfixOperation(num1, num2, item);
                            mainStack.push(result);
                        } else {
                            System.out.println("Stack underflow.");
                        }
                    } else if (item.equals("d")) {
                        for (int x : mainStack) {
                            System.out.println(x);
                        }
                    } else if (item.equals("r")) {
                        try {
                            int randNum = randomNums.pop();
                            System.out.println(randNum);
                            mainStack.push(randNum);
                        } catch (EmptyStackException e) {
                            System.out.println("Stack overflow.");
                        }
                    } else if (item.equals("=")) {
                        System.out.println(mainStack.peek());
                    } else {
                        System.out.println(String.format("Unrecognised operator or operand \"%s\".", item));
                    }
                }
            }

        }

    }

    /***
     * Checks if the given string looks like an infix notation
     * @param s string that may contain an operator, looking like 3+4-
     * @return true or false
     */
    private boolean isInfix(String s) {
        if (s.length() > 1) {
            //we only want to check if there are more than 1 characters per expression
            for (int i = 0; i < s.length(); i++) {
                for (String x : allowedOperators) {
                    if (s.charAt(i) == x.charAt(0)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /***
     * Executes the given infix expression, and returns its result
     * @param s infix expression e.g. 5+3
     * @return result of operation as Integer e.g. 8
     */
    private int performInfixOp(String s) {
        String operand = "";
        String el;
        int result = 0;
        //iterate over every single character in the given string
        //to separate operands from operators. Later, we will need these two stacks for actual infix operations
        for (int i = 0; i < s.length(); i++) {
            el = String.valueOf(s.charAt(i));
            if (isInteger(el)) {
                operand += el;
            } else {
                infixOperatorsStack.push(s.charAt(i));
                if (operand.length() > 0) {
                    infixOperandsStack.push(Integer.parseInt(operand));
                }
                operand = "";
            }
        }
        if (operand.length() > 0) {
            infixOperandsStack.push(Integer.parseInt(operand));
        }

        //perform operations until we are left with 2 operands in the stack
        //that would mean we can return result back to the calling function
        while (infixOperandsStack.size() >= 2 && infixOperatorsStack.size() > 0) {
            int num1 = infixOperandsStack.get(0);
            int num2 = infixOperandsStack.get(1);
            char op = infixOperatorsStack.get(0);
            if (op == '+') {
                result = num2 + num1;
                removeStackElements(infixOperandsStack, 2);
                removeStackElements(infixOperatorsStack, 1);
                infixOperandsStack.add(0, result);
            } else if (op == '-') {
                result = num1 - num2;
                removeStackElements(infixOperandsStack, 2);
                removeStackElements(infixOperatorsStack, 1);
                infixOperandsStack.add(0, result);
            } else if (op == '*') {
                result = num2 * num1;
                removeStackElements(infixOperandsStack, 2);
                removeStackElements(infixOperatorsStack, 1);
                infixOperandsStack.add(0, result);
            } else if (op == '/') {
                result = num2 / num1;
                removeStackElements(infixOperandsStack, 2);
                removeStackElements(infixOperatorsStack, 1);
                infixOperandsStack.add(0, result);
            } else if (op == '%') {
                result = num2 % num1;
                removeStackElements(infixOperandsStack, 2);
                removeStackElements(infixOperatorsStack, 1);
                infixOperandsStack.add(0, result);
            } else if (op == '^') {
                result = (int) Math.pow(num2, num1);
                removeStackElements(infixOperandsStack, 2);
                removeStackElements(infixOperatorsStack, 1);
                infixOperandsStack.add(0, result);
            }
        }
        return result;
    }

    /***
     * Removes the first x number of items from stack
     * @param stack stack from which you remove items
     * @param count number of items you want to remove
     */
    private void removeStackElements(Stack stack, int count) {
        while (count > 0) {
            stack.remove(0);
            count--;
        }
    }

    /***
     * Performs reverse postfix notation operation on the given operands
     * Also caps the max and min result to 2147483647 and -2147483648 respectively
     * @param num1 operand 1
     * @param num2 operand 2
     * @param op operation e.g. +
     * @return result as int
     */
    private int performPostfixOperation(int num1, int num2, String op) {
        switch (op) {
            case "+":
                if ((long) num2 + num1 > MAX_INT) {
                    return MAX_INT;
                } else {
                    return num2 + num1;
                }
            case "-":
                if ((long) num2 - num1 < MIN_INT) {
                    return MIN_INT;
                } else {
                    return num2 - num1;
                }
            case "*":
                if ((long) num2 * num1 > MAX_INT) {
                    return MAX_INT;
                } else {
                    return num2 * num1;
                }
            case "/":
                try {
                    return num2 / num1;
                } catch (ArithmeticException e) {
                    System.out.println("Divide by 0.");
                    return 0;
                }
            case "%":
                return num2 % num1;
            case "^":
                return (int) Math.pow(num2, num1);
            default:
                return -1;
        }
    }

    /***
     * Checks if a given string is int or not
     * @param s string to check if an integer
     * @return true or false
     */
    private boolean isInteger(String s) {
        return s.matches("-?\\d+") ? true : false;
    }

    /***
     * Checks if given string is an operator like +, -, %
     * @param s string to check if an operator
     * @return true or false
     */
    private boolean isOperator(String s) {
        for (int i = 0; i < allowedOperators.length; i++) {
            if (s.equals(allowedOperators[i])) {
                return true;
            }
        }
        return false;
    }

}
