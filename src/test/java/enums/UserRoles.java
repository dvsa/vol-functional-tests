package enums;

public enum UserRoles {

    INTERNAL_ADMIN("internal-admin"),
    INTERNAL_LIMITED_READ_ONLY("internal-limited-read-only"),
    INTERNAL_READ_ONLY("internal-read-only"),
    INTERNAL_CASE_WORKER("internal-case-worker"),
    INTERNAL("internal"),
    EXTERNAL("selfserve");

    private UserRoles(String userRoles){
        this.userRoles = userRoles;
    }

    public String getUserRoles(){
        return userRoles;
    }

    private final String userRoles;

}