import java.util.Scanner;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			System.out.println(210);
			System.out.flush();

			long response = sc.nextLong();

			int R4 = (int) (response / (1L << 52) % 128);
			int R5 = (int) (response / (1L << 42) % 128);
			int R6 = (int) (response / (1L << 35) % 128);

			System.out.println(42);
			System.out.flush();

			response = sc.nextLong();

			long remain = response - (1 << 10) * R4 - (1 << 8) * R5 - (1 << 7) * R6;
			int R1 = (int) (remain / (1L << 42) % 128);
			int R2 = (int) (remain / (1L << 21) % 128);
			int R3 = (int) (remain / (1L << 14) % 128);

			System.out.println(String.format("%d %d %d %d %d %d", R1, R2, R3, R4, R5, R6));
			System.out.flush();

			int verdict = sc.nextInt();
			if (verdict == -1) {
				System.exit(0);
			}
		}

		sc.close();
	}
}
