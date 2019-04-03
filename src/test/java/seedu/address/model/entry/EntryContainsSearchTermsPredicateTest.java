package seedu.address.model.entry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.testutil.EntryBuilder;

public class EntryContainsSearchTermsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        EntryContainsSearchTermsPredicate firstPredicate = new EntryContainsSearchTermsPredicate(firstPredicateKeywordList);
        EntryContainsSearchTermsPredicate secondPredicate = new EntryContainsSearchTermsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        EntryContainsSearchTermsPredicate firstPredicateCopy = new EntryContainsSearchTermsPredicate(
            firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different entry -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_titleContainsKeywords_returnsTrue() {
        // One keyword
        EntryContainsSearchTermsPredicate predicate = new EntryContainsSearchTermsPredicate(
            Collections.singletonList("Alice"));
        assertTrue(predicate.test(new EntryBuilder().withTitle("Alice Bob").build()));

        // Multiple keywords
        predicate = new EntryContainsSearchTermsPredicate(Arrays.asList("Alice", "Bob"));
        assertTrue(predicate.test(new EntryBuilder().withTitle("Alice Bob").build()));

        // Only one matching keyword
        predicate = new EntryContainsSearchTermsPredicate(Arrays.asList("Bob", "Carol"));
        assertTrue(predicate.test(new EntryBuilder().withTitle("Alice Carol").build()));

        // Mixed-case keywords
        predicate = new EntryContainsSearchTermsPredicate(Arrays.asList("aLIce", "bOB"));
        assertTrue(predicate.test(new EntryBuilder().withTitle("Alice Bob").build()));
    }

    @Test
    public void test_titleDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        EntryContainsSearchTermsPredicate predicate = new EntryContainsSearchTermsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new EntryBuilder().withTitle("Alice").build()));

        // Non-matching keyword
        predicate = new EntryContainsSearchTermsPredicate(Arrays.asList("Carol"));
        assertFalse(predicate.test(new EntryBuilder().withTitle("Alice Bob").build()));

        // Keywords match description, link and address, but does not match title
        predicate = new EntryContainsSearchTermsPredicate(Arrays.asList("12345", "https://example.com", "Main", "Street"));
        assertFalse(predicate.test(new EntryBuilder().withTitle("Alice").withDescription("12345")
                .withLink("https://example.com").withAddress("Main Street").build()));
    }
}
