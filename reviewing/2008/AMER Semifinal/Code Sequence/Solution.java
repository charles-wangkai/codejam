import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {
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
    Element result = search(S);

    return result.nextDetermined ? String.valueOf(result.nextValue) : "UNKNOWN";
  }

  static Element search(int[] sequence) {
    if (sequence.length <= 2) {
      return new Element(true, false, -1);
    }

    boolean valid = false;
    int nextValue = -1;
    for (int beginIndex = 0; beginIndex <= 1; ++beginIndex) {
      int[] nextSequence = buildNextSequence(sequence, beginIndex);
      if (nextSequence != null) {
        Element subResult = search(nextSequence);
        if (subResult.valid) {
          valid = true;

          if (beginIndex % 2 != sequence.length % 2) {
            if (beginIndex + 1 == sequence.length) {
              return new Element(true, false, -1);
            }

            int currNextValue =
                addMod(
                    nextSequence[nextSequence.length - 1],
                    subtractMod(sequence[beginIndex + 1], sequence[beginIndex]));

            if (nextValue != -1 && currNextValue != nextValue) {
              return new Element(true, false, -1);
            }

            nextValue = currNextValue;
          } else {
            if (!subResult.nextDetermined
                || (nextValue != -1 && subResult.nextValue != nextValue)) {
              return new Element(true, false, -1);
            }

            nextValue = subResult.nextValue;
          }
        }
      }
    }

    return new Element(valid, true, nextValue);
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
            .filter(i -> i % 2 == beginIndex % 2)
            .map(i -> sequence[i])
            .boxed()
            .collect(Collectors.toList());
    if (beginIndex == 1) {
      nextSequence.add(0, subtractMod(sequence[0], diff));
    }

    return nextSequence.stream().mapToInt(x -> x).toArray();
  }

  static int addMod(int x, int y) {
    return (x + y) % MODULUS;
  }

  static int subtractMod(int x, int y) {
    return (x - y + MODULUS) % MODULUS;
  }
}

class Element {
  boolean valid;
  boolean nextDetermined;
  int nextValue;

  Element(boolean valid, boolean nextDetermined, int nextValue) {
    this.valid = valid;
    this.nextDetermined = nextDetermined;
    this.nextValue = nextValue;
  }
}
