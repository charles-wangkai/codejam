import java.util.Arrays;
import java.util.Scanner;

public class Solution {
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
    int row = -1;
    int col = -1;
    for (int r = 0; r < R; ++r) {
      for (int c = 0; c < C; ++c) {
        if (board[r][c] == 'K') {
          row = r;
          col = c;
        }

        squares[r][c] = board[r][c] != '#';
      }
    }

    int maxMatchingNumWithStart = computeMaxMatchingNum(squares);

    squares[row][col] = false;
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

        for (int code = 0; code < maxMatchingNums.length; ++code) {
          if (maxMatchingNums[code] != -1) {
            boolean[] prevSquares = decode(C + 1, code);

            if (squares[r][c]) {
              int nextCode = encode(advance(prevSquares, true));
              nextMaxMatchingNums[nextCode] =
                  Math.max(nextMaxMatchingNums[nextCode], maxMatchingNums[code]);

              if (c != 0 && prevSquares[0]) {
                prevSquares[0] = false;

                nextCode = encode(advance(prevSquares, false));
                nextMaxMatchingNums[nextCode] =
                    Math.max(nextMaxMatchingNums[nextCode], maxMatchingNums[code] + 1);

                prevSquares[0] = true;
              }
              if (r != 0 && c != C - 1 && prevSquares[C - 2]) {
                prevSquares[C - 2] = false;

                nextCode = encode(advance(prevSquares, false));
                nextMaxMatchingNums[nextCode] =
                    Math.max(nextMaxMatchingNums[nextCode], maxMatchingNums[code] + 1);

                prevSquares[C - 2] = true;
              }
              if (r != 0 && prevSquares[C - 1]) {
                prevSquares[C - 1] = false;

                nextCode = encode(advance(prevSquares, false));
                nextMaxMatchingNums[nextCode] =
                    Math.max(nextMaxMatchingNums[nextCode], maxMatchingNums[code] + 1);

                prevSquares[C - 1] = true;
              }
              if (r != 0 && c != 0 && prevSquares[C]) {
                prevSquares[C] = false;

                nextCode = encode(advance(prevSquares, false));
                nextMaxMatchingNums[nextCode] =
                    Math.max(nextMaxMatchingNums[nextCode], maxMatchingNums[code] + 1);

                prevSquares[C] = true;
              }
            } else {
              int nextCode = encode(advance(prevSquares, false));
              nextMaxMatchingNums[nextCode] =
                  Math.max(nextMaxMatchingNums[nextCode], maxMatchingNums[code]);
            }
          }
        }

        maxMatchingNums = nextMaxMatchingNums;
      }
    }

    return Arrays.stream(maxMatchingNums).max().getAsInt();
  }

  static boolean[] decode(int size, int code) {
    boolean[] prevSquares = new boolean[size];
    for (int i = 0; i < prevSquares.length; ++i) {
      prevSquares[i] = (code & 1) != 0;
      code >>= 1;
    }

    return prevSquares;
  }

  static boolean[] advance(boolean[] prevSquares, boolean square) {
    boolean[] result = new boolean[prevSquares.length];
    result[0] = square;
    for (int i = 1; i < result.length; ++i) {
      result[i] = prevSquares[i - 1];
    }

    return result;
  }

  static int encode(boolean[] prevSquares) {
    int code = 0;
    for (int i = prevSquares.length - 1; i >= 0; --i) {
      code = code * 2 + (prevSquares[i] ? 1 : 0);
    }

    return code;
  }
}
