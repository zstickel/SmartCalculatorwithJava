package calculator;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static Map <String, BigInteger> variables = new HashMap<>();
    enum TypeInput {
        COMMAND,
        ASSIGNMENT,
        EXPRESSION,
        SEARCHVALUE,
        EMPTY
    }
    private static final Map<String, Integer> precedence = new HashMap<>();
    static {
        precedence.put("+", 1);
        precedence.put("-", 1);
        precedence.put("*", 2);
        precedence.put("/", 2);
        precedence.put("(", 0);
        precedence.put(")",0);// Parentheses are special cases
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while(!exit) {
            String input = scanner.nextLine();
            TypeInput typeInput = getTypeInput(input);
            switch(typeInput){
                case EMPTY -> doNothing();
                case ASSIGNMENT -> assignVariable(input);
                case COMMAND -> exit = runCommand(input);
                case EXPRESSION -> calculateExpression(input);
                case SEARCHVALUE -> printSearchValue(input);

            }
        }
    }

    public static void  replaceVariablesWithValues(String [] expressions){
        for (int i = 0; i< expressions.length; i++){
            if (variables.containsKey(expressions[i])){
                expressions[i] = String.valueOf(variables.get(expressions[i]));
            }
        }
    }
    public static List<String> infixToPostfix(String[] expression) {
        List<String> result = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        replaceVariablesWithValues(expression);
        for (String token : expression) {
            if (isOperand(token)) {
                // Add operands (numbers and variables) to the result as they arrive
                result.add(token);
            } else if (token.equals("(")) {
                // Push left parenthesis on the stack
                stack.push(token);
            } else if (token.equals(")")) {
                // Pop from stack to result until a left parenthesis is encountered
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    result.add(stack.pop());
                }
                stack.pop(); // Discard the left parenthesis
            } else if (isOperator(token)) {
                // Incoming operator has higher precedence than the top of the stack
                while (!stack.isEmpty() && precedence.get(stack.peek()) >= precedence.get(token)) {
                    result.add(stack.pop());
                }
                // Push the incoming operator on the stack
                stack.push(token);
            }
        }

        // Pop all remaining operators from the stack to the result
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }

        return result;
    }

    private static boolean isOperand(String token) {
        // Consider tokens that are not operators and parentheses as operands
        return !isOperator(token) && !token.equals("(") && !token.equals(")");
    }

    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }


    static boolean isIncomingHigherPrecedence(String top, String incoming){
        return (incoming.equals("*") || incoming.equals("/")) && (top.equals("+") || top.equals("-"));
    }


    static void doNothing(){

    }

    static void printSearchValue(String input){
        String trimmed = input.trim();
        if(variables.containsKey(trimmed)){
            System.out.println(variables.get(trimmed));
        }else{
            System.out.println("Unknown variable");
        }
    }

    static void calculateExpression(String input){
        String conditionedInput = conditionOperand(input);
        if (isValidFormula(conditionedInput)) {
            System.out.println(toListAndSum(conditionedInput));
        }else {
            System.out.println("Invalid expression");
        }
    }

    static boolean runCommand(String input){
        switch (input) {
            case "/exit" -> {
                System.out.println("Bye!");
                return true;
            }
            case "/help" ->
                    System.out.println("The program adds or subtracts numbers based on the user input. Two consecutive minus symbols will be turned into a plus symbol. ");
            case "/test" -> {
                String[] infix = {"2","*","(","3","+","4",")","+","1"};
                List<String> postfixList = infixToPostfix(infix);
                System.out.println("Postfix list: " + postfixList);
                System.out.println(sumFromPostFix(postfixList));
            }
            default -> System.out.println("Unknown command");
        }
        return false;

    }

    static BigInteger sumFromPostFix(List<String> postFixList){
        Stack<BigInteger> calculateStack = new Stack<>();
        for (String element : postFixList){
            if (isOperator(element)){
                BigInteger operand2 = calculateStack.pop();
                BigInteger operand1 = calculateStack.pop();
                BigInteger result = performOperation(element, operand1, operand2);
                calculateStack.push(result);
            }else{
                calculateStack.push(new BigInteger(element));
            }
        }
        return calculateStack.pop();
    }



    private static BigInteger performOperation(String operator, BigInteger operand1, BigInteger operand2) {
        switch (operator) {
            case "+":
                return operand1.add(operand2);
            case "-":
                return operand1.subtract(operand2);
            case "*":
                return operand1.multiply(operand2);
            case "/":
                if (operand2.equals(new BigInteger("0"))) {
                    throw new IllegalArgumentException("Cannot divide by zero");
                }
                return operand1.divide(operand2);
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
    static void assignVariable(String input){
        String[] parts = input.split("=");
        if (parts.length == 2) {
            // Trim whitespace around variable name and value
            String variableName = parts[0].trim();
            if (!variableName.matches("[a-zA-Z_][a-zA-Z_]*")) {
                System.out.println("Invalid identifier");
                return;
            }

            BigInteger value;
            if(variables.containsKey(parts[1].trim())){
                value = variables.get(parts[1].trim());
            }else {
                try {
                    // Try to parse the right side as an integer
                    value = new BigInteger(parts[1].trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid assignment");
                    return;
                }
            }
            // Store the variable and its value in the map
            variables.put(variableName, value);
        }  else {
                System.out.println("Invalid format: " + input);
            }
    }


    static TypeInput getTypeInput(String input){
        if(input.isEmpty()){
            return TypeInput.EMPTY;
        }
        if(input.charAt(0) == '/'){
            return  TypeInput.COMMAND;
        }
        if (input.matches("^[^=]*=[^=]*$")){
            return TypeInput.ASSIGNMENT;
        }
        if (input.matches(".*[+\\-*/].*")){
            return TypeInput.EXPRESSION;
        }
        return TypeInput.SEARCHVALUE;
    }

    static int sum(List<Integer> numbers, List<String> operands){
        int sum = numbers.get(0);
        for(int i=0; i< numbers.size()-1; i++){
            if (operands.get(i).equals("+")){
                sum += numbers.get(i+1);
            }else{
                sum -= numbers.get(i+1);
            }
        }
        return sum;
    }


    public static boolean isValidFormula(String expression) {
        // Check for balanced parentheses

        if (!areParenthesesBalanced(expression)) {
            return false;
        }

        // Tokenize the expression
        String[] tokens = expression.split("\\s+");
        Stack<String> stack = new Stack<>();
        String previousToken = "";
        for (String token : tokens) {
            if (isOperator(token)) {
                // An operator must be preceded by a number, variable, or closing parenthesis
                if (previousToken.isEmpty() || isOperator(previousToken) || previousToken.equals("(")) {
                    return false;
                }
            } else if (token.equals("(")) {
                // A left parenthesis can be preceded by an operator or another left parenthesis
                if (!previousToken.isEmpty() && (Character.isDigit(previousToken.charAt(previousToken.length() - 1)) || previousToken.equals(")"))) {
                    return false;
                }
                stack.push(token);
            } else if (token.equals(")")) {
                // A right parenthesis must be preceded by a number, variable, or another right parenthesis
                if (previousToken.isEmpty() || isOperator(previousToken) || previousToken.equals("(")) {
                    return false;
                }
                if (stack.isEmpty() || !stack.pop().equals("(")) {
                    return false;
                }
            } else if (isOperand(token)) {
                // An operand can be preceded by an operator or a left parenthesis
                if (!previousToken.isEmpty() && (Character.isDigit(previousToken.charAt(previousToken.length() - 1)) || previousToken.equals(")"))) {
                    return false;
                }
            }
            previousToken = token;
        }
        // The last token should not be an operator or left parenthesis
        if (isOperator(previousToken) || previousToken.equals("(")) {
            return false;
        }

        return stack.isEmpty(); // Ensure no unmatched left parentheses
    }

    private static boolean areParenthesesBalanced(String expression) {
        Stack<Character> stack = new Stack<>();
        for (char ch : expression.toCharArray()) {
            if (ch == '(') {
                stack.push(ch);
            } else if (ch == ')') {
                if (stack.isEmpty() || stack.pop() != '(') {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    static String conditionOperand(String operand){
        while (operand.contains("--")) {
            operand = operand.replaceAll("--", "+");
        }
        while (operand.contains("++")) {
            operand = operand.replaceAll("\\+\\+", "+");
        }
        while(operand.contains("+-")){
            operand = operand.replaceAll("\\+-","-");
        }
        /*
        while (operand.contains("**")) {
            operand = operand.replaceAll("\\*\\*+", "*");
        }
        // Replace all occurrences of "//" or more with "/"
        while (operand.contains("//")) {
            operand = operand.replaceAll("//+", "/");
        }
         */
        operand = operand.replaceAll("\\(", "( ");
        operand = operand.replaceAll("\\)", " )");
        return operand;
    }
    static BigInteger toListAndSum(String inputString){
        String withoutWhiteSpace = inputString.replaceAll("\\s+", " ");
        String [] inputSplit = withoutWhiteSpace.split(" ");
        List<String> postfixList = infixToPostfix(inputSplit);
        return sumFromPostFix(postfixList);
    }
}


         /*         Code from previous stages
                    String[] inputNumsAsStrings = input.split(" ");
                    if (inputNumsAsStrings.length == 1){
                        if(!inputNumsAsStrings[0].isEmpty()) {
                            System.out.println(inputNumsAsStrings[0]);
                        }
                    }else {
                        int sum = 0;
                        for(String numberAsString : inputNumsAsStrings){
                            sum += Integer.parseInt(numberAsString);
                        }
                        System.out.println(sum);
                    }

                    ORIGINAL REGEX for operations: "^\\s*(-?\\d+|\\+\\s*\\d+)(\\s*[\\+\\-]+\\s*-?\\d+)*\\s*$"
        List<Integer> numberList = new ArrayList<>();
        List<String> operandList = new ArrayList<>();
        boolean oddCycle = true;
        for(String inputItem : inputSplit){

            if (oddCycle){
                if(variables.containsKey(inputItem)){
                    numberList.add(variables.get(inputItem));
                }else {
                    numberList.add(Integer.parseInt(inputItem));
                }
            }else{
                operandList.add(conditionOperand(inputItem));
            }
            oddCycle = !oddCycle;
        }
        return sum(numberList,operandList);

            static boolean isValidFormula(String formula){
        String regex = "^\\s*([a-zA-Z_][a-zA-Z_0-9]*|-?\\d+)(\\s*[\\+\\-]\\s*([a-zA-Z_][a-zA-Z_0-9]*|-?\\d+))*\\s*$";

        // Compile the regular expression pattern
        Pattern pattern = Pattern.compile(regex);

        // Match the formula against the pattern
        Matcher matcher = pattern.matcher(formula);

        // Return true if the formula matches the pattern, indicating it's valid
        return matcher.matches();
    }

                    */