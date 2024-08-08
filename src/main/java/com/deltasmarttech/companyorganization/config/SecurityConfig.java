package com.deltasmarttech.companyorganization.config;

import com.deltasmarttech.companyorganization.models.AppRole;
import com.deltasmarttech.companyorganization.models.Permission;
import com.deltasmarttech.companyorganization.models.Role;
import com.deltasmarttech.companyorganization.repositories.RoleRepository;
import com.deltasmarttech.companyorganization.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize)->authorize
                        .requestMatchers("/api/v1/auth/**")
                        .permitAll()

                        //MANAGER ENDPOINTS
                        .requestMatchers("/api/v1/manager/**").hasAnyRole(AppRole.ADMIN.name(), AppRole.MANAGER.name())
                        .requestMatchers(GET,"/api/v1/manager/**").hasAnyAuthority(Permission.ADMIN_READ.name(), Permission.MANAGER_READ.name())
                        .requestMatchers(POST,"/api/v1/manager/**").hasAnyAuthority(Permission.ADMIN_CREATE.name(), Permission.MANAGER_CREATE.name())
                        .requestMatchers(PUT,"/api/v1/manager/**").hasAnyAuthority(Permission.ADMIN_UPDATE.name(), Permission.MANAGER_UPDATE.name())
                        .requestMatchers(DELETE,"/api/v1/manager/**").hasAnyAuthority(Permission.ADMIN_DELETE.name(), Permission.MANAGER_DELETE.name())

                        //ADMIN ENDPOINTS
                        .requestMatchers("/api/v1/admin/**").hasRole(AppRole.ADMIN.name())
                        .requestMatchers(GET,"/api/v1/admin/**").hasAuthority(Permission.ADMIN_READ.name())
                        .requestMatchers(POST,"/api/v1/admin/**").hasAuthority(Permission.ADMIN_CREATE.name())
                        .requestMatchers(PUT,"/api/v1/admin/**").hasAuthority(Permission.ADMIN_UPDATE.name())
                        .requestMatchers(DELETE,"/api/v1/admin/**").hasAuthority(Permission.ADMIN_DELETE.name())

                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement((session)->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

  @Bean
  public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
    return args -> {
        // Retrieve or create roles
        Role adminRole = roleRepository.findByRoleName(AppRole.ADMIN)
                .orElseGet(() -> {
                    Role newAdminRole = new Role(AppRole.ADMIN);
                    return roleRepository.save(newAdminRole);
                });


        Role managerRole = roleRepository.findByRoleName(AppRole.MANAGER)
          .orElseGet(() -> {
              Role newManagerRole = new Role(AppRole.MANAGER);
              return roleRepository.save(newManagerRole);
          });

        Role employeeRole = roleRepository.findByRoleName(AppRole.EMPLOYEE)
                .orElseGet(() -> {
                    Role newEmployeeRole = new Role(AppRole.EMPLOYEE);
                    return roleRepository.save(newEmployeeRole);
                });

        Set<Role> adminRoles = Set.of(adminRole);
        Set<Role> managerRoles = Set.of(managerRole);
        Set<Role> employeeRoles = Set.of(employeeRole);
    };
  }

}
