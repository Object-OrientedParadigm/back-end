package com.example.todo.oauth.persentation;

import com.example.todo.oauth.application.OauthService;
import com.example.todo.oauth.domain.OauthServerType;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/oauth")
@RestController
public class OauthController {

    private final OauthService oauthService;

    @SneakyThrows
    @GetMapping("/{oauthServerType}")
    ResponseEntity<Void> redirectAuthCodeRequestUrl(
            @PathVariable OauthServerType oauthServerType,
            HttpServletResponse response
    ) {
        String redirectUrl = oauthService.getAuthCodeRequestUrl(oauthServerType);
        response.sendRedirect(redirectUrl);
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/login/{oauthServerType}")
//    ResponseEntity<Long> login(
//            @PathVariable OauthServerType oauthServerType,
//            @RequestParam("code") String code
//    ) {
//        Long login = Long.valueOf(oauthService.login(oauthServerType, code));
//        return ResponseEntity.ok(login);
//    }

    @GetMapping("/login/{oauthServerType}")
    public ResponseEntity<String> login(@PathVariable OauthServerType oauthServerType, @RequestParam("code") String code) {
        String jwt = oauthService.login(oauthServerType, code);
        return ResponseEntity.ok(jwt);
    }
}