import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      int[] x = new int[N];
      int[] y = new int[N];
      int[] z = new int[N];
      int[] p = new int[N];
      for (int i = 0; i < N; ++i) {
        x[i] = sc.nextInt();
        y[i] = sc.nextInt();
        z[i] = sc.nextInt();
        p[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %.9f", tc, solve(x, y, z, p)));
    }

    sc.close();
  }

  static double solve(int[] x, int[] y, int[] z, int[] p) {
    double result = 0;
    for (int i = 0; i < x.length; ++i) {
      for (int j = i + 1; j < x.length; ++j) {
        result =
            Math.max(
                result,
                (double) (Math.abs(x[i] - x[j]) + Math.abs(y[i] - y[j]) + Math.abs(z[i] - z[j]))
                    / (p[i] + p[j]));
      }
    }

    return result;
  }
}
