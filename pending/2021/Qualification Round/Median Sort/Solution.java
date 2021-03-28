import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    int N = sc.nextInt();
    sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      solve(sc, N);
    }

    sc.close();
  }

  static void solve(Scanner sc, int N) {
    List<Integer> result =
        sort(sc, -1, -1, IntStream.rangeClosed(1, N).boxed().collect(Collectors.toSet()));
    System.out.println(result.stream().map(String::valueOf).collect(Collectors.joining(" ")));
    System.out.flush();

    readResponse(sc);
  }

  static int readResponse(Scanner sc) {
    int result = sc.nextInt();
    if (result == -1) {
      System.exit(1);
    }

    return result;
  }

  static int ask(Scanner sc, int value1, int value2, int value3) {
    System.out.println(String.format("%d %d %d", value1, value2, value3));
    System.out.flush();

    return readResponse(sc);
  }

  static List<Integer> sort(Scanner sc, int lower, int upper, Set<Integer> values) {
    if (values.size() <= 1) {
      return new ArrayList<>(values);
    }

    int pivot1;
    int pivot2;
    Set<Integer> leftValues = new HashSet<>();
    Set<Integer> middleValues = new HashSet<>();
    Set<Integer> rightValues = new HashSet<>();
    if (lower == -1 && upper == -1) {
      int value1 = removeOne(values);
      int value2 = removeOne(values);
      int value3 = removeOne(values);

      pivot2 = ask(sc, value1, value2, value3);

      if (pivot2 == value1) {
        pivot1 = value2;
        rightValues.add(value3);
      } else if (pivot2 == value2) {
        pivot1 = value3;
        rightValues.add(value1);
      } else {
        pivot1 = value1;
        rightValues.add(value2);
      }
    } else if (values.size() >= 2) {
      int value1 = removeOne(values);
      int value2 = removeOne(values);

      if (lower == -1) {
        pivot2 = ask(sc, upper, value1, value2);

        if (pivot2 == value1) {
          pivot1 = value2;
        } else {
          pivot1 = value1;
        }
      } else {
        pivot1 = ask(sc, lower, value1, value2);

        if (pivot1 == value1) {
          pivot2 = value2;
        } else {
          pivot2 = value1;
        }
      }
    } else if (lower == -1) {
      pivot1 = removeOne(values);
      pivot2 = upper;
    } else {
      pivot1 = lower;
      pivot2 = removeOne(values);
    }

    while (!values.isEmpty()) {
      int value = removeOne(values);

      int median = ask(sc, pivot1, pivot2, value);
      if (median == pivot1) {
        leftValues.add(value);
      } else if (median == pivot2) {
        rightValues.add(value);
      } else {
        middleValues.add(value);
      }
    }

    List<Integer> leftSorted = sort(sc, lower, pivot1, leftValues);
    List<Integer> middleSorted = sort(sc, pivot1, pivot2, middleValues);
    List<Integer> rightSorted = sort(sc, pivot2, upper, rightValues);

    List<Integer> result = new ArrayList<>();
    result.addAll(leftSorted);
    if (pivot1 != lower) {
      result.add(pivot1);
    }
    result.addAll(middleSorted);
    if (pivot2 != upper) {
      result.add(pivot2);
    }
    result.addAll(rightSorted);

    return result;
  }

  static int removeOne(Set<Integer> values) {
    int result = values.iterator().next();
    values.remove(result);

    return result;
  }
}
