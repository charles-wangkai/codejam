import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 1; tc <= C; ++tc) {
      int M = sc.nextInt();
      int Q = sc.nextInt();
      double[][] p = new double[Q][4];
      for (int i = 0; i < p.length; ++i) {
        for (int j = 0; j < p[i].length; ++j) {
          p[i][j] = sc.nextDouble();
        }
      }

      System.out.println(String.format("Case #%d: %.9f", tc, solve(M, p)));
    }

    sc.close();
  }

  static double solve(int M, double[][] p) {
    List<Double> probs = new ArrayList<>();
    search(probs, p, 0, 1);

    return probs.stream().sorted(Comparator.reverseOrder()).limit(M).mapToDouble(x -> x).sum();
  }

  static void search(List<Double> probs, double[][] p, int index, double prob) {
    if (index == p.length) {
      probs.add(prob);

      return;
    }

    for (double pi : p[index]) {
      search(probs, p, index + 1, prob * pi);
    }
  }
}
