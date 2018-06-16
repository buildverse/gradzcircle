package com.drishika.gradzcircle.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.domain.Address;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateCertification;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateEmployment;
import com.drishika.gradzcircle.domain.CandidateLanguageProficiency;
import com.drishika.gradzcircle.domain.CandidateNonAcademicWork;
import com.drishika.gradzcircle.domain.CandidateProject;
import com.drishika.gradzcircle.domain.User;
import com.drishika.gradzcircle.repository.AddressRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.search.CandidateCertificationSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateEducationSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateEmploymentSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateLanguageProficiencySearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateNonAcademicWorkSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateProjectSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateSearchRepository;
import com.drishika.gradzcircle.service.matching.CandidateMatcher;
import com.drishika.gradzcircle.service.matching.Matcher;

/**
 * Service to manage Candidate
 */

@Service
@Transactional

public class CandidateService {

	private static final Logger logger = LoggerFactory.getLogger(CandidateService.class);

	private CandidateRepository candidateRepository;

	private CandidateSearchRepository candidateSearchRepository;

	private final CandidateEducationSearchRepository candidateEducationSearchRepository;

	private final CandidateProjectSearchRepository candidateProjectSearchRepository;

	private final CandidateEmploymentSearchRepository candidateEmploymentSearchRepository;

	private final CandidateCertificationSearchRepository candidateCertificationSearchRepository;

	private final CandidateNonAcademicWorkSearchRepository candidateNonAcademicWorkSearchRepository;

	private final CandidateLanguageProficiencySearchRepository candidateLanguageProficiencySearchRepository;
	
	//private final Matcher<Candidate> jobMatcher;

	// private final AddressSearchRepository addressSearchRepository;

	private final AddressRepository addressRepository;

	public CandidateService(CandidateRepository candidateRepository,
			CandidateSearchRepository candidateSearchRepository,
			CandidateEducationSearchRepository candidateEducationSearchRepository,
			CandidateProjectSearchRepository candidateProjectSearchRepository,
			CandidateEmploymentSearchRepository candidateEmploymentSearchRepository,
			CandidateCertificationSearchRepository candidateCertificationSearchRepository,
			CandidateNonAcademicWorkSearchRepository candidateNonAcademicWorkSearchRepository,
			CandidateLanguageProficiencySearchRepository candidateLanguageProficiencySearchRepository,
			AddressRepository addressRepository) {
		this.candidateRepository = candidateRepository;
		this.candidateSearchRepository = candidateSearchRepository;
		this.addressRepository = addressRepository;
		// this.addressSearchRepository = addressSearchRepository;
		this.candidateCertificationSearchRepository = candidateCertificationSearchRepository;
		this.candidateEducationSearchRepository = candidateEducationSearchRepository;
		this.candidateEmploymentSearchRepository = candidateEmploymentSearchRepository;
		this.candidateProjectSearchRepository = candidateProjectSearchRepository;
		this.candidateNonAcademicWorkSearchRepository = candidateNonAcademicWorkSearchRepository;
		this.candidateLanguageProficiencySearchRepository = candidateLanguageProficiencySearchRepository;
		//this.jobMatcher = jobMatcher;
	}

	public void createCandidate(User user) {
		Candidate candidate = new Candidate();
		candidate.setLogin(user);
		candidateRepository.save(candidate);
		candidateSearchRepository.save(candidate);
		logger.debug("Created Information for candidate: {}", candidate);
	}

	public Candidate createCandidate(Candidate candidate) {
		logger.debug("REST request to save Candidate : {}", candidate);

		Candidate result = candidateRepository.save(candidate);
		//Replace with Future
		//jobMatcher.match(result);
		candidateSearchRepository.save(result);
		return result;
	}

	public Candidate updateCandidate(Candidate candidate) {

		logger.debug("Saving {} with addres {}", candidate, candidate.getAddresses());
		candidate.getAddresses().forEach(candidateAddress -> candidateAddress.setCandidate(candidate));
		Candidate result = candidateRepository.save(candidate);
		//Replace with Future
		//jobMatcher.match(result);
		candidateSearchRepository.save(result);
		return result;
	}

	public List<Candidate> getAllCandidates() {
		logger.debug("REST request to get all Candidates");
		return candidateRepository.findAllWithEagerRelationships();
	}

	public Candidate getCandidate(Long id) {
		logger.debug("REST request to get Candidate : {}", id);
		Candidate candidate = candidateRepository.findOneWithEagerRelationships(id);
		if (candidate != null) {
			Set<Address> addresses = addressRepository.findAddressByCandidate(candidate);
			addresses.forEach(candidateAddress -> {
				candidateAddress.setCandidate(null);
			});
			candidate.setAddresses(addresses);
			logger.debug("Retruning candidate {}", candidate.getAddresses());
		}
		return candidate;
	}

	public Candidate getCandidateByLoginId(Long id) {
		logger.debug("REST request to get Candidate : {}", id);
		Candidate candidate = candidateRepository.findByLoginId(new Long(id).longValue());
		Set<Address> addresses = addressRepository.findAddressByCandidate(candidate);
		if(candidate!= null)
			candidate.setAddresses(addresses);
		logger.debug("Retruning candidate {}", candidate);
		return candidate;
	}

	public void deleteCandidate(Long id) {
		logger.debug("REST request to delete Candidate : {}", id);
		candidateRepository.delete(id);
		candidateSearchRepository.delete(id);
	}

	public List<Candidate> searchCandidates(String query) {
		logger.debug("REST request to search Candidates for query {}", query);
		return StreamSupport.stream(candidateSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}

	public Candidate retrieveCandidatePublicProfile(String query) {
		logger.debug("REST request to get Candidate public profile for query {}", query);
		Candidate candidate = candidateSearchRepository.findOne(Long.parseLong(query));
		if (candidate == null)
			return candidate;
		Set<Address> addresses = addressRepository.findAddressByCandidate(candidate);
		Set<CandidateEducation> candidateEducations = StreamSupport
				.stream(candidateEducationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toSet());
		Set<CandidateEmployment> candidateEmployments = StreamSupport
				.stream(candidateEmploymentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toSet());

		if (candidateEducations != null & candidateEducations.size() > 0) {
			candidateEducations.forEach(candidateEducation -> {
				Set<CandidateProject> candidateEducationProjects = StreamSupport
						.stream(candidateProjectSearchRepository
								.search(queryStringQuery(candidateEducation.getId().toString())).spliterator(), false)
						.collect(Collectors.toSet());
				candidateEducation.setProjects(candidateEducationProjects);
			});
		}
		if (candidateEmployments != null & candidateEmployments.size() > 0) {
			candidateEmployments.forEach(candidateEmployment -> {
				Set<CandidateProject> candidateEmploymentProjects = StreamSupport
						.stream(candidateProjectSearchRepository
								.search(queryStringQuery(candidateEmployment.getId().toString())).spliterator(), false)
						.collect(Collectors.toSet());
				candidateEmployment.setProjects(candidateEmploymentProjects);
			});
		}

		Set<CandidateCertification> candidateCertifications = StreamSupport
				.stream(candidateCertificationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toSet());
		Set<CandidateNonAcademicWork> candidateNonAcademicWorks = StreamSupport
				.stream(candidateNonAcademicWorkSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toSet());
		Set<CandidateLanguageProficiency> candidateLanguageProficiencies = StreamSupport
				.stream(candidateLanguageProficiencySearchRepository.search(queryStringQuery(query)).spliterator(),
						false)
				.collect(Collectors.toSet());
		// log.debug("Education, employments, Projects, certs and extra are
		// {},{},{},{},{}",candidateEducations,candidateEmployments,candidateCertifications,candidateNonAcademicWorks);
		trimCandidateAddressData(addresses);
		trimCandidateEducationData(candidateEducations);
		trimCandidateEmploymentData(candidateEmployments);
		trimCandidateCertifications(candidateCertifications);
		trimCandidateLanguageProficienies(candidateLanguageProficiencies);
		trimCandidateNonAcademics(candidateNonAcademicWorks);
		candidate.setAddresses(addresses);
		candidate.setEducations(candidateEducations);
		candidate.setEmployments(candidateEmployments);
		candidate.setCertifications(candidateCertifications);
		candidate.setNonAcademics(candidateNonAcademicWorks);
		candidate.setCandidateLanguageProficiencies(candidateLanguageProficiencies);
		// log.debug("Education, employments, Projects, certs and extra are
		// {},{},{},{},{}",candidateEducations,candidateEmployments,candidateCertifications,candidateNonAcademicWorks);
		// log.debug("Candidate details are {}",candidate);
		return candidate;

	}

	private void trimCandidateCertifications(Set<CandidateCertification> certifications) {
		certifications.forEach(certification -> {
			certification.setCandidate(null);
		});
	}

	private void trimCandidateNonAcademics(Set<CandidateNonAcademicWork> nonAcademicWorks) {
		nonAcademicWorks.forEach(nonAcademicWork -> {
			nonAcademicWork.setCandidate(null);
		});
	}

	private void trimCandidateLanguageProficienies(Set<CandidateLanguageProficiency> languageProficiencies) {
		languageProficiencies.forEach(languageProficiency -> {
			languageProficiency.setCandidate(null);
		});
	}

	private void trimCandidateEmploymentData(Set<CandidateEmployment> candidateEmployments) {
		candidateEmployments.forEach(candidateEmployment -> {
			candidateEmployment.setCandidate(null);
			if (candidateEmployment.getProjects() != null) {
				candidateEmployment.getProjects().forEach(candidateProject -> {
					candidateProject.setEmployment(null);
				});
			}
		});
	}

	private void trimCandidateEducationData(Set<CandidateEducation> candidateEducations) {
		candidateEducations.forEach(candidateEducation -> {
			candidateEducation.setCandidate(null);
			if (candidateEducation.getProjects() != null) {
				candidateEducation.getProjects().forEach(candidateProject -> {
					candidateProject.setEducation(null);
				});
			}
		});
	}

	private void trimCandidateAddressData(Set<Address> addresses) {

		if (addresses != null) {
			addresses.forEach(address -> {
				if (address.getCountry() != null) {
					address.getCountry().setCorporates(null);
					address.getCountry().setVisas(null);
					address.getCountry().setNationality(null);
					address.getCountry().setAddresses(null);
				}
			});
		}

	}
}