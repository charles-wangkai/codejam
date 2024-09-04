// https://raw.githubusercontent.com/google/coding-competitions-archive/main/codejam/2009/round_3/football_team/analysis.pdf

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      int[] xs = new int[N];
      int[] ys = new int[N];
      for (int i = 0; i < N; ++i) {
        xs[i] = sc.nextInt();
        ys[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(xs, ys)));
    }

    sc.close();
  }

  static int solve(int[] xs, int[] ys) {
    int N = xs.length;

    int[] sortedIndices =
        IntStream.range(0, N)
            .boxed()
            .sorted(Comparator.comparing(i -> xs[i]))
            .mapToInt(Integer::intValue)
            .toArray();

    boolean[][] edges = new boolean[N][N];
    Map<Integer, Integer> yToLastIndex = new HashMap<>();
    for (int i = sortedIndices.length - 1; i >= 0; --i) {
      for (int dy = -1; dy <= 1; ++dy) {
        int adjY = ys[sortedIndices[i]] + dy;
        if (yToLastIndex.containsKey(adjY)) {
          edges[sortedIndices[i]][yToLastIndex.get(adjY)] = true;
          edges[yToLastIndex.get(adjY)][sortedIndices[i]] = true;
        }
      }

      yToLastIndex.put(ys[sortedIndices[i]], sortedIndices[i]);
    }

    Map<Integer, List<Integer>> yToIndices = new HashMap<>();
    for (int i = 0; i < sortedIndices.length; ++i) {
      yToIndices.putIfAbsent(ys[sortedIndices[i]], new ArrayList<>());
      yToIndices.get(ys[sortedIndices[i]]).add(sortedIndices[i]);
    }

    if (isOneColorable(edges)) {
      return 1;
    }
    if (isTwoColorable(edges, yToIndices)) {
      return 2;
    }
    if (isThreeColorable(edges, yToIndices)) {
      return 3;
    }

    return 4;
  }

  static boolean isOneColorable(boolean[][] edges) {
    int N = edges.length;

    for (int i = 0; i < N; ++i) {
      for (int j = 0; j < N; ++j) {
        if (edges[i][j]) {
          return false;
        }
      }
    }

    return true;
  }

  static boolean isTwoColorable(boolean[][] edges, Map<Integer, List<Integer>> yToIndices) {
    for (int y : yToIndices.keySet()) {
      for (int index : yToIndices.get(y)) {
        if (findConnectedIndices(edges, yToIndices, y - 1, index).size() >= 2
            || findConnectedIndices(edges, yToIndices, y + 1, index).size() >= 2) {
          return false;
        }
      }
    }

    return true;
  }

  static boolean isThreeColorable(boolean[][] edges, Map<Integer, List<Integer>> yToIndices) {
    for (int y : yToIndices.keySet()) {
      List<Integer> indices = yToIndices.get(y);
      for (int i = 1; i < indices.size() - 1; ++i) {
        List<Integer> surroundingIndices = new ArrayList<>();
        surroundingIndices.add(indices.get(i - 1));
        surroundingIndices.addAll(findConnectedIndices(edges, yToIndices, y - 1, indices.get(i)));
        surroundingIndices.add(indices.get(i + 1));
        surroundingIndices.addAll(
            reversed(findConnectedIndices(edges, yToIndices, y + 1, indices.get(i))));

        if (isInnerVertex(edges, surroundingIndices) && surroundingIndices.size() % 2 == 1) {
          return false;
        }
      }
    }

    return true;
  }

  static List<Integer> findConnectedIndices(
      boolean[][] edges, Map<Integer, List<Integer>> yToIndices, int y, int index) {
    return yToIndices.getOrDefault(y, List.of()).stream()
        .filter(p -> edges[index][p])
        .collect(Collectors.toList());
  }

  static List<Integer> reversed(List<Integer> a) {
    Collections.reverse(a);

    return a;
  }

  static boolean isInnerVertex(boolean[][] edges, List<Integer> surroundingIndices) {
    return IntStream.range(0, surroundingIndices.size())
        .allMatch(
            i ->
                edges[surroundingIndices.get(i)][
                    surroundingIndices.get((i + 1) % surroundingIndices.size())]);
  }
}
