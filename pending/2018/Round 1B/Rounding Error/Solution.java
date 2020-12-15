import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int N = sc.nextInt();
			int L = sc.nextInt();
			int[] C = new int[L];
			for (int i = 0; i < C.length; i++) {
				C[i] = sc.nextInt();
			}

			System.out.println(String.format("Case #%d: %d", tc, solve(N, C)));
		}

		sc.close();
	}

	static int solve(int N, int[] C) {
		int result = 0;
		List<Element> elements = new ArrayList<>();
		for (int count : Arrays.copyOf(C, N)) {
			int oldPercentage = computePercentage(N, count);

			if (isRoundUp(N, count)) {
				result += oldPercentage;
			} else {
				boolean found = false;
				for (int i = oldPercentage + 1; i <= 100; i++) {
					int nextCount = divideToCeil(N * (i * 10 - 5), 1000);

					if (isRoundUp(N, nextCount)) {
						int distance = nextCount - count;
						int newPercentage = computePercentage(N, nextCount);
						elements.add(new Element(distance, oldPercentage, newPercentage));

						found = true;
						break;
					}
				}
				if (!found) {
					result += oldPercentage;
				}
			}
		}

		int remain = N - Arrays.stream(C).sum();
		Collections.sort(elements, (element1, element2) -> Integer.compare(element1.distance, element2.distance));

		for (Element element : elements) {
			if (element.distance <= remain) {
				remain -= element.distance;
				result += element.newPercentage;
			} else {
				result += element.oldPercentage;
			}
		}

		result += remain * 100 / N;

		return result;
	}

	static int computePercentage(int N, int x) {
		return (x * 1000 / N + 5) / 10;
	}

	static boolean isRoundUp(int N, int x) {
		return x * 1000 / N % 10 >= 5;
	}

	static int divideToCeil(int x, int y) {
		return x / y + (x % y == 0 ? 0 : 1);
	}
}

class Element {
	int distance;
	int oldPercentage;
	int newPercentage;

	Element(int distance, int oldPercentage, int newPercentage) {
		this.distance = distance;
		this.oldPercentage = oldPercentage;
		this.newPercentage = newPercentage;
	}
}