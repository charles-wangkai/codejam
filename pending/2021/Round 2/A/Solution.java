import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    int N = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      for (int i = 0; i < N - 1; ++i) {
        System.out.println(String.format("M %d %d", i + 1, N));
        System.out.flush();
        int minIndex = sc.nextInt() - 1;

        if (minIndex != i) {
          System.out.println(String.format("S %d %d", i + 1, minIndex + 1));
          System.out.flush();
          int resp = sc.nextInt();
          if (resp == -1) {
            System.exit(1);
          }
        }
      }

      System.out.println("D");
      System.out.flush();
      int resp = sc.nextInt();
      if (resp == -1) {
        System.exit(1);
      }
    }

    sc.close();
  }
}
