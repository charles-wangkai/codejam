import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solution {
  static final int MODULUS = 1_000_000_009;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 1; tc <= C; ++tc) {
      int n = sc.nextInt();
      int k = sc.nextInt();
      @SuppressWarnings("unchecked")
      List<Integer>[] edgeLists = new List[n];
      for (int i = 0; i < edgeLists.length; ++i) {
        edgeLists[i] = new ArrayList<>();
      }
      for (int i = 0; i < n - 1; ++i) {
        int x = sc.nextInt() - 1;
        int y = sc.nextInt() - 1;

        edgeLists[x].add(y);
        edgeLists[y].add(x);
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(edgeLists, k)));
    }

    sc.close();
  }

  static int solve(List<Integer>[] edgeLists, int k) {
    int n = edgeLists.length;

    @SuppressWarnings("unchecked")
    List<Integer>[] childLists = new List[n];
    for (int i = 0; i < childLists.length; ++i) {
      childLists[i] = new ArrayList<>();
    }
    buildChildLists(childLists, edgeLists, new boolean[n], 0);

    return search(k, childLists, 0, 0);
  }

  static void buildChildLists(
      List<Integer>[] childLists, List<Integer>[] edgeLists, boolean[] visited, int v) {
    visited[v] = true;

    for (int adj : edgeLists[v]) {
      if (!visited[adj]) {
        childLists[v].add(adj);
        buildChildLists(childLists, edgeLists, visited, adj);
      }
    }
  }

  static int search(int k, List<Integer>[] childLists, int v, int usedNum) {
    int result = 1;

    for (int i = 0; i < childLists[v].size(); ++i) {
      result = multiplyMod(result, Math.max(0, k - (usedNum + i)));
      result =
          multiplyMod(
              result,
              search(
                  k,
                  childLists,
                  childLists[v].get(i),
                  childLists[v].size() + (usedNum == 0 ? 0 : 1)));
    }

    return result;
  }

  static int multiplyMod(int x, int y) {
    return (int) ((long) x * y % MODULUS);
  }
}
