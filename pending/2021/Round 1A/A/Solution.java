import java.math.BigInteger;
import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      int[] X = new int[N];
      for (int i = 0; i < X.length; ++i) {
        X[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(X)));
    }

    sc.close();
  }

  static int solve(int[] X) {
    int result = 0;
    String last = String.valueOf(X[0]);
    for (int i = 1; i < X.length; ++i) {
      String current = String.valueOf(X[i]);

      if (new BigInteger(current).compareTo(new BigInteger(last)) > 0) {
        last = current;
      } else {
        String max = current + "9".repeat(last.length() - current.length());
        if (new BigInteger(max).compareTo(new BigInteger(last)) > 0) {
          result += last.length() - current.length();
          if (last.startsWith(current)) {
            last = new BigInteger(last).add(BigInteger.ONE).toString();
          } else {
            last = current + "0".repeat(last.length() - current.length());
          }
        } else {
          result += last.length() - current.length() + 1;
          last = current + "0".repeat(last.length() - current.length() + 1);
        }
      }
    }

    return result;
  }
}
