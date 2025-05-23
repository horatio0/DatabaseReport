package com.example.databaseprogrammingreport.JWT;

import com.example.databaseprogrammingreport.DTO.UserDetailDTO;
import com.example.databaseprogrammingreport.Entity.RefreshTokenEntity;
import com.example.databaseprogrammingreport.Repository.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    //authenticationManager 객체
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private RefreshTokenRepository refreshTokenRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
        //클라이언트 요청에서 ID와 password 추출
        String id = obtainUsername(request);
        String password = obtainPassword(request);

        //ID와 password를 담아 둘 DTO 클래스임
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(id, password, null);

        //authenticationManager 호출
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication){
        UserDetailDTO userDetailDTO = (UserDetailDTO) authentication.getPrincipal();

        //Get User info
        String id = userDetailDTO.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends  GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //Create JWT Tokens
        String access = jwtUtil.createJwt("access", id, role, 600000L);         //10m
        String refresh = jwtUtil.createJwt("refresh", id, role, 86400000L);     //24h

        //Save Refresh Token
        addRefreshToken(refresh, id, 86400000L);

        //Set response headers
        response.addHeader("Authorization", "Bearer " + access);
        response.addCookie(createCookie("refresh", refresh)); // refresh 토큰은 XSS (자바스크립트) 공격에 방어하기 위해 (httponly)쿠키에 담는다
        response.setStatus(200);
    }

    private void addRefreshToken(String refresh, String id, Long expiredAt){

        Date date = new Date(System.currentTimeMillis() + expiredAt);

        RefreshTokenEntity token = new RefreshTokenEntity();
        token.setRefreshToken(refresh);
        token.setMemberId(id);
        token.setExpiredAt(date.toString());

        refreshTokenRepository.save(token);
    }

    private Cookie createCookie(String key, String value){
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);

        return cookie;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed){
        response.setStatus(401);
    }
}