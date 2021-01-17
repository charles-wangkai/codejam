import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 1; tc <= N; ++tc) {
      int M = sc.nextInt();
      double P = sc.nextDouble();
      int X = sc.nextInt();

      System.out.println(String.format("Case #%d: %.9f", tc, solve(M, P, X)));
    }

    sc.close();
  }

  static double solve(int M, double P, int X) {
    double[][] probs = new double[M + 1][];
    for (int i = 0; i < probs.length; ++i) {
      probs[i] = new double[(1 << i) + 1];
      probs[i][1 << i] = 1;

      if (i != 0) {
        for (int j = 1; j < 1 << i; ++j) {
          for (int k = Math.max(0, j - (1 << (i - 1))); k <= j - k; ++k) {
            probs[i][j] =
                Math.max(probs[i][j], (1 - P) * probs[i - 1][k] + P * probs[i - 1][j - k]);
          }
        }
      }
    }

    return probs[M][(int) (X * (1L << M) / 1_000_000)];
  }
}
