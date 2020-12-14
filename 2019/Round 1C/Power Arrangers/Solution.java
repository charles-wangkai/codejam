import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			List<Integer> permutationIndices = IntStream.range(0, 119).boxed().collect(Collectors.toList());
			Set<Character> candidates = IntStream.range(0, 5).mapToObj(i -> (char) ('A' + i))
					.collect(Collectors.toSet());
			StringBuilder result = new StringBuilder();

			for (int i = 0; i < 5; i++) {
				Map<Character, List<Integer>> candidateToPermutationIndices = new HashMap<>();
				for (char candidate : candidates) {
					candidateToPermutationIndices.put(candidate, new ArrayList<>());
				}

				for (int permutationIndex : permutationIndices) {
					System.out.println(permutationIndex * 5 + i + 1);
					System.out.flush();

					char response = sc.next().charAt(0);
					if (response == 'N') {
						System.exit(0);
					}

					char candidate = response;
					candidateToPermutationIndices.get(candidate).add(permutationIndex);
				}

				char missing = candidates.stream()
						.min((candidate1, candidate2) -> Integer.compare(
								candidateToPermutationIndices.get(candidate1).size(),
								candidateToPermutationIndices.get(candidate2).size()))
						.get();
				result.append(missing);
				candidates.remove(missing);
				permutationIndices = candidateToPermutationIndices.get(missing);
			}

			System.out.println(result);
			System.out.flush();

			char verdict = sc.next().charAt(0);
			if (verdict == 'N') {
				System.exit(0);
			}
		}

		sc.close();
	}
}
