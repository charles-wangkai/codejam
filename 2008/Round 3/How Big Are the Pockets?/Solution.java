import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Solution {
  static final int[] X_OFFSETS = {0, 1, 0, -1};
  static final int[] Y_OFFSETS = {1, 0, -1, 0};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 1; tc <= N; ++tc) {
      int L = sc.nextInt();
      Pair[] pairs = new Pair[L];
      for (int i = 0; i < pairs.length; ++i) {
        String S = sc.next();
        int T = sc.nextInt();

        pairs[i] = new Pair(S, T);
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(pairs)));
    }

    sc.close();
  }

  static int solve(Pair[] pairs) {
    Map<Integer, List<Integer>> pathLowerXToYs = new HashMap<>();
    Map<Integer, List<Integer>> pathLowerYToXs = new HashMap<>();
    int x = 0;
    int y = 0;
    int direction = 0;
    for (Pair pair : pairs) {
      for (int i = 0; i < pair.T; ++i) {
        for (char ch : pair.S.toCharArray()) {
          if (ch == 'L') {
            direction = (direction - 1 + X_OFFSETS.length) % X_OFFSETS.length;
          } else if (ch == 'R') {
            direction = (direction + 1) % X_OFFSETS.length;
          } else {
            if (direction == 0) {
              addPath(pathLowerYToXs, y, x);
            } else if (direction == 1) {
              addPath(pathLowerXToYs, x, y);
            } else if (direction == 2) {
              addPath(pathLowerYToXs, y - 1, x);
            } else {
              addPath(pathLowerXToYs, x - 1, y);
            }

            x += X_OFFSETS[direction];
            y += Y_OFFSETS[direction];
          }
        }
      }
    }

    Set<Pocket> pockets = new HashSet<>();
    for (int lowerX : pathLowerXToYs.keySet()) {
      List<Integer> ys = pathLowerXToYs.get(lowerX);
      Collections.sort(ys);

      for (int i = 1; i + 1 < ys.size(); i += 2) {
        for (int lowerY = ys.get(i); lowerY <= ys.get(i + 1) - 1; ++lowerY) {
          pockets.add(new Pocket(lowerX, lowerY));
        }
      }
    }
    for (int lowerY : pathLowerYToXs.keySet()) {
      List<Integer> xs = pathLowerYToXs.get(lowerY);
      Collections.sort(xs);

      for (int i = 1; i + 1 < xs.size(); i += 2) {
        for (int lowerX = xs.get(i); lowerX <= xs.get(i + 1) - 1; ++lowerX) {
          pockets.add(new Pocket(lowerX, lowerY));
        }
      }
    }

    return pockets.size();
  }

  static void addPath(Map<Integer, List<Integer>> path, int key, int value) {
    if (!path.containsKey(key)) {
      path.put(key, new ArrayList<>());
    }

    path.get(key).add(value);
  }
}

class Pocket {
  int lowerX;
  int lowerY;

  Pocket(int lowerX, int lowerY) {
    this.lowerX = lowerX;
    this.lowerY = lowerY;
  }

  @Override
  public int hashCode() {
    return Objects.hash(lowerX, lowerY);
  }

  @Override
  public boolean equals(Object obj) {
    Pocket other = (Pocket) obj;

    return lowerX == other.lowerX && lowerY == other.lowerY;
  }
}

class Pair {
  String S;
  int T;

  Pair(String s, int t) {
    S = s;
    T = t;
  }
}
