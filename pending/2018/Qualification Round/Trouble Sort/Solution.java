import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int N = sc.nextInt();
			int[] V = new int[N];
			for (int i = 0; i < V.length; i++) {
				V[i] = sc.nextInt();
			}

			System.out.println(String.format("Case #%d: %s", tc, solve(V)));
		}

		sc.close();
	}

	static String solve(int[] V) {
		List<Integer> evenPositionedValues = new ArrayList<>();
		List<Integer> oddPositionedValues = new ArrayList<>();
		for (int i = 0; i < V.length; i++) {
			if (i % 2 == 0) {
				evenPositionedValues.add(V[i]);
			} else {
				oddPositionedValues.add(V[i]);
			}
		}

		Collections.sort(evenPositionedValues);
		Collections.sort(oddPositionedValues);

		for (int i = 0; i < V.length - 1; i++) {
			if (getSortedValue(evenPositionedValues, oddPositionedValues, i) > getSortedValue(evenPositionedValues,
					oddPositionedValues, i + 1)) {
				return String.valueOf(i);
			}
		}

		return "OK";
	}

	static int getSortedValue(List<Integer> evenPositionedValues, List<Integer> oddPositionedValues, int index) {
		return ((index % 2 == 0) ? evenPositionedValues : oddPositionedValues).get(index / 2);
	}
}
