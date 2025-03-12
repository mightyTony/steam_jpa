package com.example.steam.domain.order.kakao;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.order.Order;
import com.example.steam.domain.order.dto.KakaoPayReadyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KakaoPayClient {

    @Value("${kakaopay.secret-key}")
    private String secretKey;
    @Value("${kakaopay.cid}")
    private String cid;
    @Value("${kakaopay.url.ready}")
    private String readyUrl;
    @Value("${kakaopay.url.approval}")
    private String approvalUrl;
    @Value("${kakaopay.url.cancel}")
    private String cancelUrl;
    @Value("${kakaopay.url.fail}")
    private String failUrl;

    private final RestTemplate restTemplate;

    // 카카오 요구 헤더
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY " + secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public KakaoPayReadyResponse requestReadyPayment(Long userId, Order order, List<Game> games) {
        // 카카오페이 요청 데이터 생성
        String itemName;
        if (games.size() > 1) {
            itemName = games.get(0).toString() + " 외" + (games.size()-1) + " 상품" ;
        } else {
            itemName = games.get(0).toString();
        }
        KakaoPayReadyRequest requestBody = KakaoPayReadyRequest.builder()
                .cid(cid)
                .partnerOrderId(order.getId().toString())
                .partnerUserId(userId.toString())
                .itemName(itemName)
                .totalAmount(order.getTotalPrice())
                .quantity(order.getOrderItems().size())
                .taxFreeAmount(0)
                .vatAmount(0)
                .approvalUrl(approvalUrl)
                .cancelUrl(cancelUrl)
                .failUrl(failUrl)
                .build();

        // 카카오페이 API호출
        ResponseEntity<KakaoPayReadyResponse> responseEntity = restTemplate.postForEntity
                (readyUrl, new HttpEntity<>(requestBody, getHeaders()), KakaoPayReadyResponse.class);

        // 응답객체 바디 반환
        return responseEntity.getBody();
    }
}
