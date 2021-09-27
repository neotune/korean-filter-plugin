package org.elasticsearch.index.common.parser;

import com.google.common.primitives.Chars;
import org.elasticsearch.index.common.util.JasoUnicodeConstant;

public class KoreanJasoParser extends AbstractKoreanParser {
    private char[] doubleJongsungList = { 'ㄲ', 'ㄳ', 'ㄵ', 'ㄶ', 'ㄸ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅃ', 'ㅄ', 'ㅆ', 'ㅉ' };
    private String[] divideJongsungList = { "ㄱㄱ", "ㄱㅅ", "ㄴㅈ", "ㄴㅎ", "ㄷㄷ", "ㄹㄱ", "ㄹㅁ", "ㄹㅂ", "ㄹㅅ", "ㄹㅌ", "ㄹㅍ", "ㄹㅎ", "ㅂㅂ", "ㅂㅅ", "ㅅㅅ", "ㅈㅈ" };

    @Override
    protected void processForKoreanChar(StringBuilder sb, char chosung, char jungsung, char jongsung) {
        sb.append(chosung).append(jungsung);

        if (jongsung != JasoUnicodeConstant.UNICODE_JONG_SUNG_EMPTY) {
            sb.append(jongsung);
        }
    }

    @Override
    protected void processForOther(StringBuilder sb, char eachToken) {
        sb.append(eachToken);
    }

    private String separateDoubleConsonant(char jongsung) {
        return isDoubleConsonant(jongsung) ? separateConsonant(jongsung) : String.valueOf(jongsung);
    }

    private boolean isDoubleConsonant(char jongsung) {
        return Chars.indexOf(doubleJongsungList, jongsung) > -1;
    }

    private String separateConsonant(char jongsung) {
        int index = Chars.indexOf(doubleJongsungList, jongsung);
        if (index == -1)
            return String.valueOf(jongsung);
        return divideJongsungList[index];
    }

}


