import java.util.Scanner;

public class Solution {
	static final char[] COLORS = { 'B', 'W' };
	static final int[] R_OFFSETS = { -1, 0, 1, 0 };
	static final int[] C_OFFSETS = { 0, 1, 0, -1 };

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int R = sc.nextInt();
			int C = sc.nextInt();
			char[][] grid = new char[R][C];
			for (int r = 0; r < R; r++) {
				String line = sc.next();

				for (int c = 0; c < C; c++) {
					grid[r][c] = line.charAt(c);
				}
			}

			System.out.println(String.format("Case #%d: %d", tc, solve(grid)));
		}

		sc.close();
	}

	static int solve(char[][] grid) {
		int R = grid.length;
		int C = grid[0].length;

		int result = -1;
		for (int upperMaxR = 0; upperMaxR < R; upperMaxR++) {
			for (int leftMaxC = 0; leftMaxC < C; leftMaxC++) {
				for (char upperLeftColor : COLORS) {
					for (char upperRightColor : COLORS) {
						for (char lowerLeftColor : COLORS) {
							for (char lowerRightColor : COLORS) {
								if (upperMaxR == R - 1 || leftMaxC == C - 1 || isExist(grid, upperLeftColor,
										upperRightColor, lowerLeftColor, lowerRightColor)) {
									boolean[][] visited = new boolean[R][C];

									for (int r = 0; r < R; r++) {
										for (int c = 0; c < C; c++) {
											result = Math.max(result, search(grid, upperMaxR, leftMaxC, upperLeftColor,
													upperRightColor, lowerLeftColor, lowerRightColor, visited, r, c));
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	static boolean isExist(char[][] grid, char upperLeftColor, char upperRightColor, char lowerLeftColor,
			char lowerRightColor) {
		int R = grid.length;
		int C = grid[0].length;

		for (int r = 0; r < R - 1; r++) {
			for (int c = 0; c < C - 1; c++) {
				if (grid[r][c] == upperLeftColor && grid[r][c + 1] == upperRightColor
						&& grid[r + 1][c] == lowerLeftColor && grid[r + 1][c + 1] == lowerRightColor) {
					return true;
				}
			}
		}

		return false;
	}

	static int search(char[][] grid, int upperMaxR, int leftMaxC, char upperLeftColor, char upperRightColor,
			char lowerLeftColor, char lowerRightColor, boolean[][] visited, int r, int c) {
		int R = grid.length;
		int C = grid[0].length;

		if (visited[r][c] || grid[r][c] != findTargetColor(upperMaxR, leftMaxC, upperLeftColor, upperRightColor,
				lowerLeftColor, lowerRightColor, r, c)) {
			return 0;
		}
		visited[r][c] = true;

		int result = 1;
		for (int i = 0; i < R_OFFSETS.length; i++) {
			int adjR = r + R_OFFSETS[i];
			int adjC = c + C_OFFSETS[i];

			if (adjR >= 0 && adjR < R && adjC >= 0 && adjC < C) {
				result += search(grid, upperMaxR, leftMaxC, upperLeftColor, upperRightColor, lowerLeftColor,
						lowerRightColor, visited, adjR, adjC);
			}
		}
		return result;
	}

	static char findTargetColor(int upperMaxR, int leftMaxC, char upperLeftColor, char upperRightColor,
			char lowerLeftColor, char lowerRightColor, int r, int c) {
		if (r <= upperMaxR) {
			if (c <= leftMaxC) {
				return upperLeftColor;
			} else {
				return upperRightColor;
			}
		} else {
			if (c <= leftMaxC) {
				return lowerLeftColor;
			} else {
				return lowerRightColor;
			}
		}
	}
}
