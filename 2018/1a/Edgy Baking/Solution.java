import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int N = sc.nextInt();
			int P = sc.nextInt();
			int[] W = new int[N];
			int[] H = new int[N];
			for (int i = 0; i < N; i++) {
				W[i] = sc.nextInt();
				H[i] = sc.nextInt();
			}

			System.out.println(String.format("Case #%d: %.10f", tc, solve(W, H, P)));
		}

		sc.close();
	}

	static double solve(int[] W, int[] H, int P) {
		int base = IntStream.range(0, W.length).map(i -> (W[i] + H[i]) * 2).sum();

		List<Range> ranges = new ArrayList<>();
		ranges.add(new Range(base, base));

		for (int i = 0; i < W.length; i++) {
			double minDelta = Math.min(W[i], H[i]) * 2;
			double maxDelta = Math.sqrt(W[i] * W[i] + H[i] * H[i]) * 2;

			List<Range> additives = ranges.stream()
					.map(range -> new Range(range.left + minDelta, range.right + maxDelta))
					.collect(Collectors.toList());
			ranges.addAll(additives);

			Collections.sort(ranges, (range1, range2) -> Double.compare(range1.left, range2.left));

			ranges = simplify(ranges, P);

			if (ranges.get(ranges.size() - 1).right >= P) {
				return P;
			}
		}

		return ranges.get(ranges.size() - 1).right;
	}

	static List<Range> simplify(List<Range> ranges, int P) {
		List<Range> result = new ArrayList<>();
		for (Range range : ranges) {
			if (range.left > P || (!result.isEmpty() && result.get(result.size() - 1).right >= range.right)) {
				continue;
			}

			if (result.isEmpty() || result.get(result.size() - 1).right < range.left) {
				result.add(range);
			} else {
				result.get(result.size() - 1).right = range.right;
			}
		}
		return result;
	}
}

class Range {
	double left;
	double right;

	Range(double left, double right) {
		this.left = left;
		this.right = right;
	}
}