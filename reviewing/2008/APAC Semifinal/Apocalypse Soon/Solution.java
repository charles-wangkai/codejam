import java.util.Scanner;

public class Solution {
  static final int[] R_OFFSETS = {-1, 0, 0, 1};
  static final int[] C_OFFSETS = {0, -1, 1, 0};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int C = sc.nextInt();
      int R = sc.nextInt();
      int myC = sc.nextInt() - 1;
      int myR = sc.nextInt() - 1;
      int[][] S = new int[R][C];
      for (int r = 0; r < R; ++r) {
        for (int c = 0; c < C; ++c) {
          S[r][c] = sc.nextInt();
        }
      }

      System.out.println(String.format("Case #%d: %s", tc, solve(S, myC, myR)));
    }

    sc.close();
  }

  static String solve(int[][] S, int myC, int myR) {
    int maxDayNum = search(S, myR, myC);

    return (maxDayNum == Integer.MAX_VALUE) ? "forever" : String.format("%d day(s)", maxDayNum);
  }

  static int search(int[][] current, int myR, int myC) {
    int R = current.length;
    int C = current[0].length;

    int[][] next = new int[R][C];
    for (int r = 0; r < R; ++r) {
      for (int c = 0; c < C; ++c) {
        next[r][c] = current[r][c];
      }
    }

    for (int r = 0; r < R; ++r) {
      for (int c = 0; c < C; ++c) {
        if (r != myR || c != myC) {
          int direction = -1;
          for (int i = 0; i < R_OFFSETS.length; ++i) {
            int adjR = r + R_OFFSETS[i];
            int adjC = c + C_OFFSETS[i];
            if (adjR >= 0
                && adjR < R
                && adjC >= 0
                && adjC < C
                && (direction == -1
                    || current[adjR][adjC]
                        > current[r + R_OFFSETS[direction]][c + C_OFFSETS[direction]])) {
              direction = i;
            }
          }

          if (direction != -1) {
            next[r + R_OFFSETS[direction]][c + C_OFFSETS[direction]] =
                Math.max(
                    0, next[r + R_OFFSETS[direction]][c + C_OFFSETS[direction]] - current[r][c]);
          }
        }
      }
    }

    if (next[myR][myC] == 0) {
      return 0;
    }
    if (isForever(next, myR, myC)) {
      return Integer.MAX_VALUE;
    }

    int maxSubResult = search(next, myR, myC);
    for (int i = 0; i < R_OFFSETS.length; ++i) {
      int adjR = myR + R_OFFSETS[i];
      int adjC = myC + C_OFFSETS[i];
      if (adjR >= 0 && adjR < R && adjC >= 0 && adjC < C && next[adjR][adjC] != 0) {
        int old = next[adjR][adjC];
        next[adjR][adjC] = Math.max(0, next[adjR][adjC] - current[myR][myC]);
        maxSubResult = Math.max(maxSubResult, search(next, myR, myC));

        next[adjR][adjC] = old;
      }
    }

    return (maxSubResult == Integer.MAX_VALUE) ? Integer.MAX_VALUE : (maxSubResult + 1);
  }

  static boolean isForever(int[][] next, int myR, int myC) {
    int R = next.length;
    int C = next[0].length;

    for (int i = 0; i < R_OFFSETS.length; ++i) {
      int adjR = myR + R_OFFSETS[i];
      int adjC = myC + C_OFFSETS[i];
      if (adjR >= 0 && adjR < R && adjC >= 0 && adjC < C && next[adjR][adjC] != 0) {
        return false;
      }
    }

    return true;
  }
}
