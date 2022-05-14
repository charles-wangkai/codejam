import java.util.Arrays;
import java.util.Scanner;

public class Solution {
  static final String TARGET = "welcome to code jam";
  static final int MODULUS = 10000;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    sc.nextLine();
    for (int tc = 1; tc <= N; ++tc) {
      String text = sc.nextLine();

      System.out.println(String.format("Case #%d: %04d", tc, solve(text)));
    }

    sc.close();
  }

  static int solve(String text) {
    int[] wayNums = new int[TARGET.length()];
    for (char ch : text.toCharArray()) {
      int[] nextWayNums = Arrays.copyOf(wayNums, wayNums.length);
      for (int i = 0; i < nextWayNums.length; ++i) {
        if (TARGET.charAt(i) == ch) {
          nextWayNums[i] = addMod(nextWayNums[i], (i == 0) ? 1 : wayNums[i - 1]);
        }
      }

      wayNums = nextWayNums;
    }

    return wayNums[wayNums.length - 1];
  }

  static int addMod(int x, int y) {
    return (x + y) % MODULUS;
  }
}
