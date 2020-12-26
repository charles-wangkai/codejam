import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 1; tc <= N; ++tc) {
      int P = sc.nextInt();
      int K = sc.nextInt();
      int L = sc.nextInt();
      int[] freqs = new int[L];
      for (int i = 0; i < freqs.length; ++i) {
        freqs[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(P, K, freqs)));
    }

    sc.close();
  }

  static long solve(int P, int K, int[] freqs) {
    int[] sortedFreqs =
        Arrays.stream(freqs).boxed().sorted(Comparator.reverseOrder()).mapToInt(x -> x).toArray();

    long result = 0;
    for (int i = 0; i < sortedFreqs.length; ++i) {
      result += sortedFreqs[i] * (i / K + 1L);
    }

    return result;
  }
}
