import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      int N = sc.nextInt();
      int[] x = new int[N];
      int[] y = new int[N];
      int[] z = new int[N];
      int[] vx = new int[N];
      int[] vy = new int[N];
      int[] vz = new int[N];
      for (int i = 0; i < N; ++i) {
        x[i] = sc.nextInt();
        y[i] = sc.nextInt();
        z[i] = sc.nextInt();
        vx[i] = sc.nextInt();
        vy[i] = sc.nextInt();
        vz[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d: %s", tc + 1, solve(x, y, z, vx, vy, vz)));
    }

    sc.close();
  }

  static String solve(int[] x, int[] y, int[] z, int[] vx, int[] vy, int[] vz) {
    int xSum = Arrays.stream(x).sum();
    int ySum = Arrays.stream(y).sum();
    int zSum = Arrays.stream(z).sum();
    int vxSum = Arrays.stream(vx).sum();
    int vySum = Arrays.stream(vy).sum();
    int vzSum = Arrays.stream(vz).sum();

    long denom = (long) vxSum * vxSum + (long) vySum * vySum + (long) vzSum * vzSum;
    double t =
        (denom == 0)
            ? 0
            : Math.max(
                0,
                -(double) ((long) xSum * vxSum + (long) ySum * vySum + (long) zSum * vzSum)
                    / denom);

    return String.format("%.9f %.9f", computeD(x, y, z, vx, vy, vz, t), t);
  }

  static double computeD(int[] x, int[] y, int[] z, int[] vx, int[] vy, int[] vz, double t) {
    int N = x.length;

    double xCenter = IntStream.range(0, N).mapToDouble(i -> x[i] + vx[i] * t).sum() / N;
    double yCenter = IntStream.range(0, N).mapToDouble(i -> y[i] + vy[i] * t).sum() / N;
    double zCenter = IntStream.range(0, N).mapToDouble(i -> z[i] + vz[i] * t).sum() / N;

    return Math.sqrt(xCenter * xCenter + yCenter * yCenter + zCenter * zCenter);
  }
}
