import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Solution {
  static final Map<Character, char[]> LETTER_TO_CANDIDATES =
      Map.of('C', new char[] {'C'}, 'J', new char[] {'J'}, '?', new char[] {'C', 'J'});

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int X = sc.nextInt();
      int Y = sc.nextInt();
      String S = sc.next();

      System.out.println(String.format("Case #%d: %d", tc, solve(X, Y, S)));
    }

    sc.close();
  }

  static int solve(int X, int Y, String S) {
    Map<Character, Integer> letterToCost = Map.of((char) 0, 0);
    for (char ch : S.toCharArray()) {
      Map<Character, Integer> nextLetterToCost = new HashMap<>();
      for (char candidate : LETTER_TO_CANDIDATES.get(ch)) {
        for (char letter : letterToCost.keySet()) {
          nextLetterToCost.put(
              candidate,
              Math.min(
                  nextLetterToCost.getOrDefault(candidate, Integer.MAX_VALUE),
                  letterToCost.get(letter)
                      + computeCost(X, Y, String.format("%c%c", letter, candidate))));
        }
      }

      letterToCost = nextLetterToCost;
    }

    return letterToCost.values().stream().mapToInt(x -> x).min().getAsInt();
  }

  static int computeCost(int X, int Y, String s) {
    if (s.equals("CJ")) {
      return X;
    } else if (s.equals("JC")) {
      return Y;
    }

    return 0;
  }
}
