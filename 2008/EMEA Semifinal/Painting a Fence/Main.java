import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {
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
    int N = C.length;

    int[] sortedIndices =
        IntStream.range(0, N)
            .boxed()
            .sorted(Comparator.comparing(i -> A[i]))
            .mapToInt(Integer::intValue)
            .toArray();

    Map<String, List<Integer>> colorToIndices = new HashMap<>();
    for (int index : sortedIndices) {
      colorToIndices.putIfAbsent(C[index], new ArrayList<>());
      colorToIndices.get(C[index]).add(index);
    }

    int result = Integer.MAX_VALUE;
    List<String> colors = List.copyOf(colorToIndices.keySet());
    for (int i = 0; i < colors.size(); ++i) {
      result = Math.min(result, computeOfferNum(A, B, List.of(colorToIndices.get(colors.get(i)))));
    }
    for (int i = 0; i < colors.size(); ++i) {
      for (int j = i + 1; j < colors.size(); ++j) {
        result =
            Math.min(
                result,
                computeOfferNum(
                    A,
                    B,
                    List.of(colorToIndices.get(colors.get(i)), colorToIndices.get(colors.get(j)))));
      }
    }
    for (int i = 0; i < colors.size(); ++i) {
      for (int j = i + 1; j < colors.size(); ++j) {
        for (int k = j + 1; k < colors.size(); ++k) {
          result =
              Math.min(
                  result,
                  computeOfferNum(
                      A,
                      B,
                      List.of(
                          colorToIndices.get(colors.get(i)),
                          colorToIndices.get(colors.get(j)),
                          colorToIndices.get(colors.get(k)))));
        }
      }
    }

    return (result == Integer.MAX_VALUE) ? "IMPOSSIBLE" : String.valueOf(result);
  }

  static int computeOfferNum(int[] A, int[] B, List<List<Integer>> indicesList) {
    int result = 0;
    int[] positions = new int[indicesList.size()];
    int last = 0;
    while (true) {
      int nextLast = last;
      for (int i = 0; i < positions.length; ++i) {
        while (positions[i] != indicesList.get(i).size()
            && A[indicesList.get(i).get(positions[i])] <= last + 1) {
          nextLast = Math.max(nextLast, B[indicesList.get(i).get(positions[i])]);
          ++positions[i];
        }
      }
      if (nextLast == last) {
        return Integer.MAX_VALUE;
      }

      last = nextLast;
      ++result;
      if (last == END) {
        return result;
      }
    }
  }
}
