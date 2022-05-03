import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 1; tc <= N; ++tc) {
      int T = sc.nextInt();
      int NA = sc.nextInt();
      int NB = sc.nextInt();
      sc.nextLine();
      String[] aTrips = new String[NA];
      for (int i = 0; i < aTrips.length; ++i) {
        aTrips[i] = sc.nextLine();
      }
      String[] bTrips = new String[NB];
      for (int i = 0; i < bTrips.length; ++i) {
        bTrips[i] = sc.nextLine();
      }

      System.out.println(String.format("Case #%d: %s", tc, solve(T, aTrips, bTrips)));
    }

    sc.close();
  }

  static String solve(int T, String[] aTrips, String[] bTrips) {
    List<Event> events = new ArrayList<>();
    for (String aTrip : aTrips) {
      String[] parts = aTrip.split(" ");
      int departureTime = convertToTime(parts[0]);
      int arrivalTime = convertToTime(parts[1]);

      events.add(new Event(departureTime, true, true));
      events.add(new Event(arrivalTime + T, false, false));
    }
    for (String bTrip : bTrips) {
      String[] parts = bTrip.split(" ");
      int departureTime = convertToTime(parts[0]);
      int arrivalTime = convertToTime(parts[1]);

      events.add(new Event(departureTime, true, false));
      events.add(new Event(arrivalTime + T, false, true));
    }
    Collections.sort(
        events,
        Comparator.comparing((Event event) -> event.time)
            .thenComparing(event -> event.isDeparture));

    int aStart = 0;
    int bStart = 0;
    int aRest = 0;
    int bRest = 0;
    for (Event event : events) {
      if (event.aOrB) {
        if (event.isDeparture) {
          if (aRest == 0) {
            ++aStart;
          } else {
            --aRest;
          }
        } else {
          ++aRest;
        }
      } else {
        if (event.isDeparture) {
          if (bRest == 0) {
            ++bStart;
          } else {
            --bRest;
          }
        } else {
          ++bRest;
        }
      }
    }

    return String.format("%d %d", aStart, bStart);
  }

  static int convertToTime(String s) {
    int hour = Integer.parseInt(s.substring(0, 2));
    int minute = Integer.parseInt(s.substring(3));

    return hour * 60 + minute;
  }
}

class Event {
  int time;
  boolean isDeparture;
  boolean aOrB;

  Event(int time, boolean isDeparture, boolean aOrB) {
    this.time = time;
    this.isDeparture = isDeparture;
    this.aOrB = aOrB;
  }
}