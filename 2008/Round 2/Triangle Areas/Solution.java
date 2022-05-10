// https://codingcompetitions.withgoogle.com/codejam/round/0000000000432cc0/0000000000432bd5#analysis

import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 1; tc <= C; ++tc) {
      int N = sc.nextInt();
      int M = sc.nextInt();
      int A = sc.nextInt();

      System.out.println(String.format("Case #%d: %s", tc, solve(N, M, A)));
    }

    sc.close();
  }

  static String solve(int N, int M, int A) {
    if (A > N * M) {
      return "IMPOSSIBLE";
    }

    int x1 = A / M;
    int y1 = 0;
    int x2 = x1 + ((A % M == 0) ? 0 : 1);
    int y2 = M;
    int x3 = 0;
    int y3 = A % M;

    return String.format("%d %d %d %d %d %d", x1, y1, x2, y2, x3, y3);
  }
}
