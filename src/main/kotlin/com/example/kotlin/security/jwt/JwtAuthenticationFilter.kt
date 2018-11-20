package com.example.kotlin.security.jwt

import com.example.kotlin.security.service.CustomUserDetailsService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter: OncePerRequestFilter() {

    val log = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private val tokenProvider:JwtTokenProvider? = null

    @Autowired
    private val customUserDetailsService: CustomUserDetailsService? = null

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            val jwt:String? = getJwtFromRequest(request)

            if(StringUtils.hasText(jwt) && tokenProvider!!.validateToken(jwt)) {
                val userId:Long? = tokenProvider.getUserIdFormJWT(jwt)

                val userDetails:UserDetails = customUserDetailsService!!.loadUserById(userId!!)
                var authenticationToken:UsernamePasswordAuthenticationToken =
                        UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)

                authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                SecurityContextHolder.getContext().authentication = authenticationToken
            }

        } catch(ex:Exception) {
            log.error("Could not set user authentication in security context", ex)
        }
        filterChain.doFilter(request, response)
    }

    fun getJwtFromRequest(request:HttpServletRequest):String?{
        var bearerToken:String? = request.getHeader("Authorization")
        return if(StringUtils.hasText(bearerToken) && bearerToken!!.startsWith("Bearer ")){
            return bearerToken?.substring(7, bearerToken.length)
        } else null
    }
}
