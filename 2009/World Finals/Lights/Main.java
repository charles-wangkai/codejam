// https://raw.githubusercontent.com/google/coding-competitions-archive/main/codejam/2009/world_finals/lights/analysis.pdf
// https://github.com/KirarinSnow/Google-Code-Jam/blob/master/World%20Finals%202009/F1.py

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
  static final double EPSILON = 1e-9;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      int rx = sc.nextInt();
      int ry = sc.nextInt();
      int gx = sc.nextInt();
      int gy = sc.nextInt();
      int n = sc.nextInt();
      int[] cx = new int[n];
      int[] cy = new int[n];
      int[] cr = new int[n];
      for (int i = 0; i < n; ++i) {
        cx[i] = sc.nextInt();
        cy[i] = sc.nextInt();
        cr[i] = sc.nextInt();
      }

      System.out.println(String.format("Case #%d:\n%s", tc + 1, solve(rx, ry, gx, gy, cx, cy, cr)));
    }

    sc.close();
  }

  static String solve(int rx, int ry, int gx, int gy, int[] cx, int[] cy, int[] cr) {
    List<RaySegment> lr = segs(cx, cy, cr, rx, ry);
    List<RaySegment> lg = segs(cx, cy, cr, gx, gy);

    List<Double> ms = moments(cx, cy, cr, lr, lg);
    List<Double> ms2 = new ArrayList<>();
    for (double j : ms) {
      if (ms2.isEmpty() || j > ms2.get(ms2.size() - 1) + EPSILON) {
        ms2.add(j);
      }
    }

    double[] ar = new double[5];

    double mx = 0;
    List<Element3> il = intm(cx, cy, cr, lr, lg, mx);
    for (double m : ms2.subList(1, ms2.size())) {
      List<Element3> iln = intm(cx, cy, cr, lr, lg, m);
      Map<Element4, List<Element5>> pairs = new HashMap<>();

      List<List<Element3>> ls = List.of(il, iln);
      for (int lk = 0; lk < ls.size(); ++lk) {
        List<Element3> l = ls.get(lk);
        for (Element3 e3 : l) {
          Element4 e4 = new Element4(e3.t, e3.k);
          pairs.putIfAbsent(e4, new ArrayList<>());
          pairs.get(e4).add(new Element5(lk, e3.y));
        }
      }

      List<Element6> segs = new ArrayList<>();
      for (Element4 tk : pairs.keySet()) {
        if (pairs.get(tk).size() == 2) {
          List<Element5> e5s = pairs.get(tk);
          Collections.sort(e5s);

          double y1 = e5s.get(0).y;
          double y2 = e5s.get(1).y;

          double midx = (m + mx) / 2;
          int t = tk.t;
          int k = tk.k;
          double midy;
          if (t == 0 || t == 4) {
            double v = cr[k] * cr[k] - (midx - cx[k]) * (midx - cx[k]);
            if (-EPSILON < v && v < 0) {
              v = 0;
            }
            if (t == 0) {
              midy = cy[k] + Math.sqrt(v);
            } else {
              midy = cy[k] - Math.sqrt(v);
            }
          } else {
            midy = (y1 + y2) / 2;
          }
          segs.add(new Element6(midy, y1, y2, tk));
        }
      }
      Collections.sort(segs);

      for (int sk = 0; sk < segs.size() - 1; ++sk) {
        Element6 seg = segs.get(sk);
        Element6 segn = segs.get(sk + 1);
        double pa = area(cx, cy, cr, mx, m, seg.y1, segn.y1, seg.y2, segn.y2, seg.tk, segn.tk);
        double midx = (m + mx) / 2;
        double midy = (seg.midy + segn.midy) / 2;
        int pc = color(rx, ry, gx, gy, cx, cy, cr, midx, midy);

        ar[pc] += pa;
      }

      il = iln;
      mx = m;
    }

    return IntStream.range(0, 4)
        .mapToObj(i -> String.format("%.9f", ar[i]))
        .collect(Collectors.joining("\n"));
  }

  static List<Double> moments(
      int[] cx, int[] cy, int[] cr, List<RaySegment> lr, List<RaySegment> lg) {
    Set<Double> ms = new HashSet<>();
    ms.add(0.0);
    ms.add(100.0);
    for (int i = 0; i < cx.length; ++i) {
      ms.add((double) (cx[i] - cr[i]));
      ms.add((double) (cx[i] + cr[i]));
    }
    for (List<RaySegment> l : List.of(lr, lg)) {
      for (RaySegment raySegment : l) {
        ms.add(raySegment.x1);
        ms.add(raySegment.x2);
      }
    }
    for (RaySegment l1 : lr) {
      for (RaySegment l2 : lg) {
        DistanceAndPoint ip = intline(l1.x1, l1.y1, l1.x2, l1.y2, l2.x1, l2.y1, l2.x2, l2.y2);
        if (ip != null) {
          ms.add(ip.point.x);
        }
      }
    }

    return ms.stream().sorted().collect(Collectors.toList());
  }

  static List<RaySegment> segs(int[] cx, int[] cy, int[] cr, int lx, int ly) {
    List<RaySegment> ret = new ArrayList<>();
    List<Element1> pps = new ArrayList<>();
    double ca = 0;
    double cd = Double.MAX_VALUE;
    List<Double> dists = new ArrayList<>();
    for (int pillar = 0; pillar < cx.length; ++pillar) {
      double d = dist(lx, ly, cx[pillar], cy[pillar]);
      dists.add(d);

      double sep = d - cr[pillar];
      double cv = center(lx, ly, cx[pillar], cy[pillar]);

      double av = offset(d, cr[pillar]);

      double ad = cr[pillar] / Math.tan(av);

      if (sep < cd) {
        cd = sep;
        ca = norm(cv);
      }

      pps.add(new Element1(norm(cv - av), ad, pillar, 0));
      pps.add(new Element1(norm(cv + av), ad, pillar, 1));
    }

    Collections.sort(pps);

    pps = partition(pps, ca);

    List<Element2> stack = current(cx, cy, cr, lx, ly, ca);

    for (Element1 e : pps) {
      Point p = coord(lx, ly, e.a, e.d);
      if (stack.isEmpty() || (e.pk == stack.get(0).hk && stack.size() == 1)) {
        Point i = intedge(p.x, p.y, e.a);
        ret.add(new RaySegment(p.x, p.y, i.x, i.y));
      } else {
        int hk = stack.get(0).hk;
        if (e.pk == hk) {
          hk = stack.get(1).hk;
        }

        Point i = intpil(cx, cy, cr, p.x, p.y, e.a, hk);
        double di = dist(lx, ly, i.x, i.y);
        double dp = dist(lx, ly, p.x, p.y);
        if (dp < di + EPSILON) {
          ret.add(new RaySegment(p.x, p.y, i.x, i.y));
        }
      }

      if (e.t == 0) {
        stack.add(new Element2(dists.get(e.pk), e.pk));
        Collections.sort(stack);
      } else {
        for (int v = 0; v < stack.size(); ++v) {
          if (stack.get(v).hk == e.pk) {
            stack.remove(v);

            break;
          }
        }
      }
    }

    return ret;
  }

  static Point coord(double lx, double ly, double a, double d) {
    return new Point(lx + d * Math.sin(a), ly + d * Math.cos(a));
  }

  static double dist(double x1, double y1, double x2, double y2) {
    return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
  }

  static double center(double ii, double jj, double i, double j) {
    return Math.atan2(i - ii, j - jj);
  }

  static double offset(double d, double k) {
    return Math.asin(k / d);
  }

  static double norm(double a) {
    return ((a % (2 * Math.PI)) + (2 * Math.PI)) % (2 * Math.PI);
  }

  static List<Element1> partition(List<Element1> x, double a) {
    List<Element1> pre = new ArrayList<>();
    int i = 0;
    while (i < x.size() && x.get(i).a < a) {
      pre.add(x.get(i));
      ++i;
    }

    return Stream.concat(x.subList(i, x.size()).stream(), pre.stream())
        .collect(Collectors.toList());
  }

  static List<Element2> current(int[] cx, int[] cy, int[] cr, int lx, int ly, double a) {
    List<Element2> cs = new ArrayList<>();
    for (int pillar = 0; pillar < cx.length; ++pillar) {
      double d = dist(lx, ly, cx[pillar], cy[pillar]);
      double cv = center(lx, ly, cx[pillar], cy[pillar]);
      double av = offset(d, cr[pillar]);
      double la = cv - av;
      double ra = cv + av;
      if ((la <= a && a < ra)
          || (la <= a + 2 * Math.PI && a + 2 * Math.PI < ra)
          || (la <= a - 2 * Math.PI && a - 2 * Math.PI < ra)) {
        cs.add(new Element2(d, pillar));
      }
    }

    Collections.sort(cs);

    return cs;
  }

  static Point intedge(double px, double py, double a) {
    List<Point> cs =
        List.of(new Point(0, 0), new Point(0, 100), new Point(100, 100), new Point(100, 0));
    List<DistanceAndPoint> q = new ArrayList<>();
    for (int i = 0; i < cs.size(); ++i) {
      Point s = cs.get(i);
      Point t = cs.get((i + 1) % cs.size());
      double qx = px + 142 * Math.sin(a);
      double qy = py + 142 * Math.cos(a);
      DistanceAndPoint ip = intline(px, py, qx, qy, s.x, s.y, t.x, t.y);
      if (ip != null) {
        q.add(ip);
      }
    }

    return q.stream()
        .min(
            Comparator.<DistanceAndPoint, Double>comparing(dp -> dp.distance)
                .thenComparing(dp -> dp.point.x)
                .thenComparing(dp -> dp.point.y))
        .get()
        .point;
  }

  static DistanceAndPoint intline(
      double ax, double ay, double bx, double by, double cx, double cy, double dx, double dy) {
    double a1 = center(ax, ay, bx, by);
    double a2 = center(cx, cy, dx, dy);
    double dnum = ax * Math.cos(a1) - ay * Math.sin(a1) - cx * Math.cos(a1) + cy * Math.sin(a1);
    double dden = Math.sin(a2) * Math.cos(a1) - Math.cos(a2) * Math.sin(a1);
    if (Math.abs(dden) < EPSILON) {
      return null;
    }

    double d = dnum / dden;
    double ix = cx + d * Math.sin(a2);
    double iy = cy + d * Math.cos(a2);
    double d1 = dist(ax, ay, ix, iy);
    double d1x = dist(bx, by, ix, iy);
    double e1 = dist(ax, ay, bx, by);
    double d2 = dist(cx, cy, ix, iy);
    double d2x = dist(dx, dy, ix, iy);
    double e2 = dist(cx, cy, dx, dy);

    if (d1 < e1 + EPSILON && d1x < e1 + EPSILON && d2 < e2 + EPSILON && d2x < e2 + EPSILON) {
      return new DistanceAndPoint(d1, new Point(ix, iy));
    }

    return null;
  }

  static Point intpil(int[] cx, int[] cy, int[] cr, double px, double py, double a, int hk) {
    double ca = center(px, py, cx[hk], cy[hk]);
    double d = dist(px, py, cx[hk], cy[hk]);
    double th = ca - a;
    double disc = cr[hk] * cr[hk] - d * d * Math.sin(th) * Math.sin(th);
    if (-EPSILON < disc && disc < 0) {
      disc = 0;
    }
    if (disc <= -EPSILON) {
      return null;
    }

    double x = d * Math.cos(th) - Math.sqrt(disc);

    return coord(px, py, a, x);
  }

  static List<Element3> intm(
      int[] cx, int[] cy, int[] cr, List<RaySegment> lr, List<RaySegment> lg, double m) {
    List<Element3> il = new ArrayList<>();
    for (int pk = 0; pk < cx.length; ++pk) {
      if (cx[pk] - cr[pk] - EPSILON < m && m < cx[pk] + cr[pk] + EPSILON) {
        Point i = intpil(cx, cy, cr, m, 0, 0, pk);
        double iy2 = 2 * cy[pk] - i.y;
        il.add(new Element3(i.y, 4, pk));
        il.add(new Element3(iy2, 0, pk));
      }
    }

    for (int lw = 0; lw < 2; ++lw) {
      List<RaySegment> l = List.of(lr, lg).get(lw);
      for (int lk = 0; lk < l.size(); ++lk) {
        RaySegment lx = l.get(lk);
        DistanceAndPoint ip = intline(lx.x1, lx.y1, lx.x2, lx.y2, m, 0, m, 100);
        if (ip != null) {
          il.add(new Element3(ip.point.y, 2 + lw, lk));
        }
      }
    }

    il.add(new Element3(0, 1, 0));
    il.add(new Element3(100, 1, 1));

    Collections.sort(il);

    return il;
  }

  static double area(
      int[] cx,
      int[] cy,
      int[] cr,
      double x1,
      double x2,
      double l1,
      double h1,
      double l2,
      double h2,
      Element4 tl,
      Element4 th) {
    double ta = (x2 - x1) * (h1 - l1 + h2 - l2) / 2;

    List<Element4> e4s = List.of(tl, th);
    for (int w = 0; w < e4s.size(); ++w) {
      int t = e4s.get(w).t;
      int k = e4s.get(w).k;
      if (t == 0 || t == 4) {
        double y1 = List.of(l1, h1).get(w);
        double y2 = List.of(l2, h2).get(w);
        double chs = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
        double a = Math.acos(1 - chs / (2 * cr[k] * cr[k]));
        double sa = cr[k] * cr[k] * (a - Math.sin(a)) / 2;
        if (t == 4 * w) {
          ta -= sa;
        } else {
          ta += sa;
        }
      }
    }

    return ta;
  }

  static int color(
      int rx, int ry, int gx, int gy, int[] cx, int[] cy, int[] cr, double px, double py) {
    int col = 3;
    for (int hk = 0; hk < cx.length; ++hk) {
      double dc = dist(px, py, cx[hk], cy[hk]);
      if (dc < cr[hk]) {
        return 4;
      }
    }
    for (int co = 0; co < 2; ++co) {
      int lx = List.of(rx, gx).get(co);
      int ly = List.of(ry, gy).get(co);
      double a = center(px, py, lx, ly);
      double d = dist(px, py, lx, ly);
      for (int hk = 0; hk < cx.length; ++hk) {
        Point ip = intpil(cx, cy, cr, px, py, a, hk);
        if (ip != null) {
          double ix = ip.x;
          double iy = ip.y;
          double di = dist(px, py, ix, iy);
          double ds = dist(lx, ly, ix, iy);
          if (di < d + EPSILON && ds < d + EPSILON) {
            col ^= 1 + co;

            break;
          }
        }
      }
    }

    return col;
  }
}

class Element6 implements Comparable<Element6> {
  double midy;
  double y1;
  double y2;
  Element4 tk;

  Element6(double midy, double y1, double y2, Element4 tk) {
    this.midy = midy;
    this.y1 = y1;
    this.y2 = y2;
    this.tk = tk;
  }

  @Override
  public int compareTo(Element6 o) {
    int cmp = Double.compare(midy, o.midy);
    if (cmp != 0) {
      return cmp;
    }

    cmp = Double.compare(y1, o.y1);
    if (cmp != 0) {
      return cmp;
    }

    cmp = Double.compare(y2, o.y2);
    if (cmp != 0) {
      return cmp;
    }

    return tk.compareTo(o.tk);
  }
}

class Element5 implements Comparable<Element5> {
  int lk;
  double y;

  Element5(int lk, double y) {
    this.lk = lk;
    this.y = y;
  }

  @Override
  public int compareTo(Element5 o) {
    int cmp = Integer.compare(lk, o.lk);
    if (cmp != 0) {
      return cmp;
    }

    return Double.compare(y, o.y);
  }
}

class Element4 implements Comparable<Element4> {
  int t;
  int k;

  Element4(int t, int k) {
    this.t = t;
    this.k = k;
  }

  @Override
  public int hashCode() {
    return Objects.hash(t, k);
  }

  @Override
  public boolean equals(Object obj) {
    Element4 other = (Element4) obj;

    return t == other.t && k == other.k;
  }

  @Override
  public int compareTo(Element4 o) {
    int cmp = Integer.compare(t, o.t);
    if (cmp != 0) {
      return cmp;
    }

    return Integer.compare(k, o.k);
  }
}

class Element3 implements Comparable<Element3> {
  double y;
  int t;
  int k;

  Element3(double y, int t, int k) {
    this.y = y;
    this.t = t;
    this.k = k;
  }

  @Override
  public int compareTo(Element3 o) {
    int cmp = Double.compare(y, o.y);
    if (cmp != 0) {
      return cmp;
    }

    cmp = Integer.compare(t, o.t);
    if (cmp != 0) {
      return cmp;
    }

    return Integer.compare(k, o.k);
  }
}

class DistanceAndPoint {
  double distance;
  Point point;

  DistanceAndPoint(double distance, Point point) {
    this.distance = distance;
    this.point = point;
  }
}

class Point {
  double x;
  double y;

  Point(double x, double y) {
    this.x = x;
    this.y = y;
  }
}

class Element2 implements Comparable<Element2> {
  double hd;
  int hk;

  Element2(double hd, int hk) {
    this.hd = hd;
    this.hk = hk;
  }

  @Override
  public int compareTo(Element2 o) {
    int cmp = Double.compare(hd, o.hd);
    if (cmp != 0) {
      return cmp;
    }

    return Integer.compare(hk, o.hk);
  }
}

class Element1 implements Comparable<Element1> {
  double a;
  double d;
  int pk;
  int t;

  Element1(double a, double d, int pk, int t) {
    this.a = a;
    this.d = d;
    this.pk = pk;
    this.t = t;
  }

  @Override
  public int compareTo(Element1 o) {
    int cmp = Double.compare(a, o.a);
    if (cmp != 0) {
      return cmp;
    }

    cmp = Double.compare(d, o.d);
    if (cmp != 0) {
      return cmp;
    }

    cmp = Integer.compare(pk, o.pk);
    if (cmp != 0) {
      return cmp;
    }

    return Integer.compare(t, o.t);
  }
}

class RaySegment {
  double x1;
  double y1;
  double x2;
  double y2;

  RaySegment(double x1, double y1, double x2, double y2) {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
  }
}
