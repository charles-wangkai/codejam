import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;

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
    Map<Integer, Element>[][] seen = new Map[W][W];
    for (int r = 0; r < W; ++r) {
      for (int c = 0; c < W; ++c) {
        seen[r][c] = new HashMap<>();
      }
    }
    List<Element> elements = new ArrayList<>();
    for (int r = 0; r < W; ++r) {
      for (int c = 0; c < W; ++c) {
        if (Character.isDigit(square[r][c])) {
          int value = square[r][c] - '0';

          elements.add(new Element(square[r][c], r, c, -1, -1, value, -1));
        }
      }
    }
    Collections.sort(elements, Comparator.comparing(e -> e.value));
    Queue<Element> queue = new ArrayDeque<>(elements);

    while (true) {
      int prevCode = queue.peek().code;
      int prevValue = queue.peek().value;

      elements = new ArrayList<>();
      while (!queue.isEmpty() && queue.peek().code == prevCode) {
        Element head = queue.poll();
        if (seen[head.r][head.c].containsKey(head.value)) {
          continue;
        }
        seen[head.r][head.c].put(head.value, head);

        for (int i = 0; i < queries.length; ++i) {
          if (solutions[i] == null && queries[i] == head.value) {
            solutions[i] = buildExpression(square, seen, head);
          }
        }

        elements.add(head);
      }
      if (Arrays.stream(solutions).allMatch(Objects::nonNull)) {
        break;
      }

      List<Item> items = new ArrayList<>();
      for (Element element : elements) {
        for (int d1 = 0; d1 < R_OFFSETS.length; ++d1) {
          int r1 = element.r + R_OFFSETS[d1];
          int c1 = element.c + C_OFFSETS[d1];
          if (r1 >= 0 && r1 < W && c1 >= 0 && c1 < W) {
            for (int d2 = 0; d2 < R_OFFSETS.length; ++d2) {
              int r2 = r1 + R_OFFSETS[d2];
              int c2 = c1 + C_OFFSETS[d2];
              if (r2 >= 0 && r2 < W && c2 >= 0 && c2 < W) {
                items.add(
                    new Item(
                        element.r,
                        element.c,
                        d1,
                        d2,
                        String.format("%c%c", square[r1][c1], square[r2][c2])));
              }
            }
          }
        }
      }
      Collections.sort(items, Comparator.comparing(item -> item.part));
      for (Item item : items) {
        int r1 = item.r + R_OFFSETS[item.d1];
        int c1 = item.c + C_OFFSETS[item.d1];
        int r2 = r1 + R_OFFSETS[item.d2];
        int c2 = c1 + C_OFFSETS[item.d2];

        char operator = square[r1][c1];
        int digit = square[r2][c2] - '0';
        int value = prevValue + (operator == '+' ? 1 : -1) * digit;

        queue.offer(
            new Element(
                (prevCode * 128 + item.part.charAt(0)) * 128 + item.part.charAt(1),
                r2,
                c2,
                item.d1,
                item.d2,
                value,
                prevValue));
      }
    }

    return String.join("\n", solutions);
  }

  static String buildExpression(char[][] square, Map<Integer, Element>[][] seen, Element head) {
    StringBuilder result = new StringBuilder();
    Element element = head;
    while (true) {
      result.append(square[element.r][element.c]);

      if (element.d1 == -1) {
        break;
      }

      int prevR1 = element.r - R_OFFSETS[element.d2];
      int prevC1 = element.c - C_OFFSETS[element.d2];
      result.append(square[prevR1][prevC1]);

      int prevR2 = prevR1 - R_OFFSETS[element.d1];
      int prevC2 = prevC1 - C_OFFSETS[element.d1];

      element = seen[prevR2][prevC2].get(element.prevValue);
    }
    result.reverse();

    return result.toString();
  }
}

class Item {
  int r;
  int c;
  int d1;
  int d2;
  String part;

  Item(int r, int c, int d1, int d2, String part) {
    this.r = r;
    this.c = c;
    this.d1 = d1;
    this.d2 = d2;
    this.part = part;
  }
}

class Element {
  int code;
  int r;
  int c;
  int d1;
  int d2;
  int value;
  int prevValue;

  Element(int code, int r, int c, int d1, int d2, int value, int prevValue) {
    this.code = code;
    this.r = r;
    this.c = c;
    this.d1 = d1;
    this.d2 = d2;
    this.value = value;
    this.prevValue = prevValue;
  }
}
