// https://raw.githubusercontent.com/google/coding-competitions-archive/main/codejam/2008/emea_semifinal/scaled_triangle/analysis.pdf
// https://en.wikipedia.org/wiki/Banach_fixed-point_theorem
// https://aaronschlegel.me/calculate-matrix-inverse-2x2-3x3.html

import java.util.Scanner;

public class Main {
  static final int SIZE = 3;
  static final double EPSILON = 1e-7;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 1; tc <= N; ++tc) {
      int[] x1 = new int[2];
      int[] y1 = new int[2];
      int[] x2 = new int[2];
      int[] y2 = new int[2];
      int[] x3 = new int[2];
      int[] y3 = new int[2];
      for (int i = 0; i < 2; ++i) {
        x1[i] = sc.nextInt();
        y1[i] = sc.nextInt();
        x2[i] = sc.nextInt();
        y2[i] = sc.nextInt();
        x3[i] = sc.nextInt();
        y3[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %s", tc, solve(x1, y1, x2, y2, x3, y3)));
    }

    sc.close();
  }

  static String solve(int[] x1, int[] y1, int[] x2, int[] y2, int[] x3, int[] y3) {
    double[][] p = new double[][] {{x1[1], x2[1], x3[1]}, {y1[1], y2[1], y3[1]}, {1, 1, 1}};

    double[][] t =
        multiply(
            p, inverse(new double[][] {{x1[0], x2[0], x3[0]}, {y1[0], y2[0], y3[0]}, {1, 1, 1}}));

    while (true) {
      double[][] nextP = multiply(t, p);
      if (isClose(p, nextP)) {
        break;
      }

      p = nextP;
    }

    return String.format("%.9f %.9f", p[0][0], p[1][0]);
  }

  static double[][] inverse(double[][] m) {
    double det =
        m[0][0] * (m[1][1] * m[2][2] - m[1][2] * m[2][1])
            - m[0][1] * (m[1][0] * m[2][2] - m[1][2] * m[2][0])
            + m[0][2] * (m[1][0] * m[2][1] - m[1][1] * m[2][0]);

    return new double[][] {
      {
        (m[1][1] * m[2][2] - m[1][2] * m[2][1]) / det,
        -(m[0][1] * m[2][2] - m[0][2] * m[2][1]) / det,
        (m[0][1] * m[1][2] - m[0][2] * m[1][1]) / det
      },
      {
        -(m[1][0] * m[2][2] - m[1][2] * m[2][0]) / det,
        (m[0][0] * m[2][2] - m[0][2] * m[2][0]) / det,
        -(m[0][0] * m[1][2] - m[0][2] * m[1][0]) / det
      },
      {
        (m[1][0] * m[2][1] - m[1][1] * m[2][0]) / det,
        -(m[0][0] * m[2][1] - m[0][1] * m[2][0]) / det,
        (m[0][0] * m[1][1] - m[0][1] * m[1][0]) / det
      }
    };
  }

  static double[][] multiply(double[][] m1, double[][] m2) {
    double[][] result = new double[SIZE][SIZE];
    for (int i = 0; i < SIZE; ++i) {
      for (int j = 0; j < SIZE; ++j) {
        for (int k = 0; k < SIZE; ++k) {
          result[i][j] += m1[i][k] * m2[k][j];
        }
      }
    }

    return result;
  }

  static boolean isClose(double[][] m1, double[][] m2) {
    for (int i = 0; i < SIZE; ++i) {
      for (int j = 0; j < SIZE; ++j) {
        if (Math.abs(m1[i][j] - m2[i][j]) > EPSILON) {
          return false;
        }
      }
    }

    return true;
  }
}
