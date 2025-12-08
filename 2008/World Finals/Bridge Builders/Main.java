import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

public class Main {
  static final int[] R_OFFSETS = {-1, 0, 1, 0};
  static final int[] C_OFFSETS = {0, 1, 0, -1};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      int N = sc.nextInt();
      int M = sc.nextInt();
      char[][] cells = new char[N][M];
      for (int r = 0; r < N; ++r) {
        String line = sc.next();
        for (int c = 0; c < M; ++c) {
          cells[r][c] = line.charAt(c);
        }
      }

      System.out.println(String.format("Case #%d: %d", tc + 1, solve(cells)));
    }

    sc.close();
  }

  static int solve(char[][] cells) {
    int N = cells.length;
    int M = cells[0].length;

    int[][] minDistances = new int[N][M];
    for (int r = 0; r < N; ++r) {
      for (int c = 0; c < M; ++c) {
        minDistances[r][c] = (cells[r][c] == '.') ? 0 : Integer.MAX_VALUE;
      }
    }

    int result = 0;
    boolean[][] visitedForests = new boolean[N][M];
    while (true) {
      Element nearest = findNearestForest(cells, visitedForests);
      if (nearest == null) {
        break;
      }

      visitedForests[nearest.forest.r][nearest.forest.c] = true;

      int[][] distanceMap = computeDistanceMap(cells, nearest.forest);
      for (int r = 0; r < N; ++r) {
        for (int c = 0; c < M; ++c) {
          minDistances[r][c] = Math.min(minDistances[r][c], distanceMap[r][c]);
        }
      }

      for (int d = 0; d <= nearest.pathDistance; ++d) {
        result += d - Math.min(d, nearest.pathDistance - d);
      }
    }

    for (int r = 0; r < N; ++r) {
      for (int c = 0; c < M; ++c) {
        result += minDistances[r][c];
      }
    }

    return result;
  }

  static Element findNearestForest(char[][] cells, boolean[][] visitedForests) {
    int N = cells.length;
    int M = cells[0].length;

    int[][] distances = new int[N][M];
    boolean[][] visited = new boolean[N][M];
    Queue<Point> queue = new ArrayDeque<>();
    for (int r = 0; r < N; ++r) {
      for (int c = 0; c < M; ++c) {
        if (visitedForests[r][c]) {
          visited[r][c] = true;
          queue.offer(new Point(r, c));
        }
      }
    }

    if (queue.isEmpty()) {
      return new Element(new Point(0, 0), 0);
    }

    while (!queue.isEmpty()) {
      Point head = queue.poll();
      if (cells[head.r][head.c] == 'T' && !visitedForests[head.r][head.c]) {
        return new Element(head, distances[head.r][head.c]);
      }

      for (int i = 0; i < R_OFFSETS.length; ++i) {
        int adjR = head.r + R_OFFSETS[i];
        int adjC = head.c + C_OFFSETS[i];
        if (adjR >= 0
            && adjR < N
            && adjC >= 0
            && adjC < M
            && cells[adjR][adjC] != '.'
            && !visited[adjR][adjC]) {
          distances[adjR][adjC] = distances[head.r][head.c] + 1;
          visited[adjR][adjC] = true;
          queue.offer(new Point(adjR, adjC));
        }
      }
    }

    return null;
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
}

class Point {
  int r;
  int c;

  Point(int r, int c) {
    this.r = r;
    this.c = c;
  }
}

class Element {
  Point forest;
  int pathDistance;

  Element(Point forest, int pathDistance) {
    this.forest = forest;
    this.pathDistance = pathDistance;
  }
}
