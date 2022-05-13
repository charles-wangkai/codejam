// https://www.cnblogs.com/czsharecode/p/9777533.html

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Solution {
  static final int[] R_OFFSETS = {-1, -1, 0, 0};
  static final int[] C_OFFSETS = {-1, 1, -1, 1};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 1; tc <= C; ++tc) {
      int M = sc.nextInt();
      int N = sc.nextInt();
      char[][] seats = new char[M][N];
      for (int r = 0; r < M; ++r) {
        String line = sc.next();
        for (int c = 0; c < N; ++c) {
          seats[r][c] = line.charAt(c);
        }
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(seats)));
    }

    sc.close();
  }

  static int solve(char[][] seats) {
    int M = seats.length;
    int N = seats[0].length;

    Vertex[][] vertices = new Vertex[M][N];
    List<Vertex> leftVertices = new ArrayList<>();
    List<Vertex> rightVertices = new ArrayList<>();
    for (int r = 0; r < M; ++r) {
      for (int c = 0; c < N; ++c) {
        if (seats[r][c] == '.') {
          if (c % 2 == 0) {
            vertices[r][c] = new Vertex(leftVertices.size());
            leftVertices.add(vertices[r][c]);
          } else {
            vertices[r][c] = new Vertex(rightVertices.size());
            rightVertices.add(vertices[r][c]);
          }
        }
      }
    }
    for (int r = 0; r < M; ++r) {
      for (int c = 0; c < N; ++c) {
        if (seats[r][c] == '.') {
          for (int i = 0; i < R_OFFSETS.length; ++i) {
            int adjR = r + R_OFFSETS[i];
            int adjC = c + C_OFFSETS[i];
            if (adjR >= 0 && adjR < M && adjC >= 0 && adjC < N && seats[adjR][adjC] == '.') {
              vertices[r][c].adjacents.add(vertices[adjR][adjC].index);
              vertices[adjR][adjC].adjacents.add(vertices[r][c].index);
            }
          }
        }
      }
    }

    int matchingCount = 0;
    for (int i = 0; i < leftVertices.size(); ++i) {
      if (leftVertices.get(i).matching == -1
          && search(leftVertices, rightVertices, new boolean[rightVertices.size()], i)) {
        ++matchingCount;
      }
    }

    return leftVertices.size() + rightVertices.size() - matchingCount;
  }

  static boolean search(
      List<Vertex> leftVertices,
      List<Vertex> rightVertices,
      boolean[] rightVisited,
      int leftIndex) {
    for (int rightIndex : leftVertices.get(leftIndex).adjacents) {
      if (!rightVisited[rightIndex]) {
        rightVisited[rightIndex] = true;

        if (rightVertices.get(rightIndex).matching == -1
            || search(
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
}

class Vertex {
  int index;
  Set<Integer> adjacents = new HashSet<>();
  int matching = -1;

  Vertex(int index) {
    this.index = index;
  }
}
