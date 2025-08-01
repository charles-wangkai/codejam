// https://raw.githubusercontent.com/google/coding-competitions-archive/main/codejam/2008/amer_semifinal/code_sequence/analysis.pdf

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
  static final int MODULUS = 10007;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      int[] S = new int[N];
      for (int i = 0; i < S.length; ++i) {
        S[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %s", tc, solve(S)));
    }

    sc.close();
  }

  static String solve(int[] S) {
    int result = search(S);

    return (result == Integer.MAX_VALUE) ? "UNKNOWN" : String.valueOf(result);
  }

  static Integer search(int[] sequence) {
    if (sequence.length <= 2) {
      return Integer.MAX_VALUE;
    }

    int nextValue = -1;
    for (int beginIndex = 0; beginIndex <= 1; ++beginIndex) {
      int[] nextSequence = buildNextSequence(sequence, beginIndex);
      if (nextSequence != null) {
        Integer subResult = search(nextSequence);
        if (subResult != null) {
          if (beginIndex == sequence.length % 2) {
            if (subResult == Integer.MAX_VALUE || (nextValue != -1 && subResult != nextValue)) {
              return Integer.MAX_VALUE;
            }

            nextValue = subResult;
          } else {
            if (beginIndex + 1 == sequence.length) {
              return Integer.MAX_VALUE;
            }

            int currNextValue =
                addMod(
                    nextSequence[nextSequence.length - 1],
                    subtractMod(sequence[beginIndex + 1], sequence[beginIndex]));

            if (nextValue != -1 && currNextValue != nextValue) {
              return Integer.MAX_VALUE;
            }

            nextValue = currNextValue;
          }
        }
      }
    }

    return (nextValue == -1) ? null : nextValue;
  }

  static int[] buildNextSequence(int[] sequence, int beginIndex) {
    int diff = -1;
    for (int i = beginIndex; i + 1 < sequence.length; i += 2) {
      int currDiff = subtractMod(sequence[i + 1], sequence[i]);
      if (diff != -1 && currDiff != diff) {
        return null;
      }

      diff = currDiff;
    }

    List<Integer> nextSequence =
        IntStream.range(0, sequence.length)
            .filter(i -> i % 2 == beginIndex)
            .map(i -> sequence[i])
            .boxed()
            .collect(Collectors.toList());
    if (beginIndex == 1) {
      nextSequence.add(0, subtractMod(sequence[0], diff));
    }

    return nextSequence.stream().mapToInt(Integer::intValue).toArray();
  }

  static int addMod(int x, int y) {
    return Math.floorMod(x + y, MODULUS);
  }

  static int subtractMod(int x, int y) {
    return addMod(x, -y);
  }
}
