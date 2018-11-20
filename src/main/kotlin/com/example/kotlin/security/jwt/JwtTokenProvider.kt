package com.example.kotlin.security.jwt

import com.example.react.security.model.UserPrincipal
import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider {

    val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${app.jwtSecret}")
    private val jwtSecret: String? = null

    @Value("\${app.jwtExpirationInMs}")
    private val jwtExpirationInMs: Int = 0


    fun generateToken(authentication: Authentication) : String {
        var userPrincipal:UserPrincipal = authentication.principal as UserPrincipal

        var now:Date = Date()
        var expiryDate:Date = Date(now.time + jwtExpirationInMs)

        return Jwts.builder()
                .setSubject(userPrincipal.id.toString())
                .setIssuedAt(Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact()
    }

    fun getUserIdFormJWT(token:String?):Long {
         var claims:Claims = Jwts.parser()
                 .setSigningKey(jwtSecret)
                 .parseClaimsJws(token)
                 .body

        return claims.subject.toLong()
    }

    fun validateToken(authToken:String?):Boolean {
        try{
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken)
            return true
        } catch (ex : SignatureException) {
            log.error("Invalid JWT signature")
        } catch (ex : MalformedJwtException) {
            log.error("Invalid JWT token")
        } catch (ex : ExpiredJwtException) {
            log.error("Expired JWT toke")
        } catch (ex : UnsupportedJwtException) {
            log.error("Unsupported JWT token")
        } catch (ex : IllegalArgumentException) {
            log.error("JWT claims string is empty")
        }

        return false
    }
}