/*
package com.kamar.issuemanagementsystem.authority.utility;

import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.authority.repository.UserAuthorityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

*/
/**
 * implementation of the user authority util contract.
 * @author kamar baraka.*//*


@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class UserAuthorityUtilityImpl implements UserAuthorityUtility {

    private final UserAuthorityRepository userAuthorityRepository;

    @Override
    public UserAuthority getFor(String authorityName)  {

        */
/*get the authority*//*

        return userAuthorityRepository.findById(authorityName.toUpperCase()).orElseThrow();
    }
}
*/
