import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      int K = sc.nextInt();

      int edgeSum = 0;
      boolean[] used = new boolean[N + 1];
      List<Integer> orders = IntStream.rangeClosed(1, N).boxed().collect(Collectors.toList());
      Collections.shuffle(orders);
      int orderIndex = 0;
      int seenCount = 0;
      for (int i = 0; i <= K; ++i) {
        int R = sc.nextInt();
        int P = sc.nextInt();

        used[R] = true;
        edgeSum += P;
        ++seenCount;

        if (seenCount == N) {
          break;
        }

        if (i != K) {
          int next = -1;
          while (true) {
            next = orders.get(orderIndex);
            if (!used[next]) {
              break;
            }
            ++orderIndex;
          }

          System.out.println(String.format("T %d", next));
          System.out.flush();
        }
      }

      System.out.println(
          String.format("E %d", (long) Math.round((double) edgeSum * N / (2 * seenCount))));
      System.out.flush();
    }

    sc.close();
  }
}