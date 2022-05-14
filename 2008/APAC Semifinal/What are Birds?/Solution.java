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

    String[] result = new String[notShown.length];
    for (int i = 0; i < result.length; ++i) {
      animals.add(notShown[i]);

      notShown[i].X = "BIRD";
      boolean birdPossible = isPossible(animals);

      notShown[i].X = "NOT BIRD";
      boolean notBirdPossible = isPossible(animals);

      if (birdPossible && notBirdPossible) {
        result[i] = "UNKNOWN";
      } else if (birdPossible) {
        result[i] = "BIRD";
      } else {
        result[i] = "NOT BIRD";
      }

      animals.remove(animals.size() - 1);
    }

    return String.join("\n", result);
  }

  static boolean isPossible(List<Animal> animals) {
    int minH = Integer.MAX_VALUE;
    int maxH = Integer.MIN_VALUE;
    int minW = Integer.MAX_VALUE;
    int maxW = Integer.MIN_VALUE;
    for (Animal animal : animals) {
      if (animal.X.equals("BIRD")) {
        minH = Math.min(minH, animal.H);
        maxH = Math.max(maxH, animal.H);
        minW = Math.min(minW, animal.W);
        maxW = Math.max(maxW, animal.W);
      }
    }

    int minH_ = minH;
    int maxH_ = maxH;
    int minW_ = minW;
    int maxW_ = maxW;
    return animals.stream()
        .filter(animal -> animal.X.equals("NOT BIRD"))
        .allMatch(
            animal ->
                !(animal.H >= minH_
                    && animal.H <= maxH_
                    && animal.W >= minW_
                    && animal.W <= maxW_));
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
