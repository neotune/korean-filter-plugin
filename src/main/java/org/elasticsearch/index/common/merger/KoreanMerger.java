package org.elasticsearch.index.common.merger;

import java.util.List;

import org.elasticsearch.index.common.util.HangulUtil;

public class KoreanMerger {

    /**
     * 분리된 자소 리스트를 합쳐서 한글로 변환한다.
     *
     * @param jasoList
     * @return
     * @throws Exception
     */
    public String merge(List<String> jasoList) throws Exception {
        String result = "";

        if (jasoList.size() == 0) {
            return "";
        }

        int jungSungSize = HangulUtil.JUNG_SUNG.length;
        int jongSungSize = HangulUtil.JONG_SUNG.length;

        int startIdx = 0;
        while (true) {
            if (startIdx >= jasoList.size()) {
                break;
            }

            // 자소 리스트에서 한글 한글자에 해당하는 사이즈를 구한다.
            int oneHangulJasoSize = HangulUtil.getOneHangulJasoSize(startIdx, jasoList);
            if (oneHangulJasoSize == -1) {
                throw new Exception("한글은 최소 2개 이상의 유니코드 조합으로 이루어져야 합니다.");
            }

            // 한글 유니코드가 시작되는 Decimal 값 을 구한다.
            int decimalCode = HangulUtil.START_KOREA_UNICODE_DECIMAL;

            // 초성에 해당하는 Decimal 값 을 더한다.
            int chosungIdx = HangulUtil.getChoSungIndex(startIdx, jasoList);
            if (chosungIdx >= 0) {
                decimalCode = decimalCode + (jongSungSize * jungSungSize * chosungIdx);
            }

            // 중성에 해당하는 Decimal 값 을 더한다.
            int jungsungIdx = HangulUtil.getJungSungIndex(startIdx, jasoList);
            if (jungsungIdx >= 0) {
                decimalCode = decimalCode + (jongSungSize * jungsungIdx);
            }

            // 종성에 해당하는 Decimal 값 을 더한다.
            if (oneHangulJasoSize > 2) {
                int jongsungIdx = HangulUtil.getJongSungIndex(startIdx, jasoList);
                if (jongsungIdx >= 0) {
                    decimalCode = decimalCode + jongsungIdx;
                }
            }

            // Decimal값을 String으로 변환한다.
            String hangul = Character.toString((char) decimalCode);
            result = result + hangul;

            startIdx = startIdx + oneHangulJasoSize;
        }

        return result;
    }

}


