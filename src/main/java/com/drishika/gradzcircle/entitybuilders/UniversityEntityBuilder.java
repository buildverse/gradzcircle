
package com.drishika.gradzcircle.entitybuilders;
import com.drishika.gradzcircle.domain.elastic.University;
import org.springframework.data.elasticsearch.core.completion.Completion;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

public class UniversityEntityBuilder {
    
        private University university;
    
        public UniversityEntityBuilder(Long id) {
            university = new University();
            university.setId(id);
        }
    
        public UniversityEntityBuilder name(String name) {
            university.setUniversityName(name);
            return this;
        }
    
        public UniversityEntityBuilder suggest(String[] input) {
            return suggest(input, null);
        }
    
        public UniversityEntityBuilder suggest(String[] input, Integer weight) {
            Completion suggest = new Completion(input);
            suggest.setWeight(weight);
            university.setSuggest(suggest);
            return this;
        }
    
        public University build() {
            return university;
        }
    
        public IndexQuery buildIndex() {
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setId(university.getId().toString());
            indexQuery.setObject(university);
            return indexQuery;
        }
    }