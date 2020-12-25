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
        d[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %s", tc, solve(K, d)));
    }

    sc.close();
  }

  static String solve(int K, int[] d) {
    int[] values = new int[K + 1];

    int[] A = new int[K + 1];
    for (int i = 1; i < A.length; ++i) {
      add(A, i, 1);
    }

    int index = 0;
    for (int value = 1; value <= K; ++value) {
      int step = (value - 1) % (K - value + 1) + 1;
      int maxRight = sum(A, K) - sum(A, index);
      if (step <= maxRight) {
        index = findNext(A, index, K, step);
      } else {
        index = findNext(A, 0, index - 1, step - maxRight);
      }

      values[index] = value;
      add(A, index, -1);
    }

    return Arrays.stream(d)
        .mapToObj(i -> String.valueOf(values[i]))
        .collect(Collectors.joining(" "));
  }

  static int findNext(int[] A, int lowerIndex, int upperIndex, int targetDiff) {
    int baseSum = sum(A, lowerIndex);
    int result = -1;
    while (lowerIndex <= upperIndex) {
      int middleIndex = (lowerIndex + upperIndex) / 2;
      if (sum(A, middleIndex) - baseSum >= targetDiff) {
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

  static int sum(int[] A, int i) {
    int sum = 0;
    while (i > 0) {
      sum += A[i];
      i -= LSB(i);
    }

    return sum;
  }

  static void add(int[] A, int i, int k) {
    while (i < A.length) {
      A[i] += k;
      i += LSB(i);
    }
  }
}
