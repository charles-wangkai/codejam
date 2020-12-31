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

    int x1;
    int y1;
    int x2;
    int y2;
    int x3;
    int y3;
    if (A % M == 0) {
      x1 = A / M;
      y1 = 0;
      x2 = A / M;
      y2 = M;
      x3 = 0;
      y3 = 0;
    } else {
      x1 = A / M;
      y1 = 0;
      x2 = A / M + 1;
      y2 = M;
      x3 = 0;
      y3 = A % M;
    }

    return String.format("%d %d %d %d %d %d", x1, y1, x2, y2, x3, y3);
  }
}
