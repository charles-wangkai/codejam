import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 1; tc <= C; ++tc) {
      int N = sc.nextInt();
      int M = sc.nextInt();
      @SuppressWarnings("unchecked")
      Map<Integer, Boolean>[] flavorToMaltedMaps = new Map[M];
      for (int i = 0; i < flavorToMaltedMaps.length; ++i) {
        flavorToMaltedMaps[i] = new HashMap<>();
      }
      for (int i = 0; i < flavorToMaltedMaps.length; ++i) {
        int T = sc.nextInt();
        for (int j = 0; j < T; ++j) {
          int flavor = sc.nextInt() - 1;
          int malted = sc.nextInt();

          flavorToMaltedMaps[i].put(flavor, malted == 1);
        }
      }

      System.out.println(String.format("Case #%d: %s", tc, solve(N, flavorToMaltedMaps)));
    }

    sc.close();
  }

  static String solve(int N, Map<Integer, Boolean>[] flavorToMaltedMaps) {
    boolean[] result = new boolean[N];
    boolean[] satisfied = new boolean[flavorToMaltedMaps.length];
    while (true) {
      int[] maltedFlavors =
          IntStream.range(0, satisfied.length)
              .filter(
                  i ->
                      !satisfied[i]
                          && flavorToMaltedMaps[i].size() == 1
                          && flavorToMaltedMaps[i].values().iterator().next())
              .map(i -> flavorToMaltedMaps[i].keySet().iterator().next())
              .distinct()
              .toArray();
      if (maltedFlavors.length == 0) {
        break;
      }

      for (int maltedFlavor : maltedFlavors) {
        result[maltedFlavor] = true;
      }

      for (int i = 0; i < satisfied.length; ++i) {
        if (!satisfied[i]) {
          for (int maltedFlavor : maltedFlavors) {
            if (flavorToMaltedMaps[i].containsKey(maltedFlavor)) {
              if (flavorToMaltedMaps[i].get(maltedFlavor)) {
                satisfied[i] = true;

                break;
              } else {
                flavorToMaltedMaps[i].remove(maltedFlavor);
              }
            }
          }
        }
      }

      if (IntStream.range(0, satisfied.length)
          .anyMatch(i -> !satisfied[i] && flavorToMaltedMaps[i].isEmpty())) {
        return "IMPOSSIBLE";
      }
    }

    return IntStream.range(0, result.length)
        .map(i -> result[i] ? 1 : 0)
        .mapToObj(String::valueOf)
        .collect(Collectors.joining(" "));
  }
}