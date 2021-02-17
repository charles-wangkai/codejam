import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Solution {
  static final int[] R_OFFSETS = {-1, 0, 1, 0};
  static final int[] C_OFFSETS = {0, 1, 0, -1};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      int M = sc.nextInt();
      char[][] cells = new char[N][M];
      for (int r = 0; r < N; ++r) {
        String line = sc.next();
        for (int c = 0; c < M; ++c) {
          cells[r][c] = line.charAt(c);
        }
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(cells)));
    }

    sc.close();
  }

  static int solve(char[][] cells) {
    int N = cells.length;
    int M = cells[0].length;

    List<Point> forests = findForests(cells);
    int[][][] distanceMaps =
        forests.stream().map(forest -> computeDistanceMap(cells, forest)).toArray(int[][][]::new);

    if (forests.size() == 1) {
      return IntStream.range(0, N).map(r -> Arrays.stream(distanceMaps[0][r]).sum()).sum();
    }

    int result = 0;
    for (int r = 0; r < N; ++r) {
      for (int c = 0; c < M; ++c) {
        result += Math.min(distanceMaps[0][r][c], distanceMaps[1][r][c]);
      }
    }

    int distanceBetweenForests = distanceMaps[0][forests.get(1).r][forests.get(1).c];
    for (int i = 0; i <= distanceBetweenForests; ++i) {
      result += Math.max(0, i - Math.min(i, distanceBetweenForests - i));
    }

    return result;
  }

  static int[][] computeDistanceMap(char[][] cells, Point forest) {
    int N = cells.length;
    int M = cells[0].length;

    int[][] distanceMap = new int[N][M];
    boolean[][] visited = new boolean[N][M];
    visited[forest.r][forest.c] = true;
    Queue<Point> queue = new ArrayDeque<>();
    queue.offer(new Point(forest.r, forest.c));

    while (!queue.isEmpty()) {
      Point head = queue.poll();

      for (int i = 0; i < R_OFFSETS.length; ++i) {
        int adjR = head.r + R_OFFSETS[i];
        int adjC = head.c + C_OFFSETS[i];
        if (adjR >= 0
            && adjR < N
            && adjC >= 0
            && adjC < M
            && cells[adjR][adjC] != '.'
            && !visited[adjR][adjC]) {
          distanceMap[adjR][adjC] = distanceMap[head.r][head.c] + 1;
          visited[adjR][adjC] = true;
          queue.offer(new Point(adjR, adjC));
        }
      }
    }

    return distanceMap;
  }

  static List<Point> findForests(char[][] cells) {
    int N = cells.length;
    int M = cells[0].length;

    List<Point> result = new ArrayList<>();
    for (int r = 0; r < N; ++r) {
      for (int c = 0; c < M; ++c) {
        if (cells[r][c] == 'T') {
          result.add(new Point(r, c));
        }
      }
    }

    return result;
  }
}

class Point {
  int r;
  int c;

  Point(int r, int c) {
    this.r = r;
    this.c = c;
  }
}
