import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {
  static final int CHECK_SAMPLE_NUM = 200;
  static final int WALK_NUM = 10;
  static final int CHECK_ROUND_NUM = CHECK_SAMPLE_NUM * WALK_NUM * 2;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      int K = sc.nextInt();

      Map<Integer, Integer> nodeToEdgeNum = sample(sc, N, K - CHECK_ROUND_NUM);
      double avgEdgeNum = check(sc, nodeToEdgeNum);

      System.out.println(String.format("E %d", (long) Math.round(avgEdgeNum * N)));
      System.out.flush();
    }

    sc.close();
  }

  static Map<Integer, Integer> sample(Scanner sc, int N, int sampleRoundNum) {
    Map<Integer, Integer> nodeToEdgeNum = new HashMap<>();

    int R = sc.nextInt();
    int P = sc.nextInt();
    nodeToEdgeNum.put(R, P);

    int R_ = R;
    List<Integer> orders =
        IntStream.rangeClosed(1, N).filter(i -> i != R_).boxed().collect(Collectors.toList());
    Collections.shuffle(orders);
    for (int i = 0; i < Math.min(sampleRoundNum, orders.size()); ++i) {
      System.out.println(String.format("T %d", orders.get(i)));
      System.out.flush();

      R = sc.nextInt();
      P = sc.nextInt();
      nodeToEdgeNum.put(R, P);
    }

    return nodeToEdgeNum;
  }

  static double check(Scanner sc, Map<Integer, Integer> nodeToEdgeNum) {
    List<Integer> orders =
        nodeToEdgeNum.keySet().stream()
            .sorted(Comparator.comparing(nodeToEdgeNum::get).reversed())
            .collect(Collectors.toList());
    List<Element> elements = new ArrayList<>();
    for (int i = 0; i < Math.min(CHECK_SAMPLE_NUM, orders.size()); ++i) {
      int innerCount = 0;
      for (int j = 0; j < WALK_NUM; ++j) {
        System.out.println(String.format("T %d", orders.get(i)));
        System.out.flush();

        sc.nextInt();
        sc.nextInt();

        System.out.println("W");
        System.out.flush();

        int R = sc.nextInt();
        sc.nextInt();

        if (nodeToEdgeNum.containsKey(R)) {
          ++innerCount;
        }
      }

      elements.add(
          new Element(
              nodeToEdgeNum.get(orders.get(i)),
              (double) innerCount / WALK_NUM * 0.5 + (double) (WALK_NUM - innerCount) / WALK_NUM));
    }

    return nodeToEdgeNum.values().stream().mapToLong(x -> x).sum()
        * computeWeightedAvg(elements)
        / nodeToEdgeNum.size();
  }

  static double computeWeightedAvg(List<Element> elements) {
    return elements.stream().mapToDouble(e -> e.weight * e.value).sum()
        / elements.stream().mapToInt(e -> e.weight).sum();
  }
}

class Element {
  int weight;
  double value;

  Element(int weight, double value) {
    this.weight = weight;
    this.value = value;
  }
}