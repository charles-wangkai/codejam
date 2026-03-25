// https://raw.githubusercontent.com/google/coding-competitions-archive/main/codejam/2009/world_finals/doubly-sorted_grid/analysis.pdf

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  static final ModInt MOD_INT = new ModInt(10007);

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      int R = sc.nextInt();
      int C = sc.nextInt();
      char[][] grid = new char[R][C];
      for (int r = 0; r < R; ++r) {
        String line = sc.next();
        for (int c = 0; c < C; ++c) {
          grid[r][c] = line.charAt(c);
        }
      }

      System.out.println(String.format("Case #%d: %d", tc + 1, solve(grid)));
    }

    sc.close();
  }

  static int solve(char[][] grid) {
    int R = grid.length;
    int C = grid[0].length;

    int[][] dp = new int[1 << (R + C)][26];
    int lastPathMask = -1;
    for (int pathMask = 0; pathMask < 1 << (R + C); ++pathMask) {
      if (Integer.bitCount(pathMask) == C) {
        for (char letter = 'a'; letter <= 'z'; ++letter) {
          if (checkPreFilled(grid, pathMask, letter)) {
            List<Integer> turnBs = new ArrayList<>();
            List<Integer> turnRs = new ArrayList<>();
            List<Integer> turnCs = new ArrayList<>();
            int r = R - 1;
            int c = 0;
            for (int b = R + C - 1; b >= 1; --b) {
              if (((pathMask >> b) & 1) == 1) {
                if (((pathMask >> (b - 1)) & 1) == 0) {
                  turnBs.add(b);
                  turnRs.add(r);
                  turnCs.add(c);
                }

                ++c;
              } else {
                --r;
              }
            }

            if (turnBs.isEmpty()) {
              dp[pathMask][letter - 'a'] = 1;
            } else {
              if (letter != 'a') {
                dp[pathMask][letter - 'a'] = dp[pathMask][letter - 'a' - 1];
              }

              for (int turnMask = 1; turnMask < 1 << turnBs.size(); ++turnMask) {
                int prevPathMask =
                    computePrevPathMask(grid, pathMask, letter, turnBs, turnRs, turnCs, turnMask);
                if (prevPathMask != -1) {
                  dp[pathMask][letter - 'a'] =
                      MOD_INT.addMod(
                          dp[pathMask][letter - 'a'],
                          ((Integer.bitCount(turnMask) % 2 == 1) ? 1 : -1)
                              * dp[prevPathMask][letter - 'a']);
                }
              }
            }
          }
        }

        lastPathMask = pathMask;
      }
    }

    return dp[lastPathMask][25];
  }

  static int computePrevPathMask(
      char[][] grid,
      int pathMask,
      char letter,
      List<Integer> turnBs,
      List<Integer> turnRs,
      List<Integer> turnCs,
      int turnMask) {
    int result = pathMask;
    for (int i = 0; i < turnBs.size(); ++i) {
      if (((turnMask >> i) & 1) == 1) {
        if (grid[turnRs.get(i)][turnCs.get(i)] != '.'
            && grid[turnRs.get(i)][turnCs.get(i)] != letter) {
          return -1;
        }

        result += (1 << (turnBs.get(i) - 1)) - (1 << turnBs.get(i));
      }
    }

    return result;
  }

  static boolean checkPreFilled(char[][] grid, int pathMask, char letter) {
    int R = grid.length;
    int C = grid[0].length;

    int maxR = R - 1;
    int c = 0;
    for (int b = R + C - 1; b >= 0; --b) {
      if (((pathMask >> b) & 1) == 1) {
        for (int r = 0; r <= maxR; ++r) {
          if (grid[r][c] != '.' && grid[r][c] > letter) {
            return false;
          }
        }

        ++c;
      } else {
        --maxR;
      }
    }

    return true;
  }
}

class ModInt {
  int modulus;

  ModInt(int modulus) {
    this.modulus = modulus;
  }

  int mod(long x) {
    return Math.floorMod(x, modulus);
  }

  int modInv(int x) {
    return BigInteger.valueOf(x).modInverse(BigInteger.valueOf(modulus)).intValue();
  }

  int addMod(int x, int y) {
    return mod(x + y);
  }

  int multiplyMod(int x, int y) {
    return mod((long) x * y);
  }

  int divideMod(int x, int y) {
    return multiplyMod(x, modInv(y));
  }

  int powMod(int base, long exponent) {
    if (exponent == 0) {
      return 1;
    }

    return multiplyMod(
        (exponent % 2 == 0) ? 1 : base, powMod(multiplyMod(base, base), exponent / 2));
  }
}
