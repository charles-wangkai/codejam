import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 1; tc <= C; ++tc) {
      int W = sc.nextInt();
      int H = sc.nextInt();
      int DX1 = sc.nextInt();
      int DY1 = sc.nextInt();
      int DX2 = sc.nextInt();
      int DY2 = sc.nextInt();
      int X = sc.nextInt();
      int Y = sc.nextInt();

      System.out.println(String.format("Case #%d: %d", tc, solve(W, H, DX1, DY1, DX2, DY2, X, Y)));
    }

    sc.close();
  }

  static int solve(int W, int H, int DX1, int DY1, int DX2, int DY2, int X, int Y) {
    return search(W, H, DX1, DY1, DX2, DY2, new boolean[W][H], X, Y);
  }

  static int search(
      int W, int H, int DX1, int DY1, int DX2, int DY2, boolean[][] visited, int x, int y) {
    if (!(x >= 0 && x < W && y >= 0 && y < H && !visited[x][y])) {
      return 0;
    }

    visited[x][y] = true;

    return 1
        + search(W, H, DX1, DY1, DX2, DY2, visited, x + DX1, y + DY1)
        + search(W, H, DX1, DY1, DX2, DY2, visited, x + DX2, y + DY2);
  }
}
