// https://en.wikipedia.org/wiki/Fenwick_tree

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {
  static final int MODULUS = 1_000_000_007;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 1; tc <= N; ++tc) {
      int n = sc.nextInt();
      int m = sc.nextInt();
      int X = sc.nextInt();
      int Y = sc.nextInt();
      int Z = sc.nextInt();
      int[] A = new int[m];
      for (int i = 0; i < A.length; ++i) {
        A[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(n, A, X, Y, Z)));
    }

    sc.close();
  }

  static int solve(int n, int[] A, int X, int Y, int Z) {
    int[] values = compress(generateSpeeds(n, A, X, Y, Z));

    int[] a = new int[values.length + 1];
    for (int value : values) {
      add(a, value, addMod(sum(a, value - 1), 1, MODULUS));
    }

    return sum(a, values.length);
  }

  static int LSB(int i) {
    return i & -i;
  }

  static int sum(int[] a, int i) {
    int sum = 0;
    while (i > 0) {
      sum = addMod(sum, a[i], MODULUS);
      i -= LSB(i);
    }

    return sum;
  }

  static void add(int[] a, int i, int k) {
    while (i < a.length) {
      a[i] = addMod(a[i], k, MODULUS);
      i += LSB(i);
    }
  }

  static int[] compress(int[] speeds) {
    int[] sortedSpeeds = Arrays.stream(speeds).distinct().sorted().toArray();
    Map<Integer, Integer> speedToSortedIndex =
        IntStream.range(0, sortedSpeeds.length)
            .boxed()
            .collect(Collectors.toMap(i -> sortedSpeeds[i], i -> i));

    return Arrays.stream(speeds).map(speed -> speedToSortedIndex.get(speed) + 1).toArray();
  }

  static int[] generateSpeeds(int n, int[] A, int X, int Y, int Z) {
    int[] result = new int[n];
    for (int i = 0; i < result.length; ++i) {
      result[i] = A[i % A.length];
      A[i % A.length] = addMod(multiplyMod(X, A[i % A.length], Z), multiplyMod(Y, i + 1, Z), Z);
    }

    return result;
  }

  static int addMod(int x, int y, int m) {
    return (x + y) % m;
  }

  static int multiplyMod(int x, int y, int m) {
    return (int) ((long) x * y % m);
  }
}
