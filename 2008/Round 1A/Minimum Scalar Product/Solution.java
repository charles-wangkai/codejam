import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int n = sc.nextInt();
      int[] x = new int[n];
      for (int i = 0; i < x.length; ++i) {
        x[i] = sc.nextInt();
      }
      int[] y = new int[n];
      for (int i = 0; i < y.length; ++i) {
        y[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(x, y)));
    }

    sc.close();
  }

  static long solve(int[] x, int[] y) {
    Arrays.sort(x);
    Arrays.sort(y);

    return IntStream.range(0, x.length).mapToLong(i -> (long) x[i] * y[y.length - 1 - i]).sum();
  }
}
