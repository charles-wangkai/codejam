import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int N = sc.nextInt();
			int[][] A = new int[N][N];
			for (int r = 0; r < N; r++) {
				for (int c = 0; c < N; c++) {
					A[r][c] = sc.nextInt();
				}
			}

			System.out.println(String.format("Case #%d: %d", tc, solve(A)));
		}

		sc.close();
	}

	static int solve(int[][] A) {
		int N = A.length;

		return IntStream.rangeClosed(-N, N).map(value -> computeChangeNum(A, value)).sum();
	}

	static int computeChangeNum(int[][] A, int value) {
		int N = A.length;

		Vertex[] leftVertices = initVertices(N);
		Vertex[] rightVertices = initVertices(N);

		int allCount = 0;
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				if (A[r][c] == value) {
					leftVertices[r].adjacents.add(c);
					rightVertices[c].adjacents.add(r);

					allCount++;
				}
			}
		}

		int matchingCount = 0;
		for (int i = 0; i < leftVertices.length; i++) {
			if (leftVertices[i].matching == -1 && search(leftVertices, rightVertices, new boolean[N], i)) {
				matchingCount++;
			}
		}

		return allCount - matchingCount;
	}

	static boolean search(Vertex[] leftVertices, Vertex[] rightVertices, boolean[] rightVisited, int leftIndex) {
		for (int rightIndex : leftVertices[leftIndex].adjacents) {
			if (!rightVisited[rightIndex]) {
				rightVisited[rightIndex] = true;

				if (rightVertices[rightIndex].matching == -1
						|| search(leftVertices, rightVertices, rightVisited, rightVertices[rightIndex].matching)) {
					leftVertices[leftIndex].matching = rightIndex;
					rightVertices[rightIndex].matching = leftIndex;

					return true;
				}
			}
		}

		return false;
	}

	static Vertex[] initVertices(int N) {
		Vertex[] vertices = new Vertex[N];
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] = new Vertex();
		}
		return vertices;
	}
}

class Vertex {
	List<Integer> adjacents = new ArrayList<>();
	int matching = -1;
}
