import java.util.Scanner;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int D = sc.nextInt();
			String P = sc.next();

			System.out.println(String.format("Case #%d: %s", tc, solve(D, P)));
		}

		sc.close();
	}

	static String solve(int D, String P) {
		int[] placeValues = new int[P.length()];
		placeValues[0] = 1;
		for (int i = 1; i < placeValues.length; i++) {
			placeValues[i] = placeValues[i - 1] * 2;
		}

		int[] counts = new int[P.length()];

		int damage = 0;
		int index = 0;
		for (char instruction : P.toCharArray()) {
			if (instruction == 'C') {
				index++;
			} else {
				counts[index]++;
				damage += placeValues[index];
			}
		}

		int hackNum = 0;
		while (damage > D) {
			if (index == 0) {
				return "IMPOSSIBLE";
			}

			if (counts[index] == 0) {
				index--;
			} else {
				counts[index]--;
				counts[index - 1]++;
				damage -= placeValues[index] - placeValues[index - 1];

				hackNum++;
			}
		}
		return String.valueOf(hackNum);
	}
}
