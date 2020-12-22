import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
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
    List<Trip> trips = new ArrayList<>();
    for (String aTrip : aTrips) {
      String[] parts = aTrip.split(" ");
      int departure = convertToTime(parts[0]);
      int arrival = convertToTime(parts[1]);

      trips.add(new Trip(true, departure, arrival));
    }
    for (String bTrip : bTrips) {
      String[] parts = bTrip.split(" ");
      int departure = convertToTime(parts[0]);
      int arrival = convertToTime(parts[1]);

      trips.add(new Trip(false, departure, arrival));
    }
    Collections.sort(trips, Comparator.comparing(trip -> trip.departure));

    int aTrainCount = 0;
    int bTrainCount = 0;
    PriorityQueue<Integer> aArrivals = new PriorityQueue<>();
    PriorityQueue<Integer> bArrivals = new PriorityQueue<>();
    for (Trip trip : trips) {
      if (trip.aOrB) {
        if (!aArrivals.isEmpty() && aArrivals.peek() + T <= trip.departure) {
          aArrivals.poll();
        } else {
          ++aTrainCount;
        }

        bArrivals.offer(trip.arrival);
      } else {
        if (!bArrivals.isEmpty() && bArrivals.peek() + T <= trip.departure) {
          bArrivals.poll();
        } else {
          ++bTrainCount;
        }

        aArrivals.offer(trip.arrival);
      }
    }

    return String.format("%d %d", aTrainCount, bTrainCount);
  }

  static int convertToTime(String s) {
    int hour = Integer.parseInt(s.substring(0, 2));
    int minute = Integer.parseInt(s.substring(3));

    return hour * 60 + minute;
  }
}

class Trip {
  boolean aOrB;
  int departure;
  int arrival;

  Trip(boolean aOrB, int departure, int arrival) {
    this.aOrB = aOrB;
    this.departure = departure;
    this.arrival = arrival;
  }
}
