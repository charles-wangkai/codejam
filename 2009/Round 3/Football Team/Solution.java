import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      Point[] players = new Point[N];
      for (int i = 0; i < players.length; ++i) {
        int x = sc.nextInt();
        int y = sc.nextInt();

        players[i] = new Point(x, y);
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(players)));
    }

    sc.close();
  }

  static int solve(Point[] players) {
    int N = players.length;

    Arrays.sort(players, Comparator.comparing(p -> p.x));

    boolean[][] edges = new boolean[N][N];
    Map<Integer, Integer> yToLastIndex = new HashMap<>();
    for (int i = players.length - 1; i >= 0; --i) {
      for (int dy = -1; dy <= 1; ++dy) {
        int adjY = players[i].y + dy;
        if (yToLastIndex.containsKey(adjY)) {
          edges[i][yToLastIndex.get(adjY)] = true;
          edges[yToLastIndex.get(adjY)][i] = true;
        }
      }

      yToLastIndex.put(players[i].y, i);
    }

    Map<Integer, List<Integer>> yToIndices = new HashMap<>();
    for (int i = 0; i < players.length; ++i) {
      if (!yToIndices.containsKey(players[i].y)) {
        yToIndices.put(players[i].y, new ArrayList<>());
      }
      yToIndices.get(players[i].y).add(i);
    }

    if (isOneColorable(edges)) {
      return 1;
    } else if (isTwoColorable(edges, yToIndices)) {
      return 2;
    } else if (isThreeColorable(edges, yToIndices)) {
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
      for (int center : yToIndices.get(y)) {
        if (findConnectedIndices(edges, yToIndices, y - 1, center).size() >= 2
            || findConnectedIndices(edges, yToIndices, y + 1, center).size() >= 2) {
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

        if (isInnerVertex(edges, surroundingIndices) && surroundingIndices.size() % 2 != 0) {
          return false;
        }
      }
    }

    return true;
  }

  static List<Integer> findConnectedIndices(
      boolean[][] edges, Map<Integer, List<Integer>> yToIndices, int y, int center) {
    return yToIndices.getOrDefault(y, List.of()).stream()
        .filter(x -> edges[center][x])
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

class Point {
  int x;
  int y;

  Point(int x, int y) {
    this.x = x;
    this.y = y;
  }
}
