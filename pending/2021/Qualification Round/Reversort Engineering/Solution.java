import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      int C = sc.nextInt();

      System.out.println(String.format("Case #%d: %s", tc, solve(N, C)));
    }

    sc.close();
  }

  static String solve(int N, int C) {
    if (C < N - 1 || C > N * (N + 1) / 2 - 1) {
      return "IMPOSSIBLE";
    }

    int[] result = new int[N];
    int[] indices = IntStream.range(0, N).toArray();
    C -= N - 1;
    for (int i = 0; i < N - 1; ++i) {
      int offset = Math.min(C, N - 1 - i);
      C -= offset;
      for (int j = i, k = i + offset; j < k; ++j, --k) {
        int temp = indices[j];
        indices[j] = indices[k];
        indices[k] = temp;
      }
      result[indices[i]] = i + 1;
    }
    result[indices[N - 1]] = N;

    return Arrays.stream(result).mapToObj(String::valueOf).collect(Collectors.joining(" "));
  }
}
