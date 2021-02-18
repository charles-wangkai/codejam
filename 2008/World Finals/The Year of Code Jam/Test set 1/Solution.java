import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      int M = sc.nextInt();
      char[][] calendar = new char[N][M];
      for (int r = 0; r < N; ++r) {
        String line = sc.next();
        for (int c = 0; c < M; ++c) {
          calendar[r][c] = line.charAt(c);
        }
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(calendar)));
    }

    sc.close();
  }

  static int solve(char[][] calendar) {
    int N = calendar.length;
    int M = calendar[0].length;

    Map<Integer, Integer> codeToHappiness = new HashMap<>();
    codeToHappiness.put(0, 0);

    for (int r = 0; r < N; ++r) {
      Map<Integer, Integer> nextCodeToHappiness = new HashMap<>();
      for (int nextCode = 0; nextCode < 1 << M; ++nextCode) {
        if (check(calendar[r], nextCode)) {
          for (int code : codeToHappiness.keySet()) {
            int nextHappiness = codeToHappiness.get(code) + computeDelta(M, nextCode, code);
            nextCodeToHappiness.put(
                nextCode, Math.max(nextCodeToHappiness.getOrDefault(nextCode, 0), nextHappiness));
          }
        }
      }

      codeToHappiness = nextCodeToHappiness;
    }

    return codeToHappiness.values().stream().mapToInt(x -> x).max().getAsInt();
  }

  static int computeDelta(int M, int nextCode, int code) {
    boolean[] nextBlues = decode(M, nextCode);
    boolean[] blues = decode(M, code);

    int result = 0;
    for (int i = 0; i < M; ++i) {
      if (nextBlues[i]) {
        if (blues[i]) {
          --result;
        } else {
          ++result;
        }

        if (i != 0 && nextBlues[i - 1]) {
          --result;
        } else {
          ++result;
        }

        result += 2;
      }
    }

    return result;
  }

  static boolean check(char[] row, int code) {
    int M = row.length;

    boolean[] blues = decode(M, code);

    return IntStream.range(0, M)
        .allMatch(
            i ->
                ((row[i] == '#' && blues[i])
                    || (row[i] == '.' && !blues[i])
                    || (row[i] == '?'
                        && !(blues[i] && i != 0 && blues[i - 1] && i != M - 1 && blues[i + 1])
                        && !(!blues[i]
                            && (i == 0 || !blues[i - 1])
                            && (i == M - 1 || !blues[i + 1])))));
  }

  static boolean[] decode(int size, int code) {
    boolean[] blues = new boolean[size];
    for (int i = 0; i < blues.length; ++i) {
      blues[i] = (code & 1) != 0;
      code >>= 1;
    }

    return blues;
  }
}
