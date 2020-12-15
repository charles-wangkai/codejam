import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int S = sc.nextInt();
			int[] D = new int[S];
			int[] A = new int[S];
			int[] B = new int[S];
			for (int i = 0; i < S; i++) {
				D[i] = sc.nextInt();
				A[i] = sc.nextInt();
				B[i] = sc.nextInt();
			}

			System.out.println(String.format("Case #%d: %s", tc, solve(D, A, B)));
		}

		sc.close();
	}

	static String solve(int[] D, int[] A, int[] B) {
		int S = D.length;

		int[] lefts = new int[S];
		int[] rights = new int[S];
		for (int i = 0; i < S; i++) {
			lefts[i] = D[i] + A[i];
			rights[i] = D[i] - B[i];
		}

		int maxLength = 1;
		Set<Range> maxLengthRanges = new HashSet<>();
		maxLengthRanges.add(new Range(0, 0));

		Candidate leftCandidate = new Candidate(lefts[0], Integer.MIN_VALUE, 0, 0);
		Candidate rightCandidate = new Candidate(rights[0], Integer.MIN_VALUE, 0, 0);
		for (int endIndex = 1; endIndex < S; endIndex++) {
			Candidate nextLeftCandidate = buildNextCandidate(leftCandidate, rightCandidate, endIndex, lefts[endIndex],
					rights[endIndex]);
			Candidate nextRightCandidate = buildNextCandidate(rightCandidate, leftCandidate, endIndex, rights[endIndex],
					lefts[endIndex]);

			leftCandidate = nextLeftCandidate;
			rightCandidate = nextRightCandidate;

			for (Candidate candidate : new Candidate[] { leftCandidate, rightCandidate }) {
				int length = endIndex - candidate.beginIndex + 1;
				if (length > maxLength) {
					maxLength = length;
					maxLengthRanges.clear();
					maxLengthRanges.add(new Range(candidate.beginIndex, endIndex));
				} else if (length == maxLength) {
					maxLengthRanges.add(new Range(candidate.beginIndex, endIndex));
				}
			}
		}

		return String.format("%d %d", maxLength, maxLengthRanges.size());
	}

	static Candidate buildNextCandidate(Candidate candidate1, Candidate candidate2, int index, int value1, int value2) {
		if (value1 == candidate1.value1) {
			return candidate1;
		} else if (value1 == candidate2.value2) {
			return new Candidate(value1, candidate2.value1, candidate2.beginIndex, index);
		} else {
			return new Candidate(value1, candidate2.value1, candidate2.xBeginIndex, index);
		}
	}
}

class Candidate {
	int value1;
	int value2;
	int beginIndex;
	int xBeginIndex;

	Candidate(int value1, int value2, int beginIndex, int xBeginIndex) {
		this.value1 = value1;
		this.value2 = value2;
		this.beginIndex = beginIndex;
		this.xBeginIndex = xBeginIndex;
	}
}

class Range {
	int beginIndex;
	int endIndex;

	Range(int beginIndex, int endIndex) {
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
	}

	@Override
	public int hashCode() {
		return Objects.hash(beginIndex, endIndex);
	}

	@Override
	public boolean equals(Object obj) {
		Range other = (Range) obj;
		return beginIndex == other.beginIndex && endIndex == other.endIndex;
	}
}