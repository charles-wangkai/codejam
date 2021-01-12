import java.util.Map;
import java.util.NavigableMap;
import java.util.Scanner;
import java.util.TreeMap;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 1; tc <= N; ++tc) {
      int M = sc.nextInt();
      double P = sc.nextDouble();
      int X = sc.nextInt();

      System.out.println(String.format("Case #%d: %.9f", tc, solve(M, P, X)));
    }

    sc.close();
  }

  static double solve(int M, double P, int X) {
    NavigableMap<Double, Double> rangeStartToProb = new TreeMap<>();
    rangeStartToProb.put(1_000_000.0, 1.0);
    rangeStartToProb.put(0.0, 0.0);

    for (int i = 0; i < M; ++i) {
      NavigableMap<Double, Double> nextRangeStartToProb = new TreeMap<>();
      double[] rangeStarts = rangeStartToProb.keySet().stream().mapToDouble(x -> x).toArray();
      for (int j = 0; j < rangeStarts.length; ++j) {
        for (int k = j; k < rangeStarts.length; ++k) {
          updateMap(
              nextRangeStartToProb,
              (rangeStarts[j] + rangeStarts[k]) / 2,
              (1 - P) * rangeStartToProb.get(rangeStarts[j])
                  + P * rangeStartToProb.get(rangeStarts[k]));
        }
      }

      rangeStartToProb = nextRangeStartToProb;
    }

    return rangeStartToProb.floorEntry((double) X).getValue();
  }

  static void updateMap(Map<Double, Double> map, double key, double value) {
    map.put(key, Math.max(map.getOrDefault(key, 0.0), value));
  }
}
