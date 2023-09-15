// https://en.wikipedia.org/wiki/Rearrangement_inequality

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {
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
    int[] sortedX = Arrays.stream(x).boxed().sorted().mapToInt(a -> a).toArray();
    int[] sortedY =
        Arrays.stream(y).boxed().sorted(Comparator.reverseOrder()).mapToInt(a -> a).toArray();

    return IntStream.range(0, sortedX.length).mapToLong(i -> (long) sortedX[i] * sortedY[i]).sum();
  }
}
