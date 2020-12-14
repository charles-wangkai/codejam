import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Solution {
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
		Point[] points = new Point[R * C];
		int index = 0;
		for (int r = 1; r <= R; r++) {
			for (int c = 1; c <= C; c++) {
				points[index] = new Point(r, c);
				index++;
			}
		}

		if (search(points, 0)) {
			return String.format("POSSIBLE\n%s", Arrays.stream(points)
					.map(point -> String.format("%d %d", point.r, point.c)).collect(Collectors.joining("\n")));
		} else {
			return "IMPOSSIBLE";
		}
	}

	static boolean search(Point[] points, int index) {
		if (index == points.length) {
			return true;
		}

		for (int i = index; i < points.length; i++) {
			if (index == 0 || (points[index - 1].r != points[i].r && points[index - 1].c != points[i].c
					&& points[index - 1].r - points[index - 1].c != points[i].r - points[i].c
					&& points[index - 1].r + points[index - 1].c != points[i].r + points[i].c)) {
				swap(points, i, index);

				if (search(points, index + 1)) {
					return true;
				}

				swap(points, i, index);
			}
		}

		return false;
	}

	static void swap(Point[] points, int index1, int index2) {
		Point temp = points[index1];
		points[index1] = points[index2];
		points[index2] = temp;
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
