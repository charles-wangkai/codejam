import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

public class Solution {
  static final int[] R_OFFSETS = {-1, 0, 1, 0};
  static final int[] C_OFFSETS = {0, 1, 0, -1};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int W = sc.nextInt();
      int Q = sc.nextInt();
      char[][] square = new char[W][W];
      for (int r = 0; r < W; ++r) {
        String line = sc.next();
        for (int c = 0; c < W; ++c) {
          square[r][c] = line.charAt(c);
        }
      }
      int[] queries = new int[Q];
      for (int i = 0; i < queries.length; ++i) {
        queries[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d:\n%s", tc, solve(square, queries)));
    }

    sc.close();
  }

  static String solve(char[][] square, int[] queries) {
    int W = square.length;

    String[] solutions = new String[queries.length];
    @SuppressWarnings("unchecked")
    Set<Integer>[][] seen = new Set[W][W];
    for (int r = 0; r < W; ++r) {
      for (int c = 0; c < W; ++c) {
        seen[r][c] = new HashSet<>();
      }
    }
    PriorityQueue<Element> pq =
        new PriorityQueue<>(
            Comparator.comparing((Element e) -> e.expression.length())
                .thenComparing(e -> e.expression));
    for (int r = 0; r < W; ++r) {
      for (int c = 0; c < W; ++c) {
        if (Character.isDigit(square[r][c])) {
          int value = square[r][c] - '0';
          String expression = String.valueOf(square[r][c]);

          pq.offer(new Element(r, c, expression, value));
        }
      }
    }

    while (true) {
      Element head = pq.poll();
      if (seen[head.r][head.c].contains(head.value)) {
        continue;
      }
      seen[head.r][head.c].add(head.value);

      for (int i = 0; i < queries.length; ++i) {
        if (queries[i] == head.value && solutions[i] == null) {
          solutions[i] = head.expression;
        }
      }
      if (Arrays.stream(solutions).allMatch(Objects::nonNull)) {
        break;
      }

      for (int d1 = 0; d1 < R_OFFSETS.length; ++d1) {
        int r1 = head.r + R_OFFSETS[d1];
        int c1 = head.c + C_OFFSETS[d1];
        if (r1 >= 0 && r1 < W && c1 >= 0 && c1 < W) {
          char operator = square[r1][c1];
          for (int d2 = 0; d2 < R_OFFSETS.length; ++d2) {
            int r2 = r1 + R_OFFSETS[d2];
            int c2 = c1 + C_OFFSETS[d2];
            if (r2 >= 0 && r2 < W && c2 >= 0 && c2 < W) {
              int digit = square[r2][c2] - '0';
              int value = head.value + (operator == '+' ? 1 : -1) * digit;
              String expression = String.format("%s%c%d", head.expression, operator, digit);

              pq.offer(new Element(r2, c2, expression, value));
            }
          }
        }
      }
    }

    return String.join("\n", solutions);
  }
}

class Element {
  int r;
  int c;
  String expression;
  int value;

  Element(int r, int c, String expression, int value) {
    this.r = r;
    this.c = c;
    this.expression = expression;
    this.value = value;
  }
}
