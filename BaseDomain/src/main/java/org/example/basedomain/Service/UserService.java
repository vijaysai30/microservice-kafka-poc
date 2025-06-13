package org.example.basedomain.Service;

import org.example.basedomain.Dto.UserDetailsDTO;
import org.example.basedomain.Entity.UserEntity;
import org.example.basedomain.Repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserRepo userRepo;

    public UserEntity createUser(UserEntity userEntity) {
        return userRepo.save(userEntity);
    }

    @Override
    public UserEntity loadUserByUsername(String userName) {
        if(userName == null) {
            throw new UsernameNotFoundException("User name not found");
        }
        return userRepo.findByUsername(userName).orElseThrow(
                () -> new UsernameNotFoundException("User name not found")
        );
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserDetailsDTO> getListOfUser() {
        List<UserEntity> listOfUser= userRepo.findAll();
        List<UserDetailsDTO> listOfUserDTO= new ArrayList<>();
        for(UserEntity user:listOfUser) {
            listOfUserDTO.add(new UserDetailsDTO(user.getUsername(),user.getEmail(),user.getRole()));
        }
        return listOfUserDTO;
    }


}
