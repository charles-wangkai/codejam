import java.util.Scanner;

public class Main {
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
    return String.format(
        "%03d",
        addMod(
            multiplyMod(multiply(new int[] {1, 0}, pow(new int[][] {{3, 1}, {5, 3}}, n))[0], 2),
            -1));
  }

  static int addMod(int x, int y) {
    return Math.floorMod(x + y, MODULUS);
  }

  static int multiplyMod(int x, int y) {
    return x * y % MODULUS;
  }

  static int[] multiply(int[] v, int[][] m) {
    int size = v.length;

    int[] result = new int[size];
    for (int i = 0; i < result.length; ++i) {
      for (int j = 0; j < size; ++j) {
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

  static int[][] pow(int[][] base, int exponent) {
    int size = base.length;

    int[][] result = new int[size][size];
    for (int i = 0; i < size; ++i) {
      result[i][i] = 1;
    }

    while (exponent != 0) {
      if ((exponent & 1) == 1) {
        result = multiply(result, base);
      }

      base = multiply(base, base);
      exponent >>= 1;
    }

    return result;
  }
}
