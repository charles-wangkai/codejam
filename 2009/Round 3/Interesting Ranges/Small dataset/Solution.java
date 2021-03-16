import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Solution {
  static final int MODULUS = 1_000_000_007;

  static List<Long> palindromes;

  public static void main(String[] args) {
    buildPalindromes();

    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      long L = sc.nextLong();
      long R = sc.nextLong();

      System.out.println(String.format("Case #%d: %d", tc, solve(L, R)));
    }

    sc.close();
  }

  static void buildPalindromes() {
    palindromes = new ArrayList<>();
    for (int i = 1; i <= 9; ++i) {
      palindromes.add((long) i);
    }
    for (int left = 1; left <= 999_999; ++left) {
      for (int middle = -1; middle <= 9; ++middle) {
        palindromes.add(
            Long.parseLong(
                String.format(
                    "%d%s%s",
                    left,
                    (middle == -1) ? "" : String.valueOf(middle),
                    new StringBuilder(String.valueOf(left)).reverse().toString())));
      }
    }

    Collections.sort(palindromes);
  }

  static int solve(long L, long R) {
    int leftIndex = 0;
    while (leftIndex != palindromes.size() && palindromes.get(leftIndex) < L) {
      ++leftIndex;
    }

    int rightIndex = palindromes.size() - 1;
    while (rightIndex != -1 && palindromes.get(rightIndex) > R) {
      --rightIndex;
    }

    if (leftIndex > rightIndex) {
      return computeAllSubrangeNum(L, R);
    }

    int result = computeAllSubrangeNum(L, palindromes.get(leftIndex) - 1);
    for (int i = leftIndex; i <= rightIndex; ++i) {
      result =
          addMod(
              result,
              computeAllSubrangeNum(
                  palindromes.get(i) + 1, (i == rightIndex) ? R : (palindromes.get(i + 1) - 1)));
    }

    result = addMod(result, computePart(L, R, leftIndex, rightIndex));
    if (leftIndex != rightIndex) {
      result =
          addMod(result, computePart(palindromes.get(leftIndex) + 1, R, leftIndex + 1, rightIndex));
    }

    return result;
  }

  static int computePart(long begin, long end, int leftIndex, int rightIndex) {
    int rightNum = 0;
    int endIndex = leftIndex - 1;
    while (endIndex + 2 <= rightIndex) {
      endIndex += 2;
      rightNum =
          addMod(
              rightNum,
              mod(
                  ((endIndex == rightIndex) ? (end + 1) : palindromes.get(endIndex + 1))
                      - palindromes.get(endIndex)));
    }

    int result = 0;
    while (leftIndex < endIndex) {
      int leftNum = mod(palindromes.get(leftIndex) - begin + 1);
      result = addMod(result, multiplyMod(leftNum, rightNum));

      leftIndex += 2;
      if (leftIndex <= rightIndex) {
        begin = palindromes.get(leftIndex - 1) + 1;
        rightNum =
            subtractMod(rightNum, mod(palindromes.get(leftIndex) - palindromes.get(leftIndex - 1)));
      }
    }

    return result;
  }

  static int computeAllSubrangeNum(long begin, long end) {
    long length = end - begin + 1;

    return BigInteger.valueOf(length)
        .multiply(BigInteger.valueOf(length + 1))
        .divide(BigInteger.TWO)
        .mod(BigInteger.valueOf(MODULUS))
        .intValue();
  }

  static int addMod(int x, int y) {
    return mod(x + y);
  }

  static int subtractMod(int x, int y) {
    return mod(x - y + MODULUS);
  }

  static int multiplyMod(int x, int y) {
    return mod((long) x * y);
  }

  static int mod(long x) {
    return (int) (x % MODULUS);
  }
}
