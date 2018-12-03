/**
 * 
 */
package com.drishika.gradzcircle.entitybuilders;

import org.springframework.data.elasticsearch.core.completion.Completion;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

import com.drishika.gradzcircle.domain.elastic.JobCategory;;

/**
 * @author abhinav
 *
 */
public class JobCategoryEntityBuilder {
	
	private JobCategory jobCategory;

	public JobCategoryEntityBuilder(Long id) {
		jobCategory = new JobCategory();
		jobCategory.setId(id);
	}

	public JobCategoryEntityBuilder name(String name) {
		jobCategory.setJobCategory(name);
		return this;
	}

	public JobCategoryEntityBuilder suggest(String[] input) {
		return suggest(input, null);
	}

	public JobCategoryEntityBuilder suggest(String[] input, Integer weight) {
		Completion suggest = new Completion(input);
		suggest.setWeight(weight);
		jobCategory.setSuggest(suggest);
		return this;
	}

	public JobCategory build() {
		return jobCategory;
	}

	public IndexQuery buildIndex() {
		IndexQuery indexQuery = new IndexQuery();
		indexQuery.setId(jobCategory.getId().toString());
		indexQuery.setObject(jobCategory);
		return indexQuery;
	}

}
