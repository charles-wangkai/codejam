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
			int result = -1;
			for (int i = 0; i < 365; i++) {
				System.out.println(IntStream.range(0, 18).mapToObj(x -> "18").collect(Collectors.joining(" ")));
				System.out.flush();

				int[] numbers = new int[18];
				for (int j = 0; j < numbers.length; j++) {
					int number = sc.nextInt();
					if (number == -1) {
						System.exit(0);
					}

					numbers[j] = number;
				}

				result = Math.max(result, Arrays.stream(numbers).sum());
			}

			System.out.println(result);
			System.out.flush();

			int verdict = sc.nextInt();
			if (verdict == -1) {
				System.exit(0);
			}
		}

		sc.close();
	}
}
