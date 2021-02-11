import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Solution {
  static final int MODULUS = 30031;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      int K = sc.nextInt();
      int P = sc.nextInt();

      System.out.println(String.format("Case #%d: %d", tc, solve(N, K, P)));
    }

    sc.close();
  }

  static int solve(int N, int K, int P) {
    Map<Integer, Integer> codeToIndex = new HashMap<>();
    for (int code = 0; code < 1 << P; ++code) {
      if (Integer.bitCount(code) == K) {
        codeToIndex.put(code, codeToIndex.size());
      }
    }

    int codeNum = codeToIndex.size();

    int[][] transition = new int[codeNum][codeNum];
    for (int code : codeToIndex.keySet()) {
      int nextCode = (code * 2 + 1) % (1 << P);

      if ((code & (1 << (P - 1))) == 0) {
        for (int i = 1; i < P; ++i) {
          if ((nextCode & (1 << i)) != 0) {
            nextCode -= 1 << i;
            transition[codeToIndex.get(code)][codeToIndex.get(nextCode)] = 1;
            nextCode += 1 << i;
          }
        }
      } else {
        transition[codeToIndex.get(code)][codeToIndex.get(nextCode)] = 1;
      }
    }

    int[] state = new int[codeNum];
    state[codeToIndex.get((1 << K) - 1)] = 1;

    state = multiply(state, pow(transition, N - K));

    return state[codeToIndex.get((1 << K) - 1)];
  }

  static int addMod(int x, int y) {
    return (x + y) % MODULUS;
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

  static int[][] pow(int[][] m, int exponent) {
    int size = m.length;

    int[][] result = new int[size][size];
    for (int i = 0; i < size; ++i) {
      result[i][i] = 1;
    }

    while (exponent != 0) {
      if ((exponent & 1) != 0) {
        result = multiply(result, m);
      }

      m = multiply(m, m);
      exponent >>= 1;
    }

    return result;
  }
}
