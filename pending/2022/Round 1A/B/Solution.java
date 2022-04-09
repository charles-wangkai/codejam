import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Solution {
  static int LENGTH = 100;
  static int FIRST_LENGTH = 30;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      if (N == -1) {
        System.exit(0);
      }

      int[] A = buildA();
      System.out.println(
          Arrays.stream(A).mapToObj(String::valueOf).collect(Collectors.joining(" ")));
      System.out.flush();
      // System.err.println(
      //     Arrays.stream(A).mapToObj(String::valueOf).collect(Collectors.joining(" ")));

      int[] B = new int[N];
      for (int i = 0; i < B.length; ++i) {
        B[i] = sc.nextInt();
      }
      // System.err.printf("%2$s: %1$s\n", Arrays.toString(B), "Arrays.toString(B)");

      List<Integer> chosen = solve(A, B);

      // long total = Arrays.stream(A).asLongStream().sum() + Arrays.stream(B).asLongStream().sum();
      // long half = chosen.stream().mapToLong(x -> x).sum();
      // System.err.printf("%2$s: %1$s\n", total, "total");
      // System.err.printf("%2$s: %1$s\n", half, "half");

      System.out.println(chosen.stream().map(String::valueOf).collect(Collectors.joining(" ")));
      System.out.flush();
      // System.err.println(chosen.stream().map(String::valueOf).collect(Collectors.joining(" ")));
    }

    sc.close();
  }

  static int[] buildA() {
    int[] A = new int[LENGTH];
    A[0] = 1;
    for (int i = 1; i < FIRST_LENGTH; ++i) {
      A[i] = A[i - 1] * 2;
    }

    int value = 3;
    int sum = 0;
    for (int i = FIRST_LENGTH; i < A.length - 1; ++i) {
      A[i] = value;

      sum += value;
      value += 2;
    }
    A[A.length - 1] = sum;

    return A;
  }

  static List<Integer> solve(int[] A, int[] B) {
    int diff = 0;
    List<Integer> left = new ArrayList<>();
    List<Integer> right = new ArrayList<>();
    for (int Bi : B) {
      if (diff <= 0) {
        left.add(Bi);
        diff += Bi;
      } else {
        right.add(Bi);
        diff -= Bi;
      }
    }

    if (diff >= 0) {
      List<Integer> temp = left;
      left = right;
      right = temp;
    } else {
      diff *= -1;
    }
    // System.err.printf("%2$s: %1$s\n", diff, "diff");

    for (int i = 0; i < FIRST_LENGTH - 1; ++i) {
      if (((diff / 2) & (1 << i)) != 0) {
        left.add(A[i]);
      }
    }
    left.add(A[FIRST_LENGTH - 1]);
    left.add(A[A.length - 1]);

    return left;
  }
}