import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 1; tc <= N; ++tc) {
      int P = sc.nextInt();
      int Q = sc.nextInt();
      int[] released = new int[Q];
      for (int i = 0; i < released.length; ++i) {
        released[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(P, released)));
    }

    sc.close();
  }

  static int solve(int P, int[] released) {
    int Q = released.length;

    int[][] coinNums = new int[Q][Q];
    for (int length = 1; length <= Q; ++length) {
      for (int beginIndex = 0; beginIndex + length - 1 < Q; ++beginIndex) {
        int endIndex = beginIndex + length - 1;

        int left = (beginIndex == 0) ? 1 : (released[beginIndex - 1] + 1);
        int right = (endIndex == Q - 1) ? P : (released[endIndex + 1] - 1);

        coinNums[beginIndex][endIndex] = Integer.MAX_VALUE;
        for (int i = beginIndex; i <= endIndex; ++i) {
          coinNums[beginIndex][endIndex] =
              Math.min(
                  coinNums[beginIndex][endIndex],
                  right
                      - left
                      + ((i == beginIndex) ? 0 : coinNums[beginIndex][i - 1])
                      + ((i == endIndex) ? 0 : coinNums[i + 1][endIndex]));
        }
      }
    }

    return coinNums[0][Q - 1];
  }
}
