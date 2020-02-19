package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        List<Product> sortedList = sortList(testList);
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

    private static final int limitTotal = 10;
    private static final int minimumPackageMatch = 3;
    private static final int minimumPackageDoesNotMatch = 3;

    private static List<Product> sortList(List<Product> list) {
        List<Product> sortedLimitedList =
                list.stream().limit(limitTotal).collect(Collectors.toList());

        boolean packageMatchesMinimumReached = sortedLimitedList.stream()
                                                                .filter(Product::packageMatches)
                                                                .count() > minimumPackageMatch;
        boolean packageDoesNotMatchesMinimumReached = sortedLimitedList.stream()
                                                                       .filter(Main::packageDoesNotMatch)
                                                                       .count() > minimumPackageDoesNotMatch;
        if (packageMatchesMinimumReached && packageDoesNotMatchesMinimumReached) {
            return sortedLimitedList;
        } else if (packageMatchesMinimumReached) {
            // find next package do not match

            // how much not matching to add?
            int numberNotMatchingToAdd =
                    minimumPackageDoesNotMatch - (int)sortedLimitedList.stream()
                                                                       .filter(Main::packageDoesNotMatch)
                                                                       .count();
            final List<Product> notMatchingToAdd;
            if (numberNotMatchingToAdd == minimumPackageDoesNotMatch) {
                // which not matching to add?
                notMatchingToAdd = list.stream()
                                       .filter(Main::packageDoesNotMatch)
                                       .limit(minimumPackageDoesNotMatch)
                                       .collect(Collectors.toList());


            } else {
                // which not matching to add?
                notMatchingToAdd = list.stream()
                                       .filter(Main::packageDoesNotMatch)
                                       .filter(element -> !sortedLimitedList.contains(element))
                                       .limit(numberNotMatchingToAdd)
                                       .collect(Collectors.toList());
            }

            // add next not matching
            sortedLimitedList.addAll(notMatchingToAdd);

            System.out.println(sortedLimitedList);

            // remove too much matching
            deleteTooMuchMatching(sortedLimitedList);
        } else if (packageDoesNotMatchesMinimumReached) {
            // find next package that matches

            // how much matching to add?
            int numberMatchingToAdd =
                    minimumPackageMatch - (int)sortedLimitedList.stream()
                                                                .filter(Product::packageMatches)
                                                                .count();
            final List<Product> matchingToAdd;
            if (numberMatchingToAdd == minimumPackageMatch) {
                // which matching to add?
                matchingToAdd = list.stream()
                                    .filter(Product::packageMatches)
                                    .limit(minimumPackageMatch)
                                    .collect(Collectors.toList());


            } else {
                // which matching to add?
                matchingToAdd = list.stream()
                                    .filter(Product::packageMatches)
                                    .filter(element -> !sortedLimitedList.contains(element))
                                    .limit(numberMatchingToAdd)
                                    .collect(Collectors.toList());
            }

            // add next matchings
            sortedLimitedList.addAll(matchingToAdd);

            System.out.println(sortedLimitedList);

            // remove too much not matching
            deleteTooMuchNotMatching(sortedLimitedList);
        } else {
            return sortedLimitedList;
        }

        return sortedLimitedList;
    }

    private static void deleteTooMuchMatching(List<Product> sortedLimitedList) {
        int indexOfNextMatchingToDelete = sortedLimitedList.size() - 1;
        while (sortedLimitedList.size() > limitTotal) {
            Product element = sortedLimitedList.get(indexOfNextMatchingToDelete);
            if (packageDoesNotMatch(element)) {
                indexOfNextMatchingToDelete--;
            } else {
                sortedLimitedList.remove(indexOfNextMatchingToDelete);
            }
        }
    }

    private static void deleteTooMuchNotMatching(List<Product> sortedLimitedList) {
        int indexOfNextNotMatchingToDelete = sortedLimitedList.size() - 1;
        while (sortedLimitedList.size() > limitTotal) {
            Product element = sortedLimitedList.get(indexOfNextNotMatchingToDelete);
            if (element.packageMatches()) {
                indexOfNextNotMatchingToDelete--;
            } else {
                sortedLimitedList.remove(indexOfNextNotMatchingToDelete);
            }
        }
    }

    private static boolean packageDoesNotMatch(Product value) {
        return !value.packageMatches();
    }
}
