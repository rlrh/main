package seedu.address.model.util;

import java.util.Optional;
import java.util.function.Function;

/**
 * Represents a builder that tries out replacement candidates and accepts suitable ones
 */
public class Candidate<T> {
    private T value;
    private Function<String, Optional<T>> mapper;

    public Candidate(T initialValue, Function<String, Optional<T>> mapper) {
        this.value = initialValue;
        this.mapper = mapper;
    }

    protected void setValue(T value) {
        this.value = value;
    }

    /**
     * Tries to see if candidate is a valid value, and replaces value if so.
     */
    public Candidate<T> tryout(String candidate) {
        mapper.apply(candidate).ifPresent(value -> this.value = value);
        return this;
    }

    public T get() {
        return this.value;
    }
}

