import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int C = sc.nextInt();
			int[] B = new int[C];
			for (int i = 0; i < B.length; i++) {
				B[i] = sc.nextInt();
			}

			System.out.println(String.format("Case #%d: %s", tc, solve(B)));
		}

		sc.close();
	}

	static String solve(int[] B) {
		int col = B.length;

		if (B[0] == 0 || B[col - 1] == 0) {
			return "IMPOSSIBLE";
		}

		int[] positions = new int[col];
		int index = 0;
		for (int i = 0; i < B.length; i++) {
			for (int j = 0; j < B[i]; j++) {
				positions[index] = i;
				index++;
			}
		}

		int row = IntStream.range(0, positions.length).map(i -> Math.abs(i - positions[i])).max().getAsInt() + 1;

		char[][] cells = new char[row][col];
		for (int r = 0; r < row; r++) {
			for (int c = 0; c < col; c++) {
				cells[r][c] = '.';
			}
		}

		for (int i = 0; i < col; i++) {
			if (positions[i] < i) {
				for (int j = 0; j < i - positions[i]; j++) {
					cells[row - 2 - j][positions[i] + 1 + j] = '/';
				}
			} else if (positions[i] > i) {
				for (int j = 0; j < positions[i] - i; j++) {
					cells[row - 2 - j][positions[i] - 1 - j] = '\\';
				}
			}
		}

		return String.format("%d\n%s", row, Arrays.stream(cells).map(String::new).collect(Collectors.joining("\n")));
	}
}
