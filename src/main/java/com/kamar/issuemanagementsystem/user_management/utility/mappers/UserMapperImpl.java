/*
package com.kamar.issuemanagementsystem.user_management.utility.mappers;

import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.user_management.data.dto.UserPresentationDTO;
import com.kamar.issuemanagementsystem.user_management.data.dto.UserRegistrationDTO;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

*/
/**
 * the user mapper implementation.
 * @author kamar baraka.*//*


@Service
public class UserMapperImpl implements UserMapper {
    @Override
    public UserEntity dtoToEntity(UserRegistrationDTO registrationDTO) {

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registrationDTO.username());
        userEntity.setPassword(registrationDTO.password());
        return userEntity;
    }

    @Override
    public UserPresentationDTO userToPresentationDTO(UserEntity userEntity) {

        */
/*map the authorities*//*

        List<String> authorities = userEntity.getAuthorities().stream().map(UserAuthority::getAuthority).toList();

        */
/*map user to presentation DTO*//*

        return new UserPresentationDTO(
                userEntity.getUsername(),
                authorities,
                userEntity.getUserRating().getRate()
        );
    }
}
*/
