import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  static final ModInt MOD_INT = new ModInt(1_000_000_009);

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 0; tc < C; ++tc) {
      int n = sc.nextInt();
      int k = sc.nextInt();
      int[] x = new int[n - 1];
      int[] y = new int[n - 1];
      for (int i = 0; i < n - 1; ++i) {
        x[i] = sc.nextInt();
        y[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %d", tc + 1, solve(x, y, k)));
    }

    sc.close();
  }

  static int solve(int[] x, int[] y, int k) {
    int n = x.length + 1;

    @SuppressWarnings("unchecked")
    List<Integer>[] adjLists = new List[n];
    for (int i = 0; i < adjLists.length; ++i) {
      adjLists[i] = new ArrayList<>();
    }
    for (int i = 0; i < x.length; ++i) {
      adjLists[x[i] - 1].add(y[i] - 1);
      adjLists[y[i] - 1].add(x[i] - 1);
    }

    return search(k, adjLists, -1, 0, 0);
  }

  static int search(int k, List<Integer>[] adjLists, int parent, int node, int usedNum) {
    int result = 1;
    int rest = k - usedNum;
    for (int adj : adjLists[node]) {
      if (adj != parent) {
        result =
            MOD_INT.multiplyMod(
                MOD_INT.multiplyMod(result, rest),
                search(k, adjLists, node, adj, adjLists[node].size()));
        --rest;
      }
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
