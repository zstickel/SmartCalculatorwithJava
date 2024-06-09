import java.math.BigInteger;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        String [] input = scanner.nextLine().split(" ");
        BigInteger a = new BigInteger(input[0]);
        BigInteger b = new BigInteger(input[1]);
        BigInteger c = new BigInteger(input[2]);
        BigInteger d = new BigInteger(input[3]);
        System.out.println(a.negate().multiply(b).add(c).subtract(d));
    }
}