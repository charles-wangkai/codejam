import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

public class Solution {
  static final int CHOICE_NUM = 4;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 1; tc <= C; ++tc) {
      int M = sc.nextInt();
      int Q = sc.nextInt();
      double[][] p = new double[Q][CHOICE_NUM];
      for (int i = 0; i < p.length; ++i) {
        for (int j = 0; j < p[i].length; ++j) {
          p[i][j] = sc.nextDouble();
        }
      }

      System.out.println(String.format("Case #%d: %.9f", tc, solve(M, p)));
    }

    sc.close();
  }

  static double solve(int M, double[][] p) {
    for (int i = 0; i < p.length; ++i) {
      p[i] =
          Arrays.stream(p[i])
              .boxed()
              .sorted(Comparator.reverseOrder())
              .mapToDouble(x -> x)
              .toArray();
    }

    double result = 0;
    Set<Long> visited = new HashSet<>();
    visited.add(0L);
    PriorityQueue<Element> pq =
        new PriorityQueue<>(Comparator.comparing((Element e) -> e.prob).reversed());
    pq.offer(new Element(computeProb(p, new int[p.length]), 0));
    for (int i = 0; i < M && !pq.isEmpty(); ++i) {
      Element head = pq.poll();
      result += head.prob;
      int[] indices = decode(p.length, head.code);

      for (int j = 0; j < p.length; ++j) {
        ++indices[j];
        if (indices[j] != CHOICE_NUM) {
          long code = encode(indices);
          if (!visited.contains(code)) {
            visited.add(code);
            pq.offer(new Element(computeProb(p, indices), code));
          }
        }
        --indices[j];
      }
    }

    return result;
  }

  static double computeProb(double[][] p, int[] indices) {
    return IntStream.range(0, p.length)
        .mapToDouble(i -> p[i][indices[i]])
        .reduce((x, y) -> x * y)
        .getAsDouble();
  }

  static long encode(int[] indices) {
    long result = 0;
    for (int i = indices.length - 1; i >= 0; --i) {
      result = result * CHOICE_NUM + indices[i];
    }

    return result;
  }

  static int[] decode(int size, long code) {
    int[] indices = new int[size];
    for (int i = 0; i < indices.length; ++i) {
      indices[i] = (int) (code % CHOICE_NUM);
      code /= CHOICE_NUM;
    }

    return indices;
  }
}

class Element {
  double prob;
  long code;

  Element(double prob, long code) {
    this.prob = prob;
    this.code = code;
  }
}
