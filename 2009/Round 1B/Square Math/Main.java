import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
  static final int[] R_OFFSETS = {-1, 0, 1, 0};
  static final int[] C_OFFSETS = {0, 1, 0, -1};
  static final int BASE = 128;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
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

      System.out.println(String.format("Case #%d:\n%s", tc + 1, solve(square, queries)));
    }

    sc.close();
  }

  static String solve(char[][] square, int[] queries) {
    int W = square.length;

    @SuppressWarnings("unchecked")
    Map<Integer, Element>[][] seenMaps = new Map[W][W];
    for (int r = 0; r < W; ++r) {
      for (int c = 0; c < W; ++c) {
        seenMaps[r][c] = new HashMap<>();
      }
    }

    List<Element> elements = new ArrayList<>();
    for (int r = 0; r < W; ++r) {
      for (int c = 0; c < W; ++c) {
        if (Character.isDigit(square[r][c])) {
          elements.add(new Element(square[r][c], r, c, -1, -1, square[r][c] - '0', -1));
        }
      }
    }
    Collections.sort(elements, Comparator.comparing(e -> e.value));
    Queue<Element> queue = new ArrayDeque<>(elements);

    Map<Integer, String> queryToExpression = new HashMap<>();
    for (int query : queries) {
      queryToExpression.put(query, null);
    }

    int rest = queryToExpression.size();
    while (true) {
      int prevHash = queue.peek().hash;
      int prevValue = queue.peek().value;

      elements = new ArrayList<>();
      while (!queue.isEmpty() && queue.peek().hash == prevHash) {
        Element head = queue.poll();
        if (!seenMaps[head.r][head.c].containsKey(head.value)) {
          seenMaps[head.r][head.c].put(head.value, head);

          if (queryToExpression.containsKey(head.value)
              && queryToExpression.get(head.value) == null) {
            queryToExpression.put(head.value, buildExpression(square, seenMaps, head));
            --rest;
          }

          elements.add(head);
        }
      }
      if (rest == 0) {
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
                items.add(new Item(element, d1, square[r1][c1], d2, square[r2][c2] - '0'));
              }
            }
          }
        }
      }
      Collections.sort(
          items,
          Comparator.<Item, Character>comparing(item -> item.operator)
              .thenComparing(item -> item.digit));

      for (Item item : items) {
        int value = prevValue + ((item.operator == '+') ? 1 : -1) * item.digit;

        queue.offer(
            new Element(
                (prevHash * BASE + item.operator) * BASE + (item.digit + '0'),
                item.element.r + R_OFFSETS[item.d1] + R_OFFSETS[item.d2],
                item.element.c + C_OFFSETS[item.d1] + C_OFFSETS[item.d2],
                item.d1,
                item.d2,
                value,
                prevValue));
      }
    }

    return Arrays.stream(queries)
        .mapToObj(queryToExpression::get)
        .collect(Collectors.joining("\n"));
  }

  static String buildExpression(
      char[][] square, Map<Integer, Element>[][] seenMaps, Element element) {
    StringBuilder result = new StringBuilder();
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

      element = seenMaps[prevR2][prevC2].get(element.prevValue);
    }
    result.reverse();

    return result.toString();
  }
}

class Item {
  Element element;
  int d1;
  char operator;
  int d2;
  int digit;

  Item(Element element, int d1, char operator, int d2, int digit) {
    this.element = element;
    this.d1 = d1;
    this.operator = operator;
    this.d2 = d2;
    this.digit = digit;
  }
}

class Element {
  int hash;
  int r;
  int c;
  int d1;
  int d2;
  int value;
  int prevValue;

  Element(int hash, int r, int c, int d1, int d2, int value, int prevValue) {
    this.hash = hash;
    this.r = r;
    this.c = c;
    this.d1 = d1;
    this.d2 = d2;
    this.value = value;
    this.prevValue = prevValue;
  }
}
