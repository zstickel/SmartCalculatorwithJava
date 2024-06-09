import java.util.Scanner;

class RemoveExtraSpacesProblem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String text = scanner.nextLine();
        String fixedTest = text.replaceAll("\\s+", " ");
        System.out.println(fixedTest);
        // write your code here
    }
}