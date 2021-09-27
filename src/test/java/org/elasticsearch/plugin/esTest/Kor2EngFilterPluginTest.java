package org.elasticsearch.plugin.esTest;

import java.io.IOException;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.plugin.analysis.KoreanFilterPlugin;


public class Kor2EngFilterPluginTest extends AbstractPluginTest {


    /**
     * 한영 오타 변환기를 테스트한다.
     *
     * @throws IOException
     */
    public void test() throws Exception {

        String source = "ㅓㅁㅍㅁㅊㅁㄹㄷ ㅑㅔㅗㅐㅜㄷ";

        String[] result = new String[]{
                "javacafe",
                "iphone"
        };

        String filterName = "kor2eng_filter";


        // 실행
        TestAnalysis analysis = createTestAnalysis(
                new Index("test", ""), Settings.builder().build(), new KoreanFilterPlugin()
        );

        TokenFilterFactory myFilter = analysis.tokenFilter.get(filterName);
        runFilter(myFilter, source, result);
    }



}
