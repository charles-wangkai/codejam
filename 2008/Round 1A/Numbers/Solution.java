import java.util.Scanner;

public class Solution {
  static final int MODULUS = 1000;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int n = sc.nextInt();

      System.out.println(String.format("Case #%d: %s", tc, solve(n)));
    }

    sc.close();
  }

  static String solve(int n) {
    int[][] transition = new int[][] {{3, 1}, {5, 3}};
    transition = pow(transition, n);

    int[] v = multiply(new int[] {1, 0}, transition);

    return String.format("%03d", subtractMod(multiplyMod(v[0], 2), 1));
  }

  static int addMod(int x, int y) {
    return (x + y) % MODULUS;
  }

  static int subtractMod(int x, int y) {
    return (x - y + MODULUS) % MODULUS;
  }

  static int multiplyMod(int x, int y) {
    return x * y % MODULUS;
  }

  static int[] multiply(int[] v, int[][] m) {
    int length = v.length;

    int[] result = new int[length];
    for (int i = 0; i < result.length; ++i) {
      for (int j = 0; j < length; ++j) {
        result[i] = addMod(result[i], multiplyMod(v[j], m[j][i]));
      }
    }

    return result;
  }

  static int[][] multiply(int[][] m1, int[][] m2) {
    int size = m1.length;

    int[][] result = new int[size][size];
    for (int i = 0; i < size; ++i) {
      for (int j = 0; j < size; ++j) {
        for (int k = 0; k < size; ++k) {
          result[i][j] = addMod(result[i][j], multiplyMod(m1[i][k], m2[k][j]));
        }
      }
    }

    return result;
  }

  static int[][] pow(int[][] m, int exponent) {
    int size = m.length;

    int[][] result = new int[size][size];
    for (int i = 0; i < size; ++i) {
      result[i][i] = 1;
    }

    while (exponent != 0) {
      if (exponent % 2 != 0) {
        result = multiply(result, m);
      }

      m = multiply(m, m);
      exponent /= 2;
    }

    return result;
  }
}
