package com.company;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class SorterTest {

    private static final int minimumPackageMatch = 3;
    private static final int minimumPackageDoesNotMatch = 3;

    private Sorter testable;

    @Before
    public void setup() {
        testable = new Sorter();
    }

    @Test
    public void given_more_than_10_products_and_much_more_matching_than_not_matching_package_then_return_at_least_three_matching_and_not_matching() {
        // Arrange
        final List<Main.Product> listWithMoreMatchingProducts = createShuffledListWithMoreMatchingThanNotMatching();

        // Act
        List<Main.Product> sortedList = testable.sortList(listWithMoreMatchingProducts);

        // Assert
        assertTrue(sortedList.stream().filter(Main.Product::packageMatches).count() >= 3);
        assertTrue(sortedList.stream().filter(this::packageDoesNotMatch).count() >= 3);
        assertNotEquals(listWithMoreMatchingProducts, sortedList);
    }

    private List<Main.Product> createShuffledListWithMoreMatchingThanNotMatching() {
        List<Main.Product> list = new ArrayList<>();
        list.addAll(createProductsWithMatching(20, true));
        list.addAll(createProductsWithMatching(5, false));
        Collections.shuffle(list);
        return list;
    }

    private List<Main.Product> createProductsWithMatching(int numberOfProducts, boolean match) {
        List<Main.Product> packageMatch = new ArrayList<>();

        for (int i = 0; i < numberOfProducts; i++) {
            final Main.Product product = new Main.Product();
            product.packageMatch = match;
            packageMatch.add(product);
        }

        return packageMatch;
    }

    private boolean packageDoesNotMatch(Main.Product value) {
        return !value.packageMatches();
    }

    @Test
    public void given_more_than_10_products_and_much_more_not_matching_than_matching_package_then_return_at_least_three_matching_and_not_matching() {
        // Arrange
        List<Main.Product> listWithMoreNotMatching = createShuffledListWithMoreNotMatchingThanMatching();

        // Act
        List<Main.Product> sortedList = testable.sortList(listWithMoreNotMatching);

        // Assert
        assertTrue(sortedList.stream().filter(Main.Product::packageMatches).count() >= 3);
        assertTrue(sortedList.stream().filter(this::packageDoesNotMatch).count() >= 3);
        assertNotEquals(listWithMoreNotMatching, sortedList);
    }

    private List<Main.Product> createShuffledListWithMoreNotMatchingThanMatching() {
        List<Main.Product> list = new ArrayList<>();
        list.addAll(createProductsWithMatching(5, true));
        list.addAll(createProductsWithMatching(20, false));
        Collections.shuffle(list);
        return list;
    }

    @Test
    public void given_less_than_10_products_but_at_least_three_matching_and_not_matching_package_products_then_return_unchanged_list() {
        // Arrange
        List<Main.Product> listWithLessThanLimit = createShuffledListWithLessThanLimitButMatchingCriteria();

        // Act
        List<Main.Product> sortedList = testable.sortList(listWithLessThanLimit);

        // Assert
        assertTrue(sortedList.stream().filter(Main.Product::packageMatches).count() >= 3);
        assertTrue(sortedList.stream().filter(this::packageDoesNotMatch).count() >= 3);
        assertEquals(listWithLessThanLimit, sortedList);
    }

    private List<Main.Product> createShuffledListWithLessThanLimitButMatchingCriteria() {
        List<Main.Product> list = new ArrayList<>();
        list.addAll(createProductsWithMatching(minimumPackageMatch, false));
        list.addAll(createProductsWithMatching(minimumPackageDoesNotMatch, true));
        Collections.shuffle(list);
        return list;
    }

    @Test
    public void given_less_than_10_products_and_less_than_three_matching_and_not_matching_package_products_then_return_unchanged_list() {
        // Arrange
        List<Main.Product> listWithLessThanLimitAndLessThanMinimums = createShuffledListWithTooLessProductsMatchingCriteria();

        // Act
        List<Main.Product> sortedList = testable.sortList(listWithLessThanLimitAndLessThanMinimums);

        // Assert
        assertEquals(listWithLessThanLimitAndLessThanMinimums, sortedList);
    }

    private List<Main.Product> createShuffledListWithTooLessProductsMatchingCriteria() {
        List<Main.Product> list = new ArrayList<>();
        list.addAll(createProductsWithMatching(minimumPackageMatch - 1, false));
        list.addAll(createProductsWithMatching(minimumPackageDoesNotMatch - 1, true));
        Collections.shuffle(list);
        return list;
    }

    @Ignore
    @Test
    public void given_more_than_10_products_with_balanced_list_between_matching_and_not_matching_package_products_then_return_first_10() {
        List<Main.Product> listWithMoreMatching = createShuffledListWithMoreMatchingThanNotMatching();
        List<Main.Product> listWithMoreNotMatching = createShuffledListWithMoreNotMatchingThanMatching();
        List<Main.Product> listWithLessThanLimit = createShuffledListWithLessThanLimitButMatchingCriteria();
        List<Main.Product> listWithLessThanLimitAndLessThanMinimums = createShuffledListWithTooLessProductsMatchingCriteria();
    }
}
