import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		sc.nextInt();
		sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int[] modDivisors = { 4, 3, 5, 7, 11, 13, 17 };

			int[] remainders = new int[modDivisors.length];
			for (int i = 0; i < remainders.length; i++) {
				System.out.println(buildQuery(modDivisors[i]));
				System.out.flush();

				int[] numbers = new int[18];
				for (int j = 0; j < numbers.length; j++) {
					int number = sc.nextInt();
					if (number == -1) {
						System.exit(0);
					}

					numbers[j] = number;
				}

				remainders[i] = Arrays.stream(numbers).sum() % modDivisors[i];
			}

			for (int i = 1;; i++) {
				if (check(modDivisors, remainders, i)) {
					System.out.println(i);
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

	static String buildQuery(int modDivisor) {
		return IntStream.range(0, 18).mapToObj(x -> String.valueOf(modDivisor)).collect(Collectors.joining(" "));
	}

	static boolean check(int[] modDivisors, int[] remainders, int x) {
		return IntStream.range(0, modDivisors.length).allMatch(i -> x % modDivisors[i] == remainders[i]);
	}
}
