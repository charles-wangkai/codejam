import java.math.BigInteger;
import java.util.Scanner;

public class Main {
  static final String TARGET = "welcome to code jam";
  static final ModInt MOD_INT = new ModInt(10000);

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    sc.nextLine();
    for (int tc = 0; tc < N; ++tc) {
      String text = sc.nextLine();

      System.out.println(String.format("Case #%d: %04d", tc + 1, solve(text)));
    }

    sc.close();
  }

  static int solve(String text) {
    int[] dp = new int[TARGET.length() + 1];
    dp[0] = 1;

    for (char c : text.toCharArray()) {
      for (int length = TARGET.length(); length >= 1; --length) {
        if (TARGET.charAt(length - 1) == c) {
          dp[length] = MOD_INT.addMod(dp[length], dp[length - 1]);
        }
      }
    }

    return dp[dp.length - 1];
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
