import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
    int[] pSums = new int[K];

    int[][] letterCounts = new int[words.length][26];
    for (int i = 0; i < words.length; ++i) {
      for (char ch : words[i].toCharArray()) {
        ++letterCounts[i][ch - 'a'];
      }
    }

    search(pSums, p.split("\\+"), letterCounts, new int[26], new ArrayList<>(), 0);

    return Arrays.stream(pSums).mapToObj(String::valueOf).collect(Collectors.joining(" "));
  }

  static void search(
      int[] pSums,
      String[] terms,
      int[][] letterCounts,
      int[] values,
      List<Integer> indices,
      int depth) {
    for (int i = indices.isEmpty() ? 0 : indices.get(indices.size() - 1);
        i < letterCounts.length;
        ++i) {
      indices.add(i);
      for (int j = 0; j < values.length; ++j) {
        values[j] += letterCounts[i][j];
      }

      int sum = 0;
      for (String term : terms) {
        int product = 1;
        for (int j = 0; j < term.length() && product != 0; ++j) {
          product = multiplyMod(product, values[term.charAt(j) - 'a']);
        }
        sum = addMod(sum, product);
      }
      pSums[depth] = addMod(pSums[depth], multiplyMod(sum, computeRepeatNum(indices)));

      if (depth != pSums.length - 1) {
        search(pSums, terms, letterCounts, values, indices, depth + 1);
      }

      for (int j = 0; j < values.length; ++j) {
        values[j] -= letterCounts[i][j];
      }
      indices.remove(indices.size() - 1);
    }
  }

  static int computeRepeatNum(List<Integer> indices) {
    int result = 1;
    int beginIndex = 0;
    while (beginIndex != indices.size()) {
      int endIndex = beginIndex;
      while (endIndex + 1 != indices.size()
          && indices.get(endIndex + 1).equals(indices.get(endIndex))) {
        ++endIndex;
      }

      result = multiplyMod(result, C(indices.size() - beginIndex, endIndex - beginIndex + 1));
      beginIndex = endIndex + 1;
    }

    return result;
  }

  static int C(int n, int r) {
    int result = 1;
    for (int i = 0; i < r; ++i) {
      result = result * (n - i) / (i + 1);
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
