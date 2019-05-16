import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int N = sc.nextInt();
			int B = sc.nextInt();
			sc.nextInt();

			String[] binaries = IntStream.range(0, N).mapToObj(Solution::convertToBinary).toArray(String[]::new);

			String[] answers = new String[10];
			for (int i = 0; i < answers.length; i++) {
				String query = buildQuery(binaries, i);

				System.out.println(query);
				System.out.flush();

				String response = sc.next();
				if (response.equals("-1")) {
					System.exit(0);
				}

				answers[i] = response;
			}

			boolean[] goods = new boolean[N];
			for (int i = 0; i < N - B; i++) {
				goods[Integer.parseInt(buildBinary(answers, i), 2)] = true;
			}

			System.out.println(IntStream.range(0, N).filter(i -> !goods[i]).mapToObj(String::valueOf)
					.collect(Collectors.joining(" ")));
			System.out.flush();

			int verdict = sc.nextInt();
			if (verdict == -1) {
				System.exit(0);
			}
		}

		sc.close();
	}

	static String convertToBinary(int x) {
		return String.format("%10s", Integer.toBinaryString(x)).replace(' ', '0');
	}

	static String buildQuery(String[] binaries, int col) {
		return Arrays.stream(binaries).map(binary -> binary.charAt(col))
				.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
	}

	static String buildBinary(String[] answers, int index) {
		return Arrays.stream(answers).map(answer -> answer.charAt(index))
				.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
	}
}
