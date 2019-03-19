package seedu.address.model.util;

import java.util.Optional;
import java.util.function.Function;

/**
 * Represents a builder that tries out replacement candidates and accepts suitable ones.
 */
public class Candidate<T> {
    private T value;
    private Function<String, Optional<T>> mapper;

    /**
     * Constructor
     * @param initialValue must be guaranteed to be suitable
     * @param mapper defines the suitability of values mapped from candidate strings
     */
    public Candidate(T initialValue, Function<String, Optional<T>> mapper) {
        this.value = initialValue;
        this.mapper = mapper;
    }

    /**
     * Tries to see if candidate string maps to a suitable value, and if so, accepts it and replaces current value.
     * @param candidate candidate string to try out
     */
    public Candidate<T> tryout(String candidate) {
        mapper.apply(candidate).ifPresent(value -> this.value = value);
        return this;
    }

    /**
     * Gets the current value.
     * @return current value
     */
    public T get() {
        return value;
    }
}

