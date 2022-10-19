package com.artmarket.controller;

import com.artmarket.config.auth.PrincipalDetail;
import com.artmarket.domain.oauth.KakaoProfile;
import com.artmarket.domain.oauth.OAuthToken;
import com.artmarket.domain.users.Users;
import com.artmarket.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Controller

public class UserController {

    private UserService userService;

    private AuthenticationManager authenticationManager;

    @Value("${art.key}")
    private String artKey;

    @GetMapping("/auth/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping("/auth/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping("/user/updateUser")
    public String updateUser(Model model, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        model.addAttribute("principal", principalDetail);
        return "user/updateUser";
    }

    @GetMapping("/user/updateForm")
    public String updateForm() {
        return "user/updateForm";
    }

    @GetMapping("/auth/kakao/login")
    public String kakaoCallback(String code) { // @ResponseBody : Data를 리턴해주는 컨트롤러 함수

        // 카카오 API 서버에게 POST 방식으로 key=value 데이터를 요청
        // 요청 방법 -> 여러가지 라이브러리  : HttpsURLConnection, Retrofit2(주로 안드로이드), OkHttp, RestTemplate
        RestTemplate rt = new RestTemplate();

        // HttpHeader 객체 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); // key=value 형태의 데이터라는 것을 알려주는 부분

        // HttpBody 객체 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "5e0f4ecd4d8948de94100c8582859170");
        params.add("client_secret", "OGnFPZSktCCT44fGRvSHkpxBaluKlM2g");
        params.add("redirect_uri", "http://localhost:8080/auth/kakao/login");
        params.add("code", code);

        // HttpHeader와 HttpBody를 하나의 객체에 담기 -> 만든 이유 : 아래의 exchange 함수에 HttpEntity를 넣어야 해서..
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers); // body 데이터와 headers 값을 가지고 있는 Entity

        // 카카오에게 Http 요청하기 (POST 방식) -> response라는 변수에 응답을 받음
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // oauthToken에 응답 데이터 담기
        // json 데이터를 자바에서 처리하기 위해 자바 객체로 바꿔야 한다.
        // 객체(현재는 OAuthToken)에 담을 때 사용할 수 있는 라이브러리 : Gson, Json Simple, ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        OAuthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("카카오 엑세스 토큰 : " + oauthToken.getAccess_token());

        // ------------------------------------------------------------------------------------

        RestTemplate rt2 = new RestTemplate();

        // HttpHeader 객체 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); // key=value 형태의 데이터라는 것을 알려주는 부분

        // HttpHeader를 객체에 담기 -> 만든 이유 : 아래의 exchange 함수에 HttpEntity를 넣어야 해서..
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 =
                new HttpEntity<>(headers2); // headers 값을 가지고 있는 Entity

        // 카카오에게 Http 요청하기 (POST 방식) -> response2라는 변수에 응답을 받음
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest2,
                String.class
        );

        ObjectMapper objectMapper2 = new ObjectMapper();

        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // User 오브젝트 : (id), username, password, email, role, createDate
        System.out.println("카카오 아이디(번호) : " + kakaoProfile.getId());
        System.out.println("카카오 이메일 : " + kakaoProfile.getKakao_account().getEmail());

        System.out.println("블로그서버 유저네임 : " + kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId());
        System.out.println("블로그서버 이메일 : " + kakaoProfile.getKakao_account().getEmail());
        UUID garbagePassword = UUID.randomUUID();
        // UUID란 중복되지 않는 어떤 특정 값을 만들어내는 알고리즘
        System.out.println("블로그서버 패스워드 : " + artKey);

        Users kakaoUser= Users.builder()
                .username(kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId())
                .password(artKey)
                .email(kakaoProfile.getKakao_account().getEmail())
                .build();

        // 가입자 혹은 비가입자 분기 (이미 회원인지 아닌지 체크)
        Users originUser = userService.findByUserName(kakaoUser.getUsername());
//
        // 비가입자(null)이면, 회원가입 후 로그인 처리
        if (originUser.getUsername() == null) {
            System.out.println("기존 회원이 아니기에 자동으로 회원가입을 진행합니다.");
            userService.findByUserName(kakaoUser);
        }

        System.out.println("자동 로그인을 진행합니다.");

        // 가입자이면 회원가입 없이 로그인 처리
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), artKey));
        SecurityContextHolder.getContext().setAuthentication(authentication);
//

//        return "카카오 인증 완료 : 코드값 : " + code;
//        return "카카오 토큰 요청 완료 : 토큰요청에 대한 응답 : " + response;
//        return "액세스 토큰으로 회원정보 요청 : 회원정보 요청에 대한 응답 : " + response2.getBody();
        return "redirect:/";
    }

}
