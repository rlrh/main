package seedu.address.ui.util;

public enum UrlType {
    INTERNAL("internal page"),
    ONLINE("online page"),
    OFFLINE("offline page"),
    READER_VIEW("reader view page");

    private String friendlyName;

    UrlType(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }
}
