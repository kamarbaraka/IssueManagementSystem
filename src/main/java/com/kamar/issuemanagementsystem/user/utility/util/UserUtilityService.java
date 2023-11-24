package com.kamar.issuemanagementsystem.user.utility.util;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * the user utility contract.
 * @author kamar baraka.*/

public interface UserUtilityService {

    boolean hasAuthority(UserDetails user, String authority);
}
