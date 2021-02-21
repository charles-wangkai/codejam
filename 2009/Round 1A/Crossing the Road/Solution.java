import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 1; tc <= C; ++tc) {
      int N = sc.nextInt();
      int M = sc.nextInt();
      int[][] S = new int[N][M];
      int[][] W = new int[N][M];
      int[][] T = new int[N][M];
      for (int r = 0; r < N; ++r) {
        for (int c = 0; c < M; ++c) {
          S[r][c] = sc.nextInt();
          W[r][c] = sc.nextInt();
          T[r][c] = sc.nextInt();
        }
      }

      System.out.println(String.format("Case #%d: %d", tc, solve(S, W, T)));
    }

    sc.close();
  }

  static int solve(int[][] S, int[][] W, int[][] T) {
    int N = S.length;
    int M = S[0].length;

    int[][] times = new int[N * 2][M * 2];
    for (int r = 0; r < times.length; ++r) {
      Arrays.fill(times[r], -1);
    }
    PriorityQueue<Element> pq = new PriorityQueue<>(Comparator.comparing(e -> e.time));
    pq.offer(new Element(N * 2 - 1, 0, 0));

    while (!pq.isEmpty()) {
      Element head = pq.poll();
      if (times[head.r][head.c] == -1) {
        times[head.r][head.c] = head.time;

        int intersectionR = head.r / 2;
        int intersectionC = head.c / 2;

        if (head.r % 2 == 0) {
          if (head.r != 0) {
            pq.offer(new Element(head.r - 1, head.c, head.time + 2));
          }

          if (head.r != N * 2 - 1) {
            pq.offer(
                new Element(
                    head.r + 1,
                    head.c,
                    computeCrossTime(
                        S[intersectionR][intersectionC],
                        W[intersectionR][intersectionC],
                        T[intersectionR][intersectionC],
                        head.time,
                        true)));
          }
        } else {
          pq.offer(
              new Element(
                  head.r - 1,
                  head.c,
                  computeCrossTime(
                      S[intersectionR][intersectionC],
                      W[intersectionR][intersectionC],
                      T[intersectionR][intersectionC],
                      head.time,
                      true)));

          if (head.r != N * 2 - 1) {
            pq.offer(new Element(head.r + 1, head.c, head.time + 2));
          }
        }

        if (head.c % 2 == 0) {
          if (head.c != 0) {
            pq.offer(new Element(head.r, head.c - 1, head.time + 2));
          }

          if (head.c != M * 2 - 1) {
            pq.offer(
                new Element(
                    head.r,
                    head.c + 1,
                    computeCrossTime(
                        S[intersectionR][intersectionC],
                        W[intersectionR][intersectionC],
                        T[intersectionR][intersectionC],
                        head.time,
                        false)));
          }
        } else {
          pq.offer(
              new Element(
                  head.r,
                  head.c - 1,
                  computeCrossTime(
                      S[intersectionR][intersectionC],
                      W[intersectionR][intersectionC],
                      T[intersectionR][intersectionC],
                      head.time,
                      false)));

          if (head.c != M * 2 - 1) {
            pq.offer(new Element(head.r, head.c + 1, head.time + 2));
          }
        }
      }
    }

    return times[0][M * 2 - 1];
  }

  static int computeCrossTime(int s, int w, int t, int time, boolean verticalOrHorizontal) {
    int cycle = s + w;

    int base = t + (verticalOrHorizontal ? 0 : s);
    base -= (base + cycle - 1) / cycle * cycle;
    base += (time - base) / cycle * cycle;

    int delta = time - base;
    if (verticalOrHorizontal) {
      if (delta < s) {
        return time + 1;
      } else {
        return base + cycle + 1;
      }
    } else {
      if (delta < w) {
        return time + 1;
      } else {
        return base + cycle + 1;
      }
    }
  }
}

class Element {
  int r;
  int c;
  int time;

  Element(int r, int c, int time) {
    this.r = r;
    this.c = c;
    this.time = time;
  }
}
