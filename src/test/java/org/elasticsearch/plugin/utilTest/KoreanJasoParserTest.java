package org.elasticsearch.plugin.utilTest;

import static org.junit.Assert.assertEquals;

import org.elasticsearch.index.common.parser.KoreanJasoParser;
import org.junit.Test;

public class KoreanJasoParserTest {

    @Test
    public void jamoTest() {
        String token = "자바카페";
        KoreanJasoParser parser = new KoreanJasoParser();
        String result = parser.parse(token);

        System.out.println(result);
        assertEquals("ㅈㅏㅂㅏㅋㅏㅍㅔ", result);
    }

    @Test
    public void jamoTest2() {
        String token = "삼성전자";
        KoreanJasoParser parser = new KoreanJasoParser();
        String result = parser.parse(token);

        System.out.println(result);
        assertEquals("ㅅㅏㅁㅅㅓㅇㅈㅓㄴㅈㅏ", result);
    }

}
