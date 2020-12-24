import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 1; tc <= N; ++tc) {
      int n = sc.nextInt();
      int A = sc.nextInt();
      int B = sc.nextInt();
      int C = sc.nextInt();
      int D = sc.nextInt();
      int x0 = sc.nextInt();
      int y0 = sc.nextInt();
      int M = sc.nextInt();

      System.out.println(String.format("Case #%d: %d", tc, solve(n, A, B, C, D, x0, y0, M)));
    }

    sc.close();
  }

  static long solve(int n, int A, int B, int C, int D, int x0, int y0, int M) {
    int[] X = new int[n];
    int[] Y = new int[n];
    X[0] = x0;
    Y[0] = y0;
    for (int i = 1; i < n; ++i) {
      X[i] = (int) (((long) A * X[i - 1] + B) % M);
      Y[i] = (int) (((long) C * Y[i - 1] + D) % M);
    }

    int[] counts = new int[9];
    for (int i = 0; i < n; ++i) {
      ++counts[X[i] % 3 * 3 + Y[i] % 3];
    }

    long result = 0;
    for (int i = 0; i < counts.length; ++i) {
      for (int j = i; j < counts.length; ++j) {
        for (int k = j; k < counts.length; ++k) {
          if ((i / 3 + j / 3 + k / 3) % 3 == 0 && (i % 3 + j % 3 + k % 3) % 3 == 0) {
            if (i == k) {
              result += counts[i] * (counts[i] - 1L) * (counts[i] - 2) / 6;
            } else if (i == j) {
              result += counts[i] * (counts[i] - 1L) / 2 * counts[k];
            } else if (j == k) {
              result += counts[j] * (counts[j] - 1L) / 2 * counts[i];
            } else {
              result += (long) counts[i] * counts[j] * counts[k];
            }
          }
        }
      }
    }

    return result;
  }
}
