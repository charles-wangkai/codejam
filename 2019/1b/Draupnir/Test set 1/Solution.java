import java.util.Scanner;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int[] days = new int[7];
			for (int i = 1; i < days.length; i++) {
				System.out.println(i);
				System.out.flush();

				int response = sc.nextInt();
				if (response == -1) {
					System.exit(0);
				}

				days[i] = response;
			}

			for (int R1 = 0;; R1++) {
				int R2 = days[2] - days[1] - 2 * R1;
				int R3 = days[3] - days[2] - 4 * R1;
				int R4 = days[4] - days[3] - 8 * R1 - 2 * R2;
				int R5 = days[5] - days[4] - 16 * R1;
				int R6 = days[6] - days[5] - 32 * R1 - 4 * R2 - 2 * R3;

				if (2 * R1 + R2 + R3 + R4 + R5 + R6 == days[1]) {
					System.out.println(String.format("%d %d %d %d %d %d", R1, R2, R3, R4, R5, R6));
					System.out.flush();

					break;
				}
			}

			int verdict = sc.nextInt();
			if (verdict == -1) {
				System.exit(0);
			}
		}

		sc.close();
	}
}
