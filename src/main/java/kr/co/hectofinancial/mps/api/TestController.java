package kr.co.hectofinancial.mps.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class TestController {

    /**
     * 요청 값 로그 출력 API
     * POST /test
     *
     * @param requestBody 요청 데이터 (Map)
     * @return "OK"
     */
    @PostMapping("/test")
    public String test(@RequestBody Map<String, Object> requestBody) {
        log.info("========== /test API 요청 시작 ==========");

        for (Map.Entry<String, Object> entry : requestBody.entrySet()) {
            log.info("Key: {}, Value: {}", entry.getKey(), entry.getValue());
        }

        log.info("========== /test API 요청 종료 ==========");

        return "OK";
    }
}
