import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Solution {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 1; tc <= N; ++tc) {
      int L = sc.nextInt();
      sc.nextLine();
      String[] lines = new String[L];
      for (int i = 0; i < lines.length; ++i) {
        lines[i] = sc.nextLine();
      }
      String definition = String.join("\n", lines);
      int A = sc.nextInt();
      Animal[] animals = new Animal[A];
      for (int i = 0; i < animals.length; ++i) {
        String name = sc.next();
        int n = sc.nextInt();
        String[] features = new String[n];
        for (int j = 0; j < features.length; ++j) {
          features[j] = sc.next();
        }

        animals[i] = new Animal(name, features);
      }

      System.out.println(String.format("Case #%d:\n%s", tc, solve(definition, animals)));
    }

    sc.close();
  }

  static String solve(String definition, Animal[] animals) {
    String[] tokens =
        Arrays.stream(definition.replace("(", " ( ").replace(")", " ) ").split("\\s"))
            .filter(x -> !x.isEmpty())
            .toArray(String[]::new);
    Node decisionTree = buildDecisionTree(tokens, 0, tokens.length - 1);

    return Arrays.stream(animals)
        .map(
            animal ->
                String.format(
                    "%.9f",
                    computeProbability(
                        decisionTree, Arrays.stream(animal.features).collect(Collectors.toSet()))))
        .collect(Collectors.joining("\n"));
  }

  static Node buildDecisionTree(String[] tokens, int beginIndex, int endIndex) {
    Node node = new Node(Double.parseDouble(tokens[beginIndex + 1]));
    if (beginIndex + 2 != endIndex) {
      node.feature = tokens[beginIndex + 2];

      int depth = 1;
      int index = beginIndex + 4;
      while (depth != 0) {
        if (tokens[index].equals("(")) {
          ++depth;
        } else if (tokens[index].equals(")")) {
          --depth;
        }

        ++index;
      }

      node.subTree1 = buildDecisionTree(tokens, beginIndex + 3, index - 1);
      node.subTree2 = buildDecisionTree(tokens, index, endIndex - 1);
    }

    return node;
  }

  static double computeProbability(Node decisionTree, Set<String> features) {
    double result = decisionTree.weight;
    if (decisionTree.feature != null) {
      result *=
          computeProbability(
              features.contains(decisionTree.feature)
                  ? decisionTree.subTree1
                  : decisionTree.subTree2,
              features);
    }

    return result;
  }
}

class Node {
  double weight;
  String feature;
  Node subTree1;
  Node subTree2;

  Node(double weight) {
    this.weight = weight;
  }
}

class Animal {
  String name;
  String[] features;

  Animal(String name, String[] features) {
    this.name = name;
    this.features = features;
  }
}
