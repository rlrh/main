package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Helper functions for handling strings.
 */
public class StringUtil {

    /**
     * Returns true if the {@code sentence} contains the {@code word}.
     *   Ignores case, but a full word match is required.
     *   <br>examples:<pre>
     *       containsWordIgnoreCase("ABc def", "abc") == true
     *       containsWordIgnoreCase("ABc def", "DEF") == true
     *       containsWordIgnoreCase("ABc def", "AB") == false //not a full word match
     *       </pre>
     * @param sentence cannot be null
     * @param word cannot be null, cannot be empty, must be a single word
     */
    public static boolean containsWordIgnoreCase(String sentence, String word) {
        requireNonNull(sentence);
        requireNonNull(word);

        String preppedWord = word.trim();
        checkArgument(!preppedWord.isEmpty(), "Word parameter cannot be empty");
        checkArgument(preppedWord.split("\\s+").length == 1, "Word parameter should be a single word");

        String preppedSentence = sentence;
        String[] wordsInPreppedSentence = preppedSentence.split("\\s+");

        return Arrays.stream(wordsInPreppedSentence)
                .anyMatch(preppedWord::equalsIgnoreCase);
    }

    /**
     * Returns true if the {@code sentence} contains the {@code phrase}.
     *   Ignores case, but a full phrase match is required.
     *   <br>examples:<pre>
     *       containsPhraseIgnoreCase("ABc def hij", "dEf hij") == true
     *       containsPhraseIgnoreCase("ABc def", "bc d") == true
     *       containsPhraseIgnoreCase("ABc def", "b def") == false //phrase not in sentence
     *       </pre>
     * @param sentence cannot be null
     * @param phrase cannot be null, cannot be empty
     */
    public static boolean containsPhraseIgnoreCase(String sentence, String phrase) {
        requireNonNull(sentence);
        requireNonNull(phrase);

        String preppedPhrase = phrase.trim();
        checkArgument(!preppedPhrase.isEmpty(), "Phrase parameter cannot be empty");

        return Pattern.compile(Pattern.quote(preppedPhrase), Pattern.CASE_INSENSITIVE).matcher(sentence).find();
    }

    /**
     * Returns a detailed message of the t, including the stack trace.
     */
    public static String getDetails(Throwable t) {
        requireNonNull(t);
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return t.getMessage() + "\n" + sw.toString();
    }

    /**
     * Returns true if {@code s} represents a non-zero unsigned integer
     * e.g. 1, 2, 3, ..., {@code Integer.MAX_VALUE} <br>
     * Will return false for any other non-null string input
     * e.g. empty string, "-1", "0", "+1", and " 2 " (untrimmed), "3 0" (contains whitespace), "1 a" (contains letters)
     * @throws NullPointerException if {@code s} is null.
     */
    public static boolean isNonZeroUnsignedInteger(String s) {
        requireNonNull(s);

        try {
            int value = Integer.parseInt(s);
            return value > 0 && !s.startsWith("+"); // "+1" is successfully parsed by Integer#parseInt(String)
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    /**
     * Gets the first given number of words of the given string
     * with ellipsis appended at the end if there more than the given number of words.
     * @param string string
     * @param numOfWords number of words
     * @return first given number of words of the given string, possibly with ellipsis at the end
     */
    public static String getFirstNWordsWithEllipsis(String string, int numOfWords) {
        if (string == null || string.isEmpty() || numOfWords <= 0) {
            return "";
        }

        String[] stringArray = string.trim().split("\\s+");
        for (int i = 0; i < stringArray.length; i++) {
            stringArray[i] = stringArray[i].trim();
        }

        StringBuilder firstNWords = new StringBuilder();
        for (int i = 0; i < (stringArray.length <= numOfWords ? stringArray.length : numOfWords); i++) {
            firstNWords.append(stringArray[i].trim());
            firstNWords.append(" ");
        }

        if (stringArray.length <= numOfWords) {
            return firstNWords.toString().trim();
        } else {
            return firstNWords.toString().trim().concat("…");
        }
    }

    /**
     * Gets the number of words in a string. Method is null-safe.
     * @param string any string, can be empty or null
     * @return number of words in string, 0 if string is empty or null
     */
    public static long getNumberOfWords(String string) {
        return Stream.ofNullable(string)
                .map(text -> text.split("\\s+"))
                .flatMap(Arrays::stream)
                .filter(word -> word.length() > 0)
                .count();
    }

    /**
     * Gets a null-safe version of a string.
     * @param string possibly null string
     * @return string if non-null, empty string if null
     */
    public static String nullSafeOf(String string) {
        return Optional.ofNullable(string).orElse("");
    }

    /**
     * Gets a UTF-8 encode-able version of a string.
     * @param string Cannot be null.
     * @return string if can be UTF-8 encode-able, empty otherwise.
     */
    public static String utfSafeOf(String string) {
        StringBuilder sb = new StringBuilder();
        for (int codepoint : string.codePoints().toArray()) {
            // Workaround to try and make javafx not crash when rendering bad text.
            // Not sure if this covers all the cases, but this handles most cases.
            if (Character.isValidCodePoint(codepoint)
                && Character.isDefined(codepoint)
                && !Character.isISOControl(codepoint)
                && !Character.isSupplementaryCodePoint(codepoint)) {
                sb.appendCodePoint(codepoint);
            }
        }
        return sb.toString();
    }

}
