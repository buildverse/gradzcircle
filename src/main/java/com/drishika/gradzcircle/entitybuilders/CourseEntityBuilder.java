
package com.drishika.gradzcircle.entitybuilders;
import com.drishika.gradzcircle.domain.elastic.Course;
import org.springframework.data.elasticsearch.core.completion.Completion;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

public class CourseEntityBuilder {
    
        private Course course;
    
        public CourseEntityBuilder(Long id) {
            course = new Course();
            course.setId(id);
        }
    
        public CourseEntityBuilder name(String name) {
            course.setCourse(name);
            return this;
        }
    
        public CourseEntityBuilder suggest(String[] input) {
            return suggest(input, null);
        }
    
        public CourseEntityBuilder suggest(String[] input, Integer weight) {
            Completion suggest = new Completion(input);
            suggest.setWeight(weight);
            course.setSuggest(suggest);
            return this;
        }
    
        public Course build() {
            return course;
        }
    
        public IndexQuery buildIndex() {
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setId(course.getId().toString());
            indexQuery.setObject(course);
            return indexQuery;
        }
    }