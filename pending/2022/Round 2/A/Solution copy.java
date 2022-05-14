import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solution {
  static int[] R_OFFSETS = {0, 1, 0, -1};
  static int[] C_OFFSETS = {1, 0, -1, 0};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      int K = sc.nextInt();

      System.out.println(String.format("Case #%d: %s", tc, solve(N, K)));
    }

    sc.close();
  }

  static String solve(int N, int K) {
    int diff = N * N - 1 - K;

    int[][] grid = new int[N][N];
    int r = 0;
    int c = -1;
    int direction = 0;
    for (int i = 1; i <= N * N; ++i) {
      r += R_OFFSETS[direction];
      c += C_OFFSETS[direction];
      if (!(r >= 0 && r < N && c >= 0 && c < N && grid[r][c] == 0)) {
        r -= R_OFFSETS[direction];
        c -= C_OFFSETS[direction];
        direction = (direction + 1) % R_OFFSETS.length;
        r += R_OFFSETS[direction];
        c += C_OFFSETS[direction];
      }

      grid[r][c] = i;
    }

    // System.err.printf("%2$s: %1$s\n", diff, "diff");

    List<String> result = new ArrayList<>();
    r = 0;
    c = 0;
    while (r != N / 2 || c != N / 2) {
      direction = 0;
      int nextR = r + R_OFFSETS[direction];
      int nextC = c + C_OFFSETS[direction];
      while (!(nextR >= 0
          && nextR < N
          && nextC >= 0
          && nextC < N
          && grid[nextR][nextC] == grid[r][c] + 1)) {
        direction = (direction + 1) % R_OFFSETS.length;
        nextR = r + R_OFFSETS[direction];
        nextC = c + C_OFFSETS[direction];
      }

      // System.err.printf("%2$s: %1$s\n", r, "r");
      // System.err.printf("%2$s: %1$s\n", c, "c");
      // System.err.printf("%2$s: %1$s\n", nextR, "nextR");
      // System.err.printf("%2$s: %1$s\n", nextC, "nextC");

      int shortcutR = r + R_OFFSETS[(direction + 1) % R_OFFSETS.length];
      int shortcutC = c + C_OFFSETS[(direction + 1) % R_OFFSETS.length];

      // System.err.printf("%2$s: %1$s\n", shortcutR, "shortcutR");
      // System.err.printf("%2$s: %1$s\n", shortcutC, "shortcutC");
      if (grid[shortcutR][shortcutC] > grid[nextR][nextC] + 1
          && grid[shortcutR][shortcutC] - grid[nextR][nextC] <= diff) {
        result.add(String.format("%d %d", grid[r][c], grid[shortcutR][shortcutC]));
        diff -= grid[shortcutR][shortcutC] - grid[nextR][nextC];
        r = shortcutR;
        c = shortcutC;
        // System.err.printf("%2$s: %1$s\n", diff, "diff");
      } else {
        r += R_OFFSETS[direction];
        c += C_OFFSETS[direction];
      }
    }

    return (diff == 0)
        ? String.format("%d\n%s", result.size(), String.join("\n", result))
        : "IMPOSSIBLE";
  }
}