import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Solution {
  static int[] solutions;

  public static void main(String[] args) {
    buildSolutions();

    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    sc.nextLine();
    for (int tc = 1; tc <= T; ++tc) {
      int[] bases = Arrays.stream(sc.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();

      System.out.println(String.format("Case #%d: %d", tc, solve(bases)));
    }

    sc.close();
  }

  static void buildSolutions() {
    solutions = new int[1 << 9];

    for (int number = 2; ; ++number) {
      int code = 0;
      for (int base = 10; base >= 2; --base) {
        code = code * 2 + (isHappy(base, number) ? 1 : 0);
      }

      for (int i = 0; i < solutions.length; ++i) {
        if ((i & code) == i && solutions[i] == 0) {
          solutions[i] = number;
        }
      }

      if (code == solutions.length - 1) {
        break;
      }
    }
  }

  static boolean isHappy(int base, int number) {
    Set<Integer> seen = new HashSet<>();
    while (number != 1) {
      int next = computeNext(base, number);
      if (seen.contains(next)) {
        return false;
      }

      seen.add(next);
      number = next;
    }

    return true;
  }

  static int computeNext(int base, int number) {
    int result = 0;
    while (number != 0) {
      int digit = number % base;
      result += digit * digit;

      number /= base;
    }

    return result;
  }

  static int solve(int[] bases) {
    return solutions[Arrays.stream(bases).map(base -> 1 << (base - 2)).sum()];
  }
}
