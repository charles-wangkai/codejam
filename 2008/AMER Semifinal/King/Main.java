// https://raw.githubusercontent.com/google/coding-competitions-archive/main/codejam/2008/amer_semifinal/king/analysis.pdf

import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 1; tc <= N; ++tc) {
      int R = sc.nextInt();
      int C = sc.nextInt();
      char[][] board = new char[R][C];
      for (int r = 0; r < R; ++r) {
        String line = sc.next();
        for (int c = 0; c < C; ++c) {
          board[r][c] = line.charAt(c);
        }
      }

      System.out.println(String.format("Case #%d: %s", tc, solve(board)));
    }

    sc.close();
  }

  static String solve(char[][] board) {
    int R = board.length;
    int C = board[0].length;

    boolean[][] squares = new boolean[R][C];
    int kingR = -1;
    int kingC = -1;
    for (int r = 0; r < R; ++r) {
      for (int c = 0; c < C; ++c) {
        if (board[r][c] == 'K') {
          kingR = r;
          kingC = c;
        }

        squares[r][c] = board[r][c] != '#';
      }
    }

    int maxMatchingNumWithStart = computeMaxMatchingNum(squares);

    squares[kingR][kingC] = false;
    int maxMatchingNumWithoutStart = computeMaxMatchingNum(squares);

    return (maxMatchingNumWithoutStart == maxMatchingNumWithStart) ? "B" : "A";
  }

  static int computeMaxMatchingNum(boolean[][] squares) {
    int R = squares.length;
    int C = squares[0].length;

    int[] maxMatchingNums = new int[1 << (C + 1)];
    Arrays.fill(maxMatchingNums, -1);
    maxMatchingNums[0] = 0;

    for (int r = 0; r < R; ++r) {
      for (int c = 0; c < C; ++c) {
        int[] nextMaxMatchingNums = new int[maxMatchingNums.length];
        Arrays.fill(nextMaxMatchingNums, -1);

        for (int mask = 0; mask < maxMatchingNums.length; ++mask) {
          if (maxMatchingNums[mask] != -1) {
            if (squares[r][c]) {
              int nextMask = advance(C, mask, 1);
              nextMaxMatchingNums[nextMask] =
                  Math.max(nextMaxMatchingNums[nextMask], maxMatchingNums[mask]);

              if (c != 0 && (mask & 1) == 1) {
                nextMask = advance(C, mask & ~1, 0);
                nextMaxMatchingNums[nextMask] =
                    Math.max(nextMaxMatchingNums[nextMask], maxMatchingNums[mask] + 1);
              }
              if (r != 0 && c != C - 1 && ((mask >> (C - 2)) & 1) == 1) {
                nextMask = advance(C, mask & (~(1 << (C - 2))), 0);
                nextMaxMatchingNums[nextMask] =
                    Math.max(nextMaxMatchingNums[nextMask], maxMatchingNums[mask] + 1);
              }
              if (r != 0 && ((mask >> (C - 1)) & 1) == 1) {
                nextMask = advance(C, mask & (~(1 << (C - 1))), 0);
                nextMaxMatchingNums[nextMask] =
                    Math.max(nextMaxMatchingNums[nextMask], maxMatchingNums[mask] + 1);
              }
              if (r != 0 && c != 0 && ((mask >> C) & 1) == 1) {
                nextMask = advance(C, mask & (~(1 << C)), 0);
                nextMaxMatchingNums[nextMask] =
                    Math.max(nextMaxMatchingNums[nextMask], maxMatchingNums[mask] + 1);
              }
            } else {
              int nextMask = advance(C, mask, 0);
              nextMaxMatchingNums[nextMask] =
                  Math.max(nextMaxMatchingNums[nextMask], maxMatchingNums[mask]);
            }
          }
        }

        maxMatchingNums = nextMaxMatchingNums;
      }
    }

    return Arrays.stream(maxMatchingNums).max().getAsInt();
  }

  static int advance(int C, int mask, int bit) {
    return ((mask << 1) + bit) & ((1 << (C + 1)) - 1);
  }
}
