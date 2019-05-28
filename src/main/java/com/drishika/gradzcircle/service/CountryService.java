/**
 * 
 */
package com.drishika.gradzcircle.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import com.drishika.gradzcircle.constants.ApplicationConstants;
import com.drishika.gradzcircle.domain.Country;
import com.drishika.gradzcircle.domain.elastic.GenericElasticSuggest;
import com.drishika.gradzcircle.entitybuilders.CountryEntityBuilder;
import com.drishika.gradzcircle.repository.CountryRepository;
import com.drishika.gradzcircle.repository.search.CountrySearchRepository;
import com.drishika.gradzcircle.service.util.GradzcircleCacheManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author abhinav
 *
 */
@Service
public class CountryService {
	
	private final Logger log = LoggerFactory.getLogger(CountryService.class);
	
	GradzcircleCacheManager<String, List<Country>> enabledCountryCache;
	
	CountrySearchRepository countrySearchRepository;
	
	ElasticsearchTemplate elasticSearchTemplate;
	
	CountryRepository countryRepository;
	
	public CountryService (GradzcircleCacheManager<String, List<Country>> enabledCountryCache,CountryRepository countryRepository,CountrySearchRepository countrySearchRepository,ElasticsearchTemplate elasticSearchTemplate) {
		this.enabledCountryCache = enabledCountryCache;
		this.countryRepository = countryRepository;
		this.countrySearchRepository = countrySearchRepository;
		this.elasticSearchTemplate = elasticSearchTemplate;
	}
	
	public List<Country> getEnabledCountries() throws Exception{
		List<Country> enabledCountryList = enabledCountryCache.getValue(ApplicationConstants.ENABLED_COUNTRIES, new Callable<List<Country>>() {
			public List<Country> call() throws Exception {
				return countryRepository.findEnabledCountries();
			}
		} );
		log.debug("Enabled Countries are {}",enabledCountryList);
		return enabledCountryList;
	}
	
	public Country createCountry (Country country) {
		Country result = countryRepository.save(country);
		if(result.isEnabled()) {
			enabledCountryCache.removeFromCache(ApplicationConstants.ENABLED_COUNTRIES);
		}
		elasticSearchTemplate.index(new CountryEntityBuilder(result.getId()).name(result.getCountryNiceName())
				.suggest(new String[] { result.getCountryNiceName() }).buildIndex());
		elasticSearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Country.class);
		return result;
	}
	
	public Country updateCountry(Country country) {
		Country result = countryRepository.save(country);
		if(result.isEnabled()) {
			enabledCountryCache.removeFromCache(ApplicationConstants.ENABLED_COUNTRIES);
		}
		elasticSearchTemplate.index(new CountryEntityBuilder(result.getId()).name(result.getCountryNiceName())
				.suggest(new String[] { result.getCountryNiceName() }).buildIndex());
		elasticSearchTemplate.refresh(com.drishika.gradzcircle.domain.elastic.Country.class);
		return country;
	}
	
	public void deleteCountry(Long id) {
		countryRepository.delete(id);
		countrySearchRepository.delete(id);
	}
	
	public List<Country> searchCountries(String query) {
		return StreamSupport.stream(countrySearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}
	
	public String searchCountryBySuggest(String query) {
		String suggest = null;
		CompletionSuggestionBuilder completionSuggestionBuilder = SuggestBuilders
				.completionSuggestion("country-suggest").text(query).field("suggest");
		SuggestResponse suggestResponse = elasticSearchTemplate.suggest(completionSuggestionBuilder,
				com.drishika.gradzcircle.domain.elastic.Country.class);
		CompletionSuggestion completionSuggestion = suggestResponse.getSuggest().getSuggestion("country-suggest");
		List<CompletionSuggestion.Entry.Option> options = completionSuggestion.getEntries().get(0).getOptions();
		List<GenericElasticSuggest> countries = new ArrayList<GenericElasticSuggest>();
		ObjectMapper objectMapper = new ObjectMapper();
		options.forEach(option -> {
			countries.add(new GenericElasticSuggest(option.getText().string(), option.getText().string()));
			// colleges.add("id:"+option.getText().string()+",name:"+option.getText().string());
		});
		try {
			suggest = objectMapper.writeValueAsString(countries);
		} catch (JsonProcessingException e) {
			log.error("Error parsing object to JSON {},{}", e.getMessage(), e);
		}
		return suggest;
	}

}
