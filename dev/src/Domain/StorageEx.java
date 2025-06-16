package Domain;

// Custom exception class for handling storage-related issues with items
public class StorageEx {

    // Inner class to define the custom exception for storage errors
    public class StorageEX extends java.lang.Exception {

        // Constructor to initialize the exception with a specific item-related error message
        public StorageEX(String message) {
            // Call the parent constructor with a custom error message
            super(message + ": is out of storage");
        }
    }
}