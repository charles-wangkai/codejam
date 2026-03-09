// https://raw.githubusercontent.com/google/coding-competitions-archive/main/codejam/2008/apac_semifinal/modern_art_plagiarism/analysis.pdf

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 0; tc < C; ++tc) {
      List<Integer>[] largeAdjLists = readTree(sc);
      List<Integer>[] smallAdjLists = readTree(sc);

      System.out.println(
          String.format(
              "Case #%d: %s", tc + 1, solve(largeAdjLists, smallAdjLists) ? "YES" : "NO"));
    }

    sc.close();
  }

  static List<Integer>[] readTree(Scanner sc) {
    int size = sc.nextInt();

    @SuppressWarnings("unchecked")
    List<Integer>[] adjLists = new List[size];
    for (int i = 0; i < adjLists.length; ++i) {
      adjLists[i] = new ArrayList<>();
    }

    for (int i = 0; i < size - 1; ++i) {
      int v1 = sc.nextInt();
      int v2 = sc.nextInt();

      adjLists[v1 - 1].add(v2 - 1);
      adjLists[v2 - 1].add(v1 - 1);
    }

    return adjLists;
  }

  static boolean solve(List<Integer>[] largeAdjLists, List<Integer>[] smallAdjLists) {
    @SuppressWarnings("unchecked")
    List<Integer>[] smallChildLists = new List[smallAdjLists.length];
    Map<Integer, List<Integer>> smallDepthToVertices = new HashMap<>();
    search(smallAdjLists, smallChildLists, smallDepthToVertices, -1, 0, 0);

    int smallMaxDepth =
        smallDepthToVertices.keySet().stream().mapToInt(Integer::intValue).max().getAsInt();

    for (int largeRoot = 0; largeRoot < largeAdjLists.length; ++largeRoot) {
      boolean[][] matched = new boolean[smallAdjLists.length][largeAdjLists.length];

      @SuppressWarnings("unchecked")
      List<Integer>[] largeChildLists = new List[largeAdjLists.length];
      Map<Integer, List<Integer>> largeDepthToVertices = new HashMap<>();
      search(largeAdjLists, largeChildLists, largeDepthToVertices, -1, largeRoot, 0);

      for (int depth = smallMaxDepth; depth >= 0; --depth) {
        for (int smallVertex : smallDepthToVertices.get(depth)) {
          for (int largeVertex : largeDepthToVertices.getOrDefault(depth, List.of())) {
            matched[smallVertex][largeVertex] =
                isMatched(matched, smallChildLists[smallVertex], largeChildLists[largeVertex]);
          }
        }
      }

      if (matched[0][largeRoot]) {
        return true;
      }
    }

    return false;
  }

  static boolean isMatched(
      boolean[][] matched, List<Integer> smallChildList, List<Integer> largeChildList) {
    MaxFlow maxFlow = new MaxFlow(smallChildList.size() + largeChildList.size() + 2);
    int source = 0;
    int sink = smallChildList.size() + largeChildList.size() + 1;
    for (int i = 0; i < smallChildList.size(); ++i) {
      maxFlow.addEdges(source, i + 1, 1);
    }
    for (int i = 0; i < largeChildList.size(); ++i) {
      maxFlow.addEdges(smallChildList.size() + 1 + i, sink, 1);
    }
    for (int i = 0; i < smallChildList.size(); ++i) {
      for (int j = 0; j < largeChildList.size(); ++j) {
        if (matched[smallChildList.get(i)][largeChildList.get(j)]) {
          maxFlow.addEdges(i + 1, smallChildList.size() + 1 + j, 1);
        }
      }
    }

    return maxFlow.dinic(source, sink) == smallChildList.size();
  }

  static void search(
      List<Integer>[] adjLists,
      List<Integer>[] childLists,
      Map<Integer, List<Integer>> depthToVertices,
      int parent,
      int node,
      int depth) {
    depthToVertices.putIfAbsent(depth, new ArrayList<>());
    depthToVertices.get(depth).add(node);

    childLists[node] = new ArrayList<>();
    for (int adj : adjLists[node]) {
      if (adj != parent) {
        childLists[node].add(adj);
        search(adjLists, childLists, depthToVertices, node, adj, depth + 1);
      }
    }
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
