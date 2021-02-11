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
    boolean[] terminalPositions = new boolean[P];
    for (int i = 0; i < K; ++i) {
      terminalPositions[i] = true;
    }

    Map<Integer, Integer> codeToWayNum = new HashMap<>();
    codeToWayNum.put(encode(terminalPositions), 1);

    for (int step = 0; step < N - K; ++step) {
      Map<Integer, Integer> nextCodeToWayNum = new HashMap<>();
      for (int code : codeToWayNum.keySet()) {
        boolean[] positions = decode(P, code);

        boolean[] nextPositions = new boolean[P];
        nextPositions[0] = true;
        for (int i = 0; i < positions.length - 1; ++i) {
          nextPositions[i + 1] = positions[i];
        }

        if (positions[positions.length - 1]) {
          int nextCode = encode(nextPositions);
          nextCodeToWayNum.put(
              nextCode, addMod(nextCodeToWayNum.getOrDefault(nextCode, 0), codeToWayNum.get(code)));
        } else {
          for (int i = 1; i < nextPositions.length; ++i) {
            if (nextPositions[i]) {
              nextPositions[i] = false;

              int nextCode = encode(nextPositions);
              nextCodeToWayNum.put(
                  nextCode,
                  addMod(nextCodeToWayNum.getOrDefault(nextCode, 0), codeToWayNum.get(code)));

              nextPositions[i] = true;
            }
          }
        }
      }

      codeToWayNum = nextCodeToWayNum;
    }

    return codeToWayNum.get(encode(terminalPositions));
  }

  static int encode(boolean[] positions) {
    int code = 0;
    for (int i = positions.length - 1; i >= 0; --i) {
      code = code * 2 + (positions[i] ? 1 : 0);
    }

    return code;
  }

  static boolean[] decode(int P, int code) {
    boolean[] positions = new boolean[P];
    for (int i = 0; i < positions.length; ++i) {
      positions[i] = (code & 1) != 0;
      code >>= 1;
    }

    return positions;
  }

  static int addMod(int x, int y) {
    return (x + y) % MODULUS;
  }
}
