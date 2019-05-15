import java.util.Scanner;

public class Solution {
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

		int result = 0;
		for (int code = (1 << (R * C)) - 1; code >= 0; code--) {
			boolean[][] pattern = decode(R, C, code);

			if (isValid(pattern) && isContain(goDeep(goDeep(grid)), grid, pattern)) {
				result = Math.max(result, countCell(pattern));
			}
		}
		return result;
	}

	static boolean isContain(char[][] dream, char[][] grid, boolean[][] pattern) {
		int dreamR = dream.length;
		int dreamC = dream[0].length;
		int patternR = pattern.length;
		int patternC = pattern[0].length;

		for (int minR = -patternR; minR < dreamR; minR++) {
			for (int minC = -patternC; minC < dreamC; minC++) {
				if (isMatch(dream, grid, pattern, minR, minC)) {
					return true;
				}
			}
		}

		return false;
	}

	static boolean isMatch(char[][] dream, char[][] grid, boolean[][] pattern, int minR, int minC) {
		int dreamR = dream.length;
		int dreamC = dream[0].length;
		int patternR = pattern.length;
		int patternC = pattern[0].length;

		for (int i = 0; i < patternR; i++) {
			for (int j = 0; j < patternC; j++) {
				int r = minR + i;
				int c = minC + j;

				if (pattern[i][j] && !(r >= 0 && r < dreamR && c >= 0 && c < dreamC && grid[i][j] == dream[r][c])) {
					return false;
				}
			}
		}

		return true;
	}

	static char[][] goDeep(char[][] dream) {
		int row = dream.length;
		int col = dream[0].length;

		char[][] result = new char[row * 2][col * 2];
		for (int r = 0; r < result.length; r++) {
			for (int c = 0; c < result[0].length; c++) {
				result[r][c] = dream[r / 2][c / 2];
			}
		}
		return result;
	}

	static int countCell(boolean[][] pattern) {
		int R = pattern.length;
		int C = pattern[0].length;

		int result = 0;
		for (int r = 0; r < R; r++) {
			for (int c = 0; c < C; c++) {
				if (pattern[r][c]) {
					result++;
				}
			}
		}
		return result;
	}

	static boolean[][] decode(int R, int C, int code) {
		boolean[][] result = new boolean[R][C];
		for (int r = 0; r < R; r++) {
			for (int c = 0; c < C; c++) {
				result[r][c] = code % 2 == 0;

				code /= 2;
			}
		}
		return result;
	}

	static boolean isValid(boolean[][] pattern) {
		int R = pattern.length;
		int C = pattern[0].length;

		boolean[][] visited = new boolean[R][C];
		boolean first = true;
		for (int r = 0; r < R; r++) {
			for (int c = 0; c < C; c++) {
				if (pattern[r][c] && !visited[r][c]) {
					if (first) {
						search(pattern, visited, r, c);

						first = false;
					} else {
						return false;
					}
				}
			}
		}
		return true;
	}

	static void search(boolean[][] pattern, boolean[][] visited, int r, int c) {
		int R = pattern.length;
		int C = pattern[0].length;

		visited[r][c] = true;

		for (int i = 0; i < R_OFFSETS.length; i++) {
			int adjR = r + R_OFFSETS[i];
			int adjC = c + C_OFFSETS[i];

			if (adjR >= 0 && adjR < R && adjC >= 0 && adjC < C && pattern[adjR][adjC] && !visited[adjR][adjC]) {
				search(pattern, visited, adjR, adjC);
			}
		}
	}
}
