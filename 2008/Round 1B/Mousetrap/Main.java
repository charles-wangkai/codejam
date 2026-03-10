import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      int K = sc.nextInt();
      int n = sc.nextInt();
      int[] d = new int[n];
      for (int i = 0; i < d.length; ++i) {
        d[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %s", tc + 1, solve(K, d)));
    }

    sc.close();
  }

  static String solve(int K, int[] d) {
    int[] values = new int[K];

    FenwickTree fenwickTree = new FenwickTree(K);
    for (int i = 0; i < K; ++i) {
      fenwickTree.add(i + 1, 1);
    }

    int index = -1;
    for (int value = 1; value <= K; ++value) {
      int step = (value - 1) % (K - value + 1) + 1;
      int maxRight =
          fenwickTree.computePrefixSum(K)
              - ((index == -1) ? 0 : fenwickTree.computePrefixSum(index + 1));
      if (step <= maxRight) {
        index = findNext(fenwickTree, index + 1, K - 1, step);
      } else {
        index = findNext(fenwickTree, 0, index - 1, step - maxRight);
      }

      values[index] = value;
      fenwickTree.add(index + 1, -1);
    }

    return Arrays.stream(d)
        .map(di -> values[di - 1])
        .mapToObj(String::valueOf)
        .collect(Collectors.joining(" "));
  }

  static int findNext(FenwickTree fenwickTree, int lowerIndex, int upperIndex, int targetDiff) {
    int baseSum = (lowerIndex == 0) ? 0 : fenwickTree.computePrefixSum(lowerIndex);
    int result = -1;
    while (lowerIndex <= upperIndex) {
      int middleIndex = (lowerIndex + upperIndex) / 2;
      if (fenwickTree.computePrefixSum(middleIndex + 1) - baseSum >= targetDiff) {
        result = middleIndex;
        upperIndex = middleIndex - 1;
      } else {
        lowerIndex = middleIndex + 1;
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
