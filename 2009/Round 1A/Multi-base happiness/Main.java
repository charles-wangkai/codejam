import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

public class Main {
  static int[] solutions;

  public static void main(String[] args) {
    precompute();

    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    sc.nextLine();
    for (int tc = 0; tc < T; ++tc) {
      int[] bases = Arrays.stream(sc.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();

      System.out.println(String.format("Case #%d: %d", tc + 1, solve(bases)));
    }

    sc.close();
  }

  static void precompute() {
    solutions = new int[1 << 9];

    for (int value = 2; ; ++value) {
      int value_ = value;
      int mask =
          IntStream.rangeClosed(2, 10)
              .filter(base -> isHappy(base, value_))
              .map(base -> 1 << (base - 2))
              .sum();

      for (int i = 0; i < solutions.length; ++i) {
        if ((i & mask) == i && solutions[i] == 0) {
          solutions[i] = value;
        }
      }

      if (mask == solutions.length - 1) {
        break;
      }
    }
  }

  static boolean isHappy(int base, int value) {
    Set<Integer> seen = new HashSet<>();
    while (true) {
      if (value == 1) {
        return true;
      }

      value = computeNext(base, value);
      if (seen.contains(value)) {
        return false;
      }

      seen.add(value);
    }
  }

  static int computeNext(int base, int value) {
    int result = 0;
    while (value != 0) {
      int digit = value % base;
      result += digit * digit;

      value /= base;
    }

    return result;
  }

  static int solve(int[] bases) {
    return solutions[Arrays.stream(bases).map(base -> 1 << (base - 2)).sum()];
  }
}
