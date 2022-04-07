import java.util.Arrays;
import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      int[] S = new int[N];
      for (int i = 0; i < S.length; ++i) {
        S[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(S)));
    }

    sc.close();
  }

  static int solve(int[] S) {
    int[] sorted = Arrays.stream(S).boxed().sorted().mapToInt(x -> x).toArray();

    int result = 0;
    int minSide = 1;
    for (int side : sorted) {
      if (side >= minSide) {
        ++result;
        ++minSide;
      }
    }

    return result;
  }
}