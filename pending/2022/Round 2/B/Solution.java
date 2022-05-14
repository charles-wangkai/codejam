import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int R = sc.nextInt();

      System.out.println(String.format("Case #%d: %d", tc, solve(R)));
    }

    sc.close();
  }

  static int solve(int R) {
    Set<Point> points1 = draw_circle_filled(R);
    Set<Point> points2 = draw_circle_filled_wrong(R);

    return (int) points1.stream().filter(p -> !points2.contains(p)).count()
        + (int) points2.stream().filter(p -> !points1.contains(p)).count();
  }

  static Set<Point> draw_circle_filled(int R) {
    Set<Point> result = new HashSet<>();
    for (int x = -R; x <= R; ++x) {
      for (int y = -R; y <= R; ++y) {
        if (Math.round(Math.sqrt(x * x + y * y)) <= R) {
          result.add(new Point(x, y));
        }
      }
    }

    return result;
  }

  static Set<Point> draw_circle_filled_wrong(int R) {
    Set<Point> result = new HashSet<>();
    for (int r = 0; r <= R; ++r) {
      result.addAll(draw_circle_perimeter(r));
    }

    return result;
  }

  static Set<Point> draw_circle_perimeter(int R) {
    Set<Point> result = new HashSet<>();
    for (int x = -R; x <= R; ++x) {
      int y = (int) Math.round(Math.sqrt(R * R - x * x));

      result.add(new Point(x, y));
      result.add(new Point(x, -y));
      result.add(new Point(y, x));
      result.add(new Point(-y, x));
    }

    return result;
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