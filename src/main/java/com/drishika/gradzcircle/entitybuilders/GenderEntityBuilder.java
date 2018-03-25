
package com.drishika.gradzcircle.entitybuilders;
import com.drishika.gradzcircle.domain.elastic.Gender;
import org.springframework.data.elasticsearch.core.completion.Completion;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

public class GenderEntityBuilder {
    
        private Gender gender;
    
        public GenderEntityBuilder(Long id) {
            gender = new Gender();
            gender.setId(id);
        }
    
        public GenderEntityBuilder name(String name) {
            gender.setGender(name);
            return this;
        }
    
        public GenderEntityBuilder suggest(String[] input) {
            return suggest(input, null);
        }
    
        public GenderEntityBuilder suggest(String[] input, Integer weight) {
            Completion suggest = new Completion(input);
            suggest.setWeight(weight);
            gender.setSuggest(suggest);
            return this;
        }
    
        public Gender build() {
            return gender;
        }
    
        public IndexQuery buildIndex() {
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setId(gender.getId().toString());
            indexQuery.setObject(gender);
            return indexQuery;
        }
    }