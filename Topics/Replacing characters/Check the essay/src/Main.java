import java.util.Scanner;


class CheckTheEssay {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String text = scanner.nextLine();
        String firstChange = text.replaceAll("Franse", "France");
        String secondChange = firstChange.replaceAll("Eifel tower", "Eiffel Tower");
        String  thirdChange = secondChange.replaceAll("19th","XIXth");
        String fourthChange = thirdChange.replaceAll("20th", "XXth");
        String fithChange = fourthChange.replaceAll("21st", "XXIst");
        System.out.println(fithChange);
        // write your code here

    }
}