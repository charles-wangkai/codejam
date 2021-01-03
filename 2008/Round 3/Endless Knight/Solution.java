import java.math.BigInteger;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Solution {
  static final int MODULUS = 10007;
  static final int LIMIT = 100_000_000;

  static short[] factorialMods;
  static short[] factorialExponents;

  public static void main(String[] args) {
    init();

    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 1; tc <= N; ++tc) {
      int H = sc.nextInt();
      int W = sc.nextInt();
      int R = sc.nextInt();
      int[] r = new int[R];
      int[] c = new int[R];
      for (int i = 0; i < R; ++i) {
        r[i] = sc.nextInt();
        c[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(H, W, r, c)));
    }

    sc.close();
  }

  static int solve(int H, int W, int[] r, int[] c) {
    int rockNum = r.length;

    int result = computeWayNums(H - 1, W - 1);

    int[] sortedRockIndices =
        IntStream.range(0, rockNum)
            .boxed()
            .sorted(Comparator.comparing(i -> r[i]))
            .mapToInt(x -> x)
            .toArray();
    for (int code = 1; code < 1 << rockNum; ++code) {
      int code_ = code;
      int[] indices =
          IntStream.range(0, rockNum)
              .filter(i -> (code_ & (1 << i)) != 0)
              .map(i -> sortedRockIndices[i])
              .toArray();

      int term = computeWayNums(r[indices[0]] - 1, c[indices[0]] - 1);
      for (int i = 0; i < indices.length; ++i) {
        term =
            multiplyMod(
                term,
                computeWayNums(
                    ((i == indices.length - 1) ? H : r[indices[i + 1]]) - r[indices[i]],
                    ((i == indices.length - 1) ? W : c[indices[i + 1]]) - c[indices[i]]));
      }

      if (indices.length % 2 == 0) {
        result = addMod(result, term);
      } else {
        result = subtractMod(result, term);
      }
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

  static void init() {
    factorialMods = new short[LIMIT + 1];
    factorialMods[0] = 1;
    factorialExponents = new short[factorialMods.length];
    for (int i = 1; i < factorialMods.length; ++i) {
      factorialExponents[i] = factorialExponents[i - 1];
      int m = i;
      while (m % MODULUS == 0) {
        ++factorialExponents[i];
        m /= MODULUS;
      }

      factorialMods[i] = (short) multiplyMod(factorialMods[i - 1], m % MODULUS);
    }
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

  static int divideMod(int x, int y) {
    return multiplyMod(x, BigInteger.valueOf(y).modInverse(BigInteger.valueOf(MODULUS)).intValue());
  }

  static int C(int n, int r) {
    return (factorialExponents[n] == factorialExponents[r] + factorialExponents[n - r])
        ? divideMod(factorialMods[n], multiplyMod(factorialMods[r], factorialMods[n - r]))
        : 0;
  }
}
