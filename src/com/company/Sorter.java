package com.company;

import java.util.List;
import java.util.stream.Collectors;

public class Sorter {
    private static final int limitTotal = 10;
    private static final int minimumPackageMatch = 3;
    private static final int minimumPackageDoesNotMatch = 3;

    public List<Main.Product> sortList(List<Main.Product> list) {
        List<Main.Product> sortedLimitedList =
                list.stream().limit(limitTotal).collect(Collectors.toList());

        boolean packageMatchesMinimumReached = sortedLimitedList.stream()
                                                                .filter(Main.Product::packageMatches)
                                                                .count() > minimumPackageMatch;
        boolean packageDoesNotMatchesMinimumReached = sortedLimitedList.stream()
                                                                       .filter(this::packageDoesNotMatch)
                                                                       .count() > minimumPackageDoesNotMatch;
        if (packageMatchesMinimumReached && packageDoesNotMatchesMinimumReached) {
            return sortedLimitedList;
        } else if (packageMatchesMinimumReached) {
            // find next package do not match

            // how much not matching to add?
            int numberNotMatchingToAdd =
                    minimumPackageDoesNotMatch - (int)sortedLimitedList.stream()
                                                                       .filter(this::packageDoesNotMatch)
                                                                       .count();
            final List<Main.Product> notMatchingToAdd;
            if (numberNotMatchingToAdd == minimumPackageDoesNotMatch) {
                // which not matching to add?
                notMatchingToAdd = list.stream()
                                       .filter(this::packageDoesNotMatch)
                                       .limit(minimumPackageDoesNotMatch)
                                       .collect(Collectors.toList());


            } else {
                // which not matching to add?
                notMatchingToAdd = list.stream()
                                       .filter(this::packageDoesNotMatch)
                                       .filter(element -> !sortedLimitedList.contains(element))
                                       .limit(numberNotMatchingToAdd)
                                       .collect(Collectors.toList());
            }

            // add next not matching
            sortedLimitedList.addAll(notMatchingToAdd);

            // remove too much matching
            deleteTooMuchMatching(sortedLimitedList);
        } else if (packageDoesNotMatchesMinimumReached) {
            // find next package that matches

            // how much matching to add?
            int numberMatchingToAdd =
                    minimumPackageMatch - (int)sortedLimitedList.stream()
                                                                .filter(Main.Product::packageMatches)
                                                                .count();
            final List<Main.Product> matchingToAdd;
            if (numberMatchingToAdd == minimumPackageMatch) {
                // which matching to add?
                matchingToAdd = list.stream()
                                    .filter(Main.Product::packageMatches)
                                    .limit(minimumPackageMatch)
                                    .collect(Collectors.toList());


            } else {
                // which matching to add?
                matchingToAdd = list.stream()
                                    .filter(Main.Product::packageMatches)
                                    .filter(element -> !sortedLimitedList.contains(element))
                                    .limit(numberMatchingToAdd)
                                    .collect(Collectors.toList());
            }

            // add next matchings
            sortedLimitedList.addAll(matchingToAdd);

            // remove too much not matching
            deleteTooMuchNotMatching(sortedLimitedList);
        } else {
            return sortedLimitedList;
        }

        return sortedLimitedList;
    }

    private void deleteTooMuchMatching(List<Main.Product> sortedLimitedList) {
        int indexOfNextMatchingToDelete = sortedLimitedList.size() - 1;
        while (sortedLimitedList.size() > limitTotal) {
            Main.Product element = sortedLimitedList.get(indexOfNextMatchingToDelete);
            if (packageDoesNotMatch(element)) {
                indexOfNextMatchingToDelete--;
            } else {
                sortedLimitedList.remove(indexOfNextMatchingToDelete);
            }
        }
    }

    private void deleteTooMuchNotMatching(List<Main.Product> sortedLimitedList) {
        int indexOfNextNotMatchingToDelete = sortedLimitedList.size() - 1;
        while (sortedLimitedList.size() > limitTotal) {
            Main.Product element = sortedLimitedList.get(indexOfNextNotMatchingToDelete);
            if (element.packageMatches()) {
                indexOfNextNotMatchingToDelete--;
            } else {
                sortedLimitedList.remove(indexOfNextNotMatchingToDelete);
            }
        }
    }

    private boolean packageDoesNotMatch(Main.Product value) {
        return !value.packageMatches();
    }
}
