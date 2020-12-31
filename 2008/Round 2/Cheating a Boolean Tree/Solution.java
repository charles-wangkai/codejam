import java.util.Arrays;
import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 1; tc <= N; ++tc) {
      int M = sc.nextInt();
      int V = sc.nextInt();
      int[] G = new int[(M - 1) / 2];
      int[] C = new int[G.length];
      for (int i = 0; i < G.length; ++i) {
        G[i] = sc.nextInt();
        C[i] = sc.nextInt();
      }
      int[] I = new int[(M + 1) / 2];
      for (int i = 0; i < I.length; ++i) {
        I[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %s", tc, solve(M, V, G, C, I)));
    }

    sc.close();
  }

  static String solve(int M, int V, int[] G, int[] C, int[] I) {
    int[][] dp = new int[M + 1][2];
    for (int i = 0; i < dp.length; ++i) {
      Arrays.fill(dp[i], Integer.MAX_VALUE);
    }
    for (int i = M; i >= 1; --i) {
      if (i > G.length) {
        dp[i][I[i - G.length - 1]] = 0;
      } else {
        int leftIndex = i * 2;
        int rightIndex = i * 2 + 1;
        for (int leftValue = 0; leftValue <= 1; ++leftValue) {
          if (dp[leftIndex][leftValue] != Integer.MAX_VALUE) {
            for (int rightValue = 0; rightValue <= 1; ++rightValue) {
              if (dp[rightIndex][rightValue] != Integer.MAX_VALUE) {
                int subDpSum = dp[leftIndex][leftValue] + dp[rightIndex][rightValue];
                int andResult = leftValue & rightValue;
                int orResult = leftValue | rightValue;

                if (G[i - 1] == 1) {
                  dp[i][andResult] = Math.min(dp[i][andResult], subDpSum);

                  if (C[i - 1] == 1) {
                    dp[i][orResult] = Math.min(dp[i][orResult], subDpSum + 1);
                  }
                } else {
                  dp[i][orResult] = Math.min(dp[i][orResult], subDpSum);

                  if (C[i - 1] == 1) {
                    dp[i][andResult] = Math.min(dp[i][andResult], subDpSum + 1);
                  }
                }
              }
            }
          }
        }
      }
    }

    return (dp[1][V] == Integer.MAX_VALUE) ? "IMPOSSIBLE" : String.valueOf(dp[1][V]);
  }
}
