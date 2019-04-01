package seedu.address.storage;

import java.io.IOException;

import seedu.address.commons.exceptions.DataConversionException;

/**
 * Basically a supplier which throws the exceptions thrown by Storage#read__EntryBook.
 * Defining this allows us to have method references to Storage::read__EntryBook.
 */
@FunctionalInterface
public interface DataConversionAndIOExceptionsThrowingSupplier<T> {
    T get() throws DataConversionException, IOException;
}
