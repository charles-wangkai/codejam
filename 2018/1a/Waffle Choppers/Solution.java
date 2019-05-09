import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int R = sc.nextInt();
			int C = sc.nextInt();
			int H = sc.nextInt();
			int V = sc.nextInt();
			char[][] cells = new char[R][C];
			for (int r = 0; r < R; r++) {
				String line = sc.next();

				for (int c = 0; c < C; c++) {
					cells[r][c] = line.charAt(c);
				}
			}

			System.out.println(String.format("Case #%d: %s", tc, solve(cells, H, V) ? "POSSIBLE" : "IMPOSSIBLE"));
		}

		sc.close();
	}

	static boolean solve(char[][] cells, int H, int V) {
		int R = cells.length;
		int C = cells[0].length;

		int[] rowCounts = new int[R];
		int[] colCounts = new int[C];
		int chipNum = 0;
		for (int r = 0; r < R; r++) {
			for (int c = 0; c < C; c++) {
				if (cells[r][c] == '@') {
					rowCounts[r]++;
					colCounts[c]++;
					chipNum++;
				}
			}
		}

		if (chipNum % ((H + 1) * (V + 1)) != 0) {
			return false;
		}

		int targetPartChipNum = chipNum / ((H + 1) * (V + 1));

		List<Integer> rowCutIndices = buildCutIndices(rowCounts, H);
		List<Integer> colCutIndices = buildCutIndices(colCounts, V);
		if (rowCutIndices == null || colCutIndices == null) {
			return false;
		}

		for (int i = 0; i < rowCutIndices.size() - 1; i++) {
			for (int j = 0; j < colCutIndices.size() - 1; j++) {
				int partChipNum = 0;

				for (int r = rowCutIndices.get(i); r < rowCutIndices.get(i + 1); r++) {
					for (int c = colCutIndices.get(j); c < colCutIndices.get(j + 1); c++) {
						if (cells[r][c] == '@') {
							partChipNum++;
						}
					}
				}

				if (partChipNum != targetPartChipNum) {
					return false;
				}
			}
		}

		return true;
	}

	static List<Integer> buildCutIndices(int[] counts, int cutNum) {
		int total = Arrays.stream(counts).sum();

		if (total % (cutNum + 1) != 0) {
			return null;
		}

		int targetPartSum = total / (cutNum + 1);

		List<Integer> result = new ArrayList<>();
		result.add(0);
		int partSum = 0;
		for (int i = 0; i < counts.length; i++) {
			partSum += counts[i];

			if (partSum > targetPartSum) {
				return null;
			}

			if (partSum == targetPartSum) {
				result.add(i + 1);
				partSum = 0;
			}
		}
		return result;
	}
}
