package com.example.databaseprogrammingreport.Controller;

import com.example.databaseprogrammingreport.Entity.RefreshTokenEntity;
import com.example.databaseprogrammingreport.JWT.JWTUtil;
import com.example.databaseprogrammingreport.Repository.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class ReissueController {
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response){

        //Get refresh token
        Cookie[] cookies = request.getCookies();
        String refresh = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        //refresh token 없을 때
        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //만료된 refresh token 일 때
        if (jwtUtil.isExpired(refresh)) {
            return new ResponseEntity<>("refresh token is expired", HttpStatus.BAD_REQUEST);
        }

        //refresh 토큰이 맞는지 확인
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        //DB에 저장된 refresh token 인지
        if (!refreshTokenRepository.existsByRefreshToken(refresh)) {
            return new ResponseEntity<>("blacklisted refresh token", HttpStatus.BAD_REQUEST);
        }

        String id = jwtUtil.getId(refresh);
        String role = jwtUtil.getRole(refresh);

        //New token
        String newAccess = jwtUtil.createJwt("access", id, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", id, role, 86400000L);

        //Refresh token DB에 저장, 기존 토큰 삭제
        refreshTokenRepository.deleteByRefreshToken(refresh);
        addRefreshToken(refresh, id, 86400000L);

        response.setHeader("Authorization", "Bearer " + newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value){
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshToken(String refresh, String id, Long expiredAt){

        Date date = new Date(System.currentTimeMillis() + expiredAt);

        RefreshTokenEntity token = new RefreshTokenEntity();
        token.setRefreshToken(refresh);
        token.setMemberId(id);
        token.setExpiredAt(date.toString());

        refreshTokenRepository.save(token);
    }
}
