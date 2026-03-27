// https://raw.githubusercontent.com/google/coding-competitions-archive/main/codejam/2009/world_finals/marbles/analysis.pdf

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      int n = sc.nextInt();
      String[] colors = new String[2 * n];
      for (int i = 0; i < colors.length; ++i) {
        colors[i] = sc.next();
      }

      System.out.println(String.format("Case #%d: %d", tc + 1, solve(colors)));
    }

    sc.close();
  }

  static int solve(String[] colors) {
    int n = colors.length / 2;

    Map<String, Integer> colorToId = new HashMap<>();

    @SuppressWarnings("unchecked")
    List<Integer>[] idToIndices = new List[n];
    for (int i = 0; i < idToIndices.length; ++i) {
      idToIndices[i] = new ArrayList<>();
    }

    for (int i = 0; i < colors.length; ++i) {
      colorToId.putIfAbsent(colors[i], colorToId.size());
      idToIndices[colorToId.get(colors[i])].add(i);
    }

    int[][] h2Cache = new int[2 * n][n + 1];
    for (int i = 0; i < h2Cache.length; ++i) {
      Arrays.fill(h2Cache[i], -1);
    }

    List<Event>[] eventLists = buildEventLists(colors, colorToId, idToIndices);
    if (eventLists == null) {
      return -1;
    }

    int result = Integer.MAX_VALUE;
    for (int h1 = 0; h1 <= n; ++h1) {
      int h2 = computeH2(colors, colorToId, h2Cache, eventLists, 0, 2 * n, h1);
      if (h2 != Integer.MAX_VALUE) {
        result = Math.min(result, h1 + h2);
      }
    }

    return result;
  }

  static int computeH2(
      String[] colors,
      Map<String, Integer> colorToId,
      int[][] h2Cache,
      List<Event>[] eventLists,
      int a,
      int b,
      int h1) {
    if (h1 < 0) {
      return Integer.MAX_VALUE;
    }
    if (a == b) {
      return 0;
    }

    if (h2Cache[a][h1] == -1) {
      List<Event> events = eventLists[colorToId.get(colors[a])];

      int result = Integer.MAX_VALUE;
      for (int round = 0; round < 2; ++round) {
        int top = 0;
        int bottom = 0;
        int h2 = 0;
        for (int i = 0; i <= events.size(); ++i) {
          int alpha = (i == 0) ? a : (events.get(i - 1).x + 1);
          int beta = (i == events.size()) ? b : events.get(i).x;

          int nextH2 = computeH2(colors, colorToId, h2Cache, eventLists, alpha, beta, h1 - top);
          if (nextH2 == Integer.MAX_VALUE) {
            h2 = Integer.MAX_VALUE;
          } else {
            h2 = Math.max(h2, nextH2 + bottom);
          }

          if (i != events.size()) {
            if (events.get(i).type == Type.TOP_BEGIN) {
              ++top;
            } else if (events.get(i).type == Type.TOP_END) {
              --top;
            } else if (events.get(i).type == Type.BOTTOM_BEGIN) {
              ++bottom;
            } else {
              --bottom;
            }
          }
        }

        result = Math.min(result, h2);

        for (Event event : events) {
          event.type = flipDirection(event.type);
        }
      }

      h2Cache[a][h1] = result;
    }

    return h2Cache[a][h1];
  }

  static List<Event>[] buildEventLists(
      String[] colors, Map<String, Integer> colorToId, List<Integer>[] idToIndices) {
    int n = idToIndices.length;

    @SuppressWarnings("unchecked")
    List<Event>[] eventLists = new List[n];
    for (int i = 0; i < eventLists.length; ++i) {
      eventLists[i] = new ArrayList<>();

      int[] directions = new int[n];
      if (!dfs(idToIndices, directions, i, 1)) {
        return null;
      }

      for (int x = 0; x < colors.length; ++x) {
        int id = colorToId.get(colors[x]);
        if (directions[id] != 0) {
          eventLists[i].add(
              new Event(x, getType(directions[id] == 1, x == idToIndices[id].get(0))));
        }
      }
    }

    return eventLists;
  }

  static boolean dfs(List<Integer>[] idToIndices, int[] directions, int pos, int direction) {
    int n = idToIndices.length;

    if (directions[pos] == direction) {
      return true;
    }
    if (directions[pos] == -direction) {
      return false;
    }

    directions[pos] = direction;

    for (int i = 0; i < n; ++i) {
      if (isCross(idToIndices, pos, i)) {
        if (!dfs(idToIndices, directions, i, -direction)) {
          return false;
        }
      }
    }

    return true;
  }

  static boolean isCross(List<Integer>[] idToIndices, int pos1, int pos2) {
    return (idToIndices[pos1].get(0) < idToIndices[pos2].get(0)
            && idToIndices[pos2].get(0) < idToIndices[pos1].get(1)
            && idToIndices[pos1].get(1) < idToIndices[pos2].get(1))
        || (idToIndices[pos2].get(0) < idToIndices[pos1].get(0)
            && idToIndices[pos1].get(0) < idToIndices[pos2].get(1)
            && idToIndices[pos2].get(1) < idToIndices[pos1].get(1));
  }

  static Type getType(boolean topOrBottom, boolean beginOrEnd) {
    if (topOrBottom) {
      return beginOrEnd ? Type.TOP_BEGIN : Type.TOP_END;
    }

    return beginOrEnd ? Type.BOTTOM_BEGIN : Type.BOTTOM_END;
  }

  static Type flipDirection(Type type) {
    if (type == Type.TOP_BEGIN) {
      return Type.BOTTOM_BEGIN;
    }
    if (type == Type.TOP_END) {
      return Type.BOTTOM_END;
    }
    if (type == Type.BOTTOM_BEGIN) {
      return Type.TOP_BEGIN;
    }

    return Type.TOP_END;
  }
}

enum Type {
  TOP_BEGIN,
  TOP_END,
  BOTTOM_BEGIN,
  BOTTOM_END;
}

class Event {
  int x;
  Type type;

  Event(int x, Type type) {
    this.x = x;
    this.type = type;
  }
}
