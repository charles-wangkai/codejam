import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Solution {
  static final int PRINTER_NUM = 3;
  static final int TARGET_SUM = 1_000_000;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int[] C = new int[PRINTER_NUM];
      int[] M = new int[PRINTER_NUM];
      int[] Y = new int[PRINTER_NUM];
      int[] K = new int[PRINTER_NUM];
      for (int i = 0; i < PRINTER_NUM; ++i) {
        C[i] = sc.nextInt();
        M[i] = sc.nextInt();
        Y[i] = sc.nextInt();
        K[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %s", tc, solve(C, M, Y, K)));
    }

    sc.close();
  }

  static String solve(int[] C, int[] M, int[] Y, int[] K) {
    int[] colors = {
      Arrays.stream(C).min().getAsInt(),
      Arrays.stream(M).min().getAsInt(),
      Arrays.stream(Y).min().getAsInt(),
      Arrays.stream(K).min().getAsInt()
    };
    int rest = Arrays.stream(colors).sum() - TARGET_SUM;
    if (rest < 0) {
      return "IMPOSSIBLE";
    }

    for (int i = 0; i < colors.length; ++i) {
      int subtrahend = Math.min(rest, colors[i]);
      colors[i] -= subtrahend;
      rest -= subtrahend;
    }

    return Arrays.stream(colors).mapToObj(String::valueOf).collect(Collectors.joining(" "));
  }
}