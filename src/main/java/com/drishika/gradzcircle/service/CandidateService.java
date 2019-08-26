package com.drishika.gradzcircle.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.config.Constants;
import com.drishika.gradzcircle.domain.Address;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateCertification;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateEmployment;
import com.drishika.gradzcircle.domain.CandidateJob;
import com.drishika.gradzcircle.domain.CandidateLanguageProficiency;
import com.drishika.gradzcircle.domain.CandidateNonAcademicWork;
import com.drishika.gradzcircle.domain.CandidateProject;
import com.drishika.gradzcircle.domain.Corporate;
import com.drishika.gradzcircle.domain.CorporateCandidate;
import com.drishika.gradzcircle.domain.Country;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.domain.Nationality;
import com.drishika.gradzcircle.domain.User;
import com.drishika.gradzcircle.repository.AddressRepository;
import com.drishika.gradzcircle.repository.CandidateCertificationRepository;
import com.drishika.gradzcircle.repository.CandidateEducationRepository;
import com.drishika.gradzcircle.repository.CandidateEmploymentRepository;
import com.drishika.gradzcircle.repository.CandidateLanguageProficiencyRepository;
import com.drishika.gradzcircle.repository.CandidateNonAcademicWorkRepository;
import com.drishika.gradzcircle.repository.CandidateProjectRepository;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.CorporateRepository;
import com.drishika.gradzcircle.repository.CountryRepository;
import com.drishika.gradzcircle.repository.JobRepository;
import com.drishika.gradzcircle.repository.NationalityRepository;
import com.drishika.gradzcircle.repository.search.CandidateCertificationSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateEducationSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateEmploymentSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateLanguageProficiencySearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateNonAcademicWorkSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateProjectSearchRepository;
import com.drishika.gradzcircle.repository.search.CandidateSearchRepository;
import com.drishika.gradzcircle.service.dto.AddressDTO;
import com.drishika.gradzcircle.service.dto.CandidateDetailDTO;
import com.drishika.gradzcircle.service.dto.CandidatePublicProfileDTO;
import com.drishika.gradzcircle.service.dto.CountryDTO;
import com.drishika.gradzcircle.service.dto.GenderDTO;
import com.drishika.gradzcircle.service.dto.JobCategoryDTO;
import com.drishika.gradzcircle.service.dto.MaritalStatusDTO;
import com.drishika.gradzcircle.service.matching.Matcher;
import com.drishika.gradzcircle.service.util.DTOConverters;
import com.drishika.gradzcircle.service.util.ProfileScoreCalculator;

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

	private final CandidateEducationRepository candidateEducationRepository;

	private final CandidateProjectSearchRepository candidateProjectSearchRepository;

	private final CandidateProjectRepository candidateProjectRepository;

	private final CandidateEmploymentRepository candidateEmploymentRepository;

	private final CandidateEmploymentSearchRepository candidateEmploymentSearchRepository;

	private final CandidateCertificationSearchRepository candidateCertificationSearchRepository;

	private final CandidateCertificationRepository candidateCertifcationRepository;

	private final CandidateNonAcademicWorkSearchRepository candidateNonAcademicWorkSearchRepository;

	private final CandidateNonAcademicWorkRepository candidateNonAcademicRepository;

	private final CandidateLanguageProficiencySearchRepository candidateLanguageProficiencySearchRepository;

	private final CandidateLanguageProficiencyRepository candidateLanguageProficiencyRepository;
	
	private final NationalityRepository nationalityRepository;
	
	private final ProfileScoreCalculator profileScoreCalculator;
	
	private final CountryRepository countryRepository;

	private final Matcher<Candidate> matcher;

	// private final AddressSearchRepository addressSearchRepository;

	private final AddressRepository addressRepository;

	private JobRepository jobRepository;
	
	private DTOConverters dtoConverter;

	private CorporateRepository corporateRepository;

	public CandidateService(CandidateRepository candidateRepository,
			CandidateSearchRepository candidateSearchRepository,
			CandidateEducationSearchRepository candidateEducationSearchRepository,
			CandidateProjectSearchRepository candidateProjectSearchRepository,
			CandidateEmploymentSearchRepository candidateEmploymentSearchRepository,
			CandidateCertificationSearchRepository candidateCertificationSearchRepository,
			CandidateNonAcademicWorkSearchRepository candidateNonAcademicWorkSearchRepository,
			CandidateLanguageProficiencySearchRepository candidateLanguageProficiencySearchRepository,
			AddressRepository addressRepository, @Qualifier("CandidateGenderMatcher") Matcher<Candidate> matcher,
			CandidateEducationRepository candidateEducationRepository,
			CandidateProjectRepository candidateProjectRepository,
			CandidateNonAcademicWorkRepository candidateNonAcademicWorkRepository,
			CandidateLanguageProficiencyRepository candidateLanguageProficiencyRepository,
			CandidateCertificationRepository candidateCertificationRepository,
			CandidateEmploymentRepository candidateEmploymentRepository, JobRepository jobRepository,
			CorporateRepository corporateRepository, NationalityRepository nationalityRepository,CountryRepository countryRepository,
			ProfileScoreCalculator profileScoreCalculator, DTOConverters dtoConverter) {
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
		this.candidateCertifcationRepository = candidateCertificationRepository;
		this.candidateEducationRepository = candidateEducationRepository;
		this.candidateEmploymentRepository = candidateEmploymentRepository;
		this.candidateNonAcademicRepository = candidateNonAcademicWorkRepository;
		this.candidateProjectRepository = candidateProjectRepository;
		this.candidateLanguageProficiencyRepository = candidateLanguageProficiencyRepository;
		this.matcher = matcher;
		this.jobRepository = jobRepository;
		this.corporateRepository = corporateRepository;
		this.countryRepository = countryRepository;
		this.nationalityRepository = nationalityRepository;
		this.profileScoreCalculator = profileScoreCalculator;
		this.dtoConverter = dtoConverter;
	}

	public void createCandidate(User user) {
		Candidate candidate = new Candidate();
		candidate.setLogin(user);
		candidate.setMatchEligible(true);
		candidateRepository.save(candidate);
		profileScoreCalculator.updateProfileScore(candidate, Constants.CANDIDATE_BASIC_PROFILE,false);
		candidateRepository.save(candidate);
		candidateSearchRepository.save(candidate);
		logger.debug("Created Information for candidate: {}", candidate);
	}

	public Candidate createCandidate(Candidate candidate) {
		logger.debug("REST request to save Candidate : {}", candidate);
		candidate.setMatchEligible(true);
		candidate = candidateRepository.save(candidate);
		profileScoreCalculator.updateProfileScore(candidate, Constants.CANDIDATE_BASIC_PROFILE,false);
		candidate = candidateRepository.save(candidate);
		// Replace with Future
		matcher.match(candidate);
		// candidateSearchRepository.save(result);
		return candidate;
	}

	public Candidate updateCandidate(Candidate candidate) {
		logger.debug("Saving {} with addres {}", candidate, candidate.getAddresses());
		Boolean enableMatch = false;
		Candidate prevCandidate = candidateRepository.findOne(candidate.getId());
		injestNationalityDetails(candidate);
		injestCountryDetails(candidate);
		if (candidate.getGender() != null) {
			if (!candidate.getGender().equals(prevCandidate.getGender())) {
				enableMatch = true;
				candidate.setCandidateJobs(prevCandidate.getCandidateJobs());
				logger.debug("Enabe Match is {}", enableMatch);
			}
		}
		candidate.setProfileScores(prevCandidate.getProfileScores());
		candidate.setEducations(prevCandidate.getEducations());
		candidate.setCandidateJobs(prevCandidate.getCandidateJobs());
		candidate.setMatchEligible(prevCandidate.getMatchEligible());
		profileScoreCalculator.updateProfileScore(candidate, Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE,false);
		Candidate result = candidateRepository.save(candidate);
		result.getAddresses().forEach(candidateAddress -> candidateAddress.setCandidate(result));
		// Replace with Future
		if (enableMatch)
			matcher.match(result);
		// candidateSearchRepository.save(result);
		
		return result;
	}
	
	
	
	private void injestNationalityDetails(Candidate candidate) {
		if(candidate.getNationality() != null) {
			Nationality nationality  = nationalityRepository.findByNationality(candidate.getNationality().getNationality());
			candidate.setNationality(nationality);
		}
	}
	
	private void injestCountryDetails(Candidate candidate) {
		if(candidate.getAddresses() != null && !candidate.getAddresses().isEmpty()) {
			Country country  = countryRepository.findByCountryNiceName(candidate.getAddresses().iterator().next().getCountry().getCountryNiceName());
			candidate.getAddresses().forEach(address -> address.setCountry(country));
		}
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
			/*
			 * addresses.forEach(candidateAddress -> { candidateAddress.setCandidate(null);
			 * });
			 */
			candidate.setAddresses(addresses);
			logger.debug("Retruning candidate {}", candidate);
		}
		 return candidate;
	}
	
	public CandidateDetailDTO getCandidateDetails(Long id ) {
		logger.debug("REST request to get Candidate Profile : {}", id);
		Candidate candidate = candidateRepository.findOne(id);
		logger.debug("Candidate is {}",candidate);
		logger.debug("Candidate educ details are {}",candidate.getEducations());
		logger.debug("Candidate emp details are {}",candidate.getEmployments());
		logger.debug("Candidate cert details are {}",candidate.getCertifications());
		logger.debug("Candidate non acad details are {}",candidate.getNonAcademics());
		logger.debug("Candidate lang details are {}",candidate.getCandidateLanguageProficiencies());
		Boolean hasEducation = candidate.getEducations()!=null && !candidate.getEducations().isEmpty()? true:false;
		Boolean hasCertification = candidate.getCertifications()!=null && !candidate.getCertifications().isEmpty()? true:false;
		Boolean hasNonAcademics = candidate.getNonAcademics()!=null && !candidate.getNonAcademics().isEmpty()? true:false;
		Boolean hasLanguages = candidate.getCandidateLanguageProficiencies()!=null && !candidate.getCandidateLanguageProficiencies().isEmpty()? true:false;
		Boolean hasEmployment = candidate.getEmployments()!=null && !candidate.getEmployments().isEmpty()?true:false;
		updateCountryAndNationalityDataForDisplay(candidate);
		CandidateDetailDTO candidateDetailsDTO = convertToCandidateDetailDTO(candidate);
		
		candidateDetailsDTO.setHasCertification(hasCertification);
		candidateDetailsDTO.setHasEducation(hasEducation);
		candidateDetailsDTO.setHasNonAcademic(hasNonAcademics);
		candidateDetailsDTO.setHasEmployment(hasEmployment);
		candidateDetailsDTO.setHasLanguages(hasLanguages);
		return candidateDetailsDTO;
	}

	public Candidate getCandidateByLoginId(Long id) {
		logger.debug("REST request to get Candidate : {}", id);
		Candidate candidate = candidateRepository.findByLoginId(id);
		//Set<Address> addresses = addressRepository.findAddressByCandidate(candidate);
		//if (candidate != null)
		//	candidate.setAddresses(addresses);
		logger.debug("Retruning candidate {}", candidate);
		return candidate;
	}
	
	/*Return zero if no education score*/
	public Double getEducationScore(Long candidateId) {
		Double educaitonScore = candidateRepository.hasEducationScore(candidateId);
		logger.debug("education score is {}", educaitonScore);
		return educaitonScore;
	}

	public void deleteCandidate(Long id) {
		logger.debug("REST request to delete Candidate : {}", id);
		candidateRepository.delete(id);
		// candidateSearchRepository.delete(id);
	}

	public List<Candidate> searchCandidates(String query) {
		logger.debug("REST request to search Candidates for query {}", query);
		return StreamSupport.stream(candidateSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}

	public CandidatePublicProfileDTO getCandidatePublicProfile(Long candidateId, Long jobId, Long corporateId) {
		logger.debug("REST request to get Candidate public profile for candidate {} {} {}", candidateId, jobId, corporateId);
		Boolean isShortListed = false;
		Boolean canShortListMore = false;
		Candidate candidate = candidateRepository.findOne(candidateId);
		if (candidate == null)
			return new CandidatePublicProfileDTO();
		Set<Address> addresses = addressRepository.findAddressByCandidate(candidate);
		List<CandidateEducation> candidateEducations = candidateEducationRepository.findByCandidateId(candidate.getId());
		List<CandidateEmployment> candidateEmployments = candidateEmploymentRepository.findByCandidateId(candidate.getId());
		if (candidateEducations != null & candidateEducations.size() > 0) {
			candidateEducations.forEach(candidateEducation -> {
				Set<CandidateProject> projects = candidateProjectRepository.findByEducation(candidateEducation);
				candidateEducation.setProjects(projects);
			});
		}
		if (candidateEmployments != null & candidateEmployments.size() > 0) {
			candidateEmployments.forEach(candidateEmployment -> {
				Set<CandidateProject> candidateProjects = candidateProjectRepository
						.findByEmployment(candidateEmployment);
				candidateEmployment.setProjects(candidateProjects);
			});
		}
		List<CandidateCertification> candidateCertifications = candidateCertifcationRepository.findCertificationsByCandidateId(candidate.getId());
		List<CandidateNonAcademicWork> candidateNonAcademicWorks = candidateNonAcademicRepository.findNonAcademicWorkByCandidateId(candidate.getId());
		Set<CandidateLanguageProficiency> candidateLanguageProficiencies = candidateLanguageProficiencyRepository
						.findCandidateLanguageProficienciesByCandidateId(candidate.getId());
		CandidateJob candidateJob = null;
		if(jobId >0 && corporateId >0) {
			candidateJob = setCandidateReviewedForJobAndGetMatchScoreAndReviwedStatus(candidate, jobId);
			isShortListed = isShortListed(candidate,corporateId);
			canShortListMore = canShortListCandidate(corporateId,jobId);
		}
		
		return convertToCandidatePublicProfileDTO(candidate, addresses, candidateEducations, candidateEmployments,
				candidateCertifications, candidateNonAcademicWorks, candidateLanguageProficiencies, isShortListed, candidateJob,canShortListMore);
	}

	
	private Boolean canShortListCandidate(Long corporateId, Long jobId) {
		logger.debug("The job id is {}",jobId);
		Corporate corporate = corporateRepository.findOne(corporateId);
		Job job = jobRepository.findOne(jobId);
		if(corporate.getJobs().contains(job) && job.getNoOfApplicantLeft()!=null && job.getNoOfApplicantLeft()==0)
			return false;
		else
			return true;
			
	}
	
	private Boolean isShortListed(Candidate candidate, Long corporateId) {
		Corporate corporate = corporateRepository.findOne(corporateId);
		logger.debug("SHORTLISTED LIST IS {}",corporate.getShortlistedCandidates().size());
		List<CorporateCandidate> linkedCandidates = corporate.getShortlistedCandidates().stream().filter(candidateCorporateLink -> candidateCorporateLink.getCandidate().equals(candidate)).collect(Collectors.toList());
		if (linkedCandidates != null && !linkedCandidates.isEmpty())
			return true;
		else
			return false;

	}

	
	private CandidateJob setCandidateReviewedForJobAndGetMatchScoreAndReviwedStatus(Candidate candidate, Long JobId) {
		logger.debug("Entering setReview and get match score  {}, {}",candidate.getId(), JobId);
		Job job = jobRepository.findOne(JobId);
		CandidateJob cJ = new CandidateJob(candidate, job);
		CandidateJob candidateJobToReturn = null;
		CandidateJob prevCandidateJob = null;
		prevCandidateJob = candidate.getCandidateJobs().stream().filter(candidateJob -> candidateJob.equals(cJ)).findFirst().orElse(null);
		candidateJobToReturn = new CandidateJob(prevCandidateJob);
		candidateJobToReturn.setReviewed(prevCandidateJob.getReviewed());
		logger.debug("The candidateJob is {}",prevCandidateJob);
		if (prevCandidateJob != null) {
			prevCandidateJob.setReviewed(true);
			candidateRepository.save(candidate);
		} 
		return candidateJobToReturn;
	}

	private CandidatePublicProfileDTO convertToCandidatePublicProfileDTO(Candidate candidate, Set<Address> addresses,
			List<CandidateEducation> candidateEducations, List<CandidateEmployment> candidateEmployments,
			List<CandidateCertification> candidateCertifications,
			List<CandidateNonAcademicWork> candidateNonAcademicWorks,
			Set<CandidateLanguageProficiency> candidateLanguageProficiencies, Boolean isShortListed, CandidateJob candidateJob, Boolean canBeShortListed) {
		CandidatePublicProfileDTO dto = new CandidatePublicProfileDTO();
		//dto.setShortListed(isShortListed);
		//dto.setShortListedForAnotherJob(isShortListedForAnotherJob);
		logger.debug("Shortlisted? {}",isShortListed);
		dto.setIsShortListed(isShortListed);
		if(candidateJob != null ) {
			dto.setMatchScore(candidateJob.getMatchScore());
			dto.setReviewed(candidateJob.getReviewed());
		}
		dto.setCanBeShortListed(canBeShortListed);
		dto.setCandidateDetails(dtoConverter.convertCandidateDetails(candidate));
		dto.setAddresses(dtoConverter.convertCandidateAddresses(addresses));
		dto.setEducations(dtoConverter.convertCandidateEducations(candidateEducations, false,candidate));
		dto.setEmployments(dtoConverter.convertCandidateEmployments(candidateEmployments, false,candidate));
		dto.setCertifications(dtoConverter.convertCandidateCertifications(candidateCertifications, false,candidate));
		dto.setCandidateLanguageProficiencies(dtoConverter.convertCandidateLanguageProficiencies(candidateLanguageProficiencies, false,candidate));
		dto.setNonAcademics(dtoConverter.convertCandidateNonAcademicWork(candidateNonAcademicWorks, false,candidate));
		return dto;
	}

	
	/**
	 * @param candidate
	 * @return
	 */
	public CandidateDetailDTO convertToCandidateDetailDTO(Candidate candidate) {
		CandidateDetailDTO candidateDetailDTO = new CandidateDetailDTO();
		candidateDetailDTO.setId(candidate.getId());
		candidateDetailDTO.setFirstName(candidate.getFirstName());
		candidateDetailDTO.setLastName(candidate.getLastName());
		candidateDetailDTO.setAboutMe(candidate.getAboutMe());
		candidateDetailDTO.setAvailableForHiring(candidate.isAvailableForHiring());
		candidateDetailDTO.setDateOfBirth(candidate.getDateOfBirth());
		candidateDetailDTO.setDifferentlyAbled(candidate.isDifferentlyAbled());
		candidateDetailDTO.setFacebook(candidate.getFacebook());
		candidateDetailDTO.setLinkedIn(candidate.getLinkedIn());
		candidateDetailDTO.setLogin(candidate.getLogin());
		candidateDetailDTO.setMiddleName(candidate.getMiddleName());
		candidateDetailDTO.setOpenToRelocate(candidate.isOpenToRelocate());
		candidateDetailDTO.setPhoneCode(candidate.getPhoneCode());
		candidateDetailDTO.setPhoneNumber(candidate.getPhoneNumber());
		candidateDetailDTO.setTwitter(candidate.getTwitter());
		candidateDetailDTO.setNationality(candidate.getNationality());
		candidateDetailDTO.setProfileScore(candidate.getProfileScore());
		if(candidate.getMaritalStatus()!=null) {
			MaritalStatusDTO maritalStatusDTO = new MaritalStatusDTO();
			maritalStatusDTO.setId(candidate.getMaritalStatus().getId());
			maritalStatusDTO.setStatus(candidate.getMaritalStatus().getStatus());
			candidateDetailDTO.setMaritalStatus(maritalStatusDTO);
		}
		if(candidate.getGender() != null) {
			GenderDTO genderDTO = new GenderDTO();
			genderDTO.setGender(candidate.getGender().getGender());
			genderDTO.setId(candidate.getGender().getId());
			candidateDetailDTO.setGender(genderDTO);
		}
		if(candidate.getJobCategories()!=null) {
			candidate.getJobCategories().forEach(jobCategory->{
				JobCategoryDTO jobCategoryDTO = new JobCategoryDTO();
				jobCategoryDTO.setId(jobCategory.getId());
				jobCategoryDTO.setJobCategory(jobCategory.getJobCategory());
				candidateDetailDTO.getJobCategories().add(jobCategoryDTO);
			});
		}
		if(candidate.getAddresses()!=null) {
			candidate.getAddresses().forEach(address->{
				AddressDTO addressDTO = new AddressDTO();
				addressDTO.setId(address.getId());
				addressDTO.setAddressLineOne(address.getAddressLineOne());
				addressDTO.setAddressLineTwo(address.getAddressLineTwo());
				addressDTO.setCity(address.getCity());
				addressDTO.setState(address.getState());
				addressDTO.setZip(address.getZip());
				CountryDTO countryDTO = new CountryDTO();
				countryDTO.setId(address.getCountry().getId());
				countryDTO.setCountryNiceName(address.getCountry().getCountryNiceName());
				countryDTO.setValue(address.getCountry().getCountryNiceName());
				countryDTO.setDisplay(address.getCountry().getCountryNiceName());
				addressDTO.setCountry(countryDTO);
				candidateDetailDTO.getAddresses().add(addressDTO);
			});
		}
		return candidateDetailDTO;
	}


	public Candidate applyJob(Candidate candidate) {
		return candidateRepository.save(candidate);
	}

	public Candidate shortListCandidateForJob(Long candidateId, Long jobId, Long corporateId) {
		Candidate candidateFromRepo = candidateRepository.findOne(candidateId);
		Corporate corporateFromRepo = corporateRepository.findOne(corporateId);
		Job job = jobRepository.findOne(jobId);
		CorporateCandidate corporateCandidateLink = new CorporateCandidate(corporateFromRepo, candidateFromRepo, jobId);
		candidateFromRepo.addCorporateCandidate(corporateCandidateLink);
		job.setNoOfApplicantsBought(job.getNoOfApplicantsBought()!=null?job.getNoOfApplicantsBought()+1:0+1);
		job.setNoOfApplicantLeft(new Long(job.getNoOfApplicants()-(job.getNoOfApplicantsBought()!=null?job.getNoOfApplicantsBought():0)));
		return candidateRepository.save(candidateFromRepo);
	}

	/* Fix this to go to DB not ES */
	@Deprecated
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

	public void updateCountryAndNationalityDataForDisplay(Candidate candidate) {
		if (candidate == null)
			return;
		if (candidate.getNationality() != null) {
			candidate.getNationality().setDisplay(candidate.getNationality().getNationality());
			candidate.getNationality().setValue(candidate.getNationality().getNationality());
		}
		if (candidate.getAddresses() != null) {
			candidate.getAddresses().forEach(address -> {
				if(address.getCountry()!=null) {
				address.getCountry().setValue(address.getCountry().getCountryNiceName());
				address.getCountry().setDisplay(address.getCountry().getCountryNiceName());
				}
			});
		}

	}

	@Deprecated
	private void trimCandidateCertifications(Set<CandidateCertification> certifications) {
		certifications.forEach(certification -> {
			certification.setCandidate(null);
		});
	}

	@Deprecated
	private void trimCandidateNonAcademics(Set<CandidateNonAcademicWork> nonAcademicWorks) {
		nonAcademicWorks.forEach(nonAcademicWork -> {
			nonAcademicWork.setCandidate(null);
		});
	}

	@Deprecated
	private void trimCandidateLanguageProficienies(Set<CandidateLanguageProficiency> languageProficiencies) {
		languageProficiencies.forEach(languageProficiency -> {
			languageProficiency.setCandidate(null);
		});
	}

	@Deprecated
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

	@Deprecated
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

	@Deprecated
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