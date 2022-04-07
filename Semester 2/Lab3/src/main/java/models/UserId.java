package models;

public class UserId {
    private final String userId;

    public String getUserId() {
        return userId;
    }

    public UserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserId) {
            return userId.equals(((UserId) obj).userId);
        }
        return false;
    }
}
