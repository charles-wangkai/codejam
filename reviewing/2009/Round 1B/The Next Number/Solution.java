import java.util.Scanner;
import java.util.stream.Collectors;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      String N = sc.next();

      System.out.println(String.format("Case #%d: %s", tc, solve(N)));
    }

    sc.close();
  }

  static String solve(String N) {
    char[] digits = N.toCharArray();

    int leftIndex = digits.length - 2;
    while (leftIndex != -1 && digits[leftIndex] >= digits[leftIndex + 1]) {
      --leftIndex;
    }

    if (leftIndex == -1) {
      String sorted =
          N.chars()
              .sorted()
              .mapToObj(ch -> String.valueOf((char) ch))
              .collect(Collectors.joining());

      int index = 0;
      while (sorted.charAt(index) == '0') {
        ++index;
      }

      return String.format(
          "%c0%s%s", sorted.charAt(index), sorted.substring(0, index), sorted.substring(index + 1));
    }

    int rightIndex = digits.length - 1;
    while (digits[rightIndex] <= digits[leftIndex]) {
      --rightIndex;
    }

    swap(digits, leftIndex, rightIndex);

    for (int i = leftIndex + 1, j = digits.length - 1; i < j; ++i, --j) {
      swap(digits, i, j);
    }

    return new String(digits);
  }

  static void swap(char[] digits, int index1, int index2) {
    char temp = digits[index1];
    digits[index1] = digits[index2];
    digits[index2] = temp;
  }
}
