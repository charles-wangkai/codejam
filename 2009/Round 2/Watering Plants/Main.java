// https://math.stackexchange.com/questions/256100/how-can-i-find-the-points-at-which-two-circles-intersect/1367732#1367732

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
  static final double EPSILON = 1e-9;
  static final int ITERATION_NUM = 100;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 1; tc <= C; ++tc) {
      int N = sc.nextInt();
      int[] X = new int[N];
      int[] Y = new int[N];
      int[] R = new int[N];
      for (int i = 0; i < N; ++i) {
        X[i] = sc.nextInt();
        Y[i] = sc.nextInt();
        R[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %.9f", tc, solve(X, Y, R)));
    }

    sc.close();
  }

  static double solve(int[] X, int[] Y, int[] R) {
    double result = -1;
    double lower = Arrays.stream(R).max().getAsInt();
    double upper = 2000;
    for (int i = 0; i < ITERATION_NUM; ++i) {
      double middle = (lower + upper) / 2;
      if (check(X, Y, R, middle)) {
        result = middle;
        upper = middle;
      } else {
        lower = middle;
      }
    }

    return result;
  }

  static boolean check(int[] X, int[] Y, int[] R, double radius) {
    int N = X.length;

    List<Point> centers = new ArrayList<>();
    for (int i = 0; i < N; ++i) {
      centers.add(new Point(X[i], Y[i]));
    }
    for (int i = 0; i < N; ++i) {
      for (int j = i + 1; j < N; ++j) {
        centers.addAll(computeCenters(X[i], Y[i], R[i], X[j], Y[j], R[j], radius));
      }
    }

    for (int i = 0; i < centers.size(); ++i) {
      for (int j = i; j < centers.size(); ++j) {
        if (coverAll(X, Y, R, centers.get(i), centers.get(j), radius)) {
          return true;
        }
      }
    }

    return false;
  }

  static boolean coverAll(int[] X, int[] Y, int[] R, Point center1, Point center2, double radius) {
    for (int i = 0; i < X.length; ++i) {
      if (!isIn(X[i], Y[i], R[i], center1, radius) && !isIn(X[i], Y[i], R[i], center2, radius)) {
        return false;
      }
    }

    return true;
  }

  static boolean isIn(int x, int y, int r, Point center, double radius) {
    return computeDistance(x, y, center.x, center.y) + r <= radius + EPSILON;
  }

  static List<Point> computeCenters(int x1, int y1, int r1, int x2, int y2, int r2, double radius) {
    List<Point> result = new ArrayList<>();

    double a = radius - r1;
    double b = radius - r2;
    double c = computeDistance(x1, y1, x2, y2);
    if (a + b >= c && b + c >= a && c + a >= b) {
      double p =
          2 * (a * a + b * b) / (c * c) - (a * a - b * b) * (a * a - b * b) / (c * c * c * c) - 1;
      if (p >= 0) {
        for (int sign : new int[] {-1, 1}) {
          double x =
              0.5 * (x1 + x2)
                  + (a * a - b * b) / (2 * c * c) * (x2 - x1)
                  + sign * 0.5 * Math.sqrt(p) * (y2 - y1);
          double y =
              0.5 * (y1 + y2)
                  + (a * a - b * b) / (2 * c * c) * (y2 - y1)
                  + sign * 0.5 * Math.sqrt(p) * (x1 - x2);

          result.add(new Point(x, y));
        }
      }
    }

    return result;
  }

  static double computeDistance(double x1, double y1, double x2, double y2) {
    return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
  }
}

class Point {
  double x;
  double y;

  Point(double x, double y) {
    this.x = x;
    this.y = y;
  }
}
