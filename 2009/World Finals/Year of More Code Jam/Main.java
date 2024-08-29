import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 1; tc <= C; ++tc) {
      int N = sc.nextInt();
      int T = sc.nextInt();
      int[][] d = new int[T][];
      for (int i = 0; i < d.length; ++i) {
        int m = sc.nextInt();
        d[i] = new int[m];
        d[i][0] = 1;
        for (int j = 1; j < d[i].length; ++j) {
          d[i][j] = sc.nextInt();
        }
      }

      System.out.println(String.format("Case #%d: %s", tc, solve(N, d)));
    }

    sc.close();
  }

  static String solve(int N, int[][] d) {
    int T = d.length;

    int maxD = Arrays.stream(d).mapToInt(di -> Arrays.stream(di).max().getAsInt()).max().getAsInt();

    int[][] E = new int[T][maxD + 1];
    for (int i = 0; i < E.length; ++i) {
      for (int j = 0; j < d[i].length; ++j) {
        ++E[i][d[i][j]];
      }
      for (int a = 1; a < E[i].length; ++a) {
        E[i][a] += E[i][a - 1];
      }
    }

    BigInteger numerator = BigInteger.ZERO;
    for (int a = 1; a <= N && a <= maxD; ++a) {
      int sum = 0;
      int sum2 = 0;
      for (int i = 0; i < E.length; ++i) {
        sum += E[i][a];
        sum2 += E[i][a] * E[i][a];
      }

      numerator =
          numerator.add(
              BigInteger.valueOf(sum * sum - sum2 + (long) sum * N)
                  .multiply((a == maxD) ? BigInteger.valueOf(N - maxD + 1) : BigInteger.ONE));
    }

    BigInteger denominator = BigInteger.valueOf((long) N * N);

    BigInteger g = numerator.gcd(denominator);
    numerator = numerator.divide(g);
    denominator = denominator.divide(g);

    return String.format(
        "%d+%d/%d", numerator.divide(denominator), numerator.remainder(denominator), denominator);
  }
}