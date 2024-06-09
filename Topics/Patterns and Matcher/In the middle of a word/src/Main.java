import java.util.*;
import java.util.regex.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String part = scanner.nextLine();
        String line = scanner.nextLine();
        Pattern pattern = Pattern.compile("(?i)\\b\\w*(?<!\\b" + part + ")\\B" + part + "\\B(?!\\w*" + part + "\\b)\\w*\\b");
        Matcher matcher = pattern.matcher(line);
        String result = matcher.find() ? "YES" : "NO";
        System.out.println(result);
    }
}