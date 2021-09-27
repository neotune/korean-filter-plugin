# elasticsearch korean-filter-plugin

[엘라스틱서치 실무가이드][1] 책에서 제공되는 자동완성 플러그인을 수정한 플러그인 입니다   
[javacafe-project/elasticsearch-elasticsearch-plugin][2] 기반으로 작성된 플러그인 이며 코드에 대한 권리는 해당 repo 에 있습니다    
코드 수정 및 이용에 문제가 될 경우 언제든지 연락 주시면 바로 처리 하겠습니다   
수정된 사항은 아래와 같습니다

1. javacafe 네이밍 삭제
2. kor2eng 시 "가" 글자 변환 안되는 버그 수정
3. spell check filter 삭제
4. solr 관련 코드 삭제
5. es 7.14.1, lucene 8.9.0 으로 변경
6. TokenFilter 작동시 term length 및 resize 하지 않는 문제 수정
7. 사용하지 않는 class 삭제
8. jamo 를 jaso 로 명칭 변경
9. 변수 및 클래스명 수정 ex) JamoUtil -> jasoUnicodeConstant



## 필터 리스트

1. 한글 자소 분리 필터 - jaso_filter
2. 한글 초성 분리 필터 - chosung_filter
3. 영한 오타 변환 필터 - eng2kor_filter
4. 한영 오타 변환 필터 - kor2eng_filter

## 개발환경

```
maven
elasticsearch 7.14.1
```

## 플러그인 빌드 방법

#### 1. git clone
```shell
git clone https://github.com/neotune/korean-filter-plugin.git
```

#### 2. mvn package
```shell
mvn package
```

#### 3. zip file path
```shell
/korean-filter-plugin/target/releases/korean-filter-plugin-7.14.1.zip
```


## 사용방법

#### 1. elasticsearch index settings / mappings
```shell
curl -XPUT -H 'Content-Type: application/json' http://localhost:9200/filter-test/ -d '{
  "settings": {
    "analysis": {
      "analyzer": {
        "jaso_analyzer": {
          "tokenizer": "keyword",
          "filter": [
            "jaso_filter",
            "lowercase",
            "whitespace_remove"
          ]
        },
        "chosung_analyzer": {
          "tokenizer": "keyword",
          "filter": [
            "chosung_filter",
            "lowercase",
            "whitespace_remove"
          ]
        },
        "eng2kor_analyzer": {
          "tokenizer": "keyword",
          "filter": [
            "eng2kor_filter",
            "lowercase",
            "whitespace_remove"
          ]
        },
        "kor2eng_analyzer": {
          "tokenizer": "keyword",
          "filter": [
            "kor2eng_filter",
            "lowercase",
            "whitespace_remove"
          ]
        }
      },
      "filter": {
        "whitespace_remove": {
          "type": "pattern_replace",
          "pattern": " ",
          "replacement": ""
        }
      }
    }
  },
  "mappings": {
    "dynamic": "strict",
    "properties": {
      "keyword": {
        "type": "text",
        "fields": {
          "jaso": {
            "type": "text",
            "analyzer": "jaso_analyzer"
          },
          "chosung": {
            "type": "text",
            "analyzer": "chosung_analyzer"
          },
          "eng_to_kor": {
            "type": "text",
            "analyzer": "eng2kor_analyzer"
          },
          "kor_to_eng": {
            "type": "text",
            "analyzer": "kor2eng_analyzer"
          }
        }
      }
    }
  }
}'
```

#### 2. 테스트 문서 색인
```shell
# 초성, 자소분리, 한영오타 test sample
curl -XPOST -H 'Content-Type: application/json' http://localhost:9200/filter-test/_doc/1 -d '{
    "keyword":"삼성전자"
}'

# 영한오타 test sample
curl -XPOST -H 'Content-Type: application/json' http://localhost:9200/filter-test/_doc/2 -d '{
    "keyword":"tkatjdwjswk"
}'
```

#### 3. 삼성전자 테스트 문서 색인 결과 확인
```shell
# 초성, 자소분리, 한영오타 test 결과 - keyword.kor_to_eng, keyword.chosung, keyword.jaso 필드 확인
curl -XGET -H 'Content-Type: application/json' http://localhost:9200/filter-test/_termvectors/1?fields=keyword.*&pretty=true
```
```json
{
  "_index": "filter-test",
  "_type": "_doc",
  "_id": "1",
  "_version": 1,
  "found": true,
  "took": 0,
  "term_vectors": {
    "keyword.kor_to_eng": {
      "field_statistics": {
        "sum_doc_freq": 1,
        "doc_count": 1,
        "sum_ttf": 1
      },
      "terms": {
        "tkatjdwjswk": {
          "term_freq": 1,
          "tokens": [
            {
              "position": 0,
              "start_offset": 0,
              "end_offset": 4
            }
          ]
        }
      }
    },
    "keyword.chosung": {
      "field_statistics": {
        "sum_doc_freq": 1,
        "doc_count": 1,
        "sum_ttf": 1
      },
      "terms": {
        "ㅅㅅㅈㅈ": {
          "term_freq": 1,
          "tokens": [
            {
              "position": 0,
              "start_offset": 0,
              "end_offset": 4
            }
          ]
        }
      }
    },
    "keyword.jaso": {
      "field_statistics": {
        "sum_doc_freq": 1,
        "doc_count": 1,
        "sum_ttf": 1
      },
      "terms": {
        "ㅅㅏㅁㅅㅓㅇㅈㅓㄴㅈㅏ": {
          "term_freq": 1,
          "tokens": [
            {
              "position": 0,
              "start_offset": 0,
              "end_offset": 4
            }
          ]
        }
      }
    },
    "keyword.eng_to_kor": {
      "field_statistics": {
        "sum_doc_freq": 1,
        "doc_count": 1,
        "sum_ttf": 1
      },
      "terms": {
        "삼성전자": {
          "term_freq": 1,
          "tokens": [
            {
              "position": 0,
              "start_offset": 0,
              "end_offset": 4
            }
          ]
        }
      }
    }
  }
}
```

#### 4. tkatjdwjswk 테스트 문서 색인 결과 확인
```shell
# 영한오타 test 결과 - keyword.eng_to_kor 필드 확인
curl -XGET -H 'Content-Type: application/json' http://localhost:9200/filter-test/_termvectors/2?fields=keyword.*&pretty=true
```
```json
{
  "_index": "filter-test",
  "_type": "_doc",
  "_id": "2",
  "_version": 1,
  "found": true,
  "took": 6,
  "term_vectors": {
    "keyword.chosung": {
      "field_statistics": {
        "sum_doc_freq": 2,
        "doc_count": 2,
        "sum_ttf": 2
      },
      "terms": {
        "tkatjdwjswk": {
          "term_freq": 1,
          "tokens": [
            {
              "position": 0,
              "start_offset": 0,
              "end_offset": 11
            }
          ]
        }
      }
    },
    "keyword.jaso": {
      "field_statistics": {
        "sum_doc_freq": 2,
        "doc_count": 2,
        "sum_ttf": 2
      },
      "terms": {
        "tkatjdwjswk": {
          "term_freq": 1,
          "tokens": [
            {
              "position": 0,
              "start_offset": 0,
              "end_offset": 11
            }
          ]
        }
      }
    },
    "keyword.kor_to_eng": {
      "field_statistics": {
        "sum_doc_freq": 2,
        "doc_count": 2,
        "sum_ttf": 2
      },
      "terms": {
        "tkatjdwjswk": {
          "term_freq": 1,
          "tokens": [
            {
              "position": 0,
              "start_offset": 0,
              "end_offset": 11
            }
          ]
        }
      }
    },
    "keyword.eng_to_kor": {
      "field_statistics": {
        "sum_doc_freq": 2,
        "doc_count": 2,
        "sum_ttf": 2
      },
      "terms": {
        "삼성전자": {
          "term_freq": 1,
          "tokens": [
            {
              "position": 0,
              "start_offset": 0,
              "end_offset": 11
            }
          ]
        }
      }
    }
  }
}
```


[1]: https://book.naver.com/bookdb/book_detail.nhn?bid=14733062
[2]: https://github.com/javacafe-project/elasticsearch-plugin
