import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Solution {
  static final int FULL = 10000;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      int[] A = new int[N];
      int[] B = new int[N];
      int[] C = new int[N];
      for (int i = 0; i < N; ++i) {
        A[i] = sc.nextInt();
        B[i] = sc.nextInt();
        C[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(A, B, C)));
    }

    sc.close();
  }

  static int solve(int[] A, int[] B, int[] C) {
    int N = A.length;

    int result = 0;
    for (int fractionA : A) {
      int[] X = new int[FULL + 2];
      int[] sortedIndices =
          IntStream.range(0, N)
              .filter(i -> A[i] <= fractionA)
              .boxed()
              .sorted(Comparator.comparing(i -> B[i]))
              .mapToInt(x -> x)
              .toArray();
      for (int index : sortedIndices) {
        int fractionB = B[index];
        add(X, C[index] + 1, 1);

        result = Math.max(result, sum(X, FULL - fractionA - fractionB + 1));
      }
    }

    return result;
  }

  static int LSB(int i) {
    return i & -i;
  }

  static int sum(int[] X, int i) {
    int sum = 0;
    while (i > 0) {
      sum += X[i];
      i -= LSB(i);
    }

    return sum;
  }

  static void add(int[] X, int i, int k) {
    while (i < X.length) {
      X[i] += k;
      i += LSB(i);
    }
  }
}
