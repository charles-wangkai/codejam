import java.util.Arrays;
import java.util.Scanner;

public class Solution {
  static final int LIMIT = 1_000_000;

  static int[] solutions;

  public static void main(String[] args) {
    buildSolutions();

    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();

      System.out.println(String.format("Case #%d: %d", tc, solve(N)));
    }

    sc.close();
  }

  static void buildSolutions() {
    solutions = new int[LIMIT + 1];
    Arrays.fill(solutions, 1);

    // for (int i = 1; i < solutions.length; ++i) {
    //   for (int j = 2; j < i; ++j) {
    //     if (i % j == 0 && i / j >= 3) {
    //       solutions[i] = Math.max(solutions[i], solutions[i / j - 1] + 1);
    //     }
    //   }
    // }

    for (int k = 2; k < solutions.length; ++k) {
      for (int j = 2; k * j + j < solutions.length; ++j) {
        int i = k * j + j;
        solutions[i] = Math.max(solutions[i], solutions[k] + 1);
      }
    }
  }

  static int solve(int N) {
    int result = 1;
    for (int i = 3; i < N; ++i) {
      if (N % i == 0 && N / i >= 3) {
        result = Math.max(result, solutions[N / i - 1] + 1);
      }
    }

    return result;
  }
}
