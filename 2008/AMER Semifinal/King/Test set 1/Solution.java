import java.util.Scanner;

public class Solution {
  static final int[] R_OFFSETS = {-1, -1, 0, 1, 1, 1, 0, -1};
  static final int[] C_OFFSETS = {0, 1, 1, 1, 0, -1, -1, -1};

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

    int code = 0;
    int row = -1;
    int col = -1;
    for (int r = R - 1; r >= 0; --r) {
      for (int c = C - 1; c >= 0; --c) {
        if (board[r][c] == 'K') {
          row = r;
          col = c;
        }

        code = code * 2 + (board[r][c] == '.' ? 1 : 0);
      }
    }

    return isAliceWin(new Boolean[R][C][1 << (R * C)][2], row, col, code, 0) ? "A" : "B";
  }

  static boolean isAliceWin(Boolean[][][][] aliceWins, int row, int col, int code, int turn) {
    int R = aliceWins.length;
    int C = aliceWins[0].length;

    int rest = code;
    boolean[][] board = new boolean[R][C];
    for (int r = 0; r < R; ++r) {
      for (int c = 0; c < C; ++c) {
        board[r][c] = rest % 2 != 0;
        rest /= 2;
      }
    }

    if (aliceWins[row][col][code][turn] == null) {
      if (turn == 0) {
        aliceWins[row][col][code][turn] = false;
        for (int i = 0; i < R_OFFSETS.length; ++i) {
          int adjR = row + R_OFFSETS[i];
          int adjC = col + C_OFFSETS[i];
          if (adjR >= 0
              && adjR < R
              && adjC >= 0
              && adjC < C
              && board[adjR][adjC]
              && isAliceWin(aliceWins, adjR, adjC, code - (1 << (adjR * C + adjC)), 1 - turn)) {
            aliceWins[row][col][code][turn] = true;
          }
        }
      } else {
        aliceWins[row][col][code][turn] = true;
        for (int i = 0; i < R_OFFSETS.length; ++i) {
          int adjR = row + R_OFFSETS[i];
          int adjC = col + C_OFFSETS[i];
          if (adjR >= 0
              && adjR < R
              && adjC >= 0
              && adjC < C
              && board[adjR][adjC]
              && !isAliceWin(aliceWins, adjR, adjC, code - (1 << (adjR * C + adjC)), 1 - turn)) {
            aliceWins[row][col][code][turn] = false;
          }
        }
      }
    }

    return aliceWins[row][col][code][turn];
  }
}
