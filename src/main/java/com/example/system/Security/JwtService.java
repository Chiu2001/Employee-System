package com.example.system.Security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.system.Entity.Employee;
import com.example.system.Repo.EmployeeRepo;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey; // 將 SECRET_KEY 注入

    @Autowired
    private EmployeeRepo employeeRepo; // 注入 EmployeeRepo

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 驗證 JWT 是否使用正確的 secret key
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token); // 驗證簽名
            return true; // 簽名有效
        } catch (Exception e) {
            return false; // 簽名無效
        }
    }

    public String generateToken(String email, UserDetails userDetails) {
        // 使用 email 查詢 Employee
        Employee employee = employeeRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        Map<String, Object> claims = new HashMap<>();
        claims.put("rank", employee.getRank().getName()); // 獲取 rank
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        long expirationTimeMillis = 1000 * 60 * 60 * 24; // 24 小時的到期時間
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTimeMillis); // 計算到期時間

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)  // 設置到期時間
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}


