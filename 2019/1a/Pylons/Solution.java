import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Solution {
	static final int SEARCH_COUNT_LIMIT = 1000;

	static int searchCount;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			int R = sc.nextInt();
			int C = sc.nextInt();

			System.out.println(String.format("Case #%d: %s", tc, solve(R, C)));
		}

		sc.close();
	}

	static String solve(int R, int C) {
		List<Point> points = new ArrayList<>();
		for (int r = 1; r <= R; r++) {
			for (int c = 1; c <= C; c++) {
				points.add(new Point(r, c));
			}
		}

		while (true) {
			Collections.shuffle(points);

			searchCount = 0;
			if (search(points, 0)) {
				return String.format("POSSIBLE\n%s", points.stream()
						.map(point -> String.format("%d %d", point.r, point.c)).collect(Collectors.joining("\n")));
			} else if (searchCount <= SEARCH_COUNT_LIMIT) {
				return "IMPOSSIBLE";
			}
		}
	}

	static boolean search(List<Point> points, int index) {
		searchCount++;

		if (searchCount > SEARCH_COUNT_LIMIT) {
			return false;
		}

		if (index == points.size()) {
			return true;
		}

		for (int i = index; i < points.size(); i++) {
			if (index == 0 || (points.get(index - 1).r != points.get(i).r && points.get(index - 1).c != points.get(i).c
					&& points.get(index - 1).r - points.get(index - 1).c != points.get(i).r - points.get(i).c
					&& points.get(index - 1).r + points.get(index - 1).c != points.get(i).r + points.get(i).c)) {
				swap(points, i, index);

				if (search(points, index + 1)) {
					return true;
				}

				swap(points, i, index);
			}
		}

		return false;
	}

	static void swap(List<Point> points, int index1, int index2) {
		Point temp = points.get(index1);
		points.set(index1, points.get(index2));
		points.set(index2, temp);
	}
}

class Point {
	int r;
	int c;

	Point(int r, int c) {
		this.r = r;
		this.c = c;
	}
}