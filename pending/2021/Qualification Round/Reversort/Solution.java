import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      int[] L = new int[N];
      for (int i = 0; i < L.length; ++i) {
        L[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(L)));
    }

    sc.close();
  }

  static int solve(int[] L) {
    int result = 0;
    for (int i = 0; i < L.length - 1; ++i) {
      int endIndex = i;
      for (int j = i; j < L.length; ++j) {
        if (L[j] < L[endIndex]) {
          endIndex = j;
        }
      }

      result += endIndex - i + 1;
      for (int j = i, k = endIndex; j <= k; ++j, --k) {
        int temp = L[j];
        L[j] = L[k];
        L[k] = temp;
      }
    }

    return result;
  }
}
