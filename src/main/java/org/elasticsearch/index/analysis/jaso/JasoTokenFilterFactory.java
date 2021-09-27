package org.elasticsearch.index.analysis.jaso;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory;

public class JasoTokenFilterFactory extends AbstractTokenFilterFactory {

    public JasoTokenFilterFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(indexSettings, name, settings);
    }

    @Override
    public TokenStream create(TokenStream stream) {
        return new JasoTokenFilter(stream);
    }

}
