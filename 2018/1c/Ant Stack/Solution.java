import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {
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
		Map<Integer, Integer> countToMinWeight = new HashMap<>();
		countToMinWeight.put(0, 0);

		for (int wi : W) {
			Map<Integer, Integer> addedCountToMinWeight = new HashMap<>();
			for (int count : countToMinWeight.keySet()) {
				int minWeight = countToMinWeight.get(count);

				if (minWeight <= wi * 6) {
					addedCountToMinWeight.put(count + 1, minWeight + wi);
				}
			}

			countToMinWeight = merge(countToMinWeight, addedCountToMinWeight);
		}

		return countToMinWeight.keySet().stream().mapToInt(x -> x).max().getAsInt();
	}

	static Map<Integer, Integer> merge(Map<Integer, Integer> countToMinWeight1,
			Map<Integer, Integer> countToMinWeight2) {
		Map<Integer, Integer> result = new HashMap<>();
		for (int count : Stream.concat(countToMinWeight1.keySet().stream(), countToMinWeight2.keySet().stream())
				.collect(Collectors.toList())) {
			result.put(count, Math.min(countToMinWeight1.getOrDefault(count, Integer.MAX_VALUE),
					countToMinWeight2.getOrDefault(count, Integer.MAX_VALUE)));
		}
		return result;
	}
}
