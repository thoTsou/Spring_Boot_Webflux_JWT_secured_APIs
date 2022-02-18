package com.example.webFluxJWT.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class User(
    private val serialVersionUID: Long = 1L,
    private val username: String,
    private val password: String,
    private val isAccountNonExpired: Boolean = true,
    private val isAccountNonLocked: Boolean = true,
    private val isCredentialsNonExpired: Boolean = true,
    private val isEnabled: Boolean = true,
    val roles: List<Role>,
): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = this.roles.map { SimpleGrantedAuthority(it.name) }.toMutableList()

    override fun getPassword(): String = this.password

    override fun getUsername(): String = this.username

    override fun isAccountNonExpired(): Boolean = this.isAccountNonExpired

    override fun isAccountNonLocked(): Boolean = this.isAccountNonLocked

    override fun isCredentialsNonExpired(): Boolean = this.isCredentialsNonExpired

    override fun isEnabled(): Boolean = this.isEnabled

    override fun toString(): String {
        return "User(serialVersionUID=$serialVersionUID, username='$username', password='$password', isAccountNonExpired=$isAccountNonExpired, isAccountNonLocked=$isAccountNonLocked, isCredentialsNonExpired=$isCredentialsNonExpired, isEnabled=$isEnabled, roles=$roles)"
    }
}