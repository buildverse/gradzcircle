package com.drishika.gradzcircle.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.drishika.gradzcircle.domain.CandidateSkills;
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
import com.drishika.gradzcircle.repository.CandidateProfileScoreRepository;
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
import com.drishika.gradzcircle.service.dto.CandidateProfileListDTO;
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

	private final Matcher<Long> matcher;

	// private final AddressSearchRepository addressSearchRepository;

	private final AddressRepository addressRepository;

	private JobRepository jobRepository;
	
	private DTOConverters dtoConverter;

	private CorporateRepository corporateRepository;
	
	private CandidateProfileScoreRepository candidateProfileScoreRepository;

	public CandidateService(CandidateRepository candidateRepository,
			CandidateSearchRepository candidateSearchRepository,
			CandidateEducationSearchRepository candidateEducationSearchRepository,
			CandidateProjectSearchRepository candidateProjectSearchRepository,
			CandidateEmploymentSearchRepository candidateEmploymentSearchRepository,
			CandidateCertificationSearchRepository candidateCertificationSearchRepository,
			CandidateNonAcademicWorkSearchRepository candidateNonAcademicWorkSearchRepository,
			CandidateLanguageProficiencySearchRepository candidateLanguageProficiencySearchRepository,
			AddressRepository addressRepository, @Qualifier("CandidateGenderMatcher") Matcher<Long> matcher,
			CandidateEducationRepository candidateEducationRepository,
			CandidateProjectRepository candidateProjectRepository,
			CandidateNonAcademicWorkRepository candidateNonAcademicWorkRepository,
			CandidateLanguageProficiencyRepository candidateLanguageProficiencyRepository,
			CandidateCertificationRepository candidateCertificationRepository,
			CandidateEmploymentRepository candidateEmploymentRepository, JobRepository jobRepository,
			CorporateRepository corporateRepository, NationalityRepository nationalityRepository,CountryRepository countryRepository,
			ProfileScoreCalculator profileScoreCalculator, DTOConverters dtoConverter,CandidateProfileScoreRepository candidateProfileScoreRepository) {
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
		this.candidateProfileScoreRepository = candidateProfileScoreRepository;
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
		matcher.match(candidate.getId());
		// candidateSearchRepository.save(result);
		return candidate;
	}

	public Candidate updateCandidate(Candidate candidate) {
		logger.debug("Saving {} with addres {}", candidate, candidate.getAddresses());
		Boolean enableMatch = false;
		Candidate prevCandidate = null;
		Optional<Candidate> prevCandidateOptional = candidateRepository.findById(candidate.getId());
		if(prevCandidateOptional.isPresent())
			prevCandidate = prevCandidateOptional.get();
		else 
			return null;
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
		candidate.setCandidateLanguageProficiencies(prevCandidate.getCandidateLanguageProficiencies());
		candidate.setCertifications(prevCandidate.getCertifications());
		candidate.setNonAcademics(prevCandidate.getNonAcademics());
		candidate.setEmployments(prevCandidate.getEmployments());
		candidate.setCandidateSkills(prevCandidate.getCandidateSkills());
		profileScoreCalculator.updateProfileScore(candidate, Constants.CANDIDATE_PERSONAL_DETAIL_PROFILE,false);
		Candidate result = candidateRepository.save(candidate);
		result.getAddresses().forEach(candidateAddress -> candidateAddress.setCandidate(result));
		// Replace with Future
		if (enableMatch)
			matcher.match(result.getId());
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
	
	public Page<CandidateProfileListDTO> getAllCandidatesForPreview(Pageable pageable) {
		logger.debug("REST request to get all Candidates for Preview");
		return candidateRepository.findCandidatesForPreview(pageable).map(candidate -> dtoConverter.convertToCandidateProfileListingDTO(candidate));
	}

	public Candidate getCandidate(Long id) {
		logger.debug("REST request to get Candidate : {}", id);
		Candidate candidate = candidateRepository.findOneWithEagerRelationships(id);
		if (candidate != null) {
			Set<Address> addresses = addressRepository.findAddressByCandidate(candidate);
			candidate.setAddresses(addresses);
			logger.debug("Retruning candidate {}", candidate);
		}
		 return candidate;
	}
	
	public CandidateDetailDTO getCandidateDetails(Long id ) {
		logger.debug("REST request to get Candidate Profile : {}", id);
		Optional<Candidate> candidateOptional = candidateRepository.findById(id);
		if(candidateOptional.isPresent())
			return setCandidateDetails(candidateOptional.get());
		else 
			return new CandidateDetailDTO();
	}
	
	public CandidateDetailDTO setCandidateDetails(Candidate candidate) {
		logger.debug("Candidate is {}",candidate);
		logger.debug("Candidate educ details are {}",candidate.getEducations());
		logger.debug("Candidate emp details are {}",candidate.getEmployments());
		logger.debug("Candidate cert details are {}",candidate.getCertifications());
		logger.debug("Candidate non acad details are {}",candidate.getNonAcademics());
		logger.debug("Candidate lang details are {}",candidate.getCandidateLanguageProficiencies());
		logger.debug("Candidate skills are {}",candidate.getCandidateSkills());
		Boolean hasEducation = candidate.getEducations()!=null && !candidate.getEducations().isEmpty()? true:false;
		Boolean hasCertification = candidate.getCertifications()!=null && !candidate.getCertifications().isEmpty()? true:false;
		Boolean hasNonAcademics = candidate.getNonAcademics()!=null && !candidate.getNonAcademics().isEmpty()? true:false;
		Boolean hasLanguages = candidate.getCandidateLanguageProficiencies()!=null && !candidate.getCandidateLanguageProficiencies().isEmpty()? true:false;
		Boolean hasEmployment = candidate.getEmployments()!=null && !candidate.getEmployments().isEmpty()?true:false;
		Boolean hasSkills = candidate.getCandidateSkills()!=null && !candidate.getCandidateSkills().isEmpty()?true:false;
		updateCountryAndNationalityDataForDisplay(candidate);
		CandidateDetailDTO candidateDetailsDTO = convertToCandidateDetailDTO(candidate);
		candidateDetailsDTO.setHasCertification(hasCertification);
		candidateDetailsDTO.setHasEducation(hasEducation);
		candidateDetailsDTO.setHasNonAcademic(hasNonAcademics);
		candidateDetailsDTO.setHasEmployment(hasEmployment);
		candidateDetailsDTO.setHasLanguages(hasLanguages);
		candidateDetailsDTO.setHasSkills(hasSkills);
		return candidateDetailsDTO;
	}

	public Candidate getCandidateByLoginId(Long id) {
		logger.debug("REST request to get Candidate : {}", id);
		Candidate candidate = candidateRepository.findByLoginId(id);
		logger.debug("Candidate go is {}",candidate);
		if (candidate != null) {
			Set<Address> addresses = addressRepository.findAddressByCandidate(candidate);
			candidate.setAddresses(addresses);
			logger.debug("Retruning candidate {}", candidate);
		}
		return candidate;
	}
	
	/*Return zero if no education score*/
	public Double getEducationScore(Long candidateId) {
		Double educaitonScore = candidateRepository.hasEducationScore(candidateId);
		logger.debug("education score is {}", educaitonScore);
		return educaitonScore;
	}

	public Candidate saveCandidate(Candidate candidate) {
		return candidateRepository.save(candidate);
	}
	public void deleteCandidate(Long id) {
		logger.debug("REST request to delete Candidate : {}", id);
		candidateRepository.deleteById(id);
		// candidateSearchRepository.delete(id);
	}
	
	public void deleteCandidate(Candidate candidate) {
		if(candidate.getProfileScores()!=null && candidate.getProfileScores().size()>0) {
			logger.debug("Removing user score record {}", candidate.getProfileScores());
			candidate.getProfileScores().forEach( score -> {
				candidateProfileScoreRepository.deleteByCandidateAndProfileId(score.getCandidate().getId(), score.getProfileCategory().getId());
			});
		}
		deleteCandidate(candidate.getId());
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
		List<CandidateSkills> candidateSkills = new ArrayList<>();
		Optional<Candidate> candidateOptional = candidateRepository.findById(candidateId);
		if (!candidateOptional.isPresent())
			return new CandidatePublicProfileDTO();
		Candidate candidate = candidateOptional.get();
		Set<Address> addresses = addressRepository.findAddressByCandidate(candidate);
		List<CandidateEducation> candidateEducations = candidateEducationRepository.findByCandidateId(candidate.getId());
		List<CandidateEmployment> candidateEmployments = candidateEmploymentRepository.findByCandidateId(candidate.getId());
		if (candidateEducations != null && candidateEducations.size() > 0) {
			candidateEducations.forEach(candidateEducation -> {
				Set<CandidateProject> projects = candidateProjectRepository.findByEducation(candidateEducation);
				candidateEducation.setProjects(projects);
			});
		}
		if (candidateEmployments != null && candidateEmployments.size() > 0) {
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
		candidateSkills.addAll(candidate.getCandidateSkills());
		CandidateJob candidateJob = null;
		//Set to show candidate public profile when checking non linked candidates
		if(jobId >0 && corporateId >0) {
			candidateJob = setCandidateReviewedForJobAndGetMatchScoreAndReviwedStatus(candidate, jobId);
			isShortListed = isShortListed(candidate,corporateId);
			canShortListMore = canShortListCandidate(corporateId,jobId);
		}
		// TODO - THIS IS A HACK. NEED TO FIX THIS SHIT. DONE TO GET CANDIDATE LIST FOR LINKED CANDIDATES
		if(jobId<=0 && corporateId >0) {
			isShortListed = isShortListed(candidate,corporateId);
			canShortListMore = true;
		}
		
		return convertToCandidatePublicProfileDTO(candidate, addresses, candidateEducations, candidateEmployments,
				candidateCertifications, candidateNonAcademicWorks, candidateLanguageProficiencies,candidateSkills, isShortListed, candidateJob,canShortListMore);
	}

	
	private Boolean canShortListCandidate(Long corporateId, Long jobId) {
		logger.debug("The job id is {}",jobId);
		Optional<Corporate> corporate = corporateRepository.findById(corporateId);
		Optional<Job> jobOptional = jobRepository.findById(jobId);
		if(!jobOptional.isPresent() || !corporate.isPresent())
			return false;
		Job job = jobOptional.get();
		if(job.getNoOfApplicantLeft() == null)
			return true;
		if( corporate.get().getJobs().contains(job) && job.getNoOfApplicantLeft()!=null && job.getNoOfApplicantLeft()==0)
			return false;
		else
			return true;
			
	}
	
	private Boolean isShortListed(Candidate candidate, Long corporateId) {
		Optional<Corporate> corporateOptional = corporateRepository.findById(corporateId);
		if(!corporateOptional.isPresent())
			return false;
		Corporate corporate = corporateOptional.get();
		logger.info("SHORTLISTED LIST IS {}",corporate.getShortlistedCandidates().size());
		List<CorporateCandidate> linkedCandidates = corporate.getShortlistedCandidates().stream().filter(candidateCorporateLink -> candidateCorporateLink.getCandidate().equals(candidate)).collect(Collectors.toList());
		if (linkedCandidates != null && !linkedCandidates.isEmpty())
			return true;
		else
			return false;

	}

	
	private CandidateJob setCandidateReviewedForJobAndGetMatchScoreAndReviwedStatus(Candidate candidate, Long JobId) {
		logger.debug("Entering setReview and get match score  {}, {}",candidate.getId(), JobId);
		Optional<Job> jobOptional = jobRepository.findById(JobId);
		if(!jobOptional.isPresent())
			return null;
		Job job = jobOptional.get();
		CandidateJob cJ = new CandidateJob(candidate, job);
		CandidateJob candidateJobToReturn = null;
		CandidateJob prevCandidateJob = null;
		prevCandidateJob = candidate.getCandidateJobs().stream().filter(candidateJob -> candidateJob.equals(cJ)).findFirst().orElse(null);
		logger.debug("The candidateJob is {}",prevCandidateJob);
		if (prevCandidateJob != null) {
			candidateJobToReturn = new CandidateJob(prevCandidateJob);
			candidateJobToReturn.setReviewed(prevCandidateJob.getReviewed());
			prevCandidateJob.setReviewed(true);
			candidateRepository.save(candidate);
		} 
		return candidateJobToReturn;
	}

	private CandidatePublicProfileDTO convertToCandidatePublicProfileDTO(Candidate candidate, Set<Address> addresses,
			List<CandidateEducation> candidateEducations, List<CandidateEmployment> candidateEmployments,
			List<CandidateCertification> candidateCertifications,
			List<CandidateNonAcademicWork> candidateNonAcademicWorks,
			Set<CandidateLanguageProficiency> candidateLanguageProficiencies, List<CandidateSkills> candidateSkills, 
			Boolean isShortListed, CandidateJob candidateJob, Boolean canBeShortListed) {
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
		dto.setCandidateSkills(dtoConverter.convertToCandidateSkillsDTO(candidateSkills,false));
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
		Optional<Candidate> candidateFromRepoOptional = candidateRepository.findById(candidateId);
		Optional<Corporate> corporateFromRepoOptional = corporateRepository.findById(corporateId);
		Optional<Job> jobOptional = jobRepository.findById(jobId);
		if(!candidateFromRepoOptional.isPresent())
			return null;
		Candidate candidateFromRepo = candidateFromRepoOptional.get();
		if(!corporateFromRepoOptional.isPresent())
			return null;
		Corporate corporateFromRepo = corporateFromRepoOptional.get();
		if(!jobOptional.isPresent())
			return null;
		Job job = jobOptional.get();
		
		CorporateCandidate corporateCandidateLink = new CorporateCandidate(corporateFromRepo, candidateFromRepo, jobId);
		candidateFromRepo.addCorporateCandidate(corporateCandidateLink);
		job.setNoOfApplicantsBought(job.getNoOfApplicantsBought()!=null?job.getNoOfApplicantsBought()+1:0+1);
		job.setNoOfApplicantLeft(new Long(job.getNoOfApplicants()-(job.getNoOfApplicantsBought()!=null?job.getNoOfApplicantsBought():0)));
		return candidateRepository.save(candidateFromRepo);
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
	
	public CandidateDetailDTO getDetails(Long id) {
		Candidate candidate = getCandidateByLoginId(id);
		return setCandidateDetails(candidate);
	}
	
}