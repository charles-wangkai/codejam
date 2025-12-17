import java.util.Scanner;

public class Main {
  static final String TARGET = "welcome to code jam";
  static final int MODULUS = 10000;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    sc.nextLine();
    for (int tc = 0; tc < N; ++tc) {
      String text = sc.nextLine();

      System.out.println(String.format("Case #%d: %04d", tc + 1, solve(text)));
    }

    sc.close();
  }

  static int solve(String text) {
    int[] dp = new int[TARGET.length() + 1];
    dp[0] = 1;

    for (char c : text.toCharArray()) {
      for (int length = TARGET.length(); length >= 1; --length) {
        if (TARGET.charAt(length - 1) == c) {
          dp[length] = addMod(dp[length], dp[length - 1]);
        }
      }
    }

    return dp[dp.length - 1];
  }

  static int addMod(int x, int y) {
    return Math.floorMod(x + y, MODULUS);
  }
}
