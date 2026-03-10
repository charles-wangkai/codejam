import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
  static final ModInt MOD_INT = new ModInt(1_000_000_007);

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 0; tc < N; ++tc) {
      int n = sc.nextInt();
      int m = sc.nextInt();
      int X = sc.nextInt();
      int Y = sc.nextInt();
      int Z = sc.nextInt();
      int[] A = new int[m];
      for (int i = 0; i < A.length; ++i) {
        A[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %d", tc + 1, solve(n, A, X, Y, Z)));
    }

    sc.close();
  }

  static int solve(int n, int[] A, int X, int Y, int Z) {
    int[] values = compress(generateSpeeds(n, A, X, Y, Z));

    FenwickTree fenwickTree = new FenwickTree(values.length);
    for (int value : values) {
      fenwickTree.add(value, MOD_INT.addMod(fenwickTree.computePrefixSum(value - 1), 1));
    }

    return fenwickTree.computePrefixSum(values.length);
  }

  static int[] compress(int[] speeds) {
    int[] sortedSpeeds =
        Arrays.stream(speeds).boxed().sorted().distinct().mapToInt(x -> x).toArray();
    Map<Integer, Integer> speedToSortedIndex =
        IntStream.range(0, sortedSpeeds.length)
            .boxed()
            .collect(Collectors.toMap(i -> sortedSpeeds[i], i -> i));

    return Arrays.stream(speeds).map(speed -> speedToSortedIndex.get(speed) + 1).toArray();
  }

  static int[] generateSpeeds(int n, int[] A, int X, int Y, int Z) {
    ModInt modInt = new ModInt(Z);

    int[] result = new int[n];
    for (int i = 0; i < result.length; ++i) {
      result[i] = A[i % A.length];
      A[i % A.length] =
          modInt.addMod(modInt.multiplyMod(X, A[i % A.length]), modInt.multiplyMod(Y, i + 1));
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

class FenwickTree {
  int[] a;

  FenwickTree(int size) {
    a = new int[Integer.highestOneBit(size) * 2 + 1];
  }

  void add(int pos, int delta) {
    while (pos < a.length) {
      a[pos] = Main.MOD_INT.addMod(a[pos], delta);
      pos += pos & -pos;
    }
  }

  int computePrefixSum(int pos) {
    int result = 0;
    while (pos != 0) {
      result = Main.MOD_INT.addMod(result, a[pos]);
      pos -= pos & -pos;
    }

    return result;
  }
}
