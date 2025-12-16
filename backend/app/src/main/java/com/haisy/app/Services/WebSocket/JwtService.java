package com.haisy.app.Services.WebSocket;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {
    
    String secret = "junaidArmaansSecretKeyForJwtSigning"; 

    byte[] secretKey = secret.getBytes(StandardCharsets.UTF_8);
    
    public String generateToken(String userName){
        Date expiry = new Date(System.currentTimeMillis()+1000*60*15);
        String token = Jwts.builder()
        .setSubject(userName).setIssuedAt(new Date()).setExpiration(expiry).signWith(SignatureAlgorithm.HS256,secretKey).compact();
        return token;
    }
}
