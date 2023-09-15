import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      int[][] matrix = new int[N][N];
      for (int r = 0; r < N; ++r) {
        String line = sc.next();
        for (int c = 0; c < N; ++c) {
          matrix[r][c] = line.charAt(c) - '0';
        }
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(matrix)));
    }

    sc.close();
  }

  static int solve(int[][] matrix) {
    int[] lastOneIndices =
        Arrays.stream(matrix)
            .mapToInt(
                row -> IntStream.range(0, row.length).filter(c -> row[c] == 1).max().orElse(-1))
            .toArray();

    int result = 0;
    for (int i = 0; i < lastOneIndices.length; ++i) {
      int firstIndex = i;
      while (lastOneIndices[firstIndex] >= i + 1) {
        ++firstIndex;
      }

      for (int j = firstIndex; j >= i + 1; --j) {
        swap(lastOneIndices, j, j - 1);
        ++result;
      }
    }

    return result;
  }

  static void swap(int[] a, int index1, int index2) {
    int temp = a[index1];
    a[index1] = a[index2];
    a[index2] = temp;
  }
}
