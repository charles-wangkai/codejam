import java.util.Scanner;
import java.util.stream.IntStream;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int M = sc.nextInt();
      int[] P = new int[M];
      long[] N = new long[M];
      for (int i = 0; i < M; ++i) {
        P[i] = sc.nextInt();
        N[i] = sc.nextLong();
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(P, N)));
    }

    sc.close();
  }

  static long solve(int[] P, long[] N) {
    return search(P, N, IntStream.range(0, P.length).mapToLong(i -> P[i] * N[i]).sum(), 1, 0);
  }

  static long search(int[] P, long[] N, long sum, long product, int index) {
    long result = (sum == product) ? sum : 0;
    if (index != P.length) {
      result = Math.max(result, search(P, N, sum, product, index + 1));

      for (int i = 0; i < N[index]; ++i) {
        sum -= P[index];
        product *= P[index];

        if (sum < product) {
          break;
        }

        result = Math.max(result, search(P, N, sum, product, index + 1));
      }
    }

    return result;
  }
}
