import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 0; tc < C; ++tc) {
      long A = sc.nextLong();
      long B = sc.nextLong();
      long P = sc.nextLong();

      System.out.println(String.format("Case #%d: %d", tc + 1, solve(A, B, P)));
    }

    sc.close();
  }

  static int solve(long A, long B, long P) {
    int size = (int) (B - A) + 1;

    Dsu dsu = new Dsu(size);

    boolean[] primes = new boolean[size];
    Arrays.fill(primes, true);
    for (int i = 2; i < primes.length; ++i) {
      if (primes[i]) {
        for (int j = 2 * i; j < primes.length; j += i) {
          primes[j] = false;
        }

        if (i >= P) {
          for (int j = (int) ((A + i - 1) / i * i - A); j + i < size; j += i) {
            dsu.union(j, j + i);
          }
        }
      }
    }

    return (int) Arrays.stream(dsu.parentOrSizes).filter(parentOrSize -> parentOrSize < 0).count();
  }
}

class Dsu {
  int[] parentOrSizes;

  Dsu(int n) {
    parentOrSizes = new int[n];
    Arrays.fill(parentOrSizes, -1);
  }

  int find(int a) {
    if (parentOrSizes[a] < 0) {
      return a;
    }

    parentOrSizes[a] = find(parentOrSizes[a]);

    return parentOrSizes[a];
  }

  void union(int a, int b) {
    int aLeader = find(a);
    int bLeader = find(b);
    if (aLeader != bLeader) {
      parentOrSizes[aLeader] += parentOrSizes[bLeader];
      parentOrSizes[bLeader] = aLeader;
    }
  }

  int getSize(int a) {
    return -parentOrSizes[find(a)];
  }

  Map<Integer, List<Integer>> buildLeaderToGroup() {
    Map<Integer, List<Integer>> leaderToGroup = new HashMap<>();
    for (int i = 0; i < parentOrSizes.length; ++i) {
      int leader = find(i);
      leaderToGroup.putIfAbsent(leader, new ArrayList<>());
      leaderToGroup.get(leader).add(i);
    }

    return leaderToGroup;
  }
}
