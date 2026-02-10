import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      String message = sc.next();

      System.out.println(String.format("Case #%d: %d", tc + 1, solve(message)));
    }

    sc.close();
  }

  static long solve(String message) {
    Map<Character, Integer> symbolToDigit = new HashMap<>();
    for (char symbol : message.toCharArray()) {
      if (!symbolToDigit.containsKey(symbol)) {
        int digit;
        if (symbolToDigit.isEmpty()) {
          digit = 1;
        } else if (symbolToDigit.size() == 1) {
          digit = 0;
        } else {
          digit = symbolToDigit.size();
        }

        symbolToDigit.put(symbol, digit);
      }
    }

    int base = Math.max(2, symbolToDigit.size());

    long result = 0;
    for (char symbol : message.toCharArray()) {
      result = result * base + symbolToDigit.get(symbol);
    }

    return result;
  }
}
