import java.util.Scanner;

public class Main {
    private static String replace(String input) {
        // write your code here
        return input.replaceAll("lion", "guinea pig");
    }    

    // Don't change the code below
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        System.out.println(replace(line)); 
    }
}