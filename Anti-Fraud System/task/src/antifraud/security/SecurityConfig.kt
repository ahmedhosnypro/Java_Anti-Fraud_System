package antifraud.security

import antifraud.service.SecurityUserDetailsService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.stereotype.Component


@Configuration
open class SecurityConfig(
    private val securityUserDetailsService: SecurityUserDetailsService,
) {
    @Bean
    open fun passwordEncoder(): BCryptPasswordEncoder? {
        return BCryptPasswordEncoder()
    }

    @Bean
    open fun authenticationProvider(): DaoAuthenticationProvider {
        return DaoAuthenticationProvider().apply {
            setUserDetailsService(securityUserDetailsService)
            setPasswordEncoder(passwordEncoder())
        }
    }

    @Bean
    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf().disable().headers().frameOptions().disable().and().authorizeHttpRequests { authorize ->
            authorize.requestMatchers("/api/auth/user").permitAll()
            authorize.requestMatchers("/actuator/shutdown").permitAll()
            authorize.requestMatchers("/h2-console/**").permitAll()
            authorize.anyRequest().authenticated()
        }.httpBasic()
            .authenticationEntryPoint { _: HttpServletRequest?, response: HttpServletResponse?, authException: org.springframework.security.core.AuthenticationException? ->
                response?.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException?.message)
            }
            .and().authenticationProvider(authenticationProvider()).build()
    }
}