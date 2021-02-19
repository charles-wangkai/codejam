import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class Solution {
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
    invertHalf(calendar);

    int row = calendar.length;
    int col = calendar[0].length;

    int s = 0;
    int t = row * col + 1;

    @SuppressWarnings("unchecked")
    Map<Integer, Integer>[] graph = new Map[row * col + 2];
    for (int i = 0; i < graph.length; ++i) {
      graph[i] = new HashMap<>();
    }

    for (int r = 0; r < row; ++r) {
      for (int c = 0; c < col; ++c) {
        int index = convertToIndex(col, r, c);

        if (calendar[r][c] == '#') {
          graph[s].put(index, INF);
          graph[index].put(s, INF);
        } else if (calendar[r][c] == '.') {
          graph[index].put(t, INF);
          graph[t].put(index, INF);
        }

        for (int i = 0; i < R_OFFSETS.length; ++i) {
          int adjR = r + R_OFFSETS[i];
          int adjC = c + C_OFFSETS[i];
          if (adjR >= 0 && adjR < row && adjC >= 0 && adjC < col) {
            int adjIndex = convertToIndex(col, adjR, adjC);

            graph[index].put(adjIndex, 1);
            graph[adjIndex].put(index, 1);
          }
        }
      }
    }

    return (row - 1) * col + row * (col - 1) - fordFulkerson(graph, s, t);
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

  static void invertHalf(char[][] calendar) {
    int N = calendar.length;
    int M = calendar[0].length;

    for (int r = 0; r < N; ++r) {
      for (int c = 0; c < M; ++c) {
        if ((r + c) % 2 == 0) {
          if (calendar[r][c] == '#') {
            calendar[r][c] = '.';
          } else if (calendar[r][c] == '.') {
            calendar[r][c] = '#';
          }
        }
      }
    }
  }

  static int convertToIndex(int col, int r, int c) {
    return r * col + c + 1;
  }

  static int fordFulkerson(Map<Integer, Integer>[] graph, int s, int t) {
    int maxFlow = 0;

    while (true) {
      int[] parents = bfs(graph, s, t);
      if (parents == null) {
        break;
      }

      int pathFlow = Integer.MAX_VALUE;
      for (int v = t; v != s; v = parents[v]) {
        pathFlow = Math.min(pathFlow, graph[parents[v]].get(v));
      }

      int pathFlow_ = pathFlow;
      for (int v = t; v != s; v = parents[v]) {
        graph[parents[v]].compute(v, (key, value) -> value - pathFlow_);
        graph[v].compute(parents[v], (key, value) -> value + pathFlow_);
      }

      maxFlow += pathFlow;
    }

    return maxFlow;
  }

  static int[] bfs(Map<Integer, Integer>[] graph, int s, int t) {
    int V = graph.length;

    int[] parents = new int[V];
    boolean[] visited = new boolean[V];

    Queue<Integer> queue = new ArrayDeque<>();
    queue.offer(s);
    parents[s] = -1;
    visited[s] = true;

    while (!queue.isEmpty()) {
      int u = queue.poll();

      for (int v : graph[u].keySet()) {
        if (!visited[v] && graph[u].get(v) > 0) {
          queue.offer(v);
          parents[v] = u;
          visited[v] = true;
        }
      }
    }

    return visited[t] ? parents : null;
  }
}
