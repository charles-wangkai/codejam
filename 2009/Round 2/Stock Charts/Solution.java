// https://www.cnblogs.com/justPassBy/p/5369930.html

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int n = sc.nextInt();
      int k = sc.nextInt();
      int[][] prices = new int[n][k];
      for (int i = 0; i < n; ++i) {
        for (int j = 0; j < k; ++j) {
          prices[i][j] = sc.nextInt();
        }
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(prices)));
    }

    sc.close();
  }

  static int solve(int[][] prices) {
    int n = prices.length;

    Vertex[] leftVertices = new Vertex[n];
    for (int i = 0; i < leftVertices.length; ++i) {
      leftVertices[i] = new Vertex(i);
    }
    Vertex[] rightVertices = new Vertex[n];
    for (int i = 0; i < rightVertices.length; ++i) {
      rightVertices[i] = new Vertex(i);
    }
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < n; ++j) {
        if (isAbove(prices[i], prices[j])) {
          leftVertices[i].adjacents.add(j);
          rightVertices[j].adjacents.add(i);
        }
      }
    }

    int matchingCount = 0;
    for (int i = 0; i < leftVertices.length; ++i) {
      if (leftVertices[i].matching == -1
          && search(leftVertices, rightVertices, new boolean[rightVertices.length], i)) {
        ++matchingCount;
      }
    }

    return n - matchingCount;
  }

  static boolean search(
      Vertex[] leftVertices, Vertex[] rightVertices, boolean[] rightVisited, int leftIndex) {
    for (int rightIndex : leftVertices[leftIndex].adjacents) {
      if (!rightVisited[rightIndex]) {
        rightVisited[rightIndex] = true;

        if (rightVertices[rightIndex].matching == -1
            || search(
                leftVertices, rightVertices, rightVisited, rightVertices[rightIndex].matching)) {
          leftVertices[leftIndex].matching = rightIndex;
          rightVertices[rightIndex].matching = leftIndex;

          return true;
        }
      }
    }

    return false;
  }

  static boolean isAbove(int[] line1, int[] line2) {
    return IntStream.range(0, line1.length).allMatch(i -> line1[i] < line2[i]);
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
