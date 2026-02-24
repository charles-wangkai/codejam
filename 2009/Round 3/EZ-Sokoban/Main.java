import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
  static final int[] R_OFFSETS = {-1, 0, 1, 0};
  static final int[] C_OFFSETS = {0, 1, 0, -1};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      int R = sc.nextInt();
      int C = sc.nextInt();
      char[][] board = new char[R][C];
      for (int r = 0; r < R; ++r) {
        String line = sc.next();
        for (int c = 0; c < C; ++c) {
          board[r][c] = line.charAt(c);
        }
      }

      System.out.println(String.format("Case #%d: %d", tc + 1, solve(board)));
    }

    sc.close();
  }

  static int solve(char[][] board) {
    int R = board.length;
    int C = board[0].length;

    Set<Point> startState = new HashSet<>();
    Set<Point> goalState = new HashSet<>();
    for (int r = 0; r < R; ++r) {
      for (int c = 0; c < C; ++c) {
        if (board[r][c] == 'o') {
          startState.add(new Point(r, c));
        } else if (board[r][c] == 'x') {
          goalState.add(new Point(r, c));
        } else if (board[r][c] == 'w') {
          startState.add(new Point(r, c));
          goalState.add(new Point(r, c));
        }
      }
    }

    Map<Set<Point>, Integer> stateToDistance = new HashMap<>();
    Queue<Element> queue = new ArrayDeque<>();
    queue.offer(new Element(startState, 0));
    while (!queue.isEmpty()) {
      Element head = queue.poll();
      if (head.state.equals(goalState)) {
        return head.distance;
      }

      if (!stateToDistance.containsKey(head.state)) {
        stateToDistance.put(head.state, head.distance);

        boolean stable = isStable(head.state);
        for (Point point : head.state) {
          Set<Point> others =
              head.state.stream().filter(p -> !p.equals(point)).collect(Collectors.toSet());
          for (int i = 0; i < R_OFFSETS.length; ++i) {
            if (isEmpty(board, others, point.r - R_OFFSETS[i], point.c - C_OFFSETS[i])
                && isEmpty(board, others, point.r + R_OFFSETS[i], point.c + C_OFFSETS[i])) {
              Set<Point> nextState =
                  Stream.concat(
                          others.stream(),
                          Stream.of(new Point(point.r + R_OFFSETS[i], point.c + C_OFFSETS[i])))
                      .collect(Collectors.toSet());
              if (!stateToDistance.containsKey(nextState) && (stable || isStable(nextState))) {
                queue.offer(new Element(nextState, head.distance + 1));
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
  int distance;

  Element(Set<Point> state, int distance) {
    this.state = state;
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
