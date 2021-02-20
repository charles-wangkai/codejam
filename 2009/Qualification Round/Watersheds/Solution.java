import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {
  static final int[] R_OFFSETS = {-1, 0, 0, 1};
  static final int[] C_OFFSETS = {0, -1, 1, 0};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int H = sc.nextInt();
      int W = sc.nextInt();
      int[][] altitudes = new int[H][W];
      for (int r = 0; r < H; ++r) {
        for (int c = 0; c < W; ++c) {
          altitudes[r][c] = sc.nextInt();
        }
      }

      System.out.println(String.format("Case #%d:\n%s", tc, solve(altitudes)));
    }

    sc.close();
  }

  static String solve(int[][] altitudes) {
    int H = altitudes.length;
    int W = altitudes[0].length;

    char[][] labels = new char[H][W];
    char nextLabel = 'a';
    for (int r = 0; r < H; ++r) {
      for (int c = 0; c < W; ++c) {
        nextLabel = (char) Math.max(nextLabel, fill(labels, altitudes, nextLabel, r, c) + 1);
      }
    }

    return Arrays.stream(labels)
        .map(
            row ->
                IntStream.range(0, W)
                    .mapToObj(i -> String.valueOf(row[i]))
                    .collect(Collectors.joining(" ")))
        .collect(Collectors.joining("\n"));
  }

  static char fill(char[][] labels, int[][] altitudes, char nextLabel, int r, int c) {
    int H = altitudes.length;
    int W = altitudes[0].length;

    if (labels[r][c] != 0) {
      return labels[r][c];
    }

    int direction = -1;
    for (int i = 0; i < R_OFFSETS.length; ++i) {
      int adjR = r + R_OFFSETS[i];
      int adjC = c + C_OFFSETS[i];
      if (adjR >= 0
          && adjR < H
          && adjC >= 0
          && adjC < W
          && altitudes[adjR][adjC] < altitudes[r][c]
          && (direction == -1
              || altitudes[adjR][adjC]
                  < altitudes[r + R_OFFSETS[direction]][c + C_OFFSETS[direction]])) {
        direction = i;
      }
    }

    if (direction == -1) {
      labels[r][c] = nextLabel;
    } else {
      labels[r][c] =
          fill(labels, altitudes, nextLabel, r + R_OFFSETS[direction], c + C_OFFSETS[direction]);
    }

    return labels[r][c];
  }
}
