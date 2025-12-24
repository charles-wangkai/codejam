import java.util.Arrays;
import java.util.Scanner;

public class Main {
  static final double EPSILON = 1e-12;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      int C = sc.nextInt();
      int N = sc.nextInt();

      System.out.println(String.format("Case #%d: %.9f", tc + 1, solve(C, N)));
    }

    sc.close();
  }

  static double solve(int C, int N) {
    double result = 0;
    double[] probs = new double[C + 1];
    probs[0] = 1;

    int length = 0;
    while (Arrays.stream(probs).sum() > EPSILON) {
      ++length;

      double[] nextProbs = new double[C + 1];
      for (int i = 0; i < nextProbs.length; ++i) {
        for (int j = 0; j <= i; ++j) {
          int added = i - j;
          int old = N - added;
          if (old >= 0 && old <= j && !(i == C && j == C)) {
            nextProbs[i] += probs[j] * computeProb(j, C - j, old, added);
          }
        }
      }

      result += length * nextProbs[C];

      probs = nextProbs;
    }

    return result;
  }

  static double computeProb(int oldN, int newN, int oldR, int newR) {
    return (double) C(oldN, oldR) * C(newN, newR) / C(oldN + newN, oldR + newR);
  }

  static long C(int n, int r) {
    long result = 1;
    for (int i = 0; i < r; ++i) {
      result = result * (n - i) / (i + 1);
    }

    return result;
  }
}
