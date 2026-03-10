import java.math.BigInteger;
import java.util.Scanner;

public class Main {
  static final ModInt MOD_INT = new ModInt(1000);

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      int n = sc.nextInt();

      System.out.println(String.format("Case #%d: %s", tc + 1, solve(n)));
    }

    sc.close();
  }

  static String solve(int n) {
    return String.format(
        "%03d",
        MOD_INT.addMod(
            MOD_INT.multiplyMod(
                multiply(new int[] {1, 0}, pow(new int[][] {{3, 1}, {5, 3}}, n))[0], 2),
            -1));
  }

  static int[] multiply(int[] v, int[][] m) {
    int size = v.length;

    int[] result = new int[size];
    for (int i = 0; i < result.length; ++i) {
      for (int j = 0; j < size; ++j) {
        result[i] = MOD_INT.addMod(result[i], MOD_INT.multiplyMod(v[j], m[j][i]));
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
          result[i][j] = MOD_INT.addMod(result[i][j], MOD_INT.multiplyMod(m1[i][k], m2[k][j]));
        }
      }
    }

    return result;
  }

  static int[][] pow(int[][] m, long exponent) {
    if (exponent == 0) {
      return buildEntity(m.length);
    }

    return multiply(
        (exponent % 2 == 1) ? m : buildEntity(m.length), pow(multiply(m, m), exponent / 2));
  }

  static int[][] buildEntity(int size) {
    int[][] result = new int[size][size];
    for (int i = 0; i < size; ++i) {
      result[i][i] = 1;
    }

    return result;
  }
}

class ModInt {
  int modulus;

  ModInt(int modulus) {
    this.modulus = modulus;
  }

  int mod(long x) {
    return Math.floorMod(x, modulus);
  }

  int modInv(int x) {
    return BigInteger.valueOf(x).modInverse(BigInteger.valueOf(modulus)).intValue();
  }

  int addMod(int x, int y) {
    return mod(x + y);
  }

  int multiplyMod(int x, int y) {
    return mod((long) x * y);
  }

  int divideMod(int x, int y) {
    return multiplyMod(x, modInv(y));
  }

  int powMod(int base, long exponent) {
    if (exponent == 0) {
      return 1;
    }

    return multiplyMod(
        (exponent % 2 == 0) ? 1 : base, powMod(multiplyMod(base, base), exponent / 2));
  }
}
