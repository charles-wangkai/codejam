import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {
  static final int FULL = 10000;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      int N = sc.nextInt();
      int[] A = new int[N];
      int[] B = new int[N];
      int[] C = new int[N];
      for (int i = 0; i < N; ++i) {
        A[i] = sc.nextInt();
        B[i] = sc.nextInt();
        C[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %d", tc + 1, solve(A, B, C)));
    }

    sc.close();
  }

  static int solve(int[] A, int[] B, int[] C) {
    int N = A.length;

    int result = 0;
    for (int fractionA : A) {
      int[] sortedIndices =
          IntStream.range(0, N)
              .filter(i -> A[i] <= fractionA)
              .boxed()
              .sorted(Comparator.comparing(i -> B[i]))
              .mapToInt(Integer::intValue)
              .toArray();

      FenwickTree fenwickTree = new FenwickTree(FULL + 1);
      for (int index : sortedIndices) {
        fenwickTree.add(C[index] + 1, 1);

        int pos = FULL - fractionA - B[index] + 1;
        if (pos >= 0) {
          result = Math.max(result, fenwickTree.computePrefixSum(pos));
        }
      }
    }

    return result;
  }
}

class FenwickTree {
  int[] a;

  FenwickTree(int size) {
    a = new int[Integer.highestOneBit(size) * 2 + 1];
  }

  void add(int pos, int delta) {
    while (pos < a.length) {
      a[pos] += delta;
      pos += pos & -pos;
    }
  }

  int computePrefixSum(int pos) {
    int result = 0;
    while (pos != 0) {
      result += a[pos];
      pos -= pos & -pos;
    }

    return result;
  }
}
