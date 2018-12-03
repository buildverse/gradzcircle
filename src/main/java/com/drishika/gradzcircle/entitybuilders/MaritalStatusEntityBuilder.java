/**
 * 
 */
package com.drishika.gradzcircle.entitybuilders;

import org.springframework.data.elasticsearch.core.completion.Completion;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

import com.drishika.gradzcircle.domain.elastic.MaritalStatus;

/**
 * @author abhinav
 *
 */
public class MaritalStatusEntityBuilder {
	
	private MaritalStatus maritalStatus;

	public MaritalStatusEntityBuilder(Long id) {
		maritalStatus = new MaritalStatus();
		maritalStatus.setId(id);
	}

	public MaritalStatusEntityBuilder name(String name) {
		maritalStatus.setStatus(name);
		return this;
	}

	public MaritalStatusEntityBuilder suggest(String[] input) {
		return suggest(input, null);
	}

	public MaritalStatusEntityBuilder suggest(String[] input, Integer weight) {
		Completion suggest = new Completion(input);
		suggest.setWeight(weight);
		maritalStatus.setSuggest(suggest);
		return this;
	}

	public MaritalStatus build() {
		return maritalStatus;
	}

	public IndexQuery buildIndex() {
		IndexQuery indexQuery = new IndexQuery();
		indexQuery.setId(maritalStatus.getId().toString());
		indexQuery.setObject(maritalStatus);
		return indexQuery;
	}

}
