// https://raw.githubusercontent.com/google/coding-competitions-archive/main/codejam/2009/world_finals/wi-fi_towers/analysis.pdf

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Main {
  static final int INF = 10000;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      int n = sc.nextInt();
      int[] x = new int[n];
      int[] y = new int[n];
      int[] r = new int[n];
      int[] s = new int[n];
      for (int i = 0; i < n; ++i) {
        x[i] = sc.nextInt();
        y[i] = sc.nextInt();
        r[i] = sc.nextInt();
        s[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %d", tc + 1, solve(x, y, r, s)));
    }

    sc.close();
  }

  static int solve(int[] x, int[] y, int[] r, int[] s) {
    int n = x.length;

    MaxFlow maxFlow = new MaxFlow(n + 2);
    int source = 0;
    int sink = n + 1;
    for (int i = 0; i < n; ++i) {
      if (s[i] < 0) {
        maxFlow.addEdges(source, i + 1, -s[i]);
      } else {
        maxFlow.addEdges(i + 1, sink, s[i]);
      }
    }
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < n; ++j) {
        if ((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j]) <= r[i] * r[i]) {
          maxFlow.addEdges(j + 1, i + 1, INF);
        }
      }
    }

    return Arrays.stream(s).filter(si -> si >= 0).sum() - maxFlow.dinic(source, sink);
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
