import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 1; tc <= C; ++tc) {
      int N = sc.nextInt();
      int M = sc.nextInt();
      @SuppressWarnings("unchecked")
      Set<Like>[] likes = new Set[M];
      for (int i = 0; i < likes.length; ++i) {
        likes[i] = new HashSet<>();
      }
      for (int i = 0; i < likes.length; ++i) {
        int T = sc.nextInt();
        for (int j = 0; j < T; ++j) {
          int flavorIndex = sc.nextInt() - 1;
          int malted = sc.nextInt();

          likes[i].add(new Like(flavorIndex, malted));
        }
      }

      System.out.println(String.format("Case #%d: %s", tc, solve(N, likes)));
    }

    sc.close();
  }

  static String solve(int N, Set<Like>[] likes) {
    int[] flavors = new int[N];
    boolean[] satisfied = new boolean[likes.length];
    while (true) {
      OptionalInt optionalMaltedIndex =
          IntStream.range(0, likes.length)
              .filter(i -> likes[i].size() == 1 && likes[i].iterator().next().malted == 1)
              .findAny();
      if (!optionalMaltedIndex.isPresent()) {
        break;
      }

      int maltedIndex = optionalMaltedIndex.getAsInt();
      int flavorIndex = likes[maltedIndex].iterator().next().flavorIndex;
      flavors[flavorIndex] = 1;
      likes[maltedIndex].clear();
      satisfied[maltedIndex] = true;

      Like toRemove = new Like(flavorIndex, 0);
      Arrays.stream(likes).forEach(l -> l.remove(toRemove));

      if (IntStream.range(0, likes.length).anyMatch(i -> likes[i].isEmpty() && !satisfied[i])) {
        return "IMPOSSIBLE";
      }
    }

    return Arrays.stream(flavors).mapToObj(String::valueOf).collect(Collectors.joining(" "));
  }
}

class Like {
  int flavorIndex;
  int malted;

  Like(int flavorIndex, int malted) {
    this.flavorIndex = flavorIndex;
    this.malted = malted;
  }

  @Override
  public int hashCode() {
    return Objects.hash(flavorIndex, malted);
  }

  @Override
  public boolean equals(Object obj) {
    Like other = (Like) obj;

    return flavorIndex == other.flavorIndex && malted == other.malted;
  }
}
