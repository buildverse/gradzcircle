/**
 * 
 */
package com.drishika.gradzcircle.service.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.config.Constants;
import com.drishika.gradzcircle.constants.ApplicationConstants;
import com.drishika.gradzcircle.domain.Address;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateAppliedJobs;
import com.drishika.gradzcircle.domain.CandidateCertification;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateEmployment;
import com.drishika.gradzcircle.domain.CandidateJob;
import com.drishika.gradzcircle.domain.CandidateLanguageProficiency;
import com.drishika.gradzcircle.domain.CandidateNonAcademicWork;
import com.drishika.gradzcircle.domain.CandidateProject;
import com.drishika.gradzcircle.domain.CandidateSkills;
import com.drishika.gradzcircle.domain.CorporateCandidate;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.exception.FileRetrieveException;
import com.drishika.gradzcircle.service.dto.AddressDTO;
import com.drishika.gradzcircle.service.dto.CandidateCertificationDTO;
import com.drishika.gradzcircle.service.dto.CandidateDTO;
import com.drishika.gradzcircle.service.dto.CandidateDetailDTO;
import com.drishika.gradzcircle.service.dto.CandidateEducationDTO;
import com.drishika.gradzcircle.service.dto.CandidateEmploymentDTO;
import com.drishika.gradzcircle.service.dto.CandidateJobDTO;
import com.drishika.gradzcircle.service.dto.CandidateLanguageProficiencyDTO;
import com.drishika.gradzcircle.service.dto.CandidateNonAcademicWorkDTO;
import com.drishika.gradzcircle.service.dto.CandidateProfileListDTO;
import com.drishika.gradzcircle.service.dto.CandidateProjectDTO;
import com.drishika.gradzcircle.service.dto.CandidateSkillsDTO;
import com.drishika.gradzcircle.service.dto.CollegeDTO;
import com.drishika.gradzcircle.service.dto.CorporateJobDTO;
import com.drishika.gradzcircle.service.dto.CountryDTO;
import com.drishika.gradzcircle.service.dto.CourseDTO;
import com.drishika.gradzcircle.service.dto.JobEconomicsDTO;
import com.drishika.gradzcircle.service.dto.JobStatistics;
import com.drishika.gradzcircle.service.dto.LanguageDTO;
import com.drishika.gradzcircle.service.dto.QualificationDTO;
import com.drishika.gradzcircle.service.storage.FileServiceS3;


/**
 * @author abhinav
 *
 */
@Component
@Transactional
public class DTOConverters {

	private final Logger logger = LoggerFactory.getLogger(DTOConverters.class);
	
	private final FileServiceS3 fileService;
	
	private final GradzcircleCacheManager<Long, String > imageUrlCache;
	
	public DTOConverters(FileServiceS3 fileService, GradzcircleCacheManager<Long, String > imageUrlCache) {
		this.fileService = fileService;
		this.imageUrlCache = imageUrlCache;
	}

	public CandidateProfileListDTO convertToCandidateProfileListingDTO(CandidateAppliedJobs candidateAppliedJob,
			Candidate candidate, Job job) {
		CandidateProfileListDTO dto = new CandidateProfileListDTO();
		setCoreCandidateFields(candidate, dto);
		setMatchScore(candidate, job, dto);
		dto.setCorporateId(job.getCorporate().getId());
		dto.setJobId(job.getId());
		CorporateCandidate corporateCandidate = candidate.getShortlistedByCorporates().stream()
				.filter(cc -> cc.getCorporate().equals(job.getCorporate())).findFirst().orElse(null);
		if (corporateCandidate != null)
			dto.setShortListed(true);
		else
			dto.setShortListed(false);
		logger.debug("The CandidateProfileListDTO is {}", dto);
		return dto;
	}

	private void setMatchScore(Candidate candidate, Job job, CandidateProfileListDTO dto) {
		CandidateJob candidateJob = candidate.getCandidateJobs().stream().filter(cJ -> cJ.getJob().equals(job))
				.findAny().orElse(null);
		if (candidateJob != null) {
			dto.setMatchScore(candidateJob.getMatchScore());
		} else {
			dto.setMatchScore(0d);
		}
	}

	private void setCoreCandidateFields(Candidate candidate, CandidateProfileListDTO dto) {
		CandidateEducation highestCandidateEducation = null;
		dto.setFirstName(candidate.getFirstName());
		dto.setId(candidate.getId());
		dto.setLastName(candidate.getLastName());
		dto.setLogin(candidate.getLogin());
		dto.setAboutMe(candidate.getAboutMe());
		if(candidate.getLogin().getImageUrl()!=null) {
			dto.setImageUrl(getImageUrl(candidate.getLogin().getId()));
		}
		highestCandidateEducation = candidate.getEducations().stream().filter(education -> education.isHighestQualification()).findFirst().orElse(null);
		if (highestCandidateEducation!=null) {

			dto.setQualificationWithHighestCourse(highestCandidateEducation.getQualification().getQualification()
					+ " in " + highestCandidateEducation.getCourse().getCourse());
			dto.setPercent(highestCandidateEducation.getPercentage()==null?highestCandidateEducation.getGrade()*10:highestCandidateEducation.getPercentage());
		}
	}

	public CandidateProfileListDTO convertToCandidateProfileListingDTO(Candidate candidate, CandidateJob candidateJob,
			Set<CorporateCandidate> linkedCandidates) {
		CandidateProfileListDTO dto = new CandidateProfileListDTO();
		logger.debug("Linked candidate and Corporate are {}", linkedCandidates);
		logger.debug("Candidate is {}", candidate.getId());
		CorporateCandidate linkedCorporateCandidate = linkedCandidates.stream()
				.filter(link -> link.getCandidate().equals(candidate)).findFirst().orElse(null);
		logger.debug("Linked {}", linkedCorporateCandidate);
		setCoreCandidateFields(candidate, dto);
		dto.setReviewed(candidateJob.getReviewed());
		dto.setMatchScore(candidateJob.getMatchScore());
		
		if (linkedCorporateCandidate != null)
			dto.setShortListed(true);
		else
			dto.setShortListed(false);
		logger.debug("Candidate Job is {} and job inside is {} and no of candidatesLeft are ", candidateJob,
				candidateJob.getJob(), candidateJob.getJob().getNoOfApplicantLeft());
		if (candidateJob.getJob().getNoOfApplicantLeft() != null && candidateJob.getJob().getNoOfApplicantLeft() == 0)
			dto.setCanBuy(false);
		else
			dto.setCanBuy(true);
		return dto;
	}

	public CandidateProfileListDTO convertToCandidateProfileListingDTO(Candidate candidate,
			CorporateCandidate corporateCandidate,Long jobId) {
		CandidateProfileListDTO dto = new CandidateProfileListDTO();
		setCoreCandidateFields(candidate, dto);
		CandidateJob candidateJob =  candidate.getCandidateJobs().stream().filter(cJob->cJob.getJob().getId().equals(jobId)).findAny().orElse(null);
		if(candidateJob!=null) {
			dto.setMatchScore(candidateJob.getMatchScore());
		}
		return dto;
	}

	public CandidateProfileListDTO convertToCandidateProfileListingDTO(Candidate candidate) {
		CandidateProfileListDTO dto = new CandidateProfileListDTO();
		setCoreCandidateFields(candidate, dto);
		return dto;
	}

	/*public CandidateProfileListDTO convertToCandidateProfileListingDTO(CorporateCandidate corporateCandidate) {
		Candidate candidate = corporateCandidate.getCandidate();
		return convertToCandidateProfileListingDTO(candidate);
	}*/

	public CorporateJobDTO convertToJobListingForCorporate(Job job, Long totalLinkedCandidates, Long totalNumberOfJobs,
			Long newApplicants, Long jobsLastMonth, Long numberOfCandidatesShortListedByJob) {
		CorporateJobDTO jobListingData = new CorporateJobDTO();
		jobListingData.setTotalLinkedCandidates(totalLinkedCandidates);
		jobListingData.setTotalNumberOfJobs(totalNumberOfJobs);
		jobListingData.setNewApplicants(newApplicants);
		jobListingData.setJobsLastMonth(jobsLastMonth);
		jobListingData.setJobStatus(job.getJobStatus());
		jobListingData.setJobTitle(job.getJobTitle());
		jobListingData.setEmploymentType(job.getEmploymentType());
		jobListingData.setJobType(job.getJobType());
		jobListingData.setCorporateEscrowAmount(job.getCorporate().getEscrowAmount());
		// FILTER OUT REVIEWED CANDIDATES
		// jobListingData.setNoOfMatchedCandidates(job.getCandidateJobs().stream().filter(candidateJob
		// -> !candidateJob.getReviewed()).collect(Collectors.toSet()).size());
		jobListingData.setNoOfMatchedCandidates(
				job.getCandidateJobs().size() - numberOfCandidatesShortListedByJob.intValue());
		//Show applied only if matched
		//Set<Candidate> appliedAndMatchedCandidates = job.getCandidateJobs().parallelStream().filter(candidateJob->candidateJob.getCandidate().equals(o))
		jobListingData.setNoOfCandidatesApplied(job.getCandidateJobs().parallelStream().filter(candidateJob-> 
			job.getAppliedCandidates().contains(candidateJob.getCandidate())).collect(Collectors.toSet()).size());
		//jobListingData.setNoOfCandidatesApplied(job.getAppliedCandidates().size());
		jobListingData.setNoOfShortListedCandidate(numberOfCandidatesShortListedByJob);
		jobListingData.setId(job.getId());
		jobListingData.setCanEdit(job.getCanEdit());
		jobListingData.setHasBeenEdited(job.getHasBeenEdited());
		jobListingData.setEverActive(job.getEverActive());
		jobListingData.setNumberOfCandidatesRemaining(job.getNoOfApplicantLeft());
		logger.debug("The number of candidates left are {}", job.getNoOfApplicantLeft());
		jobListingData.setNoOfApplicants(job.getNoOfApplicants());
		return jobListingData;
	}

	public JobEconomicsDTO convertToJobEconomicsDTO(Job job, Double filterCost) {
		JobEconomicsDTO jobEconomicsDTO = new JobEconomicsDTO();
		jobEconomicsDTO.setId(job.getId());
		if (job.getNoOfApplicantLeft() != null && job.getNoOfApplicantLeft() == 0) {
			jobEconomicsDTO.setJobCost(job.getJobCost());
			jobEconomicsDTO.setFilterCost(filterCost);
		}
		if (job.getPaymentType() != null)
			if (job.getPaymentType().name().equals("AS_YOU_GO"))
				jobEconomicsDTO.setPayAsYouGo(true);
			else
				jobEconomicsDTO.setPayAsYouGo(false);
		jobEconomicsDTO.setCorporateEscrowAmount(job.getCorporate().getEscrowAmount());
		return jobEconomicsDTO;
	}

	public CandidateJobDTO convertToJobListingForCandidate(Job job, Long candidateId, Boolean appliedJobs) {
		CandidateJobDTO jobListingData = new CandidateJobDTO();
		setCandidateJobDTOCoreFields(job, jobListingData);
		logger.debug("Job and Candidates matched are {}--{}", job, job.getCandidateJobs());
		CandidateJob matchedCandidateJob = job.getCandidateJobs().stream()
				.filter(candidateJob -> candidateJob.getCandidate().getId().equals(candidateId)).findAny().orElse(null);
		if (matchedCandidateJob == null) {
			jobListingData.setMatchScore(0D);
		} else {
			jobListingData.setMatchScore(matchedCandidateJob.getMatchScore());
		}
		if (!appliedJobs)
			job.getAppliedCandidates().stream()
					.filter(appliedCandidate -> !appliedCandidate.getId().equals(candidateId));
		else
			job.getAppliedCandidates().stream()
					.filter(appliedCandidate -> appliedCandidate.getId().equals(candidateId));
		return jobListingData;
	}

	public CandidateJobDTO convertJobViewForCandidate(Job job, Long candidateId,Boolean hasEducation) {
		CandidateJobDTO jobData = new CandidateJobDTO();
		setCandidateJobDTOCoreFields(job, jobData);
		if(hasEducation)
			jobData.setCandidateHasEducation(hasEducation);
		else
			jobData.setCandidateHasEducation(false);
		if (job.getAppliedCandidates().stream().filter(appliedCandidate -> appliedCandidate.getId().equals(candidateId))
				.findAny().isPresent()) {
			jobData.setHasCandidateApplied(true);
		} else
			jobData.setHasCandidateApplied(false);
		CandidateJob candidateJob = job.getCandidateJobs().stream()
				.filter(matchedCandidateJob -> matchedCandidateJob.getCandidateId().equals(candidateId)).findFirst()
				.orElse(null);
		if (candidateJob != null)
			jobData.setMatchScore(candidateJob.getMatchScore());
		else
			jobData.setMatchScore(0d);
		return jobData;
	}

	public CandidateJobDTO convertToJobListingForCandidateWithNoEducation(Job job, Long candidateId) {
		CandidateJobDTO jobListingData = new CandidateJobDTO();
		setCandidateJobDTOCoreFields(job, jobListingData);
		jobListingData.setMatchScore(0d);
		return jobListingData;
	}

	public CandidateJobDTO convertToJobListingForPortalAnonymous(Job job, Long totalNumberOfActiveJobs,
			List<JobStatistics> jobStatisticsByEmploymentType, List<JobStatistics> jobStatisticsByJobType) {
		CandidateJobDTO jobListingData = new CandidateJobDTO();
		setCandidateJobDTOCoreFields(job, jobListingData);
		jobListingData.setTotalNumberOfJobs(totalNumberOfActiveJobs);
		setJobStatistics(jobListingData, jobStatisticsByEmploymentType);
		setJobStatistics(jobListingData, jobStatisticsByJobType);
		return jobListingData;
	}

	public CandidateJobDTO convertToJobListingForCandidateOrGuest(Job job, Long totalNumberOfActiveJobs,
			List<JobStatistics> jobStatisticsByEmploymentType, List<JobStatistics> jobStatisticsByJobType,
			Long candidateId) {
		// logger.debug("Coverting to DTO candidateJob for job {} with total number of
		// jobs {}",job,totalNumberOfActiveJobs);
		CandidateJobDTO jobListingData = new CandidateJobDTO();
		setCandidateJobDTOCoreFields(job, jobListingData, candidateId);
		jobListingData.setTotalNumberOfJobs(totalNumberOfActiveJobs);
		setJobStatistics(jobListingData, jobStatisticsByEmploymentType);
		setJobStatistics(jobListingData, jobStatisticsByJobType);
		return jobListingData;
	}

	private void setJobStatistics(CandidateJobDTO jobListingData, List<JobStatistics> jobStatistics) {
		if (jobStatistics == null)
			return;
		jobStatistics.forEach(stat -> {
			// logger.debug("Job Stats being processed is {}",jobStatistics);
			if (ApplicationConstants.EMPLOYMENT_TYPE_CONTRACT.equals(stat.getType()))
				jobListingData.setCountOfContractEmployment(stat.getCount());
			else if (ApplicationConstants.EMPLOYMENT_TYPE_PERMANENT.equals(stat.getType()))
				jobListingData.setCountOfPermanentEmployment(stat.getCount());
			else if (ApplicationConstants.JOB_TYPE_FULL_TIME.equals(stat.getType()))
				jobListingData.setCountOfFullTimeJob(stat.getCount());
			else if (ApplicationConstants.JOB_TYPE_PART_TIME.equals(stat.getType()))
				jobListingData.setCountOfPartTimeJob(stat.getCount());
			else if (ApplicationConstants.JOB_TYPE_INTERNSHIP.equals(stat.getType()))
				jobListingData.setCountOfInternJob(stat.getCount());
			else if (ApplicationConstants.JOB_TYPE_SUMMER_JOB.equals(stat.getType()))
				jobListingData.setCountOfSummerJob(stat.getCount());
		});
	}

	private void setCandidateJobDTOCoreFields(Job job, CandidateJobDTO jobListingData, Long candidateId) {
		logger.debug("Incoming canidate Is is {}", candidateId);
		jobListingData.setJobStatus(job.getJobStatus());
		jobListingData.setJobTitle(job.getJobTitle());
		jobListingData.setEmploymentType(job.getEmploymentType());
		jobListingData.setJobType(job.getJobType());
		jobListingData.setId(job.getId());
		jobListingData.setCity(convertToCamelCase(job.getCorporate().getCity()));
		jobListingData.setCorporateName(convertToCamelCase(job.getCorporate().getName()));
		jobListingData.setSalary(job.getSalary());
		jobListingData.setUpdateDate(job.getUpdateDate());
		if (!job.getAppliedCandidates().stream().filter(candidate -> candidateId.equals(candidate.getId())).findFirst()
				.isPresent())
			jobListingData.setHasCandidateApplied(false);
		CandidateJob cJob =null;
		if (candidateId != null) 
				cJob = job.getCandidateJobs().stream().filter(candidateJob -> candidateId.equals(candidateJob.getCandidateId())).findFirst().orElse(null);
			if (cJob !=null)
				jobListingData.setMatchScore(cJob
						.getMatchScore());
		if (job.getJobDescription().length() > 500)
			jobListingData.setJobDescription(job.getJobDescription().substring(0, 499));
		else
			jobListingData.setJobDescription(job.getJobDescription());
	}

	private void setCandidateJobDTOCoreFields(Job job, CandidateJobDTO jobListingData) {
		jobListingData.setJobStatus(job.getJobStatus());
		jobListingData.setJobTitle(job.getJobTitle());
		jobListingData.setEmploymentType(job.getEmploymentType());
		jobListingData.setJobType(job.getJobType());
		jobListingData.setId(job.getId());
		jobListingData.setCity(convertToCamelCase(job.getCorporate().getCity()));
		jobListingData.setCorporateName(convertToCamelCase(job.getCorporate().getName()));
		jobListingData.setCorporateUrl(job.getCorporate().getLogin().getImageUrl());
		jobListingData.setSalary(job.getSalary());
		jobListingData.setUpdateDate(job.getUpdateDate());
		jobListingData.setCorporateLoginId(job.getCorporate().getLogin().getId());
		if(job.getCorporate().getLogin().getImageUrl()!=null)
			jobListingData.setCorporateImageUrl(getImageUrl(job.getCorporate().getLogin().getId()));
		if (job.getJobDescription().length() > 500)
			jobListingData.setJobDescription(job.getJobDescription().substring(0, 499));
		else
			jobListingData.setJobDescription(job.getJobDescription());
	}

	public String convertToCamelCase(String string) {
		if (string == null)
			return "";
		String[] splitString = string.split("\\s+");
		StringBuilder builder = new StringBuilder();
		for(int index=0;index<splitString.length;index++) {
			char[] charArray = splitString[index].toLowerCase().toCharArray();
			charArray[0] = Character.toUpperCase(charArray[0]);
			for (int i = 1; i < charArray.length; i++)
				charArray[i] = Character.toLowerCase(charArray[i]);
			if(index<splitString.length-1)
				builder.append(charArray).append(" ");
			else
				builder.append(charArray);
			
		}
		return builder.toString();
	}

	/**
	 * @param candidateNonAcademicWorks
	 * @param dto
	 */
	public List<CandidateNonAcademicWorkDTO> convertCandidateNonAcademicWork(
			List<CandidateNonAcademicWork> candidateNonAcademicWorks, Boolean withCandidateProfileScore,
			Candidate candidate) {
		List<CandidateNonAcademicWorkDTO> nonAcadWorksDTOs = new ArrayList<>();

		candidateNonAcademicWorks.forEach(nonAcademic -> {
			CandidateNonAcademicWorkDTO nonAcademicWorkDTO = new CandidateNonAcademicWorkDTO();
			nonAcademicWorkDTO.setId(nonAcademic.getId());
			nonAcademicWorkDTO.setNonAcademicInitiativeTitle(nonAcademic.getNonAcademicInitiativeTitle());
			nonAcademicWorkDTO.setNonAcademicInitiativeDescription(nonAcademic.getNonAcademicInitiativeDescription());
			nonAcademicWorkDTO.setNonAcademicWorkEndDate(nonAcademic.getNonAcademicWorkEndDate());
			nonAcademicWorkDTO.setNonAcademicWorkStartDate(nonAcademic.getNonAcademicWorkStartDate());
			nonAcademicWorkDTO.setRoleInInitiative(nonAcademic.getRoleInInitiative());
			nonAcademicWorkDTO.setDuration(nonAcademic.getDuration());
			nonAcademicWorkDTO.setIsCurrentActivity(nonAcademic.isIsCurrentActivity());
			// dto.getNonAcademics().add(nonAcademicWorkDTO);
			nonAcadWorksDTOs.add(nonAcademicWorkDTO);
			if (withCandidateProfileScore) {
				CandidateDTO candidateDTO = new CandidateDTO();
				candidateDTO.setProfileScore(nonAcademic.getCandidate().getProfileScore() != null
						? nonAcademic.getCandidate().getProfileScore()
						: 0d);
				nonAcademicWorkDTO.setCandidate(candidateDTO);
			}

		});

		return nonAcadWorksDTOs;
	}

	/**
	 * @param candidateLanguageProficiencies
	 * @param dto
	 */
	public List<CandidateLanguageProficiencyDTO> convertCandidateLanguageProficiencies(
			Set<CandidateLanguageProficiency> candidateLanguageProficiencies, Boolean withCandidateProfileScore,
			Candidate candidate) {
		List<CandidateLanguageProficiencyDTO> languageProficiencyDTOs = new ArrayList<>();

		candidateLanguageProficiencies.forEach(candidateLanguageProficiency -> {
			CandidateLanguageProficiencyDTO candidateLanguageProficiencyDTO = new CandidateLanguageProficiencyDTO();
			LanguageDTO languageDTO = new LanguageDTO();
			candidateLanguageProficiencyDTO.setId(candidateLanguageProficiency.getId());
			languageDTO.setLanguage(candidateLanguageProficiency.getLanguage().getLanguage());
			languageDTO.setId(candidateLanguageProficiency.getLanguage().getId());
			languageDTO.setValue(candidateLanguageProficiency.getLanguage().getLanguage());
			languageDTO.setDisplay(candidateLanguageProficiency.getLanguage().getLanguage());
			candidateLanguageProficiencyDTO.setLanguage(languageDTO);
			candidateLanguageProficiencyDTO.setProficiency(candidateLanguageProficiency.getProficiency());
			if (withCandidateProfileScore) {
				CandidateDTO candidateDTO = new CandidateDTO();
				candidateDTO.setProfileScore(candidateLanguageProficiency.getCandidate().getProfileScore() != null
						? candidateLanguageProficiency.getCandidate().getProfileScore()
						: 0d);
				candidateLanguageProficiencyDTO.setCandidate(candidateDTO);

			}
			languageProficiencyDTOs.add(candidateLanguageProficiencyDTO);
		});

		return languageProficiencyDTOs;
	}
	
	public List<CandidateSkillsDTO> convertToCandidateSkillsDTO(List<CandidateSkills> skills,Boolean withCandidateProfileScore) {
		logger.debug("Candidate SKills are {}",skills);
		List<CandidateSkillsDTO> dto = new ArrayList<>();
		
		skills.forEach(skill->{
			CandidateSkillsDTO candidateSkillDTO = new CandidateSkillsDTO();
			candidateSkillDTO.setId(skill.getId());
			candidateSkillDTO.setSkillName(skill.getSkills().getSkill());
			if (withCandidateProfileScore) {
				CandidateDTO candidateDTO = new CandidateDTO();
				candidateDTO.setProfileScore(skill.getCandidate().getProfileScore() != null
						? skill.getCandidate().getProfileScore()
						: 0d);
				candidateSkillDTO.setCandidate(candidateDTO);

			}
			dto.add(candidateSkillDTO);
		});
		return dto;
	}
	
	public CandidateSkillsDTO convertToCandidateSkillDTO(Optional<CandidateSkills> skill, Boolean withCandidateProfileScore) {
		logger.debug("Candidate SKills are {}", skill);
		CandidateSkillsDTO candidateSkillDTO = null;
		if(skill.isPresent()) {
			CandidateSkills skills = skill.get();
			candidateSkillDTO = new CandidateSkillsDTO();
			candidateSkillDTO.setId(skills.getId());
			candidateSkillDTO.setSkillName(skills.getSkills().getSkill());
			if (withCandidateProfileScore) {
				CandidateDTO candidateDTO = new CandidateDTO();
				candidateDTO.setProfileScore(skills.getCandidate().getProfileScore() != null
						? skills.getCandidate().getProfileScore()
						: 0d);
				candidateSkillDTO.setCandidate(candidateDTO);

			}
		}
		return candidateSkillDTO;
	}

	/**
	 * @param candidateLanguageProficiencies
	 * @param dto
	 */
	public CandidateLanguageProficiencyDTO convertCandidateLanguageProficiency(
			CandidateLanguageProficiency candidateLanguageProficiency) {

		CandidateLanguageProficiencyDTO candidateLanguageProficiencyDTO = new CandidateLanguageProficiencyDTO();
		LanguageDTO languageDTO = new LanguageDTO();
		candidateLanguageProficiencyDTO.setId(candidateLanguageProficiency.getId());
		languageDTO.setLanguage(candidateLanguageProficiency.getLanguage().getLanguage());
		languageDTO.setId(candidateLanguageProficiency.getLanguage().getId());
		languageDTO.setValue(candidateLanguageProficiency.getLanguage().getLanguage());
		languageDTO.setDisplay(candidateLanguageProficiency.getLanguage().getLanguage());
		candidateLanguageProficiencyDTO.setLanguage(languageDTO);
		candidateLanguageProficiencyDTO.setProficiency(candidateLanguageProficiency.getProficiency());

		CandidateDTO candidateDTO = new CandidateDTO();
		candidateDTO.setProfileScore(candidateLanguageProficiency.getCandidate().getProfileScore() != null
				? candidateLanguageProficiency.getCandidate().getProfileScore()
				: 0d);
		candidateLanguageProficiencyDTO.setCandidate(candidateDTO);

		return candidateLanguageProficiencyDTO;
	}

	/**
	 * @param candidateCertifications
	 * @param dto
	 */
	public List<CandidateCertificationDTO> convertCandidateCertifications(
			List<CandidateCertification> candidateCertifications, Boolean withCandidateProfileScore,
			Candidate candidate) {
		List<CandidateCertificationDTO> candidateCertificationDTOs = new ArrayList<>();
		candidateCertifications.forEach(certification -> {
			CandidateCertificationDTO certificationDTO = new CandidateCertificationDTO();
			certificationDTO.setId(certification.getId());
			certificationDTO.setCertificationTitle(certification.getCertificationTitle());
			certificationDTO.setCertificationDetails(certification.getCertificationDetails());
			certificationDTO.setCertificationDate(certification.getCertificationDate());
			candidateCertificationDTOs.add(certificationDTO);
			if (withCandidateProfileScore) {
				CandidateDTO candidateDTO = new CandidateDTO();
				candidateDTO.setProfileScore(certification.getCandidate().getProfileScore() != null
						? certification.getCandidate().getProfileScore()
						: 0d);
				certificationDTO.setCandidate(candidateDTO);
			}
		});

		return candidateCertificationDTOs;
	}

	/**
	 * @param candidateEmployments
	 * @param dto
	 */
	public List<CandidateEmploymentDTO> convertCandidateEmployments(List<CandidateEmployment> candidateEmployments,
			Boolean withCandidateProfileScore, Candidate candidate) {
		List<CandidateEmploymentDTO> employmentDTOs = new ArrayList<>();
		candidateEmployments.forEach(employment -> {
			CandidateEmploymentDTO employmentDTO = new CandidateEmploymentDTO();
			employmentDTO.setJobTitle(employment.getJobTitle());
			employmentDTO.setEmployerName(employment.getEmployerName());
			employmentDTO.setEmploymentStartDate(employment.getEmploymentStartDate());
			employmentDTO.setEmploymentEndDate(employment.getEmploymentEndDate());
			employmentDTO.setEmploymentType(employment.getEmploymentType().getEmploymentType());
			employmentDTO.setJobType(employment.getJobType().getJobType());
			employmentDTO.setJobDescription(employment.getJobDescription());
			employmentDTO.setId(employment.getId());
			Comparator<CandidateProject> comparator = Comparator.comparing(CandidateProject::getProjectEndDate,
					Comparator.nullsLast(Comparator.naturalOrder()));
			List<CandidateProject> projects = new ArrayList<CandidateProject>(employment.getProjects());
			projects.sort(comparator.reversed());
			projects.forEach(project -> {
				CandidateProjectDTO projectDTO = setCandidateProjects(project);
				employmentDTO.getProjects().add(projectDTO);
			});
			employmentDTOs.add(employmentDTO);
			if (withCandidateProfileScore) {
				CandidateDTO candidateDTO = new CandidateDTO();
				candidateDTO.setProfileScore(employment.getCandidate().getProfileScore() != null
						? employment.getCandidate().getProfileScore()
						: 0d);
				employmentDTO.setCandidate(candidateDTO);
			}
		});

		return employmentDTOs;
	}

	/**
	 * @param project
	 * @return
	 */
	public CandidateProjectDTO setCandidateProjects(CandidateProject project) {
		if (project == null)
			return null;
		CandidateProjectDTO projectDTO = new CandidateProjectDTO();
		projectDTO.setProjectTitle(project.getProjectTitle());
		projectDTO.setId(project.getId());
		projectDTO.setProjectDescription(project.getProjectDescription());
		projectDTO.setProjectStartDate(project.getProjectStartDate());
		projectDTO.setProjectEndDate(project.getProjectEndDate());
		projectDTO.setContributionInProject(project.getContributionInProject());
		projectDTO.setIsCurrentProject(project.isIsCurrentProject());
		projectDTO.setProjectType(project.getProjectType());
		if (project.getEducation() != null)
			projectDTO.setEducationId(project.getEducation().getId());
		if (project.getEmployment() != null)
			projectDTO.setEmploymentId(project.getEmployment().getId());
		return projectDTO;
	}

	/**
	 * @param candidateEducations
	 * @param dto
	 */
	public List<CandidateEducationDTO> convertCandidateEducations(List<CandidateEducation> candidateEducations,
			Boolean withCandidateProfileScore, Candidate candidate) {
		List<CandidateEducationDTO> educationDTOs = new ArrayList<>();
		if(candidateEducations != null) {
			if (candidateEducations.isEmpty()) {
				CandidateEducationDTO educationDTO = new CandidateEducationDTO();
				if (withCandidateProfileScore) {
					CandidateDTO candidateDTO = new CandidateDTO();
					if(candidate != null)
						candidateDTO.setProfileScore(candidate.getProfileScore() != null ? candidate.getProfileScore() : 0d);
					educationDTO.setCandidate(candidateDTO);
				}
				educationDTOs.add(educationDTO);
			} else {
				candidateEducations.forEach(education -> {
					CandidateEducationDTO educationDTO = new CandidateEducationDTO();
					CollegeDTO collegeDTO = new CollegeDTO();
					educationDTO.setId(education.getId());
					educationDTO.setEducationFromDate(education.getEducationFromDate());
					educationDTO.setEducationToDate(education.getEducationToDate());
					collegeDTO.setCollegeName(education.getCollege().getCollegeName());
					collegeDTO.getUniversity()
							.setUniversityName((education.getCollege().getUniversity().getUniversityName()));
					// educationDTO.setCollegeName(education.getCollege().getCollegeName());
					// educationDTO.setUniversityName(education.getCollege().getUniversity().getUniversityName());
					educationDTO.setCollege(collegeDTO);
					String scoreType = education.getScoreType();
					if (scoreType != null && scoreType.equals(Constants.GPA))
						educationDTO.setGrade(education.getGrade());
					else
						educationDTO.setPercentage(education.getPercentage());
					educationDTO.setScoreType(education.getScoreType());
					educationDTO.setIsPursuingEducation(education.isIsPursuingEducation());
					educationDTO.setHighestQualification(education.getHighestQualification());
					QualificationDTO qualificationDTO = new QualificationDTO();
					qualificationDTO.setQualification(education.getQualification().getQualification());
					educationDTO.setQualification(qualificationDTO);
					CourseDTO courseDTO = new CourseDTO();
					courseDTO.setCourse((education.getCourse().getCourse()));
					educationDTO.setCourse(courseDTO);
					Comparator<CandidateProject> comparator = Comparator.comparing(CandidateProject::getProjectEndDate,
							Comparator.nullsLast(Comparator.naturalOrder()));
					List<CandidateProject> projects = new ArrayList<CandidateProject>(education.getProjects());
					projects.sort(comparator.reversed());
					projects.forEach(project -> {
						CandidateProjectDTO projectDTO = setCandidateProjects(project);
						educationDTO.getProjects().add(projectDTO);
					});
					educationDTOs.add(educationDTO);
					if (withCandidateProfileScore) {
						CandidateDTO candidateDTO = new CandidateDTO();
						candidateDTO.setProfileScore(education.getCandidate().getProfileScore() != null
								? education.getCandidate().getProfileScore()
								: 0d);
						educationDTO.setCandidate(candidateDTO);
					}
				});
			}
		}
		return educationDTOs;
	}

	/**
	 * @param addresses
	 * @param dto
	 */
	public Set<AddressDTO> convertCandidateAddresses(Set<Address> addresses) {
		Set<AddressDTO> addressDTOs = new HashSet<>();
		addresses.forEach(address -> {
			AddressDTO addressDTO = new AddressDTO();
			addressDTO.setAddressLineOne(address.getAddressLineOne());
			addressDTO.setAddressLineTwo(address.getAddressLineTwo());
			addressDTO.setCity(address.getCity());
			CountryDTO countryDTO = new CountryDTO();
			countryDTO.setCountryNiceName(
					address.getCountry() != null ? address.getCountry().getCountryNiceName() : null);
			addressDTO.setCountry(countryDTO);
			addressDTO.setState(address.getState());
			addressDTO.setZip(address.getZip());
			addressDTOs.add(addressDTO);
		});
		return addressDTOs;
	}

	/**
	 * @param candidate
	 * @return
	 */
	public CandidateDetailDTO convertCandidateDetails(Candidate candidate) {
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
		if(candidate.getLogin().getImageUrl()!=null) {
			candidateDetailDTO.setImageUrl(getImageUrl(candidate.getLogin().getId()));
		}
		return candidateDetailDTO;
	}
	
	private String getImageUrl(Long userId) {
		String imageUrl = null;
		try {
			imageUrl = imageUrlCache.getValue(userId, new Callable<String>() {
				public String call() throws Exception {
					return fileService.getObject("gradzcircle-assets", userId.toString()).getLinks().get(0).getHref();
				}
			});
		} catch (Exception e) {
			logger.error("Error fetching Asset from Amazon S3 {}", e.getCause());
		}
		return imageUrl;
	}

}
