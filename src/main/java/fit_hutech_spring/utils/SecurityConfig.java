package fit_hutech_spring.utils;

import fit_hutech_spring.services.OAuthService;
import fit_hutech_spring.services.UserService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Import mới
import org.springframework.security.core.context.SecurityContextHolder; // Import mới
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken; // Import mới
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList; // Import mới
import java.util.List; // Import mới

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

        @Autowired
        private OAuthService oAuthService;

        @Autowired
        private UserService userService;

        @Bean
        public UserDetailsService userDetailsService() {
                return userService;
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                        PasswordEncoder passwordEncoder) {
                var auth = new DaoAuthenticationProvider(userDetailsService);
                auth.setPasswordEncoder(passwordEncoder);
                return auth;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(@NotNull HttpSecurity http) throws Exception {
                return http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/css/**", "/js/**", "/", "/oauth/**", "/register",
                                                                "/error")
                                                .permitAll()
                                                .requestMatchers("/books/edit/**", "/books/add", "/books/delete",
                                                                "/categories/**")
                                                .hasAnyAuthority("ADMIN")
                                                .requestMatchers("/books", "/cart", "/cart/**")
                                                .hasAnyAuthority("ADMIN", "USER")
                                                .requestMatchers("/api/**")
                                                .hasAnyAuthority("ADMIN", "USER")
                                                .anyRequest().authenticated())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login")
                                                .deleteCookies("JSESSIONID")
                                                .invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .permitAll())
                                .formLogin(formLogin -> formLogin
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .defaultSuccessUrl("/")
                                                .failureUrl("/login?error")
                                                .permitAll())
                                .oauth2Login(oauth2Login -> oauth2Login
                                                .loginPage("/login")
                                                .failureUrl("/login?error")
                                                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                                                .userService(oAuthService))
                                                .successHandler((request, response, authentication) -> {
                                                        // 1. Lấy thông tin user
                                                        var oidcUser = (DefaultOidcUser) authentication.getPrincipal();
                                                        var email = oidcUser.getEmail();
                                                        var name = oidcUser.getName();
                                                        String provider = ((OAuth2AuthenticationToken) authentication)
                                                                        .getAuthorizedClientRegistrationId();

                                                        // 2. Lưu vào DB
                                                        userService.saveOauthUser(email, name, provider);

                                                        // 3. CẤP QUYỀN USER (Đoạn sửa lỗi ở đây)
                                                        // Thay vì dùng 'var', hãy khai báo rõ List<GrantedAuthority>
                                                        List<GrantedAuthority> authorities = new ArrayList<>(
                                                                        authentication.getAuthorities());

                                                        // Bây giờ bạn có thể add thoải mái
                                                        authorities.add(new SimpleGrantedAuthority("USER"));

                                                        // Tạo Authentication mới
                                                        var newAuth = new OAuth2AuthenticationToken(
                                                                        oidcUser,
                                                                        authorities,
                                                                        provider);

                                                        SecurityContextHolder.getContext().setAuthentication(newAuth);

                                                        response.sendRedirect("/");
                                                })
                                                .permitAll())
                                .rememberMe(rememberMe -> rememberMe
                                                .key("hutech")
                                                .rememberMeCookieName("hutech")
                                                .tokenValiditySeconds(24 * 60 * 60)
                                                .userDetailsService(userDetailsService()))
                                .sessionManagement(sessionManagement -> sessionManagement
                                                .maximumSessions(1)
                                                .expiredUrl("/login"))
                                .httpBasic(httpBasic -> httpBasic
                                                .realmName("hutech"))
                                .build();
        }
}