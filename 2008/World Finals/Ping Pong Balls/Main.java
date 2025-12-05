// https://raw.githubusercontent.com/google/coding-competitions-archive/main/codejam/2008/world_finals/ping_pong_balls/analysis.pdf

import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Main implements Runnable {
  public static void main(String[] args) {
    new Thread(null, new Main(), "Main", 1 << 28).start();
  }

  @Override
  public void run() {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 0; tc < C; ++tc) {
      int W = sc.nextInt();
      int H = sc.nextInt();
      int DX1 = sc.nextInt();
      int DY1 = sc.nextInt();
      int DX2 = sc.nextInt();
      int DY2 = sc.nextInt();
      int X0 = sc.nextInt();
      int Y0 = sc.nextInt();

      System.out.println(
          String.format("Case #%d: %d", tc + 1, solve(W, H, DX1, DY1, DX2, DY2, X0, Y0)));
    }

    sc.close();
  }

  static long solve(int W, int H, int DX1, int DY1, int DX2, int DY2, int X0, int Y0) {
    if (isCollinear(DX1, DY1, DX2, DY2)) {
      return search(W, H, DX1, DY1, DX2, DY2, new HashSet<>(), X0, Y0);
    }

    int bMin = 0;
    int bMax = 0;
    while (isIn(W, H, DX1, DY1, DX2, DY2, X0, Y0, 0, bMax + 1)) {
      ++bMax;
    }

    long result = bMax - bMin + 1;
    for (int a = 1; ; ++a) {
      while (bMin <= bMax && !isIn(W, H, DX1, DY1, DX2, DY2, X0, Y0, a, bMin)) {
        ++bMin;
      }
      if (bMin == bMax + 1) {
        break;
      }

      while (!isIn(W, H, DX1, DY1, DX2, DY2, X0, Y0, a, bMax)) {
        --bMax;
      }
      while (isIn(W, H, DX1, DY1, DX2, DY2, X0, Y0, a, bMax + 1)) {
        ++bMax;
      }

      result += bMax - bMin + 1;
    }

    return result;
  }

  static boolean isIn(
      int W, int H, int DX1, int DY1, int DX2, int DY2, int X0, int Y0, int a, int b) {
    int x = X0 + a * DX1 + b * DX2;
    int y = Y0 + a * DY1 + b * DY2;

    return x >= 0 && x < W && y >= 0 && y < H;
  }

  static boolean isCollinear(int x1, int y1, int x2, int y2) {
    return x1 * y2 == y1 * x2;
  }

  static int search(
      int W, int H, int DX1, int DY1, int DX2, int DY2, Set<Point> visited, int x, int y) {
    Point point = new Point(x, y);
    if (!(x >= 0 && x < W && y >= 0 && y < H && !visited.contains(point))) {
      return 0;
    }

    visited.add(point);

    return 1
        + search(W, H, DX1, DY1, DX2, DY2, visited, x + DX1, y + DY1)
        + search(W, H, DX1, DY1, DX2, DY2, visited, x + DX2, y + DY2);
  }
}

class Point {
  int x;
  int y;

  Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public boolean equals(Object obj) {
    Point other = (Point) obj;

    return x == other.x && y == other.y;
  }
}
