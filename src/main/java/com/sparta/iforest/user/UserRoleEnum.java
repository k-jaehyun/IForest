package com.sparta.iforest.user;

public enum UserRoleEnum {

    User(Authority.USER),
    ADMIN(Authority.ADMIN),
    BAN(Authority.BAN);

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String BAN = "ROLE_BAN";
    }
}
