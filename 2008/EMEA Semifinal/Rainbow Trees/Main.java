import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  static final int MODULUS = 1_000_000_009;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 1; tc <= C; ++tc) {
      int n = sc.nextInt();
      int k = sc.nextInt();
      int[] x = new int[n - 1];
      int[] y = new int[n - 1];
      for (int i = 0; i < n - 1; ++i) {
        x[i] = sc.nextInt();
        y[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(x, y, k)));
    }

    sc.close();
  }

  static int solve(int[] x, int[] y, int k) {
    int n = x.length + 1;

    @SuppressWarnings("unchecked")
    List<Integer>[] adjLists = new List[n];
    for (int i = 0; i < adjLists.length; ++i) {
      adjLists[i] = new ArrayList<>();
    }
    for (int i = 0; i < x.length; ++i) {
      adjLists[x[i] - 1].add(y[i] - 1);
      adjLists[y[i] - 1].add(x[i] - 1);
    }

    return search(k, adjLists, -1, 0, 0);
  }

  static int search(int k, List<Integer>[] adjLists, int parent, int node, int usedNum) {
    int result = 1;
    int rest = k - usedNum;
    for (int adj : adjLists[node]) {
      if (adj != parent) {
        result =
            multiplyMod(
                multiplyMod(result, rest), search(k, adjLists, node, adj, adjLists[node].size()));
        --rest;
      }
    }

    return result;
  }

  static int multiplyMod(int x, int y) {
    return Math.floorMod((long) x * y, MODULUS);
  }
}
