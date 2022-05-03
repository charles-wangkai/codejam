// https://en.wikipedia.org/wiki/Circular_segment#Arc_length_and_area

import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 1; tc <= N; ++tc) {
      double f = sc.nextDouble();
      double R = sc.nextDouble();
      double t = sc.nextDouble();
      double r = sc.nextDouble();
      double g = sc.nextDouble();

      System.out.println(String.format("Case #%d: %.9f", tc, solve(f, R, t, r, g)));
    }

    sc.close();
  }

  static double solve(double f, double R, double t, double r, double g) {
    double totalArea = Math.PI * R * R / 4;

    double innerR = R - t - f;
    double rectSize = g - 2 * f;
    if (innerR <= 0 || rectSize <= 0) {
      return 1;
    }

    double gapArea = 0;
    for (double lowerX = r + f; lowerX < innerR; lowerX += g + 2 * r) {
      double upperX = lowerX + rectSize;

      for (double lowerY = r + f;
          computeDistanceToOrigin(lowerX, lowerY) < innerR;
          lowerY += g + 2 * r) {
        double upperY = lowerY + rectSize;

        if (computeDistanceToOrigin(upperX, upperY) <= innerR) {
          gapArea += rectSize * rectSize;
        } else {
          Double upCrossX = computeUpCrossX(innerR, lowerX, upperX, upperY);
          Double leftCrossY = computeLeftCrossY(innerR, lowerX, lowerY, upperY);
          Double rightCrossY = computeRightCrossY(innerR, upperX, lowerY, upperY);
          Double downCrossX = computeDownCrossX(innerR, lowerX, upperX, lowerY);

          if (upCrossX != null) {
            if (rightCrossY != null) {
              gapArea +=
                  rectSize * rectSize
                      - (upperX - upCrossX) * (upperY - rightCrossY) / 2
                      + computeCircularSegmentArea(
                          innerR, Math.atan2(upperY, upCrossX) - Math.atan2(rightCrossY, upperX));
            } else {
              gapArea +=
                  (upCrossX - lowerX + downCrossX - lowerX) * rectSize / 2
                      + computeCircularSegmentArea(
                          innerR, Math.atan2(upperY, upCrossX) - Math.atan2(lowerY, downCrossX));
            }
          } else {
            if (rightCrossY != null) {
              gapArea +=
                  (leftCrossY - lowerY + rightCrossY - lowerY) * rectSize / 2
                      + computeCircularSegmentArea(
                          innerR, Math.atan2(leftCrossY, lowerX) - Math.atan2(rightCrossY, upperX));
            } else {
              gapArea +=
                  (leftCrossY - lowerY) * (downCrossX - lowerX) / 2
                      + computeCircularSegmentArea(
                          innerR, Math.atan2(leftCrossY, lowerX) - Math.atan2(lowerY, downCrossX));
            }
          }
        }
      }
    }

    return (totalArea - gapArea) / totalArea;
  }

  static double computeCircularSegmentArea(double innerR, double angle) {
    return innerR * innerR / 2 * (angle - Math.sin(angle));
  }

  static double computeDistanceToOrigin(double x, double y) {
    return Math.sqrt(x * x + y * y);
  }

  static Double computeUpCrossX(double innerR, double lowerX, double upperX, double upperY) {
    if (upperY > innerR) {
      return null;
    }

    double upCrossX = Math.sqrt(innerR * innerR - upperY * upperY);

    return (upCrossX >= lowerX && upCrossX <= upperX) ? upCrossX : null;
  }

  static Double computeLeftCrossY(double innerR, double lowerX, double lowerY, double upperY) {
    double leftCrossY = Math.sqrt(innerR * innerR - lowerX * lowerX);

    return (leftCrossY >= lowerY && leftCrossY <= upperY) ? leftCrossY : null;
  }

  static Double computeRightCrossY(double innerR, double upperX, double lowerY, double upperY) {
    if (upperX > innerR) {
      return null;
    }

    double rightCrossY = Math.sqrt(innerR * innerR - upperX * upperX);

    return (rightCrossY >= lowerY && rightCrossY <= upperY) ? rightCrossY : null;
  }

  static Double computeDownCrossX(double innerR, double lowerX, double upperX, double lowerY) {
    double downCrossX = Math.sqrt(innerR * innerR - lowerY * lowerY);

    return (downCrossX >= lowerX && downCrossX <= upperX) ? downCrossX : null;
  }
}
