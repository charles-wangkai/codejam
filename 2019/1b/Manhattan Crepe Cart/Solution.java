import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int P = sc.nextInt();
			int Q = sc.nextInt();
			int[] X = new int[P];
			int[] Y = new int[P];
			char[] D = new char[P];
			for (int i = 0; i < P; i++) {
				X[i] = sc.nextInt();
				Y[i] = sc.nextInt();
				D[i] = sc.next().charAt(0);
			}

			System.out.println(String.format("Case #%d: %s", tc, solve(X, Y, D, Q)));
		}

		sc.close();
	}

	static String solve(int[] X, int[] Y, char[] D, int Q) {
		int P = X.length;

		Set<Integer> xCandidates = new HashSet<>();
		xCandidates.add(0);

		Set<Integer> yCandidates = new HashSet<>();
		yCandidates.add(0);

		for (int i = 0; i < P; i++) {
			if (D[i] == 'N') {
				yCandidates.add(Y[i] + 1);
			} else if (D[i] == 'S') {
				yCandidates.add(Y[i]);
			} else if (D[i] == 'E') {
				xCandidates.add(X[i] + 1);
			} else {
				xCandidates.add(X[i]);
			}
		}

		int resultX = -1;
		int countForResultX = -1;
		for (int xCandidate : xCandidates) {
			int count = 0;
			for (int i = 0; i < P; i++) {
				if ((D[i] == 'E' && X[i] < xCandidate) || (D[i] == 'W' && X[i] > xCandidate)) {
					count++;
				}
			}

			if (count > countForResultX || (count == countForResultX && xCandidate < resultX)) {
				resultX = xCandidate;
				countForResultX = count;
			}
		}

		int resultY = -1;
		int countForResultY = -1;
		for (int yCandidate : yCandidates) {
			int count = 0;
			for (int i = 0; i < P; i++) {
				if ((D[i] == 'N' && Y[i] < yCandidate) || (D[i] == 'S' && Y[i] > yCandidate)) {
					count++;
				}
			}

			if (count > countForResultY || (count == countForResultY && yCandidate < resultY)) {
				resultY = yCandidate;
				countForResultY = count;
			}
		}

		return String.format("%d %d", resultX, resultY);
	}
}
