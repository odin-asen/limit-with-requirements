package com.company;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Sorter {
    private static final int limitTotal = 10;
    private static final int minimumPackageMatch = 3;
    private static final int minimumPackageDoesNotMatch = 3;

    public List<Main.Product> sortList(List<Main.Product> originalList) {
        if (originalList.size() <= limitTotal) {
            return originalList;
        } else {
            List<Main.Product> limitedList = originalList.stream().limit(limitTotal).collect(Collectors.toList());

            if (listMatchesMinimumRequirements(limitedList)) {
                return limitedList;
            } else {

                final int desiredMinimumNumberOfElements = determineMinimumValueForReorderingList(limitedList);
                final Predicate<Main.Product> predicate = determineFilterPredicateForList(limitedList);

                // how much matching predicate to add?
                final List<Main.Product> matchingPredicateToAdd =
                        getProductsToAddThatMatchPredicate(originalList, limitedList, desiredMinimumNumberOfElements, predicate);

                // add next matchings
                limitedList.addAll(matchingPredicateToAdd);

                // remove too much matching predicate from end of list
                deleteElementsNotMatchingPredicateUntilLimitTotal(limitedList, predicate);
            }

            return limitedList;
        }
    }

    private boolean listMatchesMinimumRequirements(List<Main.Product> list) {
        boolean hasMinimumAmountOfPackagesThatMatch = listHasMinimumAmountOfProductsMatchingPackage(list);
        boolean hasMinimumAmountOfPackagesThatDoNotMatch = list.stream()
                                                               .filter(this::packageDoesNotMatch)
                                                               .count() > minimumPackageDoesNotMatch;

        return hasMinimumAmountOfPackagesThatMatch && hasMinimumAmountOfPackagesThatDoNotMatch;
    }

    private Predicate<Main.Product> determineFilterPredicateForList(List<Main.Product> list) {
        boolean packageMatchesMinimumReached = listHasMinimumAmountOfProductsMatchingPackage(list);
        if (packageMatchesMinimumReached) {
            return this::packageDoesNotMatch;
        } else {
            return Main.Product::packageMatches;
        }
    }

    private int determineMinimumValueForReorderingList(List<Main.Product> list) {
        boolean packageMatchesMinimumReached = listHasMinimumAmountOfProductsMatchingPackage(list);
        if (packageMatchesMinimumReached) {
            return minimumPackageDoesNotMatch;
        } else {
            return minimumPackageMatch;
        }
    }

    private boolean listHasMinimumAmountOfProductsMatchingPackage(List<Main.Product> limitedList) {
        return limitedList.stream()
                          .filter(Main.Product::packageMatches)
                          .count() > minimumPackageMatch;
    }

    private List<Main.Product> getProductsToAddThatMatchPredicate(List<Main.Product> originalList,
                                                                  List<Main.Product> limitedList,
                                                                  int minimumOfElementsToBeInSortedList,
                                                                  Predicate<Main.Product> filterPredicateOfMatchingProduct) {
        int numberMatchingToAdd =
                minimumOfElementsToBeInSortedList - (int)limitedList.stream()
                                                                                 .filter(filterPredicateOfMatchingProduct)
                                                                                 .count();

        // which matching to add?
        return originalList.stream()
                           .filter(filterPredicateOfMatchingProduct)
                           .filter(element -> !limitedList.contains(element))
                           .limit(numberMatchingToAdd)
                           .collect(Collectors.toList());
    }

    private void deleteElementsNotMatchingPredicateUntilLimitTotal(List<Main.Product> sortedLimitedList,
                                                                   Predicate<Main.Product> predicate) {
        int indexOfNextElementNotMatchingPredicateToDelete = sortedLimitedList.size() - 1;
        while (sortedLimitedList.size() > limitTotal) {
            Main.Product element = sortedLimitedList.get(indexOfNextElementNotMatchingPredicateToDelete);
            if (predicate.test(element)) {
                indexOfNextElementNotMatchingPredicateToDelete--;
            } else {
                sortedLimitedList.remove(indexOfNextElementNotMatchingPredicateToDelete);
            }
        }
    }

    private boolean packageDoesNotMatch(Main.Product value) {
        return !value.packageMatches();
    }
}
