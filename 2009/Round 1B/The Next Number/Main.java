import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      String N = sc.next();

      System.out.println(String.format("Case #%d: %s", tc + 1, solve(N)));
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
              .mapToObj(c -> (char) c)
              .map(String::valueOf)
              .collect(Collectors.joining());

      int index =
          IntStream.range(0, sorted.length())
              .filter(i -> sorted.charAt(i) != '0')
              .findFirst()
              .getAsInt();

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

    return String.valueOf(digits);
  }

  static void swap(char[] digits, int index1, int index2) {
    char temp = digits[index1];
    digits[index1] = digits[index2];
    digits[index2] = temp;
  }
}
