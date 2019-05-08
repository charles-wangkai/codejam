import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			long N = sc.nextLong();
			long K = sc.nextLong();

			System.out.println(String.format("Case #%d: %s", tc, solve(N, K)));
		}

		sc.close();
	}

	static String solve(long N, long K) {
		SortedMap<Long, Long> distanceToCount = new TreeMap<>();
		distanceToCount.put(N, 1L);
		while (true) {
			long distance = distanceToCount.lastKey();
			long count = distanceToCount.remove(distance);

			long leftDistance = (distance - 1) / 2;
			long rightDistance = distance / 2;

			if (K <= count) {
				return String.format("%d %d", rightDistance, leftDistance);
			}

			K -= count;
			distanceToCount.put(leftDistance, distanceToCount.getOrDefault(leftDistance, 0L) + count);
			distanceToCount.put(rightDistance, distanceToCount.getOrDefault(rightDistance, 0L) + count);
		}
	}
}
