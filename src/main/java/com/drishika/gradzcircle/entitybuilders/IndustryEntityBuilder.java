/**
 * 
 */
package com.drishika.gradzcircle.entitybuilders;

import org.springframework.data.elasticsearch.core.completion.Completion;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

import com.drishika.gradzcircle.domain.elastic.Industry;

/**
 * @author abhinav
 *
 */
public class IndustryEntityBuilder {
	
	private Industry industry;

	public IndustryEntityBuilder(Long id) {
		industry = new Industry();
		industry.setId(id);
	}

	public IndustryEntityBuilder name(String name) {
		industry.setIndustryName(name);
		return this;
	}

	public IndustryEntityBuilder suggest(String[] input) {
		return suggest(input, null);
	}

	public IndustryEntityBuilder suggest(String[] input, Integer weight) {
		Completion suggest = new Completion(input);
		suggest.setWeight(weight);
		industry.setSuggest(suggest);
		return this;
	}

	public Industry build() {
		return industry;
	}

	public IndexQuery buildIndex() {
		IndexQuery indexQuery = new IndexQuery();
		indexQuery.setId(industry.getId().toString());
		indexQuery.setObject(industry);
		return indexQuery;
	}


}
