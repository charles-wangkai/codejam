import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Solution {
  static final int END = 10000;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int N = sc.nextInt();
      Offer[] offers = new Offer[N];
      for (int i = 0; i < offers.length; ++i) {
        String C = sc.next();
        int A = sc.nextInt();
        int B = sc.nextInt();

        offers[i] = new Offer(C, A, B);
      }

      System.out.println(String.format("Case #%d: %s", tc, solve(offers)));
    }

    sc.close();
  }

  static String solve(Offer[] offers) {
    Arrays.sort(offers, Comparator.comparing(offer -> offer.A));

    Map<String, List<Offer>> colorToOffers = new HashMap<>();
    for (Offer offer : offers) {
      if (!colorToOffers.containsKey(offer.C)) {
        colorToOffers.put(offer.C, new ArrayList<>());
      }
      colorToOffers.get(offer.C).add(offer);
    }

    List<String> colors = new ArrayList<>(colorToOffers.keySet());

    int minOfferNum = Integer.MAX_VALUE;
    for (int i = 0; i < colors.size(); ++i) {
      minOfferNum =
          Math.min(
              minOfferNum,
              computeOfferNum(List.of(colorToOffers.get(colors.get(i)), List.of(), List.of())));

      for (int j = i + 1; j < colors.size(); ++j) {
        minOfferNum =
            Math.min(
                minOfferNum,
                computeOfferNum(
                    List.of(
                        colorToOffers.get(colors.get(i)),
                        colorToOffers.get(colors.get(j)),
                        List.of())));

        for (int k = j + 1; k < colors.size(); ++k) {
          minOfferNum =
              Math.min(
                  minOfferNum,
                  computeOfferNum(
                      List.of(
                          colorToOffers.get(colors.get(i)),
                          colorToOffers.get(colors.get(j)),
                          colorToOffers.get(colors.get(k)))));
        }
      }
    }

    return (minOfferNum == Integer.MAX_VALUE) ? "IMPOSSIBLE" : String.valueOf(minOfferNum);
  }

  static int computeOfferNum(List<List<Offer>> offersList) {
    int[] indices = new int[offersList.size()];
    int last = 0;
    int offerNum = 0;
    while (true) {
      int nextLast = last;
      for (int i = 0; i < indices.length; ++i) {
        while (indices[i] != offersList.get(i).size()
            && offersList.get(i).get(indices[i]).A <= last + 1) {
          nextLast = Math.max(nextLast, offersList.get(i).get(indices[i]).B);
          ++indices[i];
        }
      }

      if (nextLast == last) {
        return Integer.MAX_VALUE;
      }

      last = nextLast;
      ++offerNum;
      if (last == END) {
        return offerNum;
      }
    }
  }
}

class Offer {
  String C;
  int A;
  int B;

  Offer(String C, int A, int B) {
    this.C = C;
    this.A = A;
    this.B = B;
  }
}
