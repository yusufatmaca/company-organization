package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.exceptions.APIException;
import com.deltasmarttech.companyorganization.models.User;
import com.deltasmarttech.companyorganization.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CustomSecurityExpressionRoot {

    @Autowired
    private UserRepository userRepository;

    public boolean isAdminOrAccountOwner(String email) {

        User user = userRepository.findByEmail(email).orElse(null);
        if (!user.isActive()) {
            throw new APIException("User is already inactive!");
        }

        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();


        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        String currentUsername = authentication.getName();
        return user.getEmail().equals(currentUsername);
    }
}
