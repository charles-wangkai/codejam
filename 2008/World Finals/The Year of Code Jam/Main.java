// https://raw.githubusercontent.com/google/coding-competitions-archive/main/codejam/2008/world_finals/the_year_of_code_jam/analysis.pdf

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Main {
  static final int INF = 5;
  static final int[] R_OFFSETS = {-1, 0, 1, 0};
  static final int[] C_OFFSETS = {0, 1, 0, -1};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      int M = sc.nextInt();
      char[][] calendar = new char[N][M];
      for (int r = 0; r < N; ++r) {
        String line = sc.next();
        for (int c = 0; c < M; ++c) {
          calendar[r][c] = line.charAt(c);
        }
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(calendar)));
    }

    sc.close();
  }

  static int solve(char[][] calendar) {
    calendar = extend(calendar);
    flipHalf(calendar);

    int row = calendar.length;
    int col = calendar[0].length;

    int s = 0;
    int t = row * col + 1;

    List<Edge> edges = new ArrayList<>();

    @SuppressWarnings("unchecked")
    List<Integer>[] edgeLists = new List[row * col + 2];
    for (int i = 0; i < edgeLists.length; ++i) {
      edgeLists[i] = new ArrayList<>();
    }
    for (int r = 0; r < row; ++r) {
      for (int c = 0; c < col; ++c) {
        int index = r * col + c + 1;

        if (calendar[r][c] == '#') {
          addEdges(edges, edgeLists, s, index, INF);
        } else if (calendar[r][c] == '.') {
          addEdges(edges, edgeLists, index, t, INF);
        }

        for (int i = 0; i < R_OFFSETS.length; ++i) {
          int adjR = r + R_OFFSETS[i];
          int adjC = c + C_OFFSETS[i];
          if (adjR >= 0 && adjR < row && adjC >= 0 && adjC < col) {
            int adjIndex = adjR * col + adjC + 1;

            addEdges(edges, edgeLists, index, adjIndex, 1);
          }
        }
      }
    }

    return (row - 1) * col + row * (col - 1) - dinic(edges, edgeLists, s, t);
  }

  static char[][] extend(char[][] calendar) {
    int N = calendar.length;
    int M = calendar[0].length;

    char[][] result = new char[N + 2][M + 2];
    for (int r = 0; r < result.length; ++r) {
      Arrays.fill(result[r], '.');
    }
    for (int r = 0; r < N; ++r) {
      for (int c = 0; c < M; ++c) {
        result[r + 1][c + 1] = calendar[r][c];
      }
    }

    return result;
  }

  static void flipHalf(char[][] calendar) {
    int row = calendar.length;
    int col = calendar[0].length;

    for (int r = 0; r < row; ++r) {
      for (int c = 0; c < col; ++c) {
        if ((r + c) % 2 == 0 && calendar[r][c] != '?') {
          calendar[r][c] = (char) ('.' + '#' - calendar[r][c]);
        }
      }
    }
  }

  static void addEdges(List<Edge> edges, List<Integer>[] edgeLists, int u, int v, int z) {
    edges.add(new Edge(u, v, z));
    edgeLists[u].add(edges.size() - 1);

    edges.add(new Edge(v, u, 0));
    edgeLists[v].add(edges.size() - 1);
  }

  static int dinic(List<Edge> edges, List<Integer>[] edgeLists, int s, int t) {
    int result = 0;
    while (true) {
      int[] levels = bfs(edges, edgeLists, s, t);
      if (levels == null) {
        break;
      }

      while (true) {
        int minflow = dfs(edges, edgeLists, levels, s, t, Integer.MAX_VALUE);
        if (minflow == 0) {
          break;
        }

        result += minflow;
      }
    }

    return result;
  }

  static int[] bfs(List<Edge> edges, List<Integer>[] edgeLists, int s, int t) {
    int[] levels = new int[edgeLists.length];
    Arrays.fill(levels, -1);
    levels[s] = 0;

    Queue<Integer> queue = new ArrayDeque<>();
    queue.offer(s);

    while (!queue.isEmpty()) {
      int head = queue.poll();
      if (head == t) {
        return levels;
      }

      for (int e : edgeLists[head]) {
        Edge edge = edges.get(e);
        if (edge.capacity != 0 && levels[edge.to] == -1) {
          levels[edge.to] = levels[head] + 1;
          queue.offer(edge.to);
        }
      }
    }

    return null;
  }

  static int dfs(List<Edge> edges, List<Integer>[] edgeLists, int[] levels, int s, int t, int low) {
    if (s == t) {
      return low;
    }

    int result = 0;
    for (int e : edgeLists[s]) {
      Edge edge = edges.get(e);
      if (edge.capacity != 0 && levels[edge.to] == levels[s] + 1) {
        int next = dfs(edges, edgeLists, levels, edge.to, t, Math.min(low - result, edge.capacity));
        edge.capacity -= next;
        edges.get(e ^ 1).capacity += next;

        result += next;
        if (result == low) {
          break;
        }
      }
    }

    if (result == 0) {
      levels[s] = -1;
    }

    return result;
  }
}

class Edge {
  int from;
  int to;
  int capacity;

  Edge(int from, int to, int capacity) {
    this.from = from;
    this.to = to;
    this.capacity = capacity;
  }
}