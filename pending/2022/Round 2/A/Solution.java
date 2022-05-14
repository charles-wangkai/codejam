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

    List<String> result = new ArrayList<>();
    int r = 0;
    int c = 0;
    int direction = 0;
    int step = N - 1;
    while (r != N / 2 || c != N / 2) {
      // System.err.printf("%2$s: %1$s\n", r, "r");
      // System.err.printf("%2$s: %1$s\n", c, "c");
      // System.err.printf("%2$s: %1$s\n", direction, "direction");
      // System.err.printf("%2$s: %1$s\n", get(N, r, c), "get(N,r,c)");

      int currentValue = get(N, r, c);

      int shortcutR = r + R_OFFSETS[(direction + 1) % R_OFFSETS.length];
      int shortcutC = c + C_OFFSETS[(direction + 1) % R_OFFSETS.length];
      int shortcutValue = get(N, shortcutR, shortcutC);

      if (shortcutValue > currentValue + 1 && shortcutValue - currentValue - 1 <= diff) {
        result.add(String.format("%d %d", currentValue, shortcutValue));
        diff -= shortcutValue - currentValue - 1;
        r = shortcutR;
        c = shortcutC;

        if (direction != 3) {
          step -= 2;
        } else {
          --step;
        }

        if (r == N / 2 && c == N / 2) {
          break;
        }

        r += R_OFFSETS[direction];
        c += C_OFFSETS[direction];
      } else {
        r += R_OFFSETS[direction] * step;
        c += C_OFFSETS[direction] * step;

        direction = (direction + 1) % R_OFFSETS.length;

        r += R_OFFSETS[direction];
        c += C_OFFSETS[direction];

        if (direction == 1 || direction == 3) {
          --step;
        }
      }
    }

    return (diff == 0)
        ? String.format("%d\n%s", result.size(), String.join("\n", result))
        : "IMPOSSIBLE";
  }

  static int get(int N, int r, int c) {
    int level = Math.min(Math.min(r, c), Math.min(N - 1 - r, N - 1 - c));

    int value = 1;
    for (int i = 0; i < level; ++i) {
      value += 4 * (N - 1 - i * 2);
    }

    int currentR = level;
    int currentC = level;
    int direction = 0;
    int step = N - 1 - level * 2;
    while (currentR != r || currentC != c) {
      if (step == 0) {
        direction = (direction + 1) % R_OFFSETS.length;
        step = N - 1 - level * 2;
      }

      currentR += R_OFFSETS[direction];
      currentC += C_OFFSETS[direction];
      ++value;
      --step;
    }

    return value;
  }
}