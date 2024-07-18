import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {
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

    int[] rowClues =
        IntStream.range(0, R).map(r -> computeLineSum(clues[r], (C % 3 == 0) ? 1 : 0)).toArray();
    int total = computeLineSum(rowClues, (rowClues.length % 3 == 0) ? 1 : 0);

    int half = R / 2;
    int[] topHalf = IntStream.range(0, half).map(i -> rowClues[i]).toArray();
    int[] downHalf = IntStream.range(0, half).map(i -> rowClues[rowClues.length - 1 - i]).toArray();

    if (half % 3 == 0) {
      return total - computeLineSum(topHalf, 1) - computeLineSum(downHalf, 1);
    }
    if (half % 3 == 1) {
      return computeLineSum(topHalf, 0) + computeLineSum(downHalf, 0) - total;
    }

    return total - computeLineSum(topHalf, 0) - computeLineSum(downHalf, 0);
  }

  static int computeLineSum(int[] lineClues, int beginIndex) {
    return IntStream.iterate(beginIndex, i -> i + 3)
        .takeWhile(i -> i < lineClues.length)
        .map(i -> lineClues[i])
        .sum();
  }
}
