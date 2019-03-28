package seedu.address.commons.util;

import java.util.Optional;
import java.util.function.Function;

/**
 * Represents a builder that tries out replacement candidates and accepts suitable ones.
 */
public class Candidate<S, T> {
    private T value;
    private Function<S, Optional<T>> mapper;

    /**
     * Constructor
     * @param initialValue must be guaranteed to be suitable
     * @param mapper defines the suitability of values mapped from candidates
     */
    public Candidate(T initialValue, Function<S, Optional<T>> mapper) {
        this.value = initialValue;
        this.mapper = mapper;
    }

    /**
     * Builder
     * @param mapper defines the suitability of values mapped from candidates
     */
    public static <S, T> Candidate<S, T> with(T initialValue, Function<S, Optional<T>> mapper) {
        return new Candidate<>(initialValue, mapper);
    }

    /**
     * Tries to see if candidate maps to a suitable value, and if so, accepts it and replaces current value.
     * @param candidate candidate to try out
     */
    public Candidate<S, T> tryout(S candidate) {
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

