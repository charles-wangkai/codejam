import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int M = sc.nextInt();
			int[][] R = new int[M][2];
			for (int i = 0; i < M; i++) {
				R[i][0] = sc.nextInt() - 1;
				R[i][1] = sc.nextInt() - 1;
			}
			int[] G = new int[M];
			for (int i = 0; i < G.length; i++) {
				G[i] = sc.nextInt();
			}

			System.out.println(String.format("Case #%d: %d", tc, solve(R, G)));
		}

		sc.close();
	}

	static int solve(int[][] R, int[] G) {
		int[] sequence = IntStream.range(0, R.length).toArray();

		return search(R, G, sequence, 1);
	}

	static int search(int[][] R, int[] G, int[] sequence, int index) {
		if (index == sequence.length) {
			Map<Integer, Integer> metalToOrder = new HashMap<>();
			for (int i = 0; i < sequence.length; i++) {
				metalToOrder.put(sequence[i], i);
			}

			int[] grams = Arrays.copyOf(G, G.length);
			int result = 0;
			while (consume(R, metalToOrder, grams, 0, 1)) {
				result++;
			}
			return result;
		}

		int result = -1;
		for (int i = index; i < sequence.length; i++) {
			swap(sequence, i, index);

			result = Math.max(result, search(R, G, sequence, index + 1));

			swap(sequence, i, index);
		}
		return result;
	}

	static void swap(int[] a, int index1, int index2) {
		int temp = a[index1];
		a[index1] = a[index2];
		a[index2] = temp;
	}

	static boolean consume(int[][] R, Map<Integer, Integer> metalToOrder, int[] grams, int metal, int amount) {
		if (amount <= grams[metal]) {
			grams[metal] -= amount;

			return true;
		}

		amount -= grams[metal];
		grams[metal] = 0;

		int r1 = R[metal][0];
		int r2 = R[metal][1];

		return metalToOrder.get(metal) < Math.min(metalToOrder.get(r1), metalToOrder.get(r2))
				&& consume(R, metalToOrder, grams, r1, amount) && consume(R, metalToOrder, grams, r2, amount);
	}
}