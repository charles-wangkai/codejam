import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int N = sc.nextInt();

			int[] counts = new int[N];
			boolean[] solds = new boolean[N];

			for (int i = 0; i < N; i++) {
				int D = sc.nextInt();

				if (D == -1) {
					System.exit(1);
				}

				int[] likes = new int[D];
				for (int j = 0; j < likes.length; j++) {
					likes[j] = sc.nextInt();
					counts[likes[j]]++;
				}

				Optional<Integer> sold = Arrays.stream(likes).filter(like -> !solds[like]).boxed()
						.sorted((like1, like2) -> Integer.compare(counts[like1], counts[like2])).findFirst();
				if (sold.isPresent()) {
					solds[sold.get()] = true;
					System.out.println(sold.get());
				} else {
					System.out.println(-1);
				}
				System.out.flush();
			}
		}

		sc.close();
	}
}
