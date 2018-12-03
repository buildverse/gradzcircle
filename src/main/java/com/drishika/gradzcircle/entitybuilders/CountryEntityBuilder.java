/**
 * 
 */
package com.drishika.gradzcircle.entitybuilders;

import org.springframework.data.elasticsearch.core.completion.Completion;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import com.drishika.gradzcircle.domain.elastic.Country;

/**
 * @author abhinav
 *
 */
public class CountryEntityBuilder {
	
	private Country country;

	public CountryEntityBuilder(Long id) {
		country = new Country();
		country.setId(id);
	}

	public CountryEntityBuilder name(String name) {
		country.setCountryNiceName(name);
		return this;
	}

	public CountryEntityBuilder suggest(String[] input) {
		return suggest(input, null);
	}

	public CountryEntityBuilder suggest(String[] input, Integer weight) {
		Completion suggest = new Completion(input);
		suggest.setWeight(weight);
		country.setSuggest(suggest);
		return this;
	}

	public Country build() {
		return country;
	}

	public IndexQuery buildIndex() {
		IndexQuery indexQuery = new IndexQuery();
		indexQuery.setId(country.getId().toString());
		indexQuery.setObject(country);
		return indexQuery;
	}

}
