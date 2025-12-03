import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
  static final int MODULUS = 30031;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      int N = sc.nextInt();
      int K = sc.nextInt();
      int P = sc.nextInt();

      System.out.println(String.format("Case #%d: %d", tc + 1, solve(N, K, P)));
    }

    sc.close();
  }

  static int solve(int N, int K, int P) {
    int[] masks = IntStream.range(0, 1 << P).filter(mask -> Integer.bitCount(mask) == K).toArray();
    Map<Integer, Integer> maskToIndex =
        IntStream.range(0, masks.length).boxed().collect(Collectors.toMap(i -> masks[i], i -> i));

    int[][] transition = new int[masks.length][masks.length];
    for (int mask : masks) {
      int nextMask = (mask * 2 + 1) % (1 << P);

      if (((mask >> (P - 1)) & 1) == 0) {
        for (int b = 1; b < P; ++b) {
          if (((nextMask >> b) & 1) == 1) {
            nextMask -= 1 << b;
            transition[maskToIndex.get(mask)][maskToIndex.get(nextMask)] = 1;
            nextMask += 1 << b;
          }
        }
      } else {
        transition[maskToIndex.get(mask)][maskToIndex.get(nextMask)] = 1;
      }
    }

    int[] state = new int[masks.length];
    state[maskToIndex.get((1 << K) - 1)] = 1;

    state = multiply(state, pow(transition, N - K));

    return state[maskToIndex.get((1 << K) - 1)];
  }

  static int addMod(int x, int y) {
    return Math.floorMod(x + y, MODULUS);
  }

  static int multiplyMod(int x, int y) {
    return Math.floorMod(x * y, MODULUS);
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
