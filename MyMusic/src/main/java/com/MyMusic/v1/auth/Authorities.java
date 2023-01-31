package com.MyMusic.v1.auth;

import com.MyMusic.v1.services.models.Role;
import org.springframework.security.core.GrantedAuthority;

public class Authorities {
    static class UserAuthority implements GrantedAuthority {
        @Override
        public String getAuthority() {
            return Role.USER.name();
        }
    }

    static class AdminAuthority implements GrantedAuthority {
        @Override
        public String getAuthority() {
            return Role.ADMIN.name();
        }
    }

    static class CreatorAuthority implements GrantedAuthority {
        @Override
        public String getAuthority() {
            return Role.CREATOR.name();
        }
    }

    static GrantedAuthority fromRole(Role r) {
        return switch (r) {
            case USER -> new UserAuthority();
            case ADMIN -> new AdminAuthority();
            case CREATOR -> new CreatorAuthority();
        };
    }
}
