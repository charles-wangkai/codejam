import java.util.ArrayList;
import java.util.Arrays;
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
			int[] P = new int[N];
			for (int i = 0; i < P.length; i++) {
				P[i] = sc.nextInt();
			}

			System.out.println(String.format("Case #%d: %s", tc, solve(P).stream().collect(Collectors.joining(" "))));
		}

		sc.close();
	}

	static List<String> solve(int[] P) {
		List<String> result = new ArrayList<>();
		while (true) {
			int partyNum = (int) Arrays.stream(P).filter(x -> x != 0).count();

			if (partyNum == 0) {
				break;
			}

			StringBuilder instruction = new StringBuilder();
			if (partyNum == 2) {
				for (int i = 0; i < P.length; i++) {
					if (P[i] != 0) {
						instruction.append((char) ('A' + i));
						P[i]--;
					}
				}
			} else {
				int maxValue = Arrays.stream(P).max().getAsInt();
				int index = IntStream.range(0, P.length).filter(i -> P[i] == maxValue).findAny().getAsInt();

				instruction.append((char) ('A' + index));
				P[index]--;
			}

			result.add(instruction.toString());
		}
		return result;
	}
}
