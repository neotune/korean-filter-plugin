package org.elasticsearch.index.analysis.jaso;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.elasticsearch.index.common.parser.KoreanJasoParser;

public final class JasoTokenFilter extends TokenFilter {

    private KoreanJasoParser parser;
    private CharTermAttribute termAtt;

    public JasoTokenFilter(TokenStream stream) {
        super(stream);
        this.parser = new KoreanJasoParser();
        this.termAtt = addAttribute(CharTermAttribute.class);
    }

    @Override
    public boolean incrementToken() throws IOException {

        if (input.incrementToken()) {
            CharSequence parserdData = parser.parse(termAtt.toString());
            termAtt.setEmpty();
            termAtt.resizeBuffer(parserdData.length());
            termAtt.append(parserdData);
            termAtt.setLength(parserdData.length());

            return true;
        }

        return false;
    }

}
