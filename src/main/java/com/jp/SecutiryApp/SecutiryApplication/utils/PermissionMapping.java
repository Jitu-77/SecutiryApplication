package com.jp.SecutiryApp.SecutiryApplication.utils;

import com.jp.SecutiryApp.SecutiryApplication.enums.Permission;
import com.jp.SecutiryApp.SecutiryApplication.enums.Permission.*;
import com.jp.SecutiryApp.SecutiryApplication.enums.Role;
import com.jp.SecutiryApp.SecutiryApplication.enums.Role.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PermissionMapping {
    private static final Map<Role, Set<Permission>> map = Map.of(
            Role.USER, Set.of(Permission.USER_VIEW, Permission.POST_VIEW),
            Role.CREATOR, Set.of(Permission.POST_CREATE, Permission.USER_UPDATE, Permission.POST_UPDATE),
            Role.ADMIN, Set.of(Permission.POST_CREATE, Permission.USER_UPDATE, Permission.POST_UPDATE,
                    Permission.USER_DELETE, Permission.USER_CREATE, Permission.POST_DELETE)
    );

    public static Set<SimpleGrantedAuthority> getAuthoritiesForRole(Role role){
            return map.get(role)
                    //to convert teh set of Permission to SimpleGrantedAuthority
                    .stream()
                    .map(permission -> new SimpleGrantedAuthority(permission.name()))
                    .collect(Collectors.toSet());

    }
}
