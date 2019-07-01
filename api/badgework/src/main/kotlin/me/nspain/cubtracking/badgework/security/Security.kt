package me.nspain.cubtracking.badgework.security

import com.auth0.spring.security.api.JwtWebSecurityConfigurer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import java.lang.Exception

@Configuration
@EnableWebSecurity
open class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    private lateinit var environment: Environment

    @Value("\${auth0.audience}")
    private val audience: String? = null

    @Value("\${auth0.issuer}")
    private val issuer: String? = null

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        val disableAuth = environment.getProperty("app.disable_auth", Boolean::class.java)
        if (disableAuth != null && disableAuth) {
            http.authorizeRequests().anyRequest().permitAll()
        } else {
            http.authorizeRequests()
                    .antMatchers("/auth").permitAll()
                    .anyRequest().authenticated()
            JwtWebSecurityConfigurer
                    .forRS256(audience, issuer!!)
                    .configure(http)
        }
    }

}