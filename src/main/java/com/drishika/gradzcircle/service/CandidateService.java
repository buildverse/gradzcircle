package com.drishika.gradzcircle.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.HashSet;
import java.util.List;
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
import com.drishika.gradzcircle.service.dto.CandidateCertificationDTO;
import com.drishika.gradzcircle.service.dto.CandidateDetailDTO;
import com.drishika.gradzcircle.service.dto.CandidateEducationDTO;
import com.drishika.gradzcircle.service.dto.CandidateEmploymentDTO;
import com.drishika.gradzcircle.service.dto.CandidateLanguageProficiencyDTO;
import com.drishika.gradzcircle.service.dto.CandidateNonAcademicWorkDTO;
import com.drishika.gradzcircle.service.dto.CandidateProjectDTO;
import com.drishika.gradzcircle.service.dto.CandidatePublicProfileDTO;
import com.drishika.gradzcircle.service.dto.CountryDTO;
import com.drishika.gradzcircle.service.dto.GenderDTO;
import com.drishika.gradzcircle.service.dto.JobCategoryDTO;
import com.drishika.gradzcircle.service.dto.LanguageDTO;
import com.drishika.gradzcircle.service.dto.MaritalStatusDTO;
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
	
	private final CountryRepository countryRepository;

	private final Matcher<Candidate> matcher;

	// private final AddressSearchRepository addressSearchRepository;

	private final AddressRepository addressRepository;

	private JobRepository jobRepository;

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
			CorporateRepository corporateRepository, NationalityRepository nationalityRepository,CountryRepository countryRepository) {
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
	}

	public void createCandidate(User user) {
		Candidate candidate = new Candidate();
		candidate.setLogin(user);
		candidate.setMatchEligible(true);
		candidateRepository.save(candidate);
		candidateSearchRepository.save(candidate);
		logger.debug("Created Information for candidate: {}", candidate);
	}

	public Candidate createCandidate(Candidate candidate) {
		logger.debug("REST request to save Candidate : {}", candidate);
		candidate.setMatchEligible(true);
		Candidate result = candidateRepository.save(candidate);
		// Replace with Future
		matcher.match(result);
		// candidateSearchRepository.save(result);
		return result;
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
		candidate.setEducations(prevCandidate.getEducations());
		candidate.setCandidateJobs(prevCandidate.getCandidateJobs());
		candidate.setMatchEligible(prevCandidate.getMatchEligible());
		Candidate result = candidateRepository.save(candidate);
		result.getAddresses().forEach(candidateAddress -> candidateAddress.setCandidate(result));
		// Replace with Future
		if (enableMatch)
			matcher.match(result);
		// candidateSearchRepository.save(result);
		
		return result;
	}
	
	
	
	private void injestNationalityDetails(Candidate candidate) {
		Nationality nationality  = nationalityRepository.findByNationality(candidate.getNationality().getNationality());
		candidate.setNationality(nationality);
	}
	
	private void injestCountryDetails(Candidate candidate) {
		Country country  = countryRepository.findByCountryNiceName(candidate.getAddresses().iterator().next().getCountry().getCountryNiceName());
		candidate.getAddresses().forEach(address -> address.setCountry(country));
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

	public Candidate getCandidateByLoginId(Long id) {
		logger.debug("REST request to get Candidate : {}", id);
		Candidate candidate = candidateRepository.findByLoginId(id);
		Set<Address> addresses = addressRepository.findAddressByCandidate(candidate);
		if (candidate != null)
			candidate.setAddresses(addresses);
		logger.debug("Retruning candidate {}", candidate);
		return candidate;
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
		logger.debug("REST request to get Candidate public profile for candidate {}", candidateId);
		Boolean shortListed = false;
		Candidate candidate = candidateRepository.findOne(candidateId);
		if (candidate == null)
			return new CandidatePublicProfileDTO();
		Set<Address> addresses = addressRepository.findAddressByCandidate(candidate);
		Set<CandidateEducation> candidateEducations = new HashSet<CandidateEducation>(
				candidateEducationRepository.findByCandidateId(candidate.getId()));
		Set<CandidateEmployment> candidateEmployments = new HashSet<>(
				candidateEmploymentRepository.findByCandidateId(candidate.getId()));
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
		Set<CandidateCertification> candidateCertifications = new HashSet<>(
				candidateCertifcationRepository.findCertificationsByCandidateId(candidate.getId()));
		Set<CandidateNonAcademicWork> candidateNonAcademicWorks = new HashSet<>(
				candidateNonAcademicRepository.findNonAcademicWorkByCandidateId(candidate.getId()));
		Set<CandidateLanguageProficiency> candidateLanguageProficiencies = new HashSet<>(
				candidateLanguageProficiencyRepository
						.findCandidateLanguageProficienciesByCandidateId(candidate.getId()));

		/*
		 * candidate.getAddresses().addAll(addresses);
		 * candidate.getEducations().addAll(candidateEducations);
		 * candidate.getEmployments().addAll(candidateEmployments);
		 * candidate.getCertifications().addAll(candidateCertifications);
		 * candidate.getNonAcademics().addAll(candidateNonAcademicWorks);
		 * candidate.getCandidateLanguageProficiencies().addAll(
		 * candidateLanguageProficiencies);
		 */
		if(jobId >0 && corporateId >0) {
			setCandidateReviewedForJob(candidate, jobId);
			shortListed = isShortListed(candidate, jobId, corporateId);
		}
		return convertToCandidatePublicProfileDTO(candidate, addresses, candidateEducations, candidateEmployments,
				candidateCertifications, candidateNonAcademicWorks, candidateLanguageProficiencies, shortListed);
	}

	private Boolean isShortListed(Candidate candidate, Long jobId, Long corporateId) {
		Corporate corporate = corporateRepository.findOne(corporateId);
		CorporateCandidate cC = new CorporateCandidate(corporate, candidate, jobId);
		if (corporate.getShortlistedCandidates().stream().filter(link -> link.equals(cC)).findFirst().isPresent())
			return true;
		else
			return false;

	}

	private void setCandidateReviewedForJob(Candidate candidate, Long JobId) {
		Job job = jobRepository.findOne(JobId);
		CandidateJob cJ = new CandidateJob(candidate, job);
		CandidateJob candidateJobForReview = null;
		if (candidate.getCandidateJobs().stream().filter(candidateJob -> candidateJob.equals(cJ)).findFirst()
				.isPresent())
			candidateJobForReview = candidate.getCandidateJobs().stream()
					.filter(candidateJob -> candidateJob.equals(cJ)).findFirst().get();
		if (candidateJobForReview != null) {
			candidateJobForReview.setReviewed(true);
			candidateRepository.save(candidate);
		}

	}

	private CandidatePublicProfileDTO convertToCandidatePublicProfileDTO(Candidate candidate, Set<Address> addresses,
			Set<CandidateEducation> candidateEducations, Set<CandidateEmployment> candidateEmployments,
			Set<CandidateCertification> candidateCertifications,
			Set<CandidateNonAcademicWork> candidateNonAcademicWorks,
			Set<CandidateLanguageProficiency> candidateLanguageProficiencies, Boolean isShortListed) {
		CandidatePublicProfileDTO dto = new CandidatePublicProfileDTO();
		dto.setShortListed(isShortListed);
		setCandidateDetails(candidate, dto);
		setCandidateAddresses(addresses, dto);
		setCandidateEducations(candidateEducations, dto);
		setCandidateEmployments(candidateEmployments, dto);
		setCandidateCertifications(candidateCertifications, dto);
		setCandidateLanguageProficiencies(candidateLanguageProficiencies, dto);
		setCandidateNonAcademicWork(candidateNonAcademicWorks, dto);
		return dto;
	}

	/**
	 * @param candidateNonAcademicWorks
	 * @param dto
	 */
	private void setCandidateNonAcademicWork(Set<CandidateNonAcademicWork> candidateNonAcademicWorks,
			CandidatePublicProfileDTO dto) {
		candidateNonAcademicWorks.forEach(nonAcademic -> {
			CandidateNonAcademicWorkDTO nonAcademicWorkDTO = new CandidateNonAcademicWorkDTO();
			nonAcademicWorkDTO.setNonAcademicInitiativeTitle(nonAcademic.getNonAcademicInitiativeTitle());
			nonAcademicWorkDTO.setNonAcademicInitiativeDescription(nonAcademic.getNonAcademicInitiativeDescription());
			nonAcademicWorkDTO.setNonAcademicWorkEndDate(nonAcademic.getNonAcademicWorkEndDate());
			nonAcademicWorkDTO.setNonAcademicWorkStartDate(nonAcademic.getNonAcademicWorkStartDate());
			nonAcademicWorkDTO.setRoleInInitiative(nonAcademic.getRoleInInitiative());
			dto.getNonAcademics().add(nonAcademicWorkDTO);
		});
	}

	/**
	 * @param candidateLanguageProficiencies
	 * @param dto
	 */
	private void setCandidateLanguageProficiencies(Set<CandidateLanguageProficiency> candidateLanguageProficiencies,
			CandidatePublicProfileDTO dto) {
		candidateLanguageProficiencies.forEach(languageProficiency -> {
			CandidateLanguageProficiencyDTO languageProficiencyDTO = new CandidateLanguageProficiencyDTO();
			LanguageDTO languageDTO = new LanguageDTO();
			languageDTO.setId(languageProficiency.getLanguage().getId());
			languageDTO.setLanguage(languageProficiency.getLanguage().getLanguage());
			languageProficiencyDTO.setLanguage(languageDTO);
			languageProficiencyDTO.setProficiency(languageProficiency.getProficiency());
			dto.getCandidateLanguageProficiencies().add(languageProficiencyDTO);
		});
	}

	/**
	 * @param candidateCertifications
	 * @param dto
	 */
	private void setCandidateCertifications(Set<CandidateCertification> candidateCertifications,
			CandidatePublicProfileDTO dto) {
		candidateCertifications.forEach(certification -> {
			CandidateCertificationDTO certificationDTO = new CandidateCertificationDTO();
			certificationDTO.setCertificationTitle(certification.getCertificationTitle());
			certificationDTO.setCertificationDetails(certification.getCertificationDetails());
			certificationDTO.setCertificationDate(certification.getCertificationDate());
			dto.getCertifications().add(certificationDTO);
		});
	}

	/**
	 * @param candidateEmployments
	 * @param dto
	 */
	private void setCandidateEmployments(Set<CandidateEmployment> candidateEmployments, CandidatePublicProfileDTO dto) {
		candidateEmployments.forEach(employment -> {
			CandidateEmploymentDTO employmentDTO = new CandidateEmploymentDTO();
			employmentDTO.setJobTitle(employment.getJobTitle());
			employmentDTO.setEmployerName(employment.getEmployerName());
			employmentDTO.setEmploymentStartDate(employment.getEmploymentStartDate());
			employmentDTO.setEmploymentEndDate(employment.getEmploymentEndDate());
			employmentDTO.setEmploymentType(employment.getEmploymentType().getEmploymentType());
			employmentDTO.setJobType(employment.getJobType().getJobType());
			employmentDTO.setJobDescription(employment.getJobDescription());
			employment.getProjects().forEach(project -> {
				CandidateProjectDTO projectDTO = setCandidateProjects(project);
				employmentDTO.getProjects().add(projectDTO);
			});
			dto.getEmployments().add(employmentDTO);
		});
	}

	/**
	 * @param project
	 * @return
	 */
	private CandidateProjectDTO setCandidateProjects(CandidateProject project) {
		CandidateProjectDTO projectDTO = new CandidateProjectDTO();
		projectDTO.setProjectTitle(project.getProjectTitle());
		projectDTO.setProjectDescription(project.getProjectDescription());
		projectDTO.setProjectStartDate(project.getProjectStartDate());
		projectDTO.setProjectEndDate(project.getProjectEndDate());
		projectDTO.setContributionInProject(project.getContributionInProject());
		projectDTO.setIsCurrentProject(project.isIsCurrentProject());
		projectDTO.setProjectType(project.getProjectType());
		return projectDTO;
	}

	/**
	 * @param candidateEducations
	 * @param dto
	 */
	private void setCandidateEducations(Set<CandidateEducation> candidateEducations, CandidatePublicProfileDTO dto) {
		candidateEducations.forEach(education -> {
			CandidateEducationDTO educationDTO = new CandidateEducationDTO();
			educationDTO.setEducationFromDate(education.getEducationFromDate());
			educationDTO.setEducationToDate(education.getEducationToDate());
			educationDTO.setCollegeName(education.getCollege().getCollegeName());
			educationDTO.setUniversityName(education.getCollege().getUniversity().getUniversityName());
			String scoreType = education.getScoreType();
			if (scoreType != null && scoreType.equals(Constants.GPA))
				educationDTO.setGrade(education.getGrade());
			else
				educationDTO.setPercentage(education.getPercentage());
			educationDTO.setScoreType(education.getScoreType());
			educationDTO.setQualification(education.getQualification().getQualification());
			educationDTO.setCourse(education.getCourse().getCourse());
			education.getProjects().forEach(project -> {
				CandidateProjectDTO projectDTO = setCandidateProjects(project);
				educationDTO.getProjects().add(projectDTO);
			});
			dto.getEducations().add(educationDTO);
		});
	}

	/**
	 * @param addresses
	 * @param dto
	 */
	private void setCandidateAddresses(Set<Address> addresses, CandidatePublicProfileDTO dto) {
		addresses.forEach(address -> {
			AddressDTO addressDTO = new AddressDTO();
			addressDTO.setAddressLineOne(address.getAddressLineOne());
			addressDTO.setAddressLineTwo(address.getAddressLineTwo());
			addressDTO.setCity(address.getCity());
			CountryDTO countryDTO = new CountryDTO();
			countryDTO.setCountryNiceName(address.getCountry().getCountryNiceName());
			addressDTO.setCountry(countryDTO);
			addressDTO.setState(address.getState());
			addressDTO.setZip(address.getZip());
			dto.getAddresses().add(addressDTO);
		});
	}

	/**
	 * @param candidate
	 * @return
	 */
	private void setCandidateDetails(Candidate candidate, CandidatePublicProfileDTO dto) {
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
		dto.setCandidateDetails(candidateDetailDTO);
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
		CorporateCandidate corporateCandidateLink = new CorporateCandidate(corporateFromRepo, candidateFromRepo, jobId);
		candidateFromRepo.addCorporateCandidate(corporateCandidateLink);
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