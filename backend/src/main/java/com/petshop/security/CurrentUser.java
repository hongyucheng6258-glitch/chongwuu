package com.petshop.security;

/**
 * 当前登录用户上下文 (ThreadLocal)
 */
public class CurrentUser {

    private static final ThreadLocal<UserContext> HOLDER = new ThreadLocal<>();

    public static void set(UserContext context) {
        HOLDER.set(context);
    }

    public static UserContext get() {
        return HOLDER.get();
    }

    public static Long getUserId() {
        UserContext ctx = HOLDER.get();
        return ctx == null ? null : ctx.getUserId();
    }

    public static boolean isAdmin() {
        UserContext ctx = HOLDER.get();
        return ctx != null && "ADMIN".equals(ctx.getRole());
    }

    public static void clear() {
        HOLDER.remove();
    }

    public static Integer getMemberLevel() {
        UserContext ctx = HOLDER.get();
        return ctx != null ? ctx.getMemberLevel() : 0;
    }

    public static class UserContext {
        private final Long userId;
        private final String username;
        private final String role;
        private final Integer memberLevel;

        public UserContext(Long userId, String username, String role) {
            this(userId, username, role, 0);
        }

        public UserContext(Long userId, String username, String role, Integer memberLevel) {
            this.userId = userId;
            this.username = username;
            this.role = role;
            this.memberLevel = memberLevel != null ? memberLevel : 0;
        }

        public Long getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }

        public String getRole() {
            return role;
        }

        public Integer getMemberLevel() {
            return memberLevel;
        }
    }
}
