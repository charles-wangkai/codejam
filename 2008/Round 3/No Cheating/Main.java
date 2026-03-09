// https://www.cnblogs.com/czsharecode/p/9777533.html

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Main {
  static final int[] R_OFFSETS = {-1, -1, 0};
  static final int[] C_OFFSETS = {-1, 1, -1};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 0; tc < C; ++tc) {
      int M = sc.nextInt();
      int N = sc.nextInt();
      char[][] seats = new char[M][N];
      for (int r = 0; r < M; ++r) {
        String line = sc.next();
        for (int c = 0; c < N; ++c) {
          seats[r][c] = line.charAt(c);
        }
      }

      System.out.println(String.format("Case #%d: %d", tc + 1, solve(seats)));
    }

    sc.close();
  }

  static int solve(char[][] seats) {
    int M = seats.length;
    int N = seats[0].length;

    int leftCount = 0;
    int rightCount = 0;
    int[][] positions = new int[M][N];
    for (int r = 0; r < M; ++r) {
      for (int c = 0; c < N; ++c) {
        if (seats[r][c] == '.') {
          if (c % 2 == 0) {
            ++leftCount;
            positions[r][c] = -leftCount;
          } else {
            ++rightCount;
            positions[r][c] = rightCount;
          }
        }
      }
    }

    MaxFlow maxFlow = new MaxFlow(leftCount + rightCount + 2);
    int source = 0;
    int sink = leftCount + rightCount + 1;
    for (int i = 0; i < leftCount; ++i) {
      maxFlow.addEdges(source, i + 1, 1);
    }
    for (int i = 0; i < rightCount; ++i) {
      maxFlow.addEdges(leftCount + 1 + i, sink, 1);
    }
    for (int r = 0; r < M; ++r) {
      for (int c = 0; c < N; ++c) {
        if (seats[r][c] == '.') {
          for (int i = 0; i < R_OFFSETS.length; ++i) {
            int adjR = r + R_OFFSETS[i];
            int adjC = c + C_OFFSETS[i];
            if (adjR >= 0 && adjR < M && adjC >= 0 && adjC < N && seats[adjR][adjC] == '.') {
              if (positions[r][c] < 0) {
                maxFlow.addEdges(-positions[r][c], leftCount + positions[adjR][adjC], 1);
              } else {
                maxFlow.addEdges(-positions[adjR][adjC], leftCount + positions[r][c], 1);
              }
            }
          }
        }
      }
    }

    return leftCount + rightCount - maxFlow.dinic(source, sink);
  }
}

class MaxFlow {
  List<Edge> edges = new ArrayList<>();
  List<Integer>[] edgeLists;

  @SuppressWarnings("unchecked")
  MaxFlow(int size) {
    edgeLists = new List[size];
    for (int i = 0; i < edgeLists.length; ++i) {
      edgeLists[i] = new ArrayList<>();
    }
  }

  void addEdges(int u, int v, int cap) {
    edges.add(new Edge(u, v, cap));
    edgeLists[u].add(edges.size() - 1);

    edges.add(new Edge(v, u, 0));
    edgeLists[v].add(edges.size() - 1);
  }

  int dinic(int s, int t) {
    int result = 0;
    while (true) {
      int[] levels = bfs(s, t);
      if (levels == null) {
        break;
      }

      while (true) {
        int minflow = dfs(levels, s, t, Integer.MAX_VALUE);
        if (minflow == 0) {
          break;
        }

        result += minflow;
      }
    }

    return result;
  }

  private int[] bfs(int s, int t) {
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

  private int dfs(int[] levels, int s, int t, int low) {
    if (s == t) {
      return low;
    }

    int result = 0;
    for (int e : edgeLists[s]) {
      Edge edge = edges.get(e);
      if (edge.capacity != 0 && levels[edge.to] == levels[s] + 1) {
        int next = dfs(levels, edge.to, t, Math.min(low - result, edge.capacity));
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

  static class Edge {
    int from;
    int to;
    int capacity;

    Edge(int from, int to, int capacity) {
      this.from = from;
      this.to = to;
      this.capacity = capacity;
    }
  }
}
