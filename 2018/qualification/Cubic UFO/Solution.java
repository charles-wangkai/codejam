import java.util.Scanner;
import java.util.stream.IntStream;

public class Solution {
	static final double TOLERANCE = 1e-9;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int tc = 1; tc <= T; tc++) {
			double A = sc.nextDouble();

			System.out.println(String.format("Case #%d:\n%s", tc, solve(A)));
		}

		sc.close();
	}

	static String solve(double A) {
		Point upCenter = new Point(0, 0.5, 0);
		Point frontLeftCenter = new Point(-Math.sqrt(2) / 4, 0, Math.sqrt(2) / 4);
		Point frontRightCenter = new Point(Math.sqrt(2) / 4, 0, Math.sqrt(2) / 4);

		double lowerAngle = 0;
		double upperAngle = Math.atan(Math.sqrt(2));
		while (true) {
			double middleAngle = (lowerAngle + upperAngle) / 2;

			Point upCenterRotated = upCenter.rotateAboutZ(middleAngle);
			Point frontLeftCenterRotated = frontLeftCenter.rotateAboutZ(middleAngle);
			Point frontRightCenterRotated = frontRightCenter.rotateAboutZ(middleAngle);

			Point[] vertices = buildVertices(upCenterRotated, frontLeftCenterRotated, frontRightCenterRotated);
			double area = computeArea(vertices);

			if (Math.abs(area - A) < TOLERANCE) {
				return String.format("%s\n%s\n%s", upCenterRotated.toString(), frontLeftCenterRotated.toString(),
						frontRightCenterRotated.toString());
			} else if (area < A) {
				lowerAngle = middleAngle;
			} else {
				upperAngle = middleAngle;
			}
		}
	}

	static Point[] buildVertices(Point upCenterRotated, Point frontLeftCenterRotated, Point frontRightCenterRotated) {
		Point center = new Point(0, 0, 0);

		return new Point[] {
				center.subtract(upCenterRotated).subtract(frontLeftCenterRotated).add(frontRightCenterRotated),
				center.subtract(upCenterRotated).add(frontLeftCenterRotated).add(frontRightCenterRotated),
				center.add(upCenterRotated).add(frontLeftCenterRotated).add(frontRightCenterRotated),
				center.add(upCenterRotated).add(frontLeftCenterRotated).subtract(frontRightCenterRotated),
				center.add(upCenterRotated).subtract(frontLeftCenterRotated).subtract(frontRightCenterRotated),
				center.subtract(upCenterRotated).subtract(frontLeftCenterRotated).subtract(frontRightCenterRotated) };
	}

	static double computeArea(Point[] vertices) {
		return Math.abs(IntStream.range(0, vertices.length)
				.mapToDouble(i -> vertices[i].x * vertices[(i + 1) % vertices.length].z
						- vertices[i].z * vertices[(i + 1) % vertices.length].x)
				.sum() / 2);
	}
}

class Point {
	double x;
	double y;
	double z;

	Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public String toString() {
		return String.format("%.10f %.10f %.10f", x, y, z);
	}

	Point add(Point p) {
		return new Point(x + p.x, y + p.y, z + p.z);
	}

	Point subtract(Point p) {
		return new Point(x - p.x, y - p.y, z - p.z);
	}

	Point rotateAboutZ(double angle) {
		return new Point(Math.cos(angle) * x - Math.sin(angle) * y, Math.sin(angle) * x + Math.cos(angle) * y, z);
	}
}