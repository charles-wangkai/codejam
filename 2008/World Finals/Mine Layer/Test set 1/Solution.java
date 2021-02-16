import java.util.Arrays;
import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 1; tc <= N; ++tc) {
      int R = sc.nextInt();
      int C = sc.nextInt();
      int[][] clues = new int[R][C];
      for (int r = 0; r < R; ++r) {
        for (int c = 0; c < C; ++c) {
          clues[r][c] = sc.nextInt();
        }
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(clues)));
    }

    sc.close();
  }

  static int solve(int[][] clues) {
    int R = clues.length;
    int C = clues[0].length;

    return search(clues, new int[R][C], 0, 0);
  }

  static int search(int[][] clues, int[][] grid, int r, int c) {
    int R = clues.length;
    int C = clues[0].length;

    if (c == C) {
      return search(clues, grid, r + 1, 0);
    }
    if (r == R) {
      return (fill(clues, grid) && isMatch(clues, grid)) ? Arrays.stream(grid[R / 2]).sum() : -1;
    }

    int result = search(clues, grid, r, c + 1);
    if (r % 2 != 0 || c % 2 != 0) {
      grid[r][c] = 1;
      result = Math.max(result, search(clues, grid, r, c + 1));
      grid[r][c] = 0;
    }

    return result;
  }

  static boolean fill(int[][] clues, int[][] grid) {
    int R = clues.length;
    int C = clues[0].length;

    for (int r = 0; r < R; r += 2) {
      for (int c = 0; c < C; c += 2) {
        grid[r][c] = clues[r][c];
        for (int i = -1; i <= 1; ++i) {
          for (int j = -1; j <= 1; ++j) {
            if (i != 0 || j != 0) {
              int adjR = r + i;
              int adjC = c + j;
              if (adjR >= 0 && adjR < R && adjC >= 0 && adjC < C) {
                grid[r][c] -= grid[adjR][adjC];
              }
            }
          }
        }

        if (grid[r][c] != 0 && grid[r][c] != 1) {
          return false;
        }
      }
    }

    return true;
  }

  static boolean isMatch(int[][] clues, int[][] grid) {
    int R = clues.length;
    int C = clues[0].length;

    for (int r = 0; r < R; ++r) {
      for (int c = 0; c < C; ++c) {
        int clue = 0;
        for (int i = -1; i <= 1; ++i) {
          for (int j = -1; j <= 1; ++j) {
            int adjR = r + i;
            int adjC = c + j;
            if (adjR >= 0 && adjR < R && adjC >= 0 && adjC < C) {
              clue += grid[adjR][adjC];
            }
          }
        }

        if (clue != clues[r][c]) {
          return false;
        }
      }
    }

    return true;
  }
}
