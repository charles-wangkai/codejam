import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int R = sc.nextInt();
			int C = sc.nextInt();
			char[][] cells = new char[R][C];
			for (int r = 0; r < R; r++) {
				String line = sc.next();
				for (int c = 0; c < C; c++) {
					cells[r][c] = line.charAt(c);
				}
			}

			System.out.println(String.format("Case #%d: %d", tc, solve(cells)));
		}

		sc.close();
	}

	static int solve(char[][] cells) {
		int R = cells.length;
		int C = cells[0].length;

		Map<String, Boolean> stateToFirstWin = new HashMap<>();

		int result = 0;
		for (int r = 0; r < R; r++) {
			for (int c = 0; c < C; c++) {
				if (cells[r][c] == '.') {
					for (char step : new char[] { 'H', 'V' }) {
						char[][] nextCells = move(cells, r, c, step);

						if (nextCells != null && !isFirstWin(stateToFirstWin, nextCells)) {
							result++;
						}
					}
				}
			}
		}
		return result;
	}

	static boolean isFirstWin(Map<String, Boolean> stateToFirstWin, char[][] cells) {
		int R = cells.length;
		int C = cells[0].length;

		String state = buildState(cells);

		if (stateToFirstWin.containsKey(state)) {
			return stateToFirstWin.get(state);
		}

		boolean firstWin = false;
		for (int r = 0; r < R; r++) {
			for (int c = 0; c < C; c++) {
				if (cells[r][c] == '.') {
					for (char step : new char[] { 'H', 'V' }) {
						char[][] nextCells = move(cells, r, c, step);

						if (nextCells != null && !isFirstWin(stateToFirstWin, nextCells)) {
							firstWin = true;
						}
					}
				}
			}
		}

		stateToFirstWin.put(state, firstWin);
		return firstWin;
	}

	static char[][] move(char[][] cells, int beginR, int beginC, char step) {
		int R = cells.length;
		int C = cells[0].length;

		char[][] nextCells = new char[R][C];
		for (int r = 0; r < R; r++) {
			for (int c = 0; c < C; c++) {
				nextCells[r][c] = cells[r][c];
			}
		}
		nextCells[beginR][beginC] = '*';

		if (step == 'H') {
			for (int c = beginC - 1; c >= 0; c--) {
				if (nextCells[beginR][c] == '#') {
					return null;
				}
				if (nextCells[beginR][c] == '*') {
					break;
				}

				nextCells[beginR][c] = '*';
			}
			for (int c = beginC + 1; c < C; c++) {
				if (nextCells[beginR][c] == '#') {
					return null;
				}
				if (nextCells[beginR][c] == '*') {
					break;
				}

				nextCells[beginR][c] = '*';
			}
		} else {
			for (int r = beginR - 1; r >= 0; r--) {
				if (nextCells[r][beginC] == '#') {
					return null;
				}
				if (nextCells[r][beginC] == '*') {
					break;
				}

				nextCells[r][beginC] = '*';
			}
			for (int r = beginR + 1; r < R; r++) {
				if (nextCells[r][beginC] == '#') {
					return null;
				}
				if (nextCells[r][beginC] == '*') {
					break;
				}

				nextCells[r][beginC] = '*';
			}
		}

		return nextCells;
	}

	static String buildState(char[][] cells) {
		return Arrays.stream(cells).map(String::new).collect(Collectors.joining());
	}
}