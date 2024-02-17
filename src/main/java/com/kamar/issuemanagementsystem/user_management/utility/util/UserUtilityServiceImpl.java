package com.kamar.issuemanagementsystem.user_management.utility.util;

import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.authority.utility.UserAuthorityUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * implementation of the user utility contract.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
public class UserUtilityServiceImpl implements UserUtilityService {

    private final UserAuthorityUtility userAuthorityUtility;


    @Override
    public boolean hasAuthority(UserDetails user, String authority) {

        /*get user authorities*/
        String pAuth = authority.toUpperCase();
        Collection<? extends GrantedAuthority> userAuthorities = user.getAuthorities();
        /*get the authorities*/
        UserAuthority userAuth = userAuthorityUtility.getFor("user");
        UserAuthority adminAuth = userAuthorityUtility.getFor("admin");
        UserAuthority employeeAuth = userAuthorityUtility.getFor("employee");
        UserAuthority deptAdminAuth = userAuthorityUtility.getFor("department_admin");
        UserAuthority ownerAuth = userAuthorityUtility.getFor("owner");

        /*filter*/
        switch (pAuth){

            case "USER" -> {
                return userAuthorities.contains(userAuth) &&
                        (!userAuthorities.contains(adminAuth)) &&
                        (!userAuthorities.contains(employeeAuth)) &&
                        (!userAuthorities.contains(deptAdminAuth)) &&
                        (!userAuthorities.contains(ownerAuth));
            }
            case "ADMIN" -> {
                return userAuthorities.contains(adminAuth) &&
                        (!userAuthorities.contains(employeeAuth)) &&
                        (!userAuthorities.contains(deptAdminAuth));
            }
            case "EMPLOYEE" -> {
                return userAuthorities.contains(employeeAuth) &&
                        (!userAuthorities.contains(adminAuth)) &&
                        (!userAuthorities.contains(deptAdminAuth));
            }
            case "DEPARTMENT_ADMIN" -> {
                return userAuthorities.contains(deptAdminAuth) &&
                        (!userAuthorities.contains(adminAuth));
            }
            case "OWNER" -> {
                return userAuthorities.contains(ownerAuth) &&
                        (!userAuthorities.contains(employeeAuth)) &&
                        (!userAuthorities.contains(deptAdminAuth)) &&
                        (!userAuthorities.contains(adminAuth));
            }
            default -> {
                return userAuthorities.contains(userAuthorityUtility.getFor(pAuth));
            }
        }
    }
}
