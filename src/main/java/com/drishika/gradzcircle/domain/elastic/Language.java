package com.drishika.gradzcircle.domain.elastic;

import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.completion.Completion;


@Document(indexName = "language")
public class Language extends com.drishika.gradzcircle.domain.Language{

    @CompletionField(maxInputLength = 100)
    private Completion suggest;

    public Completion getSuggest() {
		return suggest;
	}

	public void setSuggest(Completion suggest) {
		this.suggest = suggest;
	}

}