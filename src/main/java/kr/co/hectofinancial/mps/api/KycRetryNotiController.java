package kr.co.hectofinancial.mps.api;

import kr.co.hectofinancial.mps.util.CryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class KycRetryNotiController {

    private final KycRetryNotiHashValidator hashValidator;
    private final CryptoUtil cryptoUtil;

    public KycRetryNotiController(CryptoUtil cryptoUtil) {
        this.cryptoUtil = cryptoUtil;
        this.hashValidator = new KycRetryNotiHashValidator(cryptoUtil);
    }

    /**
     * KYC 재시도 알림 API - 복호화 및 Hash 검증 수행
     * POST /test/kyc/retry/noti
     *
     * @param requestBody 요청 데이터 (Map)
     * @return "OK"
     */
    @PostMapping("/test/kyc/retry/noti")
    public String kycRetryNoti(@RequestBody Map<String, Object> requestBody) {
        log.info("========== /test/kyc/retry/noti API 요청 시작 ==========");

        // 요청 값 로그 출력 (복호화 전)
        for (Map.Entry<String, Object> entry : requestBody.entrySet()) {
            log.info("[원본] Key: {}, Value: {}", entry.getKey(), entry.getValue());
        }

        // 복호화 대상 필드 처리 (예시: encryptedData 필드)
        decryptAndLogFields(requestBody);

        // Hash 검증
        String hashValue = (String) requestBody.get("hash");
        if (hashValue != null) {
            boolean isHashValid = hashValidator.validateHash(requestBody, hashValue);
            log.info("Hash 검증 결과: {}", isHashValid ? "성공" : "실패");
        } else {
            log.warn("Hash 값이 없습니다.");
        }

        log.info("========== /test/kyc/retry/noti API 요청 종료 ==========");

        return "OK";
    }

    /**
     * 요청 데이터의 암호화된 필드를 복호화하여 로그 출력
     *
     * @param requestBody 요청 데이터
     */
    private void decryptAndLogFields(Map<String, Object> requestBody) {
        // TODO: 복호화 대상 필드 목록 정의 필요
        String[] encryptedFields = {"encryptedData"};

        for (String fieldName : encryptedFields) {
            Object value = requestBody.get(fieldName);
            if (value != null) {
                String decrypted = cryptoUtil.decryptAndGet(value.toString());
                log.info("[복호화] Key: {}, Value: {}", fieldName, decrypted);
            }
        }
    }

    /**
     * KYC 재시도 알림 API용 Hash Validator
     */
    @Component
    public static class KycRetryNotiHashValidator extends HashValidator {

        public KycRetryNotiHashValidator(CryptoUtil cryptoUtil) {
            super(cryptoUtil);
        }

        /**
         * KYC 재시도 알림 API의 Hash 조합 방식 정의
         *
         * @param requestData 요청 데이터
         * @return Hash 조합 문자열
         */
        @Override
        protected String buildHashData(Map<String, Object> requestData) {
            // TODO: API별 Hash 조합 방식 정의
            // 예시: field1 + field2 + field3
            StringBuilder sb = new StringBuilder();

            // 필요한 필드들을 조합
            // sb.append(requestData.getOrDefault("field1", ""));
            // sb.append(requestData.getOrDefault("field2", ""));
            // sb.append(requestData.getOrDefault("field3", ""));

            return sb.toString();
        }
    }
}
