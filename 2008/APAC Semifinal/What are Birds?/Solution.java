import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 1; tc <= C; ++tc) {
      int N = sc.nextInt();
      Animal[] shown = new Animal[N];
      for (int i = 0; i < shown.length; ++i) {
        int H = sc.nextInt();
        int W = sc.nextInt();
        String X = sc.nextLine().trim();

        shown[i] = new Animal(H, W, X);
      }
      int M = sc.nextInt();
      Animal[] notShown = new Animal[M];
      for (int i = 0; i < notShown.length; ++i) {
        int H = sc.nextInt();
        int W = sc.nextInt();

        notShown[i] = new Animal(H, W, null);
      }

      System.out.println(String.format("Case #%d:\n%s", tc, solve(shown, notShown)));
    }

    sc.close();
  }

  static String solve(Animal[] shown, Animal[] notShown) {
    List<Animal> animals = Arrays.stream(shown).collect(Collectors.toList());

    for (Animal a : notShown) {
      animals.add(a);

      a.X = "BIRD";
      boolean birdPossible = isPossible(animals);

      a.X = "NOT BIRD";
      boolean notBirdPossible = isPossible(animals);

      if (birdPossible && notBirdPossible) {
        a.X = "UNKNOWN";
      } else if (birdPossible) {
        a.X = "BIRD";
      } else {
        a.X = "NOT BIRD";
      }

      animals.remove(animals.size() - 1);
    }

    return Arrays.stream(notShown).map(a -> a.X).collect(Collectors.joining("\n"));
  }

  static boolean isPossible(List<Animal> animals) {
    int minH = Integer.MAX_VALUE;
    int maxH = Integer.MIN_VALUE;
    int minW = Integer.MAX_VALUE;
    int maxW = Integer.MIN_VALUE;

    for (Animal a : animals) {
      if (a.X.equals("BIRD")) {
        minH = Math.min(minH, a.H);
        maxH = Math.max(maxH, a.H);
        minW = Math.min(minW, a.W);
        maxW = Math.max(maxW, a.W);
      }
    }

    int minH_ = minH;
    int maxH_ = maxH;
    int minW_ = minW;
    int maxW_ = maxW;
    return animals.stream()
        .allMatch(
            a ->
                a.X.equals("BIRD")
                    || !(a.H >= minH_ && a.H <= maxH_ && a.W >= minW_ && a.W <= maxW_));
  }
}

class Animal {
  int H;
  int W;
  String X;

  Animal(int h, int w, String x) {
    H = h;
    W = w;
    X = x;
  }
}
