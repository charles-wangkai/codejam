import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Solution {
  static final int[] PRIMES = {2, 3, 5, 7};
  static final int MODULUS = Arrays.stream(PRIMES).reduce((x, y) -> x * y).getAsInt();

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 1; tc <= N; ++tc) {
      String s = sc.next();

      System.out.println(String.format("Case #%d: %d", tc, solve(s)));
    }

    sc.close();
  }

  static long solve(String s) {
    long[][] dp = new long[s.length() + 1][MODULUS];
    dp[0][0] = 1;
    for (int length = 0; length < s.length(); ++length) {
      int value = 0;
      for (int nextLength = 1; length + nextLength <= s.length(); ++nextLength) {
        value = addMod(value * 10, s.charAt(length + nextLength - 1) - '0');

        for (int prev = 0; prev < MODULUS; ++prev) {
          dp[length + nextLength][addMod(prev, value)] += dp[length][prev];
          if (length != 0) {
            dp[length + nextLength][addMod(prev, -value)] += dp[length][prev];
          }
        }
      }
    }

    return IntStream.range(0, MODULUS)
        .filter(i -> Arrays.stream(PRIMES).anyMatch(prime -> i % prime == 0))
        .mapToLong(i -> dp[s.length()][i])
        .sum();
  }

  static int addMod(int x, int y) {
    return Math.floorMod(x + y, MODULUS);
  }
}
