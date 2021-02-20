import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int L = sc.nextInt();
    int D = sc.nextInt();
    int N = sc.nextInt();
    String[] words = new String[D];
    for (int i = 0; i < words.length; ++i) {
      words[i] = sc.next();
    }
    for (int tc = 1; tc <= N; ++tc) {
      String pattern = sc.next();

      System.out.println(String.format("Case #%d: %d", tc, solve(L, words, pattern)));
    }

    sc.close();
  }

  static int solve(int L, String[] words, String pattern) {
    Set<Character>[] letterSets = buildLetterSets(L, pattern);

    return (int)
        Arrays.stream(words)
            .filter(
                word -> IntStream.range(0, L).allMatch(i -> letterSets[i].contains(word.charAt(i))))
            .count();
  }

  static Set<Character>[] buildLetterSets(int L, String pattern) {
    @SuppressWarnings("unchecked")
    Set<Character>[] letterSets = new Set[L];
    for (int i = 0; i < letterSets.length; ++i) {
      letterSets[i] = new HashSet<>();
    }

    int index = 0;
    boolean inGroup = false;
    for (char ch : pattern.toCharArray()) {
      if (ch == '(') {
        inGroup = true;
      } else if (ch == ')') {
        inGroup = false;
      } else {
        letterSets[index].add(ch);
      }

      if (!inGroup) {
        ++index;
      }
    }

    return letterSets;
  }
}
