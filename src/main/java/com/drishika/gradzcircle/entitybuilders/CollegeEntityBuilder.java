
package com.drishika.gradzcircle.entitybuilders;

import com.drishika.gradzcircle.domain.elastic.College;
import org.springframework.data.elasticsearch.core.completion.Completion;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

public class CollegeEntityBuilder {

	private College college;

	public CollegeEntityBuilder(Long id) {
		college = new College();
		college.setId(id);
	}

	public CollegeEntityBuilder name(String name) {
		college.setCollegeName(name);
		return this;
	}

	public CollegeEntityBuilder domainName(String domainName) {
		college.setDomainName(domainName);
		return this;
	}

	public CollegeEntityBuilder status(Integer status) {
		college.setStatus(status);
		return this;
	}

	public CollegeEntityBuilder suggest(String[] input) {
		return suggest(input, null);
	}

	public CollegeEntityBuilder suggest(String[] input, Integer weight) {
		Completion suggest = new Completion(input);
		suggest.setWeight(weight);
		college.setSuggest(suggest);
		return this;
	}

	public College build() {
		return college;
	}

	public IndexQuery buildIndex() {
		IndexQuery indexQuery = new IndexQuery();
		indexQuery.setId(college.getId().toString());
		indexQuery.setObject(college);
		return indexQuery;
	}
}