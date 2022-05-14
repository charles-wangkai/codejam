import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 1; tc <= C; ++tc) {
      List<Integer>[] largeAdjLists = readTree(sc);
      List<Integer>[] smallAdjLists = readTree(sc);

      System.out.println(
          String.format("Case #%d: %s", tc, solve(largeAdjLists, smallAdjLists) ? "YES" : "NO"));
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
      int v1 = sc.nextInt() - 1;
      int v2 = sc.nextInt() - 1;

      adjLists[v1].add(v2);
      adjLists[v2].add(v1);
    }

    return adjLists;
  }

  static boolean solve(List<Integer>[] largeAdjLists, List<Integer>[] smallAdjLists) {
    @SuppressWarnings("unchecked")
    List<Integer>[] smallChildLists = new List[smallAdjLists.length];
    Map<Integer, List<Integer>> smallDepthToVertices = new HashMap<>();
    search(
        smallAdjLists,
        smallChildLists,
        smallDepthToVertices,
        new boolean[smallAdjLists.length],
        0,
        0);
    int smallMaxDepth = smallDepthToVertices.keySet().stream().mapToInt(x -> x).max().getAsInt();

    for (int largeRoot = 0; largeRoot < largeAdjLists.length; ++largeRoot) {
      boolean[][] matched = new boolean[smallAdjLists.length][largeAdjLists.length];

      @SuppressWarnings("unchecked")
      List<Integer>[] largeChildLists = new List[largeAdjLists.length];
      Map<Integer, List<Integer>> largeDepthToVertices = new HashMap<>();
      search(
          largeAdjLists,
          largeChildLists,
          largeDepthToVertices,
          new boolean[largeAdjLists.length],
          largeRoot,
          0);

      for (int depth = smallMaxDepth; depth >= 0; --depth) {
        for (int smallVertex : smallDepthToVertices.get(depth)) {
          for (int largeVertex : largeDepthToVertices.getOrDefault(depth, List.of())) {
            matched[smallVertex][largeVertex] =
                isMatched(matched, smallChildLists, largeChildLists, smallVertex, largeVertex);
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
      boolean[][] matched,
      List<Integer>[] smallChildLists,
      List<Integer>[] largeChildLists,
      int smallVertex,
      int largeVertex) {
    List<Vertex> leftVertices = new ArrayList<>();
    Map<Integer, Vertex> smallChildToVertex = new HashMap<>();
    for (int smallChild : smallChildLists[smallVertex]) {
      Vertex vertex = new Vertex(leftVertices.size());
      leftVertices.add(vertex);
      smallChildToVertex.put(smallChild, vertex);
    }

    List<Vertex> rightVertices = new ArrayList<>();
    Map<Integer, Vertex> largeChildToVertex = new HashMap<>();
    for (int largeChild : largeChildLists[largeVertex]) {
      Vertex vertex = new Vertex(rightVertices.size());
      rightVertices.add(vertex);
      largeChildToVertex.put(largeChild, vertex);
    }

    for (int smallChild : smallChildLists[smallVertex]) {
      for (int largeChild : largeChildLists[largeVertex]) {
        if (matched[smallChild][largeChild]) {
          smallChildToVertex
              .get(smallChild)
              .adjacents
              .add(largeChildToVertex.get(largeChild).index);
          largeChildToVertex
              .get(largeChild)
              .adjacents
              .add(smallChildToVertex.get(smallChild).index);
        }
      }
    }

    int matchingCount = 0;
    for (int i = 0; i < leftVertices.size(); ++i) {
      if (leftVertices.get(i).matching == -1
          && augment(leftVertices, rightVertices, new boolean[rightVertices.size()], i)) {
        ++matchingCount;
      }
    }

    return matchingCount == leftVertices.size();
  }

  static boolean augment(
      List<Vertex> leftVertices,
      List<Vertex> rightVertices,
      boolean[] rightVisited,
      int leftIndex) {
    for (int rightIndex : leftVertices.get(leftIndex).adjacents) {
      if (!rightVisited[rightIndex]) {
        rightVisited[rightIndex] = true;

        if (rightVertices.get(rightIndex).matching == -1
            || augment(
                leftVertices,
                rightVertices,
                rightVisited,
                rightVertices.get(rightIndex).matching)) {
          leftVertices.get(leftIndex).matching = rightIndex;
          rightVertices.get(rightIndex).matching = leftIndex;

          return true;
        }
      }
    }

    return false;
  }

  static void search(
      List<Integer>[] adjLists,
      List<Integer>[] childLists,
      Map<Integer, List<Integer>> depthToVertices,
      boolean[] visited,
      int v,
      int depth) {
    visited[v] = true;

    if (!depthToVertices.containsKey(depth)) {
      depthToVertices.put(depth, new ArrayList<>());
    }
    depthToVertices.get(depth).add(v);

    childLists[v] = new ArrayList<>();
    for (int adj : adjLists[v]) {
      if (!visited[adj]) {
        childLists[v].add(adj);
        search(adjLists, childLists, depthToVertices, visited, adj, depth + 1);
      }
    }
  }
}

class Vertex {
  int index;
  List<Integer> adjacents = new ArrayList<>();
  int matching = -1;

  Vertex(int index) {
    this.index = index;
  }
}
