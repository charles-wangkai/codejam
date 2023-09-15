import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 1; tc <= C; ++tc) {
      long A = sc.nextLong();
      long B = sc.nextLong();
      long P = sc.nextLong();

      System.out.println(String.format("Case #%d: %d", tc, solve(A, B, P)));
    }

    sc.close();
  }

  static int solve(long A, long B, long P) {
    int size = (int) (B - A) + 1;
    int[] parents = new int[size];
    Arrays.fill(parents, -1);

    boolean[] primes = new boolean[size];
    Arrays.fill(primes, true);
    for (int i = 2; i < primes.length; ++i) {
      if (primes[i]) {
        for (int j = 2 * i; j < primes.length; j += i) {
          primes[j] = false;
        }

        if (i >= P) {
          for (int j = (int) ((A + i - 1) / i * i - A); j + i < parents.length; j += i) {
            int root1 = findRoot(parents, j);
            int root2 = findRoot(parents, j + i);
            if (root1 != root2) {
              parents[root2] = root1;
            }
          }
        }
      }
    }

    return (int) Arrays.stream(parents).filter(parent -> parent == -1).count();
  }

  static int findRoot(int[] parents, int node) {
    int root = node;
    while (parents[root] != -1) {
      root = parents[root];
    }

    int p = node;
    while (p != root) {
      int next = parents[p];
      parents[p] = root;

      p = next;
    }

    return root;
  }
}
