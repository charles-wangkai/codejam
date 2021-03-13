import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {
  static final int MODULUS = 10009;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      String p = sc.next();
      int K = sc.nextInt();
      int n = sc.nextInt();
      String[] words = new String[n];
      for (int i = 0; i < words.length; ++i) {
        words[i] = sc.next();
      }

      System.out.println(String.format("Case #%d: %s", tc, solve(p, K, words)));
    }

    sc.close();
  }

  static String solve(String p, int K, String[] words) {
    int[][] letterCounts = new int[words.length][26];
    for (int i = 0; i < words.length; ++i) {
      for (char ch : words[i].toCharArray()) {
        ++letterCounts[i][ch - 'a'];
      }
    }

    int[] pSums = new int[K];
    for (String term : p.split("\\+")) {
      int[] q = new int[1 << term.length()];
      for (int i = 0; i < q.length; ++i) {
        int i_ = i;
        q[i] =
            Arrays.stream(letterCounts)
                .mapToInt(
                    c ->
                        IntStream.range(0, term.length())
                            .filter(j -> (i_ & (1 << j)) != 0)
                            .map(j -> c[term.charAt(j) - 'a'])
                            .reduce(1, Solution::multiplyMod))
                .reduce(Solution::addMod)
                .getAsInt();
      }

      for (int i = 0; i < pSums.length; ++i) {
        pSums[i] = addMod(pSums[i], search(q, term.length(), new int[i + 1], 0));
      }
    }

    return Arrays.stream(pSums).mapToObj(String::valueOf).collect(Collectors.joining(" "));
  }

  static int search(int[] q, int termLength, int[] codes, int depth) {
    if (depth == termLength) {
      return Arrays.stream(codes).map(code -> q[code]).reduce(Solution::multiplyMod).getAsInt();
    }

    int result = 0;
    for (int i = 0; i < codes.length; ++i) {
      codes[i] += 1 << depth;
      result = addMod(result, search(q, termLength, codes, depth + 1));
      codes[i] -= 1 << depth;
    }

    return result;
  }

  static int addMod(int x, int y) {
    return (x + y) % MODULUS;
  }

  static int multiplyMod(int x, int y) {
    return x * y % MODULUS;
  }
}
