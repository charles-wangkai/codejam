import java.util.Scanner;

public class Solution {
  static final int[] PRIMES = {2, 3, 5, 7};

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
    long[][][][][] dp = new long[s.length() + 1][PRIMES[0]][PRIMES[1]][PRIMES[2]][PRIMES[3]];
    dp[0][0][0][0][0] = 1;
    for (int length = 0; length < s.length(); ++length) {
      int[] remainders = new int[PRIMES.length];
      for (int nextLength = 1; length + nextLength <= s.length(); ++nextLength) {
        for (int i = 0; i < remainders.length; ++i) {
          remainders[i] =
              (remainders[i] * 10 + s.charAt(length + nextLength - 1) - '0') % PRIMES[i];
        }

        for (int i = 0; i < PRIMES[0]; ++i) {
          for (int j = 0; j < PRIMES[1]; ++j) {
            for (int k = 0; k < PRIMES[2]; ++k) {
              for (int p = 0; p < PRIMES[3]; ++p) {
                dp[length + nextLength][(i + remainders[0]) % PRIMES[0]][
                        (j + remainders[1]) % PRIMES[1]][(k + remainders[2]) % PRIMES[2]][
                        (p + remainders[3]) % PRIMES[3]] +=
                    dp[length][i][j][k][p];
                if (length != 0) {
                  dp[length + nextLength][(i - remainders[0] + PRIMES[0]) % PRIMES[0]][
                          (j - remainders[1] + PRIMES[1]) % PRIMES[1]][
                          (k - remainders[2] + PRIMES[2]) % PRIMES[2]][
                          (p - remainders[3] + PRIMES[3]) % PRIMES[3]] +=
                      dp[length][i][j][k][p];
                }
              }
            }
          }
        }
      }
    }

    long result = 0;
    for (int i = 0; i < PRIMES[0]; ++i) {
      for (int j = 0; j < PRIMES[1]; ++j) {
        for (int k = 0; k < PRIMES[2]; ++k) {
          for (int p = 0; p < PRIMES[3]; ++p) {
            if (i == 0 || j == 0 || k == 0 || p == 0) {
              result += dp[s.length()][i][j][k][p];
            }
          }
        }
      }
    }

    return result;
  }
}
