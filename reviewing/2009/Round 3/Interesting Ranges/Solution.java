// https://github.com/KirarinSnow/Google-Code-Jam/blob/master/Round%203%202009/D.py

import java.math.BigInteger;
import java.util.Scanner;

public class Solution {
  static final int MODULUS = 1_000_000_007;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      BigInteger L = sc.nextBigInteger();
      BigInteger R = sc.nextBigInteger();

      System.out.println(String.format("Case #%d: %d", tc, solve(L, R)));
    }

    sc.close();
  }

  static int solve(BigInteger L, BigInteger R) {
    BigInteger zeroCount = countZero(L.subtract(BigInteger.ONE), R);
    BigInteger oneCount =
        R.subtract(L.subtract(BigInteger.ONE)).add(BigInteger.ONE).subtract(zeroCount);

    return zeroCount
        .multiply(zeroCount.subtract(BigInteger.ONE))
        .divide(BigInteger.TWO)
        .add(oneCount.multiply(oneCount.subtract(BigInteger.ONE)).divide(BigInteger.TWO))
        .mod(BigInteger.valueOf(MODULUS))
        .intValue();
  }

  static BigInteger countZero(BigInteger left, BigInteger right) {
    return countPrefixZero(right).subtract(countPrefixZero(left.subtract(BigInteger.ONE)));
  }

  static BigInteger countPrefixZero(BigInteger x) {
    BigInteger result = BigInteger.ZERO;
    int digitNum = computeDigitNum(x);
    for (int i = 0; i < digitNum; ++i) {
      result = result.add(countTotalPrefixZero(i));
    }
    result = result.add(countSuffixZero(x));

    return result;
  }

  static BigInteger countSuffixZero(BigInteger x) {
    int digitNum = computeDigitNum(x);
    if (digitNum == 0) {
      return BigInteger.ZERO;
    } else if (digitNum == 1) {
      return x.add(BigInteger.ONE).divide(BigInteger.TWO);
    } else if (digitNum == 2) {
      if (x.equals(BigInteger.TEN)) {
        return BigInteger.ONE;
      }

      return BigInteger.ONE
          .add(
              BigInteger.valueOf(11)
                  .multiply(
                      x.divide(BigInteger.valueOf(11))
                          .subtract(BigInteger.ONE)
                          .divide(BigInteger.TWO)))
          .add(
              x.divide(BigInteger.valueOf(11)).mod(BigInteger.TWO).equals(BigInteger.ZERO)
                  ? x.mod(BigInteger.valueOf(11)).add(BigInteger.ONE)
                  : BigInteger.ZERO);
    }

    String halfStr = x.toString().substring(0, (digitNum + 1) / 2);
    BigInteger lastPalindrome =
        new BigInteger(halfStr + new StringBuilder(halfStr).reverse().substring(digitNum % 2));
    if (x.compareTo(lastPalindrome) < 0) {
      halfStr = new BigInteger(halfStr).subtract(BigInteger.ONE).toString();
      lastPalindrome =
          new BigInteger(halfStr + new StringBuilder(halfStr).reverse().substring(digitNum % 2));
    }

    BigInteger halfValue = new BigInteger(halfStr);
    BigInteger minHalf = BigInteger.TEN.pow((digitNum - 1) / 2);
    BigInteger result =
        halfValue
            .subtract(minHalf)
            .add(BigInteger.ONE)
            .divide(BigInteger.TWO)
            .multiply(computeBlockLength(digitNum));
    if (halfValue.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
      result = result.add(x.subtract(lastPalindrome).add(BigInteger.ONE));
    }

    return result;
  }

  static int computeDigitNum(BigInteger x) {
    if (x.compareTo(BigInteger.ZERO) <= 0) {
      return 0;
    }

    return 1 + computeDigitNum(x.divide(BigInteger.TEN));
  }

  static BigInteger countTotalPrefixZero(int digitNum) {
    if (digitNum == 0) {
      return BigInteger.ZERO;
    } else if (digitNum == 1) {
      return BigInteger.valueOf(5);
    } else if (digitNum == 2) {
      return BigInteger.valueOf(45);
    }

    BigInteger blockNum =
        BigInteger.valueOf(9)
            .multiply(BigInteger.TEN.pow((digitNum - 1) / 2))
            .divide(BigInteger.TWO);

    return blockNum.multiply(computeBlockLength(digitNum));
  }

  static BigInteger computeBlockLength(int digitNum) {
    return BigInteger.valueOf((digitNum % 2 == 0) ? 11 : 1)
        .multiply(BigInteger.TEN.pow((digitNum - 1) / 2));
  }
}
