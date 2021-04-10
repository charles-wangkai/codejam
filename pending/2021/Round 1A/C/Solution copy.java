import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    int N = A.length;
    int Q = A[0].length();

    List<String> corrects = new ArrayList<>();
    for (int code = 0; code < 1 << Q; ++code) {
      int code_ = code;
      String correct =
          IntStream.range(0, Q)
              .mapToObj(i -> ((code_ & (1 << i)) == 0) ? "F" : "T")
              .collect(Collectors.joining());
      if (IntStream.range(0, N)
          .allMatch(
              i ->
                  IntStream.range(0, Q).filter(j -> A[i].charAt(j) == correct.charAt(j)).count()
                      == S[i])) {
        corrects.add(correct);
      }
    }

    StringBuilder answer = new StringBuilder();
    Rational expectedScore = new Rational(0, 1);
    for (int i = 0; i < Q; ++i) {
      int i_ = i;
      int fCount =
          (int)
              IntStream.range(0, corrects.size())
                  .filter(j -> corrects.get(j).charAt(i_) == 'F')
                  .count();
      int tCount = corrects.size() - fCount;

      if (fCount > tCount) {
        answer.append('F');
        expectedScore = add(expectedScore, new Rational(fCount, corrects.size()));
      } else {
        answer.append('T');
        expectedScore = add(expectedScore, new Rational(tCount, corrects.size()));
      }
    }

    return String.format("%s %d/%d", answer, expectedScore.numerator, expectedScore.denominator);
  }

  static Rational add(Rational r1, Rational r2) {
    long numerator = r1.numerator * r2.denominator + r2.numerator * r1.denominator;
    long denominator = r1.denominator * r2.denominator;
    long g = gcd(numerator, denominator);

    return new Rational(numerator / g, denominator / g);
  }

  static long gcd(long x, long y) {
    return (y == 0) ? x : gcd(y, x % y);
  }
}

class Rational {
  long numerator;
  long denominator;

  Rational(long numerator, long denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
  }
}
