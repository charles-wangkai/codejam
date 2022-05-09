// https://en.wikipedia.org/wiki/Fenwick_tree

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int K = sc.nextInt();
      int n = sc.nextInt();
      int[] d = new int[n];
      for (int i = 0; i < d.length; ++i) {
        d[i] = sc.nextInt() - 1;
      }

      System.out.println(String.format("Case #%d: %s", tc, solve(K, d)));
    }

    sc.close();
  }

  static String solve(int K, int[] d) {
    int[] values = new int[K];

    int[] A = new int[Integer.highestOneBit(K) * 2 + 1];
    for (int i = 0; i < K; ++i) {
      add(A, i, 1);
    }

    int index = -1;
    for (int value = 1; value <= K; ++value) {
      int step = (value - 1) % (K - value + 1) + 1;
      int maxRight = prefixSum(A, K - 1) - ((index == -1) ? 0 : prefixSum(A, index));
      if (step <= maxRight) {
        index = findNext(A, index + 1, K - 1, step);
      } else {
        index = findNext(A, 0, index - 1, step - maxRight);
      }

      values[index] = value;
      add(A, index, -1);
    }

    return Arrays.stream(d)
        .map(i -> values[i])
        .mapToObj(String::valueOf)
        .collect(Collectors.joining(" "));
  }

  static int findNext(int[] A, int lowerIndex, int upperIndex, int targetDiff) {
    int baseSum = (lowerIndex == 0) ? 0 : prefixSum(A, lowerIndex - 1);
    int result = -1;
    while (lowerIndex <= upperIndex) {
      int middleIndex = (lowerIndex + upperIndex) / 2;
      if (prefixSum(A, middleIndex) - baseSum >= targetDiff) {
        result = middleIndex;
        upperIndex = middleIndex - 1;
      } else {
        lowerIndex = middleIndex + 1;
      }
    }

    return result;
  }

  static int LSB(int x) {
    return x & -x;
  }

  static void add(int[] A, int i, int delta) {
    if (i == 0) {
      A[0] += delta;
      return;
    }
    for (; i < A.length; i += LSB(i)) A[i] += delta;
  }

  static int prefixSum(int[] A, int i) {
    int sum = A[0];
    for (; i != 0; i -= LSB(i)) sum += A[i];
    return sum;
  }
}
