import java.util.Scanner;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			sc.nextInt();
			String P = sc.next();

			System.out.println(String.format("Case #%d: %s", tc, solve(P)));
		}

		sc.close();
	}

	static String solve(String P) {
		StringBuilder result = new StringBuilder();
		for (char ch : P.toCharArray()) {
			if (ch == 'E') {
				result.append('S');
			} else {
				result.append('E');
			}
		}
		return result.toString();
	}
}
