package antifraud.security

import antifraud.user.service.SecurityUserDetailsService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
open class SecurityConfig(
    private val securityUserDetailsService: SecurityUserDetailsService,
) {
    @Bean
    open fun authenticationProvider(): DaoAuthenticationProvider {
        return DaoAuthenticationProvider().apply {
            setUserDetailsService(securityUserDetailsService)
            setPasswordEncoder(bCryptPasswordEncoder())
        }
    }

    @Bean
    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf().disable().headers().frameOptions().disable().and().authorizeHttpRequests { authorize ->
            authorize.requestMatchers("/api/auth/user").permitAll()
            authorize.requestMatchers("/api/auth/user/**").hasAuthority("ADMIN_PRIVILEGE")
            authorize.requestMatchers("/api/auth/list/**").hasAuthority("LIST_USER_PRIVILEGE")
            authorize.requestMatchers("api/antifraud/suspicious-ip/**").hasAuthority("SUSPICIOUS_IP_PRIVILEGE")
            authorize.requestMatchers("api/antifraud/stolencard/**").hasAuthority("STOLEN_CARD_PRIVILEGE")
            authorize.requestMatchers("/api/auth/role/**").hasAuthority("ADMIN_PRIVILEGE")
            authorize.requestMatchers("/api/auth/access/**").hasAuthority("ADMIN_PRIVILEGE")
            authorize.requestMatchers("/api/antifraud/transaction/**").hasAuthority("CHECK_TRANSACTION_PRIVILEGE")
            authorize.requestMatchers("/actuator/shutdown").permitAll()
            authorize.requestMatchers("/h2-console/**").permitAll()
            authorize.anyRequest().authenticated()
        }.httpBasic()
            .authenticationEntryPoint { _: HttpServletRequest?, response: HttpServletResponse?, authException: org.springframework.security.core.AuthenticationException? ->
                response?.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException?.message)
            }.and().authenticationProvider(authenticationProvider()).build()
    }

    companion object {
        @Bean
        fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
            return BCryptPasswordEncoder()
        }

        @Bean
        fun roleHierarchy(): RoleHierarchy {
            val roleHierarchy = RoleHierarchyImpl()
            val hierarchy = """
                ROLE_ADMINISTRATOR > ROLE_SUPPORT
            """.trimIndent()
            roleHierarchy.setHierarchy(hierarchy)
            return roleHierarchy
        }
    }
}