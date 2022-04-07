import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {
  static final double SAMPLE_PROB = 0.7;
  static final int MAX_OLD_LENGTH = 50;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      if (N == -1) {
        throw new RuntimeException();
      }
      int K = sc.nextInt();

      List<Integer> orders = IntStream.rangeClosed(1, N).boxed().collect(Collectors.toList());
      Collections.shuffle(orders);
      int orderIndex = 0;
      int oldLength = 0;
      Map<Integer, Integer> nodeToEdgeNum = new HashMap<>();
      for (int i = 0; i <= K; ++i) {
        int R = sc.nextInt();
        if (R == -1) {
          throw new RuntimeException();
        }
        int P = sc.nextInt();

        if (nodeToEdgeNum.containsKey(R)) {
          ++oldLength;
        } else {
          nodeToEdgeNum.put(R, P);
          oldLength = 0;
        }

        if (nodeToEdgeNum.size() == N) {
          break;
        }

        if (i != K) {
          if (oldLength == MAX_OLD_LENGTH || new Random().nextDouble() < SAMPLE_PROB) {
            int next = -1;
            while (true) {
              next = orders.get(orderIndex);
              if (!nodeToEdgeNum.containsKey(next)) {
                break;
              }
              ++orderIndex;
            }

            System.out.println(String.format("T %d", next));
            System.out.flush();

            oldLength = 0;
          } else {
            System.out.println(String.format("W"));
            System.out.flush();
          }
        }
      }

      double avgEdgeNum = computeAvgEdgeNum(N, nodeToEdgeNum);

      System.out.println(String.format("E %d", (long) Math.round(avgEdgeNum * N * 1.25)));
      System.out.flush();
    }

    sc.close();
  }

  static double computeAvgEdgeNum(int N, Map<Integer, Integer> nodeToEdgeNum) {
    return nodeToEdgeNum.values().stream().mapToDouble(x -> x).sum() / 2 / nodeToEdgeNum.size();

    // List<Integer> orders =
    //     nodeToEdgeNum.keySet().stream()
    //         .sorted(Comparator.comparing(nodeToEdgeNum::get))
    //         .collect(Collectors.toList());

    // double sum = 0;
    // for (int i = 0; i < orders.size(); ++i) {
    //   sum +=
    //       nodeToEdgeNum.get(orders.get(i))
    //           * (((double) nodeToEdgeNum.size() / N < 0.5 && i >= orders.size() - 2) ? 1 : 0.5);
    // }

    // return sum / nodeToEdgeNum.size();
  }
}