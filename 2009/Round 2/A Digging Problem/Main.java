import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 0; tc < N; ++tc) {
      int R = sc.nextInt();
      int C = sc.nextInt();
      int F = sc.nextInt();
      char[][] cave = new char[R][C];
      for (int r = 0; r < R; ++r) {
        String line = sc.next();
        for (int c = 0; c < C; ++c) {
          cave[r][c] = line.charAt(c);
        }
      }

      System.out.println(String.format("Case #%d: %s", tc + 1, solve(cave, F)));
    }

    sc.close();
  }

  static String solve(char[][] cave, int F) {
    int R = cave.length;
    int C = cave[0].length;

    int[][][] digNums = new int[R][C][C];
    for (int r = 0; r < R; ++r) {
      for (int beginIndex = 0; beginIndex < C; ++beginIndex) {
        for (int endIndex = beginIndex; endIndex < C; ++endIndex) {
          digNums[r][beginIndex][endIndex] = Integer.MAX_VALUE;
        }
      }
    }
    digNums[0][0][findEndIndex(cave, 0, 0)] = 0;

    for (int r = 0; r < R - 1; ++r) {
      for (int beginIndex = 0; beginIndex < C; ++beginIndex) {
        for (int endIndex = beginIndex; endIndex < C; ++endIndex) {
          if (digNums[r][beginIndex][endIndex] != Integer.MAX_VALUE) {
            for (int c : new int[] {beginIndex, endIndex}) {
              if (cave[r + 1][c] == '.') {
                int landingR = findLandingR(cave, r + 1, c);
                if (landingR - r <= F) {
                  update(
                      digNums,
                      landingR,
                      findBeginIndex(cave, landingR, c),
                      findEndIndex(cave, landingR, c),
                      digNums[r][beginIndex][endIndex]);
                }
              }
            }

            int beginBaseIndex =
                beginIndex + ((r == R - 1 || cave[r + 1][beginIndex] == '#') ? 0 : 1);
            int endBaseIndex = endIndex - ((r == R - 1 || cave[r + 1][endIndex] == '#') ? 0 : 1);
            if (beginBaseIndex != endBaseIndex) {
              int r_ = r;
              int[] baselessIndices =
                  IntStream.rangeClosed(beginBaseIndex, endBaseIndex)
                      .filter(c -> r_ != R - 2 && cave[r_ + 2][c] == '.')
                      .toArray();
              for (int baselessIndex : baselessIndices) {
                int landingR = findLandingR(cave, r + 2, baselessIndex);
                if (landingR - r <= F) {
                  update(
                      digNums,
                      landingR,
                      findBeginIndex(cave, landingR, baselessIndex),
                      findEndIndex(cave, landingR, baselessIndex),
                      digNums[r][beginIndex][endIndex] + 1);
                }
              }

              for (int i = 0; i <= baselessIndices.length; ++i) {
                int minIndex = (i == 0) ? beginBaseIndex : baselessIndices[i - 1];
                int maxIndex = (i == baselessIndices.length) ? endBaseIndex : baselessIndices[i];
                for (int beginC = minIndex; beginC <= maxIndex; ++beginC) {
                  for (int endC = beginC; endC <= maxIndex; ++endC) {
                    if ((beginC != beginBaseIndex || endC != endBaseIndex)
                        && (r == R - 2 || cave[r + 2][beginC] == '#' || cave[r + 2][endC] == '#')) {
                      update(
                          digNums,
                          r + 1,
                          findBeginIndex(cave, r + 1, beginC),
                          findEndIndex(cave, r + 1, endC),
                          digNums[r][beginIndex][endIndex] + (endC - beginC + 1));
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

    int minDigNum = Integer.MAX_VALUE;
    for (int beginIndex = 0; beginIndex < C; ++beginIndex) {
      for (int endIndex = beginIndex; endIndex < C; ++endIndex) {
        minDigNum = Math.min(minDigNum, digNums[R - 1][beginIndex][endIndex]);
      }
    }

    return (minDigNum == Integer.MAX_VALUE) ? "No" : String.format("Yes %d", minDigNum);
  }

  static void update(int[][][] digNums, int r, int beginIndex, int endIndex, int digNum) {
    digNums[r][beginIndex][endIndex] = Math.min(digNums[r][beginIndex][endIndex], digNum);
  }

  static int findLandingR(char[][] cave, int r, int c) {
    int landingR = r;
    while (landingR != cave.length - 1 && cave[landingR + 1][c] == '.') {
      ++landingR;
    }

    return landingR;
  }

  static int findBeginIndex(char[][] cave, int r, int c) {
    int beginIndex = c;
    while (beginIndex != 0
        && cave[r][beginIndex - 1] == '.'
        && (r == cave.length - 1 || cave[r + 1][beginIndex] == '#')) {
      --beginIndex;
    }

    return beginIndex;
  }

  static int findEndIndex(char[][] cave, int r, int c) {
    int endIndex = c;
    while (endIndex != cave[0].length - 1
        && cave[r][endIndex + 1] == '.'
        && (r == cave.length - 1 || cave[r + 1][endIndex] == '#')) {
      ++endIndex;
    }

    return endIndex;
  }
}
