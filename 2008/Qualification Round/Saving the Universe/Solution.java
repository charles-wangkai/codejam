import java.util.Arrays;
import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 1; tc <= N; ++tc) {
      int S = sc.nextInt();
      sc.nextLine();
      String[] engines = new String[S];
      for (int i = 0; i < engines.length; ++i) {
        engines[i] = sc.nextLine();
      }
      int Q = sc.nextInt();
      sc.nextLine();
      String[] queries = new String[Q];
      for (int i = 0; i < queries.length; ++i) {
        queries[i] = sc.nextLine();
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(engines, queries)));
    }

    sc.close();
  }

  static int solve(String[] engines, String[] queries) {
    int[] switchNums = new int[engines.length];
    for (String query : queries) {
      int[] nextSwitchNums = new int[switchNums.length];
      Arrays.fill(nextSwitchNums, Integer.MAX_VALUE);

      for (int i = 0; i < nextSwitchNums.length; ++i) {
        if (!engines[i].equals(query)) {
          for (int j = 0; j < switchNums.length; ++j) {
            if (switchNums[j] != Integer.MAX_VALUE) {
              nextSwitchNums[i] = Math.min(nextSwitchNums[i], switchNums[j] + (j == i ? 0 : 1));
            }
          }
        }
      }

      switchNums = nextSwitchNums;
    }

    return Arrays.stream(switchNums).min().getAsInt();
  }
}
