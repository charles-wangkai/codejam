// https://codingcompetitions.withgoogle.com/codejam/round/0000000000432cc0/0000000000432a80#analysis

import java.util.Scanner;
import java.util.stream.IntStream;

public class Solution {
  static final int ITERATION_NUM = 100;

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
    double lower = 0;
    double upper = 3_000_000;
    for (int i = 0; i < ITERATION_NUM; ++i) {
      double middle = (lower + upper) / 2;
      if (check(x, y, z, p, middle)) {
        upper = middle;
      } else {
        lower = middle;
      }
    }

    return (lower + upper) / 2;
  }

  static boolean check(int[] x, int[] y, int[] z, int[] p, double power) {
    int N = x.length;

    double A =
        IntStream.range(0, N)
            .mapToDouble(i -> x[i] + y[i] + z[i] - p[i] * power)
            .max()
            .getAsDouble();
    double B =
        IntStream.range(0, N)
            .mapToDouble(i -> x[i] + y[i] + z[i] + p[i] * power)
            .min()
            .getAsDouble();
    double C =
        IntStream.range(0, N)
            .mapToDouble(i -> x[i] + y[i] - z[i] - p[i] * power)
            .max()
            .getAsDouble();
    double D =
        IntStream.range(0, N)
            .mapToDouble(i -> x[i] + y[i] - z[i] + p[i] * power)
            .min()
            .getAsDouble();
    double E =
        IntStream.range(0, N)
            .mapToDouble(i -> x[i] - y[i] + z[i] - p[i] * power)
            .max()
            .getAsDouble();
    double F =
        IntStream.range(0, N)
            .mapToDouble(i -> x[i] - y[i] + z[i] + p[i] * power)
            .min()
            .getAsDouble();
    double G =
        IntStream.range(0, N)
            .mapToDouble(i -> -x[i] + y[i] + z[i] - p[i] * power)
            .max()
            .getAsDouble();
    double H =
        IntStream.range(0, N)
            .mapToDouble(i -> -x[i] + y[i] + z[i] + p[i] * power)
            .min()
            .getAsDouble();

    return A <= B && C <= D && E <= F && G <= H && B - G >= C + E && D + F >= A - H;
  }
}
