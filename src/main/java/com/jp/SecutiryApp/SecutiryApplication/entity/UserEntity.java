package com.jp.SecutiryApp.SecutiryApplication.entity;

import com.jp.SecutiryApp.SecutiryApplication.enums.Permission;
import com.jp.SecutiryApp.SecutiryApplication.enums.Role;
import com.jp.SecutiryApp.SecutiryApplication.utils.PermissionMapping;
import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String name;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER) // we need to add Element Collections as this is a set
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
// with permission mapping we dont save to our db
//    @ElementCollection(fetch = FetchType.EAGER) // we need to add Element Collections as this is a set
//    @Enumerated(EnumType.STRING)
//    private Set<Permission> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
// before mapping permissions
//        return roles.stream()
//                .map(role-> new SimpleGrantedAuthority("ROLE_"+role.name()))
//                .collect(Collectors.toSet());

// attaching role and Permissions and storing in our system
//    Set<SimpleGrantedAuthority> authorities = roles.stream()
//                .map(role-> new SimpleGrantedAuthority("ROLE_"+role.name()))
//                .collect(Collectors.toSet());
//
//    //    now attaching permissions in our system
//    permissions.forEach(
//            permission -> authorities.add(new SimpleGrantedAuthority(permission.name()))
//    );


//we will just map the role and the permissions instead of storing in our db
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        roles.forEach(
                role->{
                    Set<SimpleGrantedAuthority> permission  = PermissionMapping.getAuthoritiesForRole(role);
                    authorities.addAll(permission); // via these we are mapping ROLES - Permission[]
                    authorities.add(new SimpleGrantedAuthority("ROLE_"+role.name())); // adding the roles too
                }
        );
    return  authorities; //returning both roles and permissions
    }

    @Override
    public String getPassword() {
       return  this.password;
    }

    @Override
    public String getUsername() {
      return  this.email;
    }
}
