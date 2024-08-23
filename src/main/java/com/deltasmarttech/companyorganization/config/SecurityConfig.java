package com.deltasmarttech.companyorganization.config;

import com.deltasmarttech.companyorganization.exceptions.CustomAccessDeniedHandler;
import com.deltasmarttech.companyorganization.models.*;
import com.deltasmarttech.companyorganization.repositories.*;
import jakarta.transaction.Transactional;
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

    @Autowired
    private final CustomAccessDeniedHandler accessDeniedHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize)->authorize
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/v2/api-docs",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/swagger-ui.html"
                        )
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
                .exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Transactional
    @Bean
    public CommandLineRunner initData(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            CityRepository cityRepository,
            RegionRepository regionRepository,
            TownRepository townRepository,
            CompanyRepository companyRepository,
            CompanyTypeRepository companyTypeRepository,
            DepartmentTypeRepository departmentTypeRepository,
            DepartmentHierarchyRepository departmentHierarchyRepository,
            DepartmentRepository departmentRepository) {

        return args -> {

            // Set<Role> adminRoles = Set.of(adminRole);
            // Set<Role> managerRoles = Set.of(managerRole);
            // Set<Role> employeeRoles = Set.of(employeeRole);

            City city = cityRepository.findByName("İzmir")
                    .orElseGet(() -> {
                        City izmir = new City("İzmir");
                        return cityRepository.save(izmir);
                    });

            Region region = regionRepository.findByName("İzmir Güney")
                            .orElseGet(() -> {
                               Region izmirGuney = new Region("İzmir Güney");
                               izmirGuney.setCity(city);
                               return regionRepository.save(izmirGuney);
                            });

            Town town = townRepository.findByName("Urla")
                            .orElseGet(() -> {
                               Town urla = new Town("Urla");
                               urla.setRegion(region);
                               return townRepository.save(urla);
                            });

            CompanyType companyType = companyTypeRepository.findByName("Yazılım Geliştirme")
                            .orElseGet(() -> {
                                CompanyType yazilimGelistirme = new CompanyType("Yazılım Geliştirme", true);
                                return companyTypeRepository.save(yazilimGelistirme);
                            });

            Company company = companyRepository.findByName("Delta Akıllı Teknolojiler A.Ş.")
                            .orElseGet(() -> {
                                Company delta = new Company(
                                        "Delta Akıllı Teknolojiler A.Ş.",
                                        "Delta",
                                        companyType,
                                        "Teknopark İzmir A8 Binası",
                                        town);
                               return companyRepository.save(delta);
                            });

            DepartmentType departmentType1 = departmentTypeRepository.findByName("Yönetsel")
                    .orElseGet(() -> {
                       DepartmentType yonetsel = new DepartmentType("Yönetsel", true);
                        return departmentTypeRepository.save(yonetsel);
                    });

            DepartmentType departmentType2 = departmentTypeRepository.findByName("Operasyonel")
                    .orElseGet(() -> {
                        DepartmentType operasyonel = new DepartmentType("Operasyonel", true);
                        return departmentTypeRepository.save(operasyonel);
                    });

            Department department1 = departmentRepository.findByName("Genel Müdürlük")
                    .orElseGet(() -> {
                       Department genelMudurluk = new Department(
                               "Genel Müdürlük",
                               company,
                               departmentType1,
                               town,
                               "Teknopark İzmir A8 Binası",
                               true
                       );
                       return departmentRepository.save(genelMudurluk);
                    });

            Department department2 = departmentRepository.findByName("Yazılım Geliştirme")
                    .orElseGet(() -> {
                        Department genelMudurluk = new Department(
                                "Yazılım Geliştirme",
                                company,
                                departmentType2,
                                town,
                                "Teknopark İzmir A8 Binası",
                                true
                        );
                        return departmentRepository.save(genelMudurluk);
                    });

            DepartmentHierarchy departmentHierarchy = departmentHierarchyRepository.findByParentDepartmentAndChildDepartment(department1, department2)
                    .orElseGet(() -> {
                       DepartmentHierarchy genelMudurlukAndYazilimGelistirme = new DepartmentHierarchy(department1, department2);

                       return departmentHierarchyRepository.save(genelMudurlukAndYazilimGelistirme);
                    });

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


            User systemAdministrator = userRepository.findByEmail("admin@delta.smart")
                    .orElseGet(() -> {
                        User admin = new User(department1, "System", "Administrator", "admin@delta.smart", passwordEncoder.encode("E4c-p7*K"), true, true, adminRole);

                        return userRepository.save(admin);
                    });

            companyRepository.flush();

        };
    }


}
