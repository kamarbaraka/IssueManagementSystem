package com.kamar.issuemanagementsystem.authority.service;

import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.authority.exception.AuthorityException;
import com.kamar.issuemanagementsystem.authority.repository.UserAuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * implementation of the authority management service contract.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
public class UserAuthorityManagementServiceServiceImpl implements UserAuthorityManagementService {

    private final UserAuthorityRepository userAuthorityRepository;

    @Override
    public UserAuthority createAuthority(String authority) throws AuthorityException {

        /*check if authority exists*/
        if (userAuthorityRepository.findById(authority.toUpperCase()).isPresent()) {
            throw new AuthorityException("role exists");
        }

        /*create the authority*/
        UserAuthority userAuthority = new UserAuthority();
        userAuthority.setAuthority(authority.toUpperCase());

        return userAuthorityRepository.save(userAuthority);
    }

    @Override
    public void deleteAuthority(String authority) throws AuthorityException {

        /*check if it exists*/
        UserAuthority userAuthority = userAuthorityRepository.findById(authority).orElseThrow(
                () -> new AuthorityException("no such authority"));
        /*delete the authority*/
        userAuthorityRepository.delete(userAuthority);
    }

    @Override
    public UserAuthority getAuthority(String authority) throws AuthorityException {

        /*get the authority*/
        return userAuthorityRepository.findById(authority.toUpperCase()).orElseThrow(
                () -> new AuthorityException("no such authority"));

    }

    @Override
    public List<UserAuthority> getAllAuthorities() {

        /*get all authorities*/
        try {
            List<UserAuthority> allAuthorities = userAuthorityRepository.findAll();
            return allAuthorities;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
