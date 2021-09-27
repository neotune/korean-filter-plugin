package org.elasticsearch.plugin.analysis;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.index.analysis.chosung.ChosungTokenFilterFactory;
import org.elasticsearch.index.analysis.eng2kor.Eng2KorConvertFilterFactory;
import org.elasticsearch.index.analysis.jaso.JasoTokenFilterFactory;
import org.elasticsearch.index.analysis.kor2eng.Kor2EngConvertFilterFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

public class KoreanFilterPlugin extends Plugin implements AnalysisPlugin {

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenFilterFactory>> getTokenFilters() {
        Map<String, AnalysisModule.AnalysisProvider<TokenFilterFactory>> extra = new HashMap<>();

        // 한글 자소 분리 필터
        extra.put("jaso_filter", JasoTokenFilterFactory::new);

        // 한글 초성 분리 필터
        extra.put("chosung_filter", ChosungTokenFilterFactory::new);

        // 영한 오타 변환 필터
        extra.put("eng2kor_filter", Eng2KorConvertFilterFactory::new);

        // 한영 오타 변환 필터
        extra.put("kor2eng_filter", Kor2EngConvertFilterFactory::new);

        return extra;
    }

}



