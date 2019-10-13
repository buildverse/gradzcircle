/**
 * 
 */
package com.drishika.gradzcircle.entitybuilders;

import org.springframework.data.elasticsearch.core.completion.Completion;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

import com.drishika.gradzcircle.domain.elastic.Skills;

/**
 * @author abhinav
 *
 */
public class SkillsEntityBuilder {
	
	private Skills skill;
	
	public SkillsEntityBuilder(Long id) {
		skill = new Skills();
		skill.setId(id);
	}

	public SkillsEntityBuilder name(String name) {
		skill.setSkill(name);
		return this;
	}

	public SkillsEntityBuilder suggest(String[] input) {
		return suggest(input, null);
	}

	SkillsEntityBuilder suggest(String[] input, Integer weight) {
		Completion suggest = new Completion(input);
		suggest.setWeight(weight);
		skill.setSuggest(suggest);
		return this;
	}

	public Skills build() {
		return skill;
	}

	public IndexQuery buildIndex() {
		IndexQuery indexQuery = new IndexQuery();
		indexQuery.setId(skill.getId().toString());
		indexQuery.setObject(skill);
		return indexQuery;
	}

}
