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

      int[] binaryIndexedTree = new int[Integer.highestOneBit(FULL + 1) * 2 + 1];
      for (int index : sortedIndices) {
        add(binaryIndexedTree, C[index] + 1, 1);

        result = Math.max(result, computeSum(binaryIndexedTree, FULL - fractionA - B[index] + 1));
      }
    }

    return result;
  }

  static void add(int[] binaryIndexedTree, int i, int x) {
    while (i < binaryIndexedTree.length) {
      binaryIndexedTree[i] += x;
      i += i & -i;
    }
  }

  static int computeSum(int[] binaryIndexedTree, int i) {
    int result = 0;
    while (i > 0) {
      result += binaryIndexedTree[i];
      i -= i & -i;
    }

    return result;
  }
}
