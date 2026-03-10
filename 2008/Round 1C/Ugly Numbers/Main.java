import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {
  static final int[] PRIMES = {2, 3, 5, 7};
  static final ModInt MOD_INT =
      new ModInt(Arrays.stream(PRIMES).reduce((acc, x) -> acc * x).getAsInt());

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 0; tc < N; ++tc) {
      String s = sc.next();

      System.out.println(String.format("Case #%d: %d", tc + 1, solve(s)));
    }

    sc.close();
  }

  static long solve(String s) {
    long[][] dp = new long[s.length() + 1][MOD_INT.modulus];
    dp[0][0] = 1;
    for (int length = 0; length < s.length(); ++length) {
      int value = 0;
      for (int nextLength = 1; length + nextLength <= s.length(); ++nextLength) {
        value = MOD_INT.addMod(value * 10, s.charAt(length + nextLength - 1) - '0');

        for (int prev = 0; prev < MOD_INT.modulus; ++prev) {
          dp[length + nextLength][MOD_INT.addMod(prev, value)] += dp[length][prev];
          if (length != 0) {
            dp[length + nextLength][MOD_INT.addMod(prev, -value)] += dp[length][prev];
          }
        }
      }
    }

    return IntStream.range(0, MOD_INT.modulus)
        .filter(i -> Arrays.stream(PRIMES).anyMatch(prime -> i % prime == 0))
        .mapToLong(i -> dp[s.length()][i])
        .sum();
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
