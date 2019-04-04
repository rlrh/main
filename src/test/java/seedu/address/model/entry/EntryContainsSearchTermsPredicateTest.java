package seedu.address.model.entry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.EntryBuilder;
import seedu.address.testutil.FindEntryDescriptorBuilder;

public class EntryContainsSearchTermsPredicateTest {

    @Test
    public void equals() {
        EntryContainsSearchTermsPredicate firstPredicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withLink("first").build());

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));
    }

    @Test
    public void equalsTitle() {
        EntryContainsSearchTermsPredicate firstPredicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withTitle("first").build());
        EntryContainsSearchTermsPredicate secondPredicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withTitle("second").build());

        // same values -> returns true
        EntryContainsSearchTermsPredicate firstPredicateCopy = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withTitle("first").build());
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different entry -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void equalsLink() {
        EntryContainsSearchTermsPredicate firstPredicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withLink("first").build());
        EntryContainsSearchTermsPredicate secondPredicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withLink("second").build());

        // same values -> returns true
        EntryContainsSearchTermsPredicate firstPredicateCopy = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withLink("first").build());
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different entry -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void equalsDescription() {
        EntryContainsSearchTermsPredicate firstPredicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withDescription("first").build());
        EntryContainsSearchTermsPredicate secondPredicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withDescription("second").build());

        // same values -> returns true
        EntryContainsSearchTermsPredicate firstPredicateCopy = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withDescription("first").build());
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different entry -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void equalsTag() {
        EntryContainsSearchTermsPredicate firstPredicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withTags("first", "second").build());
        EntryContainsSearchTermsPredicate secondPredicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withTags("third").build());

        // same values -> returns true
        EntryContainsSearchTermsPredicate firstPredicateCopy = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withTags("first", "second").build());
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different entry -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void equalsAll() {
        EntryContainsSearchTermsPredicate firstPredicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withAll("first").build());
        EntryContainsSearchTermsPredicate secondPredicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withAll("second").build());

        // same values -> returns true
        EntryContainsSearchTermsPredicate firstPredicateCopy = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withAll("first").build());
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different entry -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void equalsTerms() {
        EntryContainsSearchTermsPredicate firstPredicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder()
                .withTitle("qwerty")
                .withDescription("dvorak")
                .withLink("dijkstra")
                .withAll("all")
                .withTags("first", "second").build());
        EntryContainsSearchTermsPredicate secondPredicate;
        FindEntryDescriptorBuilder secondBuilder =
            new FindEntryDescriptorBuilder()
                .withTitle("qwerty")
                .withDescription("dvorak")
                .withLink("dijkstra")
                .withAll("all")
                .withTags("first", "second");

        // same values -> returns true
        EntryContainsSearchTermsPredicate firstPredicateCopy = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder()
                .withTitle("qwerty")
                .withDescription("dvorak")
                .withLink("dijkstra")
                .withAll("all")
                .withTags("first", "second").build());
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different entry -> returns false
        // different title
        secondPredicate = new EntryContainsSearchTermsPredicate(
            secondBuilder
                .withTitle("dumb")
                .build());
        assertFalse(firstPredicate.equals(secondPredicate));

        // different description
        secondPredicate = new EntryContainsSearchTermsPredicate(
            secondBuilder
                .withTitle("qwerty")
                .withDescription("dumb")
                .build());
        assertFalse(firstPredicate.equals(secondPredicate));

        // different link
        secondPredicate = new EntryContainsSearchTermsPredicate(
            secondBuilder
                .withDescription("dvorak")
                .withLink("dumb")
                .build());
        assertFalse(firstPredicate.equals(secondPredicate));

        // different different all
        secondPredicate = new EntryContainsSearchTermsPredicate(
            secondBuilder
                .withLink("dijkstra")
                .withAll("dumb")
                .build());
        assertFalse(firstPredicate.equals(secondPredicate));

        // different tags
        secondPredicate = new EntryContainsSearchTermsPredicate(secondBuilder
            .withAll("all")
            .withTags("dumb")
            .build());
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_titleContainsKeyphrase_returnsTrue() {
        // One word matching
        EntryContainsSearchTermsPredicate predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withTitle("Bob").build());
        assertTrue(predicate.test(new EntryBuilder().withTitle("Alice Bob Chloe").build()));

        // Multiple words matching
        predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withTitle("Bob Chloe").build());
        assertTrue(predicate.test(new EntryBuilder().withTitle("Alice Bob Chloe").build()));

        // Phrase matching
        predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withTitle("ob Chl").build());
        assertTrue(predicate.test(new EntryBuilder().withTitle("Alice Bob Chloe").build()));

        // Mixed-case phrase
        predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withTitle("ALICE bOb").build());
        assertTrue(predicate.test(new EntryBuilder().withTitle("Alice Bob Chloe").build()));
    }

    @Test
    public void test_titleDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        EntryContainsSearchTermsPredicate predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().build());
        assertFalse(predicate.test(new EntryBuilder().withTitle("Alice Bob Chloe").build()));

        // Non-matching keyword
        predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withTitle("B0b").build());
        assertFalse(predicate.test(new EntryBuilder().withTitle("Alice Bob Chloe").build()));

        // Non-matching keyphrase
        predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withTitle("Bob-Chloe").build());
        assertFalse(predicate.test(new EntryBuilder().withTitle("Alice Bob Chloe").build()));

        // Keywords match description, link, but does not match title
        predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withTitle("b0b").build());
        assertFalse(predicate.test(new EntryBuilder().withTitle("Alice Bob Chloe").withDescription("Alice B0b Chloe")
                .withLink("https://b0b.com").build()));
    }

    @Test
    public void test_descriptionContainsKeyphrase_returnsTrue() {
        // One word matching
        EntryContainsSearchTermsPredicate predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withDescription("Bob").build());
        assertTrue(predicate.test(new EntryBuilder().withDescription("Alice Bob Chloe").build()));

        // Multiple words matching
        predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withDescription("Bob Chloe").build());
        assertTrue(predicate.test(new EntryBuilder().withDescription("Alice Bob Chloe").build()));

        // Phrase matching
        predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withDescription("ob Chl").build());
        assertTrue(predicate.test(new EntryBuilder().withDescription("Alice Bob Chloe").build()));

        // Mixed-case phrase
        predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withDescription("ALICE bOb").build());
        assertTrue(predicate.test(new EntryBuilder().withDescription("Alice Bob Chloe").build()));
    }

    @Test
    public void test_descriptionDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        EntryContainsSearchTermsPredicate predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().build());
        assertFalse(predicate.test(new EntryBuilder().withDescription("Alice Bob Chloe").build()));

        // Non-matching keyword
        predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withDescription("B0b").build());
        assertFalse(predicate.test(new EntryBuilder().withDescription("Alice Bob Chloe").build()));

        // Non-matching keyphrase
        predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withDescription("Bob-Chloe").build());
        assertFalse(predicate.test(new EntryBuilder().withDescription("Alice Bob Chloe").build()));

        // Keywords match title, link, but does not match description
        predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withDescription("b0b").build());
        assertFalse(predicate.test(new EntryBuilder().withDescription("Alice Bob Chloe").withTitle("Alice B0b Chloe")
            .withLink("https://b0b.com").build()));
    }

    @Test
    public void test_linkContainsKeyphrase_returnsTrue() {
        // Phrase matching
        EntryContainsSearchTermsPredicate predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withLink("Bob").build());
        assertTrue(predicate.test(new EntryBuilder().withLink("https://AliceBobChloe.com").build()));

        predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withLink("obChl").build());
        assertTrue(predicate.test(new EntryBuilder().withLink("https://AliceBobChloe.com").build()));

        // Mixed-case phrase
        predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withLink("ALICEbOb").build());
        assertTrue(predicate.test(new EntryBuilder().withLink("https://AliceBobChloe.com").build()));
    }

    @Test
    public void test_linkDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        EntryContainsSearchTermsPredicate predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().build());
        assertFalse(predicate.test(new EntryBuilder().withLink("https://AliceBobChloe.com").build()));

        // Non-matching keyword
        predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withLink("B0b").build());
        assertFalse(predicate.test(new EntryBuilder().withLink("https://AliceBobChloe.com").build()));

        // Non-matching keyphrase
        predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withLink("Bob-Chloe").build());
        assertFalse(predicate.test(new EntryBuilder().withLink("https://AliceBobChloe.com").build()));

        // Keywords match description, title, but does not match link
        predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withLink("b0b").build());
        assertFalse(predicate.test(new EntryBuilder().withLink("https://bob.com").withDescription("Alice B0b Chloe")
            .withTitle("Alice B0b Chloe").build()));
    }

    @Test
    public void test_tagsContainsKeyphrase_returnsTrue() {
        // One tag matching
        EntryContainsSearchTermsPredicate predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withTags("Bob").build());
        assertTrue(predicate.test(new EntryBuilder().withTags("Alice", "Bob", "Chloe").build()));

        // Multiple tags matching
        predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withTags("Bob", "Chloe").build());
        assertTrue(predicate.test(new EntryBuilder().withTags("Alice", "Bob", "Chloe").build()));
    }

    @Test
    public void test_tagsDoesNotContainKeywords_returnsFalse() {
        // Zero tags
        EntryContainsSearchTermsPredicate predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().build());
        assertFalse(predicate.test(new EntryBuilder().withTags("Alice", "Bob", "Chloe").build()));

        // Non-fully-matching tag
        predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withTags("ob").build());
        assertFalse(predicate.test(new EntryBuilder().withTags("Alice", "Bob", "Chloe").build()));

        // Non-matching tag
        predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withTags("B0b").build());
        assertFalse(predicate.test(new EntryBuilder().withTags("Alice", "Bob", "Chloe").build()));

        // Mixed-case tag
        predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withTags("bOb").build());
        assertFalse(predicate.test(new EntryBuilder().withTags("Alice", "Bob", "Chloe").build()));

        // Keywords match description, link, title, but does not match tags
        predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder().withTags("b0b").build());
        assertFalse(predicate.test(new EntryBuilder().withTags("Alice", "Bob", "Chloe")
            .withTitle("Alice B0b Chloe").withDescription("Alice B0b Chloe")
            .withLink("https://b0b.com").build()));
    }

    @Test
    public void test_atLeastOneFieldMatch_returnsTrue() {
        EntryContainsSearchTermsPredicate predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder()
                .withTitle("qwerty")
                .withDescription("dvorak")
                .withLink("dijkstra")
                .withTags("first", "second").build());

        EntryBuilder builder =
            new EntryBuilder()
                .withTitle("dumb")
                .withDescription("dumb")
                .withLink("https://dumb.com")
                .withTags("dumb", "dumb");

        // only title match -> returns true
        builder
            .withTitle("qwerty");
        assertTrue(predicate.test(builder.build()));

        // only description match -> returns true
        builder
            .withTitle("dumb")
            .withDescription("dvorak");
        assertTrue(predicate.test(builder.build()));

        // only link match -> returns true
        builder
            .withDescription("dumb")
            .withLink("https://dijkstra.com");
        assertTrue(predicate.test(builder.build()));

        // only tag match -> returns true
        builder
            .withLink("https://dumb.com")
            .withTags("first");
        assertTrue(predicate.test(builder.build()));
    }

    @Test
    public void test_atLeastOneFieldMatchesAllField_returnsTrue() {
        EntryContainsSearchTermsPredicate predicate = new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptorBuilder()
                .withAll("dumb")
                .build());

        EntryBuilder builder =
            new EntryBuilder()
                .withTitle("title")
                .withDescription("desc")
                .withLink("https://link.com")
                .withTags("tag");

        // only title match -> returns true
        builder
            .withTitle("dumb");
        assertTrue(predicate.test(builder.build()));

        // only description match -> returns true
        builder
            .withTitle("title")
            .withDescription("dumb");
        assertTrue(predicate.test(builder.build()));

        // only link match -> returns true
        builder
            .withDescription("desc")
            .withLink("https://dumb.com");
        assertTrue(predicate.test(builder.build()));

        // only tag match -> returns true
        builder
            .withLink("https://link.com")
            .withTags("dumb");
        assertTrue(predicate.test(builder.build()));
    }
}
