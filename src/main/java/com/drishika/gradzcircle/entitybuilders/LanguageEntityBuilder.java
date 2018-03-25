
package com.drishika.gradzcircle.entitybuilders;
import com.drishika.gradzcircle.domain.elastic.Language;
import org.springframework.data.elasticsearch.core.completion.Completion;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

public class LanguageEntityBuilder {
    
        private Language language;
    
        public LanguageEntityBuilder(Long id) {
            language = new Language();
            language.setId(id);
        }
    
        public LanguageEntityBuilder name(String name) {
            language.setLanguage(name);
            return this;
        }
    
        public LanguageEntityBuilder suggest(String[] input) {
            return suggest(input, null);
        }
    
        public LanguageEntityBuilder suggest(String[] input, Integer weight) {
            Completion suggest = new Completion(input);
            suggest.setWeight(weight);
            language.setSuggest(suggest);
            return this;
        }
    
        public Language build() {
            return language;
        }
    
        public IndexQuery buildIndex() {
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setId(language.getId().toString());
            indexQuery.setObject(language);
            return indexQuery;
        }
    }