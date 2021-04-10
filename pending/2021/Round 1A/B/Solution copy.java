import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int M = sc.nextInt();
      int[] P = new int[M];
      int[] N = new int[M];
      for (int i = 0; i < M; ++i) {
        P[i] = sc.nextInt();
        N[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(P, N)));
    }

    sc.close();
  }

  static int solve(int[] P, int[] N) {
    List<Integer> values = new ArrayList<>();
    for (int i = 0; i < P.length; ++i) {
      for (int j = 0; j < N[i]; ++j) {
        values.add(P[i]);
      }
    }

    int[] prefixSums = new int[values.size()];
    for (int i = 0; i < prefixSums.length; ++i) {
      prefixSums[i] = (i == 0 ? 0 : prefixSums[i - 1]) + values.get(i);
    }

    Set<State> states = Set.of(new State(0, 1));
    for (int i = values.size() - 1; i >= 0; --i) {
      Set<State> nextStates = new HashSet<>();
      for (State state : states) {
        nextStates.add(new State(state.sum + values.get(i), state.product));

        if (state.product * values.get(i) - state.sum <= (i == 0 ? 0 : prefixSums[i - 1])) {
          nextStates.add(new State(state.sum, state.product * values.get(i)));
        }
      }

      states = nextStates;
    }

    return states.stream()
        .filter(state -> state.sum == state.product)
        .mapToInt(state -> state.sum)
        .max()
        .orElse(0);
  }
}

class State {
  int sum;
  int product;

  State(int sum, int product) {
    this.sum = sum;
    this.product = product;
  }

  @Override
  public int hashCode() {
    return Objects.hash(sum, product);
  }

  @Override
  public boolean equals(Object obj) {
    State other = (State) obj;

    return sum == other.sum && product == other.product;
  }
}
