import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {
	static final int W_LIMIT = 1_000_000_000;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int N = sc.nextInt();
			int[] W = new int[N];
			for (int i = 0; i < W.length; i++) {
				W[i] = sc.nextInt();
			}

			System.out.println(String.format("Case #%d: %d", tc, solve(W)));
		}

		sc.close();
	}

	static int solve(int[] W) {
		Map<Integer, Long> countToMinWeightSum = new HashMap<>();
		countToMinWeightSum.put(0, 0L);

		for (int wi : W) {
			Map<Integer, Long> addedCountToMinWeightSum = new HashMap<>();
			for (int count : countToMinWeightSum.keySet()) {
				long minWeightSum = countToMinWeightSum.get(count);

				if (minWeightSum <= wi * 6L && minWeightSum + wi <= W_LIMIT * 7L) {
					addedCountToMinWeightSum.put(count + 1, minWeightSum + wi);
				}
			}

			countToMinWeightSum = merge(countToMinWeightSum, addedCountToMinWeightSum);
		}

		return countToMinWeightSum.keySet().stream().mapToInt(x -> x).max().getAsInt();
	}

	static Map<Integer, Long> merge(Map<Integer, Long> countToMinWeightSum1, Map<Integer, Long> countToMinWeightSum2) {
		Map<Integer, Long> result = new HashMap<>();
		for (int count : Stream.concat(countToMinWeightSum1.keySet().stream(), countToMinWeightSum2.keySet().stream())
				.collect(Collectors.toList())) {
			result.put(count, Math.min(countToMinWeightSum1.getOrDefault(count, Long.MAX_VALUE),
					countToMinWeightSum2.getOrDefault(count, Long.MAX_VALUE)));
		}
		return result;
	}
}
