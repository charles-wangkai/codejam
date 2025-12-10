import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
  static final int[] R_OFFSETS = {-1, 0, 0, 1};
  static final int[] C_OFFSETS = {0, -1, 1, 0};
  static final int[] REVERSED_DIRECTIONS = {3, 2, 1, 0};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      int H = sc.nextInt();
      int W = sc.nextInt();
      int[][] altitudes = new int[H][W];
      for (int r = 0; r < H; ++r) {
        for (int c = 0; c < W; ++c) {
          altitudes[r][c] = sc.nextInt();
        }
      }

      System.out.println(String.format("Case #%d:\n%s", tc + 1, solve(altitudes)));
    }

    sc.close();
  }

  static String solve(int[][] altitudes) {
    int H = altitudes.length;
    int W = altitudes[0].length;

    char[][] labels = new char[H][W];
    char label = 'a';
    for (int r = 0; r < H; ++r) {
      for (int c = 0; c < W; ++c) {
        if (labels[r][c] == 0) {
          fill(labels, altitudes, label, r, c);
          ++label;
        }
      }
    }

    return Arrays.stream(labels)
        .map(
            line ->
                IntStream.range(0, W)
                    .mapToObj(c -> line[c])
                    .map(String::valueOf)
                    .collect(Collectors.joining(" ")))
        .collect(Collectors.joining("\n"));
  }

  static void fill(char[][] labels, int[][] altitudes, char label, int r, int c) {
    int H = altitudes.length;
    int W = altitudes[0].length;

    labels[r][c] = label;

    for (int i = 0; i < R_OFFSETS.length; ++i) {
      int adjR = r + R_OFFSETS[i];
      int adjC = c + C_OFFSETS[i];
      if (adjR >= 0
          && adjR < H
          && adjC >= 0
          && adjC < W
          && labels[adjR][adjC] == 0
          && ((altitudes[adjR][adjC] < altitudes[r][c] && findDownDirection(altitudes, r, c) == i)
              || (altitudes[adjR][adjC] > altitudes[r][c]
                  && reverseDirection(findDownDirection(altitudes, adjR, adjC)) == i))) {
        fill(labels, altitudes, label, adjR, adjC);
      }
    }
  }

  static int findDownDirection(int[][] altitudes, int r, int c) {
    int H = altitudes.length;
    int W = altitudes[0].length;

    int result = -1;
    for (int i = 0; i < R_OFFSETS.length; ++i) {
      int adjR = r + R_OFFSETS[i];
      int adjC = c + C_OFFSETS[i];
      if (adjR >= 0
          && adjR < H
          && adjC >= 0
          && adjC < W
          && altitudes[adjR][adjC] < altitudes[r][c]
          && (result == -1
              || altitudes[adjR][adjC] < altitudes[r + R_OFFSETS[result]][c + C_OFFSETS[result]])) {
        result = i;
      }
    }

    return result;
  }

  static int reverseDirection(int direction) {
    return (direction == -1) ? -1 : REVERSED_DIRECTIONS[direction];
  }
}
