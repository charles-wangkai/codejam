import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int A = sc.nextInt();
			String[] C = new String[A];
			for (int i = 0; i < C.length; i++) {
				C[i] = sc.next();
			}

			System.out.println(String.format("Case #%d: %s", tc, solve(C)));
		}

		sc.close();
	}

	static String solve(String[] C) {
		StringBuilder result = new StringBuilder();
		List<String> programs = Arrays.asList(C);

		while (!programs.isEmpty()) {
			Map<Character, List<String>> moveToPrograms = new HashMap<>();
			for (String program : programs) {
				char move = getMove(program, result.length());

				if (!moveToPrograms.containsKey(move)) {
					moveToPrograms.put(move, new ArrayList<>());
				}

				moveToPrograms.get(move).add(program);
			}

			if (moveToPrograms.size() == 3) {
				return "IMPOSSIBLE";
			}

			char currentMove;
			if (moveToPrograms.size() == 1) {
				char move = moveToPrograms.keySet().iterator().next();

				if (move == 'R') {
					currentMove = 'P';
				} else if (move == 'P') {
					currentMove = 'S';
				} else {
					currentMove = 'R';
				}
			} else {
				if (!moveToPrograms.containsKey('R')) {
					currentMove = 'S';
				} else if (!moveToPrograms.containsKey('P')) {
					currentMove = 'R';
				} else {
					currentMove = 'P';
				}
			}

			result.append(currentMove);
			programs = moveToPrograms.getOrDefault(currentMove, new ArrayList<>());
		}

		return result.toString();
	}

	static char getMove(String program, int index) {
		return program.charAt(index % program.length());
	}
}
