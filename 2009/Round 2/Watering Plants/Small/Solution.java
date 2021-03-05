import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 1; tc <= C; ++tc) {
      int N = sc.nextInt();
      int[] X = new int[N];
      int[] Y = new int[N];
      int[] R = new int[N];
      for (int i = 0; i < N; ++i) {
        X[i] = sc.nextInt();
        Y[i] = sc.nextInt();
        R[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %.9f", tc, solve(X, Y, R)));
    }

    sc.close();
  }

  static double solve(int[] X, int[] Y, int[] R) {
    int N = X.length;

    double result = Arrays.stream(R).max().getAsInt();
    if (N == 3) {
      result =
          Math.max(
              result,
              IntStream.range(0, N)
                  .mapToDouble(
                      i ->
                          computeRadius(
                              X[i], Y[i], R[i], X[(i + 1) % N], Y[(i + 1) % N], R[(i + 1) % N]))
                  .min()
                  .getAsDouble());
    }

    return result;
  }

  static double computeRadius(int x1, int y1, int r1, int x2, int y2, int r2) {
    return (Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) + r1 + r2) / 2;
  }
}
