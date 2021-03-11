import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
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
    Queue<Element> queue = new ArrayDeque<>();
    queue.offer(new Element(beginState, true, 0));
    while (!queue.isEmpty()) {
      Element head = queue.poll();
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
            boolean nextStable = isStable(nextState);
            if (!stateToDistance.containsKey(nextState)) {
              if (head.stable || nextStable) {
                queue.offer(new Element(nextState, nextStable, head.distance + 1));
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

  static boolean isStable(Set<Point> state) {
    Set<Point> visited = new HashSet<>();
    search(state, visited, state.iterator().next());

    return visited.size() == state.size();
  }

  static void search(Set<Point> state, Set<Point> visited, Point p) {
    visited.add(p);

    for (int i = 0; i < R_OFFSETS.length; ++i) {
      Point adj = new Point(p.r + R_OFFSETS[i], p.c + C_OFFSETS[i]);
      if (state.contains(adj) && !visited.contains(adj)) {
        search(state, visited, adj);
      }
    }
  }
}

class Element {
  Set<Point> state;
  boolean stable;
  int distance;

  Element(Set<Point> state, boolean stable, int distance) {
    this.state = state;
    this.stable = stable;
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
