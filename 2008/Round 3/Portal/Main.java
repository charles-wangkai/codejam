import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
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

    Map<Integer, Integer> stateToMoveNum = new HashMap<>();
    Deque<Integer> deque = new ArrayDeque<>();
    int initialState = toState(startR, startC, R, C, R, C);
    stateToMoveNum.put(initialState, 0);
    deque.offerLast(initialState);

    while (!deque.isEmpty()) {
      int head = deque.pollFirst();
      int headCurrentR = head % 16;
      int headCurrentC = (head >> 4) % 16;
      int headPortalR1 = (head >> 8) % 16;
      int headPortalC1 = (head >> 12) % 16;
      int headPortalR2 = (head >> 16) % 16;
      int headPortalC2 = head >> 20;

      if (grid[headCurrentR][headCurrentC] == 'X') {
        return String.valueOf(stateToMoveNum.get(head));
      }

      for (int portalDirection = 0; portalDirection < R_OFFSETS.length; ++portalDirection) {
        int portalR = headCurrentR;
        int portalC = headCurrentC;
        while (portalR + R_OFFSETS[portalDirection] >= 0
            && portalR + R_OFFSETS[portalDirection] < R
            && portalC + C_OFFSETS[portalDirection] >= 0
            && portalC + C_OFFSETS[portalDirection] < C
            && grid[portalR + R_OFFSETS[portalDirection]][portalC + C_OFFSETS[portalDirection]]
                != '#') {
          portalR += R_OFFSETS[portalDirection];
          portalC += C_OFFSETS[portalDirection];
        }

        for (int state :
            new int[] {
              toState(headCurrentR, headCurrentC, portalR, portalC, headPortalR2, headPortalC2),
              toState(headCurrentR, headCurrentC, headPortalR1, headPortalC1, portalR, portalC)
            }) {
          if (!stateToMoveNum.containsKey(state)) {
            stateToMoveNum.put(state, stateToMoveNum.get(head));
            deque.offerFirst(state);
          }
        }
      }

      for (int direction = 0; direction < R_OFFSETS.length; ++direction) {
        int currentR = headCurrentR + R_OFFSETS[direction];
        int currentC = headCurrentC + C_OFFSETS[direction];
        if (currentR >= 0
            && currentR < R
            && currentC >= 0
            && currentC < C
            && grid[currentR][currentC] != '#') {
          int state =
              toState(currentR, currentC, headPortalR1, headPortalC1, headPortalR2, headPortalC2);
          if (!stateToMoveNum.containsKey(state)) {
            stateToMoveNum.put(state, stateToMoveNum.get(head) + 1);
            deque.offerLast(state);
          }
        }
      }

      if (headPortalR1 >= 0
          && headPortalR1 < R
          && headPortalC1 >= 0
          && headPortalC1 < C
          && headPortalR2 >= 0
          && headPortalR2 < R
          && headPortalC2 >= 0
          && headPortalC2 < C) {
        if (headCurrentR == headPortalR1 && headCurrentC == headPortalC1) {
          int currentR = headPortalR2;
          int currentC = headPortalC2;

          int state =
              toState(currentR, currentC, headPortalR1, headPortalC1, headPortalR2, headPortalC2);
          if (!stateToMoveNum.containsKey(state)) {
            stateToMoveNum.put(state, stateToMoveNum.get(head) + 1);
            deque.offerLast(state);
          }
        } else if (headCurrentR == headPortalR2 && headCurrentC == headPortalC2) {
          int currentR = headPortalR1;
          int currentC = headPortalC1;

          int state =
              toState(currentR, currentC, headPortalR1, headPortalC1, headPortalR2, headPortalC2);
          if (!stateToMoveNum.containsKey(state)) {
            stateToMoveNum.put(state, stateToMoveNum.get(head) + 1);
            deque.offerLast(state);
          }
        }
      }
    }

    return "THE CAKE IS A LIE";
  }

  static int toState(
      int currentR, int currentC, int portalR1, int portalC1, int portalR2, int portalC2) {
    return currentR
        + (currentC << 4)
        + (portalR1 << 8)
        + (portalC1 << 12)
        + (portalR2 << 16)
        + (portalC2 << 20);
  }
}
