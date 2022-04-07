import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int R = sc.nextInt();
      int C = sc.nextInt();

      System.out.println(String.format("Case #%d:\n%s", tc, solve(R, C)));
    }

    sc.close();
  }

  static String solve(int R, int C) {
    char[][] result = new char[2 * R + 1][2 * C + 1];
    for (int r = 0; r < result.length; ++r) {
      Arrays.fill(result[r], '.');

      if (r % 2 == 0) {
        for (int c = 0; c < result[r].length; ++c) {
          if (r != 0 || c >= 2) {
            result[r][c] = (c % 2 == 0) ? '+' : '-';
          }
        }
      } else {
        for (int c = 0; c < result[r].length; c += 2) {
          if (r != 1 || c >= 2) {
            result[r][c] = '|';
          }
        }
      }
    }

    return Arrays.stream(result).map(String::new).collect(Collectors.joining("\n"));
  }
}