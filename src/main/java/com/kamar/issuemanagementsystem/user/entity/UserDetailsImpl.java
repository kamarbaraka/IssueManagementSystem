//package com.kamar.issuemanagementsystem.user.entity;
//
//import com.kamar.issuemanagementsystem.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.util.Collection;
//
//@Component
//@RequiredArgsConstructor
//public class UserDetailsImpl implements UserDetails {
//
//    private final User user;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return user.getAuthorities();
//    }
//
//    @Override
//    public String getPassword() {
//        return user.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return user.getUsername();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return user.isAccountNonExpired();
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return user.isAccountNonLocked();
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return user.isCredentialsNonExpired();
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return user.isEnabled();
//    }
//}
