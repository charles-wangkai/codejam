import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

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

		Map<State, Integer> stateToNimber = new HashMap<>();

		int result = 0;
		for (int r = 0; r < R; r++) {
			if (!hasRadioactive(cells, r, 0, r, C - 1) && (computeNimber(cells, stateToNimber, 0, 0, r - 1, C - 1)
					^ computeNimber(cells, stateToNimber, r + 1, 0, R - 1, C - 1)) == 0) {
				result += C;
			}
		}
		for (int c = 0; c < C; c++) {
			if (!hasRadioactive(cells, 0, c, R - 1, c) && (computeNimber(cells, stateToNimber, 0, 0, R - 1, c - 1)
					^ computeNimber(cells, stateToNimber, 0, c + 1, R - 1, C - 1)) == 0) {
				result += R;
			}
		}
		return result;
	}

	static boolean hasRadioactive(char[][] cells, int minR, int minC, int maxR, int maxC) {
		int r = minR;
		int c = minC;
		while (true) {
			if (cells[r][c] == '#') {
				return true;
			}

			if (r == maxR && c == maxC) {
				return false;
			}

			if (r == maxR) {
				c++;
			} else {
				r++;
			}
		}
	}

	static int computeNimber(char[][] cells, Map<State, Integer> stateToNimber, int minR, int minC, int maxR,
			int maxC) {
		if (minR > maxR || minC > maxC) {
			return 0;
		}

		State state = new State(minR, minC, maxR, maxC);
		if (stateToNimber.containsKey(state)) {
			return stateToNimber.get(state);
		}

		Set<Integer> nimbers = new HashSet<>();
		for (int r = minR; r <= maxR; r++) {
			if (!hasRadioactive(cells, r, minC, r, maxC)) {
				nimbers.add(computeNimber(cells, stateToNimber, minR, minC, r - 1, maxC)
						^ computeNimber(cells, stateToNimber, r + 1, minC, maxR, maxC));
			}
		}
		for (int c = minC; c <= maxC; c++) {
			if (!hasRadioactive(cells, minR, c, maxR, c)) {
				nimbers.add(computeNimber(cells, stateToNimber, minR, minC, maxR, c - 1)
						^ computeNimber(cells, stateToNimber, minR, c + 1, maxR, maxC));
			}
		}

		int nimber = mex(nimbers);
		stateToNimber.put(state, nimber);

		return nimber;
	}

	static int mex(Set<Integer> nimbers) {
		for (int i = 0;; i++) {
			if (!nimbers.contains(i)) {
				return i;
			}
		}
	}
}

class State {
	int minR;
	int minC;
	int maxR;
	int maxC;

	State(int minR, int minC, int maxR, int maxC) {
		this.minR = minR;
		this.minC = minC;
		this.maxR = maxR;
		this.maxC = maxC;
	}

	@Override
	public int hashCode() {
		return Objects.hash(minR, minC, maxR, maxC);
	}

	@Override
	public boolean equals(Object obj) {
		State other = (State) obj;
		return minR == other.minR && minC == other.minC && maxR == other.maxR && maxC == other.maxC;
	}
}