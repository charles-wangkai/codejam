import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

public class Solution {
  static final int END = 10000;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      String[] C = new String[N];
      int[] A = new int[N];
      int[] B = new int[N];
      for (int i = 0; i < N; ++i) {
        C[i] = sc.next();
        A[i] = sc.nextInt();
        B[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %s", tc, solve(C, A, B)));
    }

    sc.close();
  }

  static String solve(String[] C, int[] A, int[] B) {
    int[] sortedIndices =
        IntStream.range(0, C.length)
            .boxed()
            .sorted(Comparator.comparing(i -> A[i]))
            .mapToInt(x -> x)
            .toArray();
    String[] colors = Arrays.stream(C).distinct().toArray(String[]::new);

    int minOfferNum = Integer.MAX_VALUE;
    for (int i = 0; i < colors.length; ++i) {
      for (int j = i; j < colors.length; ++j) {
        for (int k = j; k < colors.length; ++k) {
          minOfferNum =
              Math.min(
                  minOfferNum,
                  computeOfferNum(
                      C,
                      A,
                      B,
                      sortedIndices,
                      new HashSet<>(Arrays.asList(colors[i], colors[j], colors[k]))));
        }
      }
    }

    return (minOfferNum == Integer.MAX_VALUE) ? "IMPOSSIBLE" : String.valueOf(minOfferNum);
  }

  static int computeOfferNum(
      String[] C, int[] A, int[] B, int[] sortedIndices, Set<String> chosenColors) {
    PriorityQueue<Integer> rights = new PriorityQueue<>(Comparator.reverseOrder());
    int index = 0;
    int last = 0;
    int offerNum = 0;
    while (true) {
      while (index != sortedIndices.length
          && (!chosenColors.contains(C[sortedIndices[index]])
              || A[sortedIndices[index]] <= last + 1)) {
        if (chosenColors.contains(C[sortedIndices[index]])) {
          rights.offer(B[sortedIndices[index]]);
        }

        ++index;
      }

      if (rights.isEmpty() || rights.peek() <= last) {
        return Integer.MAX_VALUE;
      }

      ++offerNum;
      last = rights.poll();
      if (last == END) {
        return offerNum;
      }
    }
  }
}
