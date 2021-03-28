import java.io.File;
import java.util.Scanner;

public class Judge {
  public static void main(String[] args) throws Throwable {
    Scanner expectedSc = new Scanner(new File("expected.txt"));
    Scanner actualSc = new Scanner(new File("out.txt"));

    int correctCount = 0;
    while (expectedSc.hasNextLine()) {
      if (expectedSc.nextLine().equals(actualSc.nextLine())) {
        ++correctCount;
      }
    }
    System.out.println("correctCount: " + correctCount);

    expectedSc.close();
    actualSc.close();
  }
}
