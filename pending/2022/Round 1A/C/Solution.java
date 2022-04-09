import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int E = sc.nextInt();
      int W = sc.nextInt();
      int[][] X = new int[E][W];
      for (int i = 0; i < E; ++i) {
        for (int j = 0; j < W; ++j) {
          X[i][j] = sc.nextInt();
        }
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(X)));
    }

    sc.close();
  }

  static int solve(int[][] X) {
    Map<String, Integer> stateToOperNum = Map.of("", 0);
    for (int[] exercise : X) {
      List<String> states = buildStates(exercise);

      Map<String, Integer> nextStateToOperNum = new HashMap<>();
      for (String nextState : states) {
        for (String prevState : stateToOperNum.keySet()) {
          nextStateToOperNum.put(
              nextState,
              Math.min(
                  nextStateToOperNum.getOrDefault(nextState, Integer.MAX_VALUE),
                  stateToOperNum.get(prevState) + computeOperNum(prevState, nextState)));
        }
      }

      stateToOperNum = nextStateToOperNum;
    }

    return stateToOperNum.values().stream().mapToInt(x -> x).min().getAsInt()
        + stateToOperNum.keySet().iterator().next().length();
  }

  static int computeOperNum(String prevState, String nextState) {
    int common = 0;
    while (common < prevState.length()
        && common < nextState.length()
        && prevState.charAt(common) == nextState.charAt(common)) {
      ++common;
    }

    return prevState.length() + nextState.length() - common * 2;
  }

  static List<String> buildStates(int[] exercise) {
    List<String> states = new ArrayList<>();
    search(states, exercise, new StringBuilder());

    return states;
  }

  static void search(List<String> states, int[] rest, StringBuilder current) {
    boolean found = false;
    for (int i = 0; i < rest.length; ++i) {
      if (rest[i] != 0) {
        current.append(i);
        --rest[i];
        search(states, rest, current);
        ++rest[i];
        current.deleteCharAt(current.length() - 1);

        found = true;
      }
    }

    if (!found) {
      states.add(current.toString());
    }
  }
}