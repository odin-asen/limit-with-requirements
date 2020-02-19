package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    static class Product {
        boolean packageMatch;

        boolean packageMatches() {
            return packageMatch;
        }

        @Override
        public String toString() {
            return packageMatch ? "1" : "0";
        }
    }

    public static void main(String[] args) {
        List<Product> listWithMoreMatching = createShuffledListWithMoreMatchingThanNotMatching();
        List<Product> listWithMoreNotMatching = createShuffledListWithMoreNotMatchingThanMatching();
        List<Product> listWithLessThanLimit = createShuffledListWithLessThanLimitButMatchingCriteria();
        List<Product> listWithLessThanLimitAndLessThanMinimums = createShuffledListWithTooLessProductsMatchingCriteria();

        System.out.println("more matching");
        testList(listWithMoreMatching);
        System.out.println("more not matching");
        testList(listWithMoreNotMatching);
        System.out.println("less than limit but satisfying minimums");
        testList(listWithLessThanLimit);
        System.out.println("less than limit and less than minimum");
        testList(listWithLessThanLimitAndLessThanMinimums);
    }

    private static void testList(List<Product> testList) {
        System.out.println(testList);
        List<Product> sortedList = new Sorter().sortList(testList);
        System.out.println(sortedList);
        boolean hasAtLeastOfEach = sortedList.stream().filter(Product::packageMatches).count() >= 3 && sortedList.stream()
                                                                                                                 .filter(Main::packageDoesNotMatch)
                                                                                                                 .count() >= 3;
        System.out.println("has at least 3 of each: " + hasAtLeastOfEach);
        System.out.println("========================");
    }

    private static List<Product> createShuffledListWithMoreMatchingThanNotMatching() {
        List<Product> list = new ArrayList<>();
        list.addAll(createProductsWithMatching(20, true));
        list.addAll(createProductsWithMatching(5, false));
        Collections.shuffle(list);
        return list;
    }

    private static List<Product> createProductsWithMatching(int numberOfProducts, boolean match) {
        List<Product> packageMatch = new ArrayList<>();

        for (int i = 0; i < numberOfProducts; i++) {
            final Product product = new Product();
            product.packageMatch = match;
            packageMatch.add(product);
        }

        return packageMatch;
    }

    private static List<Product> createShuffledListWithMoreNotMatchingThanMatching() {
        List<Product> list = new ArrayList<>();
        list.addAll(createProductsWithMatching(5, true));
        list.addAll(createProductsWithMatching(20, false));
        Collections.shuffle(list);
        return list;
    }

    private static final int minimumPackageMatch = 3;
    private static final int minimumPackageDoesNotMatch = 3;

    private static List<Product> createShuffledListWithLessThanLimitButMatchingCriteria() {
        List<Product> list = new ArrayList<>();
        list.addAll(createProductsWithMatching(minimumPackageMatch, false));
        list.addAll(createProductsWithMatching(minimumPackageDoesNotMatch, true));
        Collections.shuffle(list);
        return list;
    }
    private static List<Product> createShuffledListWithTooLessProductsMatchingCriteria() {
        List<Product> list = new ArrayList<>();
        list.addAll(createProductsWithMatching(minimumPackageMatch - 1, false));
        list.addAll(createProductsWithMatching(minimumPackageDoesNotMatch - 1, true));
        Collections.shuffle(list);
        return list;
    }

    private static boolean packageDoesNotMatch(Product value) {
        return !value.packageMatches();
    }
}
