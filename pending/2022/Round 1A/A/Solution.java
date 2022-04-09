import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      String S = sc.next();

      System.out.println(String.format("Case #%d: %s", tc, solve(S)));
    }

    sc.close();
  }

  static String solve(String S) {
    String result = "";
    char prev = 0;
    char curr = 0;
    int count = 0;
    for (int i = S.length() - 1; i >= -1; --i) {
      if (i != -1 && S.charAt(i) == curr) {
        ++count;
      } else {
        int length = count * ((curr < prev) ? 2 : 1);
        result = String.valueOf(curr).repeat(length) + result;

        prev = curr;
        if (i != -1) {
          curr = S.charAt(i);
          count = 1;
        }
      }
    }

    return result;
  }
}