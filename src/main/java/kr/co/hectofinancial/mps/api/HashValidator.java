package kr.co.hectofinancial.mps.api;

import kr.co.hectofinancial.mps.util.CryptoUtil;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Hash 검증을 위한 추상 클래스
 * 각 API에서 hash 조합 방식을 정의하고, 비교는 공통으로 수행
 */
@RequiredArgsConstructor
public abstract class HashValidator {

    protected final CryptoUtil cryptoUtil;

    /**
     * Hash 조합 문자열 생성 (각 API에서 구현)
     *
     * @param requestData 요청 데이터
     * @return Hash 조합 문자열
     */
    protected abstract String buildHashData(Map<String, Object> requestData);

    /**
     * Hash 검증 수행 (공통)
     *
     * @param requestData  요청 데이터
     * @param expectedHash 비교할 Hash 값
     * @return Hash 일치 여부
     */
    public boolean validateHash(Map<String, Object> requestData, String expectedHash) {
        String hashData = buildHashData(requestData);
        return cryptoUtil.compareHash(hashData, expectedHash);
    }
}
