
package com.drishika.gradzcircle.entitybuilders;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.springframework.data.elasticsearch.core.completion.Completion;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;

import com.drishika.gradzcircle.domain.elastic.University;

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
	
	/*public UpdateQuery updateIndex() {
		IndexRequest indexRequest = new IndexRequest();
		indexRequest.source(university);
		UpdateQuery updateQuery = new UpdateQueryBuilder().withId(university.getId().toString())
					.withClass(com.drishika.gradzcircle.domain.elastic.University.class).withIndexRequest(indexRequest).build();
		return updateQuery;
	}
*/
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UniversityEntityBuilder [university=" + university + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((university == null) ? 0 : university.getId().intValue());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UniversityEntityBuilder other = (UniversityEntityBuilder) obj;
		if (university == null) {
			if (other.university != null)
				return false;
		} else if (!university.getId().equals(other.university.getId()))
			return false;
		return true;
	}
	
	
	
}