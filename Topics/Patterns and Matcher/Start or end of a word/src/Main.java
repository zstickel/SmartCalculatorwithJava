import java.util.*;
import java.util.regex.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String part = scanner.nextLine();
        String line = scanner.nextLine();

        // write your code here
        Pattern pattern = Pattern.compile("\\b" + part + "\\w*|\\b\\w*" + part + "\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        String returnValue = matcher.find() ? "YES" : "NO";
        System.out.println(returnValue);
    }
}