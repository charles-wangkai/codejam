import java.math.BigInteger;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {
  static final ModInt MOD_INT = new ModInt(10007);
  static final int LIMIT = 100_000_000;

  static short[] factorialMods;
  static short[] factorialExponents;

  public static void main(String[] args) {
    precompute();

    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 0; tc < N; ++tc) {
      int H = sc.nextInt();
      int W = sc.nextInt();
      int R = sc.nextInt();
      int[] r = new int[R];
      int[] c = new int[R];
      for (int i = 0; i < R; ++i) {
        r[i] = sc.nextInt();
        c[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %d", tc + 1, solve(H, W, r, c)));
    }

    sc.close();
  }

  static void precompute() {
    factorialMods = new short[LIMIT + 1];
    factorialMods[0] = 1;
    factorialExponents = new short[factorialMods.length];
    for (int i = 1; i < factorialMods.length; ++i) {
      factorialExponents[i] = factorialExponents[i - 1];
      int rest = i;
      while (rest % MOD_INT.modulus == 0) {
        ++factorialExponents[i];
        rest /= MOD_INT.modulus;
      }

      factorialMods[i] = (short) MOD_INT.multiplyMod(factorialMods[i - 1], rest % MOD_INT.modulus);
    }
  }

  static int solve(int H, int W, int[] r, int[] c) {
    int[] sortedRockIndices =
        IntStream.range(0, r.length)
            .boxed()
            .sorted(Comparator.comparing(i -> r[i]))
            .mapToInt(Integer::intValue)
            .toArray();

    int result = computeWayNums(H - 1, W - 1);
    for (int mask = 1; mask < 1 << sortedRockIndices.length; ++mask) {
      int mask_ = mask;
      int[] indices =
          IntStream.range(0, sortedRockIndices.length)
              .filter(i -> ((mask_ >> i) & 1) == 1)
              .map(i -> sortedRockIndices[i])
              .toArray();

      int term = computeWayNums(r[indices[0]] - 1, c[indices[0]] - 1);
      for (int i = 0; i < indices.length; ++i) {
        term =
            MOD_INT.multiplyMod(
                term,
                computeWayNums(
                    ((i == indices.length - 1) ? H : r[indices[i + 1]]) - r[indices[i]],
                    ((i == indices.length - 1) ? W : c[indices[i + 1]]) - c[indices[i]]));
      }

      result = MOD_INT.addMod(result, ((indices.length % 2 == 0) ? 1 : -1) * term);
    }

    return result;
  }

  static int computeWayNums(int height, int width) {
    if (height < 0 || width < 0 || (height + width) % 3 != 0) {
      return 0;
    }

    int sum = (height + width) / 3;
    int num1 = height - sum;
    int num2 = width - sum;
    if (num1 < 0 || num2 < 0) {
      return 0;
    }

    return C(sum, num1);
  }

  static int C(int n, int r) {
    return (factorialExponents[n] == factorialExponents[r] + factorialExponents[n - r])
        ? MOD_INT.divideMod(
            factorialMods[n], MOD_INT.multiplyMod(factorialMods[r], factorialMods[n - r]))
        : 0;
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
