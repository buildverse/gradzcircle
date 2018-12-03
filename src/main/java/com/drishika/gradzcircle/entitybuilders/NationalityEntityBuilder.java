/**
 * 
 */
package com.drishika.gradzcircle.entitybuilders;

import org.springframework.data.elasticsearch.core.completion.Completion;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

import com.drishika.gradzcircle.domain.elastic.Nationality;

/**
 * @author abhinav
 *
 */
public class NationalityEntityBuilder {
	
	private Nationality nationality;

	public NationalityEntityBuilder(Long id) {
		nationality = new Nationality();
		nationality.setId(id);
	}

	public NationalityEntityBuilder name(String name) {
		nationality.setNationality(name);
		return this;
	}

	public NationalityEntityBuilder suggest(String[] input) {
		return suggest(input, null);
	}

	public NationalityEntityBuilder suggest(String[] input, Integer weight) {
		Completion suggest = new Completion(input);
		suggest.setWeight(weight);
		nationality.setSuggest(suggest);
		return this;
	}

	public Nationality build() {
		return nationality;
	}

	public IndexQuery buildIndex() {
		IndexQuery indexQuery = new IndexQuery();
		indexQuery.setId(nationality.getId().toString());
		indexQuery.setObject(nationality);
		return indexQuery;
	}


}
