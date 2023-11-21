package com.kamar.issuemanagementsystem.authority.utility;

import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;

/**
 * the user authority utility contract.
 * @author kamar baraka.*/

public interface UserAuthorityUtility {

    UserAuthority getFor(String authorityName) ;
}
