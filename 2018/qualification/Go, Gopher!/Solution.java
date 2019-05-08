import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Solution {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			sc.nextInt();

			int r = 2;
			int c = 2;
			Set<Point> prepared = new HashSet<>();
			while (true) {
				System.out.println(String.format("%d %d", r, c));
				System.out.flush();

				int actualR = sc.nextInt();
				int actualC = sc.nextInt();

				if (actualR == 0 && actualC == 0) {
					break;
				}
				if (actualR == -1 && actualC == -1) {
					System.exit(0);
				}

				prepared.add(new Point(actualR, actualC));
				if (prepared.size() == 9) {
					prepared.clear();
					c += 3;
				}
			}
		}

		sc.close();
	}
}

class Point {
	int r;
	int c;

	public Point(int r, int c) {
		super();
		this.r = r;
		this.c = c;
	}

	@Override
	public int hashCode() {
		return Objects.hash(r, c);
	}

	@Override
	public boolean equals(Object obj) {
		Point other = (Point) obj;
		return r == other.r && c == other.c;
	}
}