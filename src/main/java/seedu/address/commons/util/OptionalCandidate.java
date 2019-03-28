package seedu.address.commons.util;

import java.util.Optional;
import java.util.function.Function;

/**
 * Represents an object that tries out replacement candidates and accepts suitable ones.
 */
public class OptionalCandidate<S, T> {
    private Optional<T> value;
    private Function<S, Optional<T>> mapper;

    /**
     * Constructor
     * @param mapper defines the suitability of values mapped from candidates
     */
    public OptionalCandidate(Function<S, Optional<T>> mapper) {
        this.value = Optional.empty();
        this.mapper = mapper;
    }

    /**
     * Builder
     * @param mapper defines the suitability of values mapped from candidates
     */
    public static <S, T> OptionalCandidate<S, T> with(Function<S, Optional<T>> mapper) {
        return new OptionalCandidate<>(mapper);
    }

    /**
     * Tries to see if candidate maps to a suitable value, and if so, accepts it and replaces current value.
     * @param candidate candidate to try out
     */
    public OptionalCandidate<S, T> tryout(S candidate) {
        mapper.apply(candidate).ifPresent(value -> this.value = Optional.of(value));
        return this;
    }

    /**
     * Gets the current value.
     * @return current value
     */
    public Optional<T> getOptional() {
        return value;
    }
}

