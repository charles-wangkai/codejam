// https://raw.githubusercontent.com/google/coding-competitions-archive/main/codejam/2009/round_3/alphabetomials/analysis.pdf

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
  static final ModInt MOD_INT = new ModInt(10009);

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      String p = sc.next();
      int K = sc.nextInt();
      int n = sc.nextInt();
      String[] words = new String[n];
      for (int i = 0; i < words.length; ++i) {
        words[i] = sc.next();
      }

      System.out.println(String.format("Case #%d: %s", tc + 1, solve(p, K, words)));
    }

    sc.close();
  }

  static String solve(String p, int K, String[] words) {
    int[][] letterCounts = new int[words.length][26];
    for (int i = 0; i < words.length; ++i) {
      for (char c : words[i].toCharArray()) {
        ++letterCounts[i][c - 'a'];
      }
    }

    int[] pSums = new int[K];
    for (String term : p.split("\\+")) {
      int[] q =
          IntStream.range(0, 1 << term.length())
              .map(
                  mask ->
                      Arrays.stream(letterCounts)
                          .mapToInt(
                              c ->
                                  IntStream.range(0, term.length())
                                      .filter(i -> ((mask >> i) & 1) == 1)
                                      .map(i -> c[term.charAt(i) - 'a'])
                                      .reduce(1, MOD_INT::multiplyMod))
                          .reduce(MOD_INT::addMod)
                          .getAsInt())
              .toArray();

      for (int i = 0; i < pSums.length; ++i) {
        pSums[i] = MOD_INT.addMod(pSums[i], search(q, term.length(), new int[i + 1], 0));
      }
    }

    return Arrays.stream(pSums).mapToObj(String::valueOf).collect(Collectors.joining(" "));
  }

  static int search(int[] q, int termLength, int[] masks, int depth) {
    if (depth == termLength) {
      return Arrays.stream(masks).map(mask -> q[mask]).reduce(MOD_INT::multiplyMod).getAsInt();
    }

    int result = 0;
    for (int i = 0; i < masks.length; ++i) {
      masks[i] += 1 << depth;
      result = MOD_INT.addMod(result, search(q, termLength, masks, depth + 1));
      masks[i] -= 1 << depth;
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
