package me.nspain.cubtracking.badgework.config

import com.auth0.spring.security.api.JwtWebSecurityConfigurer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity(debug = true)
open class AppConfig: WebSecurityConfigurerAdapter() {

    @Value(value = "\${auth0.audience}")
    val audience: String? = null

    @Value(value = "\${auth0.issuer}")
    val issuer: String? = null

    @Bean
    open fun corsConfigurationSource(): CorsConfigurationSource {
        val cfg = CorsConfiguration().apply {
            allowedOrigins = listOf("*")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
            allowCredentials = true
            allowedHeaders = listOf("*")
        }

        val src = UrlBasedCorsConfigurationSource()
        src.registerCorsConfiguration("/**", cfg)
        return src
    }

    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity.cors()
        JwtWebSecurityConfigurer
                .forRS256(audience, issuer)
                .configure(httpSecurity)
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/cubs", "/cubs/**", "/achievements", "/achievements/**").authenticated()
                .antMatchers("/**").hasAuthority("leader")
    }

}