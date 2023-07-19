package antifraud.security

import antifraud.service.SecurityUserDetailsService
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
class SecurityConfig(
    private val securityUserDetailsService: SecurityUserDetailsService,
) {
    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        return DaoAuthenticationProvider().apply {
            setUserDetailsService(securityUserDetailsService)
            setPasswordEncoder(bCryptPasswordEncoder())
        }
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf().disable().headers().frameOptions().disable().and().authorizeHttpRequests { authorize ->
            authorize.requestMatchers("/actuator/shutdown").permitAll()
            authorize.requestMatchers("/h2-console/**").permitAll()
            authorize.requestMatchers("/api/auth/user").permitAll()
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
            """.trimIndent()
            roleHierarchy.setHierarchy(hierarchy)
            return roleHierarchy
        }
    }
}