import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Solution {
  static final int[] R_OFFSETS = {-1, 0, 1, 0};
  static final int[] C_OFFSETS = {0, 1, 0, -1};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int R = sc.nextInt();
      int C = sc.nextInt();
      char[][] board = new char[R][C];
      for (int r = 0; r < R; ++r) {
        String line = sc.next();
        for (int c = 0; c < C; ++c) {
          board[r][c] = line.charAt(c);
        }
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(board)));
    }

    sc.close();
  }

  static int solve(char[][] board) {
    int R = board.length;
    int C = board[0].length;

    Set<Point> beginState = new HashSet<>();
    Set<Point> goalState = new HashSet<>();
    for (int r = 0; r < R; ++r) {
      for (int c = 0; c < C; ++c) {
        if (board[r][c] == 'o') {
          beginState.add(new Point(r, c));
        } else if (board[r][c] == 'x') {
          goalState.add(new Point(r, c));
        } else if (board[r][c] == 'w') {
          beginState.add(new Point(r, c));
          goalState.add(new Point(r, c));
        }
      }
    }

    Map<Set<Point>, Integer> stateToDistance = new HashMap<>();
    PriorityQueue<Element> pq = new PriorityQueue<>(Comparator.comparing(e -> e.distance));
    pq.offer(new Element(beginState, null, 0));
    while (!pq.isEmpty()) {
      Element head = pq.poll();
      if (stateToDistance.containsKey(head.state)) {
        continue;
      }
      if (head.state.equals(goalState)) {
        return head.distance;
      }
      stateToDistance.put(head.state, head.distance);

      for (Point before : head.state) {
        Set<Point> others =
            head.state.stream().filter(p -> !p.equals(before)).collect(Collectors.toSet());
        for (int i = 0; i < R_OFFSETS.length; ++i) {
          if (isEmpty(board, others, before.r - R_OFFSETS[i], before.c - C_OFFSETS[i])
              && isEmpty(board, others, before.r + R_OFFSETS[i], before.c + C_OFFSETS[i])) {
            Point after = new Point(before.r + R_OFFSETS[i], before.c + C_OFFSETS[i]);
            Set<Point> nextState = new HashSet<>(others);
            nextState.add(after);
            if (!stateToDistance.containsKey(nextState)) {
              if (head.orphan == null) {
                if (others.isEmpty()
                    || others.stream().anyMatch(other -> isConnected(other, after))) {
                  pq.offer(new Element(nextState, null, head.distance + 1));
                } else {
                  pq.offer(new Element(nextState, after, head.distance + 1));
                }
              } else {
                if ((before.equals(head.orphan)
                        && others.stream().anyMatch(other -> isConnected(other, after)))
                    || (!before.equals(head.orphan)
                        && isConnected(head.orphan, after)
                        && (others.size() == 1
                            || others.stream()
                                .anyMatch(
                                    other ->
                                        !other.equals(head.orphan)
                                            && isConnected(other, after))))) {
                  pq.offer(new Element(nextState, null, head.distance + 1));
                }
              }
            }
          }
        }
      }
    }

    return -1;
  }

  static boolean isEmpty(char[][] board, Set<Point> others, int r, int c) {
    return r >= 0
        && r < board.length
        && c >= 0
        && c < board[0].length
        && board[r][c] != '#'
        && !others.contains(new Point(r, c));
  }

  static boolean isConnected(Point p1, Point p2) {
    return (p1.r == p2.r && Math.abs(p1.c - p2.c) == 1)
        || (p1.c == p2.c && Math.abs(p1.r - p2.r) == 1);
  }
}

class Element {
  Set<Point> state;
  Point orphan;
  int distance;

  Element(Set<Point> state, Point orphan, int distance) {
    this.state = state;
    this.orphan = orphan;
    this.distance = distance;
  }
}

class Point {
  int r;
  int c;

  Point(int r, int c) {
    this.r = r;
    this.c = c;
  }

  @Override
  public int hashCode() {
    return Objects.hash(r, c);
  }

  @Override
  public boolean equals(Object obj) {
    Point other = (Point) obj;

    return r == other.r && c == other.c;
  }
}
