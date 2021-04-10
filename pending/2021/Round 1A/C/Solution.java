import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      sc.nextInt();
      String[] A = new String[N];
      int[] S = new int[N];
      for (int i = 0; i < N; ++i) {
        A[i] = sc.next();
        S[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %s", tc, solve(A, S)));
    }

    sc.close();
  }

  static String solve(String[] A, int[] S) {
    int Q = A[0].length();

    List<Element> candidates = new ArrayList<>();
    for (int i = 0; i < A.length; ++i) {
      candidates.add(new Element(A[i], S[i]));
      candidates.add(new Element(flip(A[i]), Q - S[i]));
    }
    Collections.sort(candidates, Comparator.comparing((Element c) -> c.score).reversed());

    return String.format("%s %d/1", candidates.get(0).answer, candidates.get(0).score);
  }

  static String flip(String s) {
    return s.chars().mapToObj(ch -> (ch == 'F') ? "T" : "F").collect(Collectors.joining());
  }
}

class Element {
  String answer;
  int score;

  Element(String answer, int score) {
    this.answer = answer;
    this.score = score;
  }
}
