import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Solution {
	static int result;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int N = sc.nextInt();
			String[] W = new String[N];
			for (int i = 0; i < W.length; i++) {
				W[i] = sc.next();
			}

			System.out.println(String.format("Case #%d: %d", tc, solve(W)));
		}

		sc.close();
	}

	static int solve(String[] W) {
		TrieNode root = new TrieNode((char) -1);
		for (String word : W) {
			addWord(root, word, word.length() - 1);
		}

		result = 0;
		search(root, true);
		return result;
	}

	static int search(TrieNode node, boolean isRoot) {
		int remain = node.letterToChild.containsKey(null) ? 1 : 0;

		for (TrieNode child : node.letterToChild.values()) {
			if (child != null) {
				remain += search(child, false);
			}
		}

		if (!isRoot && remain >= 2) {
			result += 2;
			remain -= 2;
		}

		return remain;
	}

	static void addWord(TrieNode node, String word, int index) {
		if (index == -1) {
			node.letterToChild.put(null, null);

			return;
		}

		char letter = word.charAt(index);
		if (!node.letterToChild.containsKey(letter)) {
			node.letterToChild.put(letter, new TrieNode(letter));
		}

		addWord(node.letterToChild.get(letter), word, index - 1);
	}
}

class TrieNode {
	char letter;
	Map<Character, TrieNode> letterToChild = new HashMap<>();

	TrieNode(char letter) {
		this.letter = letter;
	}
}