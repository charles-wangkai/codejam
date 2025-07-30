import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 1; tc <= C; ++tc) {
      int N = sc.nextInt();
      String recipe = null;
      Map<String, List<String>> mixtureToIngredients = new HashMap<>();
      for (int i = 0; i < N; ++i) {
        String mixture = sc.next();
        if (i == 0) {
          recipe = mixture;
        }

        List<String> ingredients = new ArrayList<>();
        int M = sc.nextInt();
        for (int j = 0; j < M; ++j) {
          String ingredient = sc.next();

          ingredients.add(ingredient);
        }

        mixtureToIngredients.put(mixture, ingredients);
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(recipe, mixtureToIngredients)));
    }

    sc.close();
  }

  static int solve(String recipe, Map<String, List<String>> mixtureToIngredients) {
    Map<String, Integer> mixtureToBowlNum = new HashMap<>();
    search(mixtureToIngredients, mixtureToBowlNum, recipe);

    return mixtureToBowlNum.get(recipe);
  }

  static void search(
      Map<String, List<String>> mixtureToIngredients,
      Map<String, Integer> mixtureToBowlNum,
      String mixture) {
    List<Integer> bowlNums = new ArrayList<>();
    for (String ingredient : mixtureToIngredients.get(mixture)) {
      if (mixtureToIngredients.containsKey(ingredient)) {
        search(mixtureToIngredients, mixtureToBowlNum, ingredient);
        bowlNums.add(mixtureToBowlNum.get(ingredient));
      }
    }

    Collections.sort(bowlNums, Comparator.reverseOrder());

    mixtureToBowlNum.put(
        mixture,
        Math.max(
            IntStream.range(0, bowlNums.size()).map(i -> bowlNums.get(i) + i).max().orElse(0),
            bowlNums.size() + 1));
  }
}
