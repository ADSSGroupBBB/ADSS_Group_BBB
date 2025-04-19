package Domain;

public class StorageEx {
    public class StorageEX extends Exception {
        public StorageEX(String message) {  // need to get the name of the item as  input
            super(message + ": is out of storage");
        }
    }
}
