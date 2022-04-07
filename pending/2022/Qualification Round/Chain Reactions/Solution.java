import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solution {
  static long funSum;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      int[] F = new int[N];
      for (int i = 0; i < F.length; ++i) {
        F[i] = sc.nextInt();
      }
      int[] P = new int[N];
      for (int i = 0; i < P.length; ++i) {
        P[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(F, P)));
    }

    sc.close();
  }

  static long solve(int[] F, int[] P) {
    int N = F.length;

    @SuppressWarnings("unchecked")
    List<Integer>[] childLists = new List[N + 1];
    for (int i = 0; i < childLists.length; ++i) {
      childLists[i] = new ArrayList<>();
    }
    for (int i = 0; i < P.length; ++i) {
      childLists[P[i]].add(i + 1);
    }

    funSum = 0;
    for (int child : childLists[0]) {
      int sub = search(F, childLists, child);
      funSum += sub;
    }

    return funSum;
  }

  static int search(int[] F, List<Integer>[] childLists, int node) {
    if (childLists[node].isEmpty()) {
      return F[node - 1];
    }

    long subSum = 0;
    int subMin = Integer.MAX_VALUE;
    for (int child : childLists[node]) {
      int sub = search(F, childLists, child);

      subSum += sub;
      subMin = Math.min(subMin, sub);
    }

    funSum += subSum - subMin;

    return Math.max(F[node - 1], subMin);
  }
}