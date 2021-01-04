import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class Solution {
  static final int[] R_OFFSETS = {-1, 0, 1, 0};
  static final int[] C_OFFSETS = {0, 1, 0, -1};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 1; tc <= N; ++tc) {
      int R = sc.nextInt();
      int C = sc.nextInt();
      char[][] grid = new char[R][C];
      for (int r = 0; r < R; ++r) {
        String line = sc.next();
        for (int c = 0; c < C; ++c) {
          grid[r][c] = line.charAt(c);
        }
      }

      System.out.println(String.format("Case #%d: %s", tc, solve(grid)));
    }

    sc.close();
  }

  static String solve(char[][] grid) {
    int R = grid.length;
    int C = grid[0].length;

    int startR = -1;
    int startC = -1;
    for (int r = 0; r < R; ++r) {
      for (int c = 0; c < C; ++c) {
        if (grid[r][c] == 'O') {
          startR = r;
          startC = c;
        }
      }
    }

    Map<State, Integer> stateToMoveNum = new HashMap<>();
    Deque<State> deque = new ArrayDeque<>();
    State initialState = new State(startR, startC, -1, -1, -1, -1, -1, -1);
    stateToMoveNum.put(initialState, 0);
    deque.offerLast(initialState);

    while (!deque.isEmpty()) {
      State head = deque.pollFirst();
      if (grid[head.currentR][head.currentC] == 'X') {
        return String.valueOf(stateToMoveNum.get(head));
      }

      for (int portalDirection = 0; portalDirection < R_OFFSETS.length; ++portalDirection) {
        int portalR = head.currentR;
        int portalC = head.currentC;
        while (portalR - R_OFFSETS[portalDirection] >= 0
            && portalR - R_OFFSETS[portalDirection] < R
            && portalC - C_OFFSETS[portalDirection] >= 0
            && portalC - C_OFFSETS[portalDirection] < C
            && grid[portalR - R_OFFSETS[portalDirection]][portalC - C_OFFSETS[portalDirection]]
                != '#') {
          portalR -= R_OFFSETS[portalDirection];
          portalC -= C_OFFSETS[portalDirection];
        }

        State state1 =
            new State(
                head.currentR,
                head.currentC,
                portalR,
                portalC,
                portalDirection,
                head.portalR2,
                head.portalC2,
                head.portalDirection2);
        if (check(state1) && !stateToMoveNum.containsKey(state1)) {
          stateToMoveNum.put(state1, stateToMoveNum.get(head));
          deque.offerFirst(state1);
        }

        State state2 =
            new State(
                head.currentR,
                head.currentC,
                head.portalR1,
                head.portalC1,
                head.portalDirection1,
                portalR,
                portalC,
                portalDirection);
        if (check(state2) && !stateToMoveNum.containsKey(state2)) {
          stateToMoveNum.put(state2, stateToMoveNum.get(head));
          deque.offerFirst(state2);
        }
      }

      for (int direction = 0; direction < R_OFFSETS.length; ++direction) {
        int currentR;
        int currentC;
        if (head.currentR == head.portalR1
            && head.currentC == head.portalC1
            && Math.abs(direction - head.portalDirection1) == 2) {
          currentR = head.portalR2;
          currentC = head.portalC2;
        } else if (head.currentR == head.portalR2
            && head.currentC == head.portalC2
            && Math.abs(direction - head.portalDirection2) == 2) {
          currentR = head.portalR1;
          currentC = head.portalC1;
        } else {
          currentR = head.currentR + R_OFFSETS[direction];
          currentC = head.currentC + C_OFFSETS[direction];
        }

        if (currentR >= 0
            && currentR < R
            && currentC >= 0
            && currentC < C
            && grid[currentR][currentC] != '#') {
          State state =
              new State(
                  currentR,
                  currentC,
                  head.portalR1,
                  head.portalC1,
                  head.portalDirection1,
                  head.portalR2,
                  head.portalC2,
                  head.portalDirection2);
          if (check(state) && !stateToMoveNum.containsKey(state)) {
            stateToMoveNum.put(state, stateToMoveNum.get(head) + 1);
            deque.offerLast(state);
          }
        }
      }
    }

    return "THE CAKE IS A LIE";
  }

  static boolean check(State state) {
    return !(state.portalR1 == state.portalR2 && state.portalC1 == state.portalC2)
        && (state.portalR1 < state.portalR2
            || (state.portalR1 == state.portalR2
                && (state.portalC1 < state.portalC2
                    || (state.portalC1 == state.portalC2
                        && state.portalDirection1 <= state.portalDirection2))));
  }
}

class State {
  int currentR;
  int currentC;
  int portalR1;
  int portalC1;
  int portalDirection1;
  int portalR2;
  int portalC2;
  int portalDirection2;

  State(
      int currentR,
      int currentC,
      int portalR1,
      int portalC1,
      int portalDirection1,
      int portalR2,
      int portalC2,
      int portalDirection2) {
    this.currentR = currentR;
    this.currentC = currentC;
    this.portalR1 = portalR1;
    this.portalC1 = portalC1;
    this.portalDirection1 = portalDirection1;
    this.portalR2 = portalR2;
    this.portalC2 = portalC2;
    this.portalDirection2 = portalDirection2;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        currentR,
        currentC,
        portalR1,
        portalC1,
        portalDirection1,
        portalR2,
        portalC2,
        portalDirection2);
  }

  @Override
  public boolean equals(Object obj) {
    State other = (State) obj;

    return currentR == other.currentR
        && currentC == other.currentC
        && portalR1 == other.portalR1
        && portalC1 == other.portalC1
        && portalDirection1 == other.portalDirection1
        && portalR2 == other.portalR2
        && portalC2 == other.portalC2
        && portalDirection2 == other.portalDirection2;
  }
}
