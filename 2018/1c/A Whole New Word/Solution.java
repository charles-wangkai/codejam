import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int N = sc.nextInt();
			sc.nextInt();
			String[] words = new String[N];
			for (int i = 0; i < words.length; i++) {
				words[i] = sc.next();
			}

			System.out.println(String.format("Case #%d: %s", tc, solve(words)));
		}

		sc.close();
	}

	static String solve(String[] words) {
		Set<String> wordSet = Arrays.stream(words).collect(Collectors.toSet());

		@SuppressWarnings("unchecked")
		List<Character>[] columnLetterLists = new List[words[0].length()];
		for (int i = 0; i < columnLetterLists.length; i++) {
			columnLetterLists[i] = buildColumnLetterList(words, i);
		}

		String newWord = search(wordSet, columnLetterLists, new StringBuilder());
		return (newWord == null) ? "-" : newWord;
	}

	static List<Character> buildColumnLetterList(String[] words, int c) {
		return IntStream.range(0, words.length).mapToObj(r -> words[r].charAt(c)).distinct()
				.collect(Collectors.toList());
	}

	static String search(Set<String> wordSet, List<Character>[] columnLetterLists, StringBuilder word) {
		if (word.length() == columnLetterLists.length) {
			if (wordSet.contains(word.toString())) {
				return null;
			} else {
				return word.toString();
			}
		}

		for (char columnLetter : columnLetterLists[word.length()]) {
			word.append(columnLetter);

			String subResult = search(wordSet, columnLetterLists, word);
			if (subResult != null) {
				return subResult;
			}

			word.deleteCharAt(word.length() - 1);
		}

		return null;
	}
}
