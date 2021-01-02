import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 1; tc <= N; ++tc) {
      int k = sc.nextInt();
      String S = sc.next();

      System.out.println(String.format("Case #%d: %d", tc, solve(k, S)));
    }

    sc.close();
  }

  static int solve(int k, String S) {
    int[][] deltas = new int[k][k];
    for (int i = 0; i < k; ++i) {
      for (int j = 0; j < k; ++j) {
        if (j != i) {
          for (int beginIndex = 0; beginIndex != S.length(); beginIndex += k) {
            deltas[i][j] += (S.charAt(beginIndex + i) == S.charAt(beginIndex + j)) ? 0 : 1;
          }
        }
      }
    }

    int[][][] dp = new int[1 << k][k][k];
    for (int code = 0; code < 1 << k; ++code) {
      for (int left = 0; left < k; ++left) {
        for (int right = 0; right < k; ++right) {
          dp[code][left][right] = Integer.MAX_VALUE;
        }
      }
    }

    for (int left = 0; left < k; ++left) {
      for (int right = 0; right < k; ++right) {
        if (right != left) {
          dp[(1 << left) + (1 << right)][left][right] = 1;
          for (int beginIndex1 = 0, beginIndex2 = k;
              beginIndex2 != S.length();
              beginIndex1 += k, beginIndex2 += k) {
            if (S.charAt(beginIndex1 + right) != S.charAt(beginIndex2 + left)) {
              ++dp[(1 << left) + (1 << right)][left][right];
            }
          }
        }
      }
    }

    for (int code = 0; code < 1 << k; ++code) {
      if (Integer.bitCount(code) >= 3) {
        for (int right = 0; right < k; ++right) {
          if ((code & (1 << right)) != 0) {
            for (int left = 0; left < k; ++left) {
              if (left != right && (code & (1 << left)) != 0) {
                int prevCode = code - (1 << left);
                for (int prevLeft = 0; prevLeft < k; ++prevLeft) {
                  if (prevLeft != right
                      && (prevCode & (1 << prevLeft)) != 0
                      && dp[prevCode][prevLeft][right] != Integer.MAX_VALUE) {
                    dp[code][left][right] =
                        Math.min(
                            dp[code][left][right],
                            dp[prevCode][prevLeft][right] + deltas[prevLeft][left]);
                  }
                }
              }
            }
          }
        }
      }
    }

    int result = Integer.MAX_VALUE;
    for (int left = 0; left < k; ++left) {
      for (int right = 0; right < k; ++right) {
        if (right != left) {
          result = Math.min(result, dp[(1 << k) - 1][left][right] + deltas[left][right]);
        }
      }
    }

    return result;
  }
}
