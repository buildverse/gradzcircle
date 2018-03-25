
package com.drishika.gradzcircle.entitybuilders;
import com.drishika.gradzcircle.domain.elastic.Qualification;
import org.springframework.data.elasticsearch.core.completion.Completion;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

public class QualificationEntityBuilder {
    
        private Qualification qualification;
    
        public QualificationEntityBuilder(Long id) {
            qualification = new Qualification();
            qualification.setId(id);
        }
    
        public QualificationEntityBuilder name(String name) {
            qualification.setQualification(name);
            return this;
        }
    
        public QualificationEntityBuilder suggest(String[] input) {
            return suggest(input, null);
        }
    
        public QualificationEntityBuilder suggest(String[] input, Integer weight) {
            Completion suggest = new Completion(input);
            suggest.setWeight(weight);
            qualification.setSuggest(suggest);
            return this;
        }
    
        public Qualification build() {
            return qualification;
        }
    
        public IndexQuery buildIndex() {
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setId(qualification.getId().toString());
            indexQuery.setObject(qualification);
            return indexQuery;
        }
    }