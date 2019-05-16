/**
 * 
 */
package com.drishika.gradzcircle.service.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
import com.drishika.gradzcircle.domain.CorporateCandidate;
import com.drishika.gradzcircle.domain.Job;
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
import com.drishika.gradzcircle.service.dto.CollegeDTO;
import com.drishika.gradzcircle.service.dto.CorporateJobDTO;
import com.drishika.gradzcircle.service.dto.CountryDTO;
import com.drishika.gradzcircle.service.dto.CourseDTO;
import com.drishika.gradzcircle.service.dto.JobStatistics;
import com.drishika.gradzcircle.service.dto.LanguageDTO;
import com.drishika.gradzcircle.service.dto.QualificationDTO;

/**
 * @author abhinav
 *
 */
@Component
@Transactional
public class DTOConverters {

	private final Logger logger = LoggerFactory.getLogger(DTOConverters.class);

	public CandidateProfileListDTO convertToCandidateProfileListingDTO(CandidateAppliedJobs candidateAppliedJob,
			Candidate candidate) {
		CandidateProfileListDTO dto = new CandidateProfileListDTO();
		dto.setFirstName(candidate.getFirstName());
		dto.setLastName(candidate.getLastName());
		dto.setLogin(candidate.getLogin());
		dto.setId(candidate.getId());
		CandidateEducation highestCandidateEducation = candidate.getEducations().stream()
				.filter(education -> education.isHighestQualification()).findFirst().get();
		dto.setQualificationWithHighestCourse(highestCandidateEducation.getQualification().getQualification() + " in "
				+ highestCandidateEducation.getCourse().getCourse());
		return dto;
	}

	public CandidateProfileListDTO convertToCandidateProfileListingDTO(Candidate candidate, CandidateJob candidateJob) {
		CandidateProfileListDTO dto = new CandidateProfileListDTO();
		dto.setFirstName(candidate.getFirstName());
		dto.setLastName(candidate.getLastName());
		dto.setLogin(candidate.getLogin());
		dto.setId(candidate.getId());
		dto.setReviewed(candidateJob.getReviewed());
		CandidateEducation highestCandidateEducation = candidate.getEducations().stream()
				.filter(education -> education.isHighestQualification()).findFirst().get();
		dto.setQualificationWithHighestCourse(highestCandidateEducation.getQualification().getQualification() + " in "
				+ highestCandidateEducation.getCourse().getCourse());
		return dto;
	}
	
	public CandidateProfileListDTO convertToCandidateProfileListingDTO(Candidate candidate, CorporateCandidate corporateCandidate) {
		CandidateProfileListDTO dto = new CandidateProfileListDTO();
		dto.setFirstName(candidate.getFirstName());
		dto.setLastName(candidate.getLastName());
		dto.setLogin(candidate.getLogin());
		dto.setId(candidate.getId());
		CandidateEducation highestCandidateEducation = candidate.getEducations().stream()
				.filter(education -> education.isHighestQualification()).findFirst().get();
		dto.setQualificationWithHighestCourse(highestCandidateEducation.getQualification().getQualification() + " in "
				+ highestCandidateEducation.getCourse().getCourse());
		return dto;
	}

	public CandidateProfileListDTO convertToCandidateProfileListingDTO(Candidate candidate) {
		CandidateProfileListDTO dto = new CandidateProfileListDTO();
		dto.setFirstName(candidate.getFirstName());
		dto.setLastName(candidate.getLastName());
		dto.setLogin(candidate.getLogin());
		dto.setId(candidate.getId());
		CandidateEducation highestCandidateEducation = null;
		if(candidate.getEducations().stream()
				.filter(education -> education.isHighestQualification()).findFirst().isPresent())
			highestCandidateEducation = candidate.getEducations().stream()
				.filter(education -> education.isHighestQualification()).findFirst().get();
		if(highestCandidateEducation != null)
			dto.setQualificationWithHighestCourse(highestCandidateEducation.getQualification().getQualification() + " in "
				+ highestCandidateEducation.getCourse().getCourse());
		return dto;
	}
	
	public CandidateProfileListDTO convertToCandidateProfileListingDTO(CorporateCandidate corporateCandidate) {
		CandidateProfileListDTO dto = new CandidateProfileListDTO();
		Candidate candidate = corporateCandidate.getCandidate();
		dto.setFirstName(candidate.getFirstName());
		dto.setLastName(candidate.getLastName());
		dto.setLogin(candidate.getLogin());
		dto.setId(candidate.getId());
		dto.setJobId(corporateCandidate.getId().getJobId());
		CandidateEducation highestCandidateEducation = null;
		if(candidate.getEducations().stream()
				.filter(education -> education.isHighestQualification()).findFirst().isPresent())
			highestCandidateEducation = candidate.getEducations().stream()
				.filter(education -> education.isHighestQualification()).findFirst().get();
		if(highestCandidateEducation != null)
			dto.setQualificationWithHighestCourse(highestCandidateEducation.getQualification().getQualification() + " in "
				+ highestCandidateEducation.getCourse().getCourse());
		return dto;
	}

	public CorporateJobDTO convertToJobListingForCorporate(Job job,Long totalLinkedCandidates, Long totalNumberOfJobs, 
			Long newApplicants, Long jobsLastMonth,Long numberOfCandidatesShortListedByJob) {
		CorporateJobDTO jobListingData = new CorporateJobDTO();
		jobListingData.setTotalLinkedCandidates(totalLinkedCandidates);
		jobListingData.setTotalNumberOfJobs(totalNumberOfJobs);
		jobListingData.setNewApplicants(newApplicants);
		jobListingData.setJobsLastMonth(jobsLastMonth);
		jobListingData.setJobStatus(job.getJobStatus());
		jobListingData.setJobTitle(job.getJobTitle());
		jobListingData.setEmploymentType(job.getEmploymentType());
		jobListingData.setJobType(job.getJobType());
		// FILTER OUT REVIEWED CANDIDATES
		jobListingData.setNoOfMatchedCandidates(job.getCandidateJobs().stream().filter(candidateJob -> !candidateJob.getReviewed()).collect(Collectors.toSet()).size());
		//jobListingData.setNoOfMatchedCandidates(job.getCandidateJobs().size());
		jobListingData.setNoOfCandidatesApplied(job.getAppliedCandidates().size());
		jobListingData.setNoOfShortListedCandidate(numberOfCandidatesShortListedByJob);
		jobListingData.setId(job.getId());
		jobListingData.setCanEdit(job.getCanEdit());
		jobListingData.setHasBeenEdited(job.getHasBeenEdited());
		jobListingData.setEverActive(job.getEverActive());
		return jobListingData;
	}

	public CandidateJobDTO convertToJobListingForCandidate(Job job, Long candidateId, Boolean appliedJobs) {
		CandidateJobDTO jobListingData = new CandidateJobDTO();
		setCandidateJobDTOCoreFields(job, jobListingData);
		logger.debug("Job and Candidates matched are {}--{}",job, job.getCandidateJobs());
		jobListingData.setMatchScore(job.getCandidateJobs().stream()
				.filter(candidateJob -> candidateJob.getCandidate().getId().equals(candidateId)).findAny().get()
				.getMatchScore());
		if (!appliedJobs)
			job.getAppliedCandidates().stream()
					.filter(appliedCandidate -> !appliedCandidate.getId().equals(candidateId));
		else
			job.getAppliedCandidates().stream()
					.filter(appliedCandidate -> appliedCandidate.getId().equals(candidateId));
		return jobListingData;
	}
	
	public CandidateJobDTO convertToJobListingForCandidateWithNoEducation(Job job, Long candidateId) {
		CandidateJobDTO jobListingData = new CandidateJobDTO();
		setCandidateJobDTOCoreFields(job, jobListingData);
		jobListingData.setMatchScore(0d);
		return jobListingData;
	}
	
	public CandidateJobDTO convertToJobListingForPortalAnonymous(Job job,Long totalNumberOfActiveJobs,
				List<JobStatistics> jobStatisticsByEmploymentType, List<JobStatistics> jobStatisticsByJobType) {
		CandidateJobDTO jobListingData = new CandidateJobDTO();
		setCandidateJobDTOCoreFields(job, jobListingData);
		jobListingData.setTotalNumberOfJobs(totalNumberOfActiveJobs);
		setJobStatistics(jobListingData,jobStatisticsByEmploymentType);
		setJobStatistics(jobListingData,jobStatisticsByJobType);
		return jobListingData;
	}
	
	public CandidateJobDTO convertToJobListingForCandidateOrGuest(Job job,Long totalNumberOfActiveJobs,
			List<JobStatistics> jobStatisticsByEmploymentType, List<JobStatistics> jobStatisticsByJobType,Long candidateId) {
	//	logger.debug("Coverting to DTO candidateJob for job {} with total number of jobs {}",job,totalNumberOfActiveJobs);
		CandidateJobDTO jobListingData = new CandidateJobDTO();
		setCandidateJobDTOCoreFields(job, jobListingData,candidateId);
		jobListingData.setTotalNumberOfJobs(totalNumberOfActiveJobs);
		setJobStatistics(jobListingData,jobStatisticsByEmploymentType);
		setJobStatistics(jobListingData,jobStatisticsByJobType);
		return jobListingData;
}
	
	private void setJobStatistics(CandidateJobDTO jobListingData,List<JobStatistics> jobStatistics) {
		if(jobStatistics == null)
			return;
		jobStatistics.forEach(stat -> {
			//logger.debug("Job Stats being processed is {}",jobStatistics);
			if(ApplicationConstants.EMPLOYMENT_TYPE_CONTRACT.equals(stat.getType()))
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
	
	private void setCandidateJobDTOCoreFields(Job job, CandidateJobDTO jobListingData,Long candidateId) {
		logger.debug("Incoming canidate Is is {}",candidateId);
		jobListingData.setJobStatus(job.getJobStatus());
		jobListingData.setJobTitle(job.getJobTitle());
		jobListingData.setEmploymentType(job.getEmploymentType());
		jobListingData.setJobType(job.getJobType());
		jobListingData.setId(job.getId());
		jobListingData.setCity(convertToCamelCase(job.getCorporate().getCity()));
		jobListingData.setCorporateName(convertToCamelCase(job.getCorporate().getName()));
		jobListingData.setSalary(job.getSalary());
		jobListingData.setUpdateDate(job.getUpdateDate());
		if(!job.getAppliedCandidates().stream().filter(candidate->candidateId.equals(candidate.getId())).findFirst().isPresent())
			jobListingData.setHasCandidateApplied(false);
		logger.debug("---------------{}",job.getCandidateJobs().stream().filter(candidateJob-> candidateId.equals(candidateJob.getCandidateId())).findFirst().isPresent());
		if(candidateId!= null)
			if(job.getCandidateJobs().stream().filter(candidateJob-> candidateId.equals(candidateJob.getCandidateId())).findFirst().isPresent())
				jobListingData.setMatchScore(job.getCandidateJobs().stream().filter(candidateJob-> candidateId.equals(candidateJob.getCandidateId())).findFirst().get().getMatchScore());
		if(job.getJobDescription().length()>500)
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
		jobListingData.setSalary(job.getSalary());
		jobListingData.setUpdateDate(job.getUpdateDate());
		if(job.getJobDescription().length()>500)
			jobListingData.setJobDescription(job.getJobDescription().substring(0, 499));
		else
			jobListingData.setJobDescription(job.getJobDescription());
	}
	
	private String convertToCamelCase(String string) {
		if(string == null)
			return "";
		char[] charArray = string.toLowerCase().toCharArray();
        charArray[0] = Character.toUpperCase(charArray[0]);
        for(int i=1 ; i<charArray.length ;i++ )
        		charArray[i] = Character.toLowerCase(charArray[i]);
        return new String(charArray);
	}
	
	
	/**
	 * @param candidateNonAcademicWorks
	 * @param dto
	 */
	public Set<CandidateNonAcademicWorkDTO>  convertCandidateNonAcademicWork(Set<CandidateNonAcademicWork> candidateNonAcademicWorks, Boolean withCandidateProfileScore,
			Candidate candidate) {
		Set<CandidateNonAcademicWorkDTO> nonAcadWorksDTOs = new HashSet<>();
		if (candidateNonAcademicWorks != null && candidateNonAcademicWorks.isEmpty()) {
			CandidateNonAcademicWorkDTO nonAcademicWorkDTO = new CandidateNonAcademicWorkDTO();
			if (withCandidateProfileScore) {
				CandidateDTO candidateDTO = new CandidateDTO();
				candidateDTO.setProfileScore(candidate.getProfileScore() != null ? candidate.getProfileScore() : 0d);
				nonAcademicWorkDTO.setCandidate(candidateDTO);
			}
			nonAcadWorksDTOs.add(nonAcademicWorkDTO);
		} else {
			candidateNonAcademicWorks.forEach(nonAcademic -> {
				CandidateNonAcademicWorkDTO nonAcademicWorkDTO = new CandidateNonAcademicWorkDTO();
				nonAcademicWorkDTO.setId(nonAcademic.getId());
				nonAcademicWorkDTO.setNonAcademicInitiativeTitle(nonAcademic.getNonAcademicInitiativeTitle());
				nonAcademicWorkDTO
						.setNonAcademicInitiativeDescription(nonAcademic.getNonAcademicInitiativeDescription());
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
		}
		return nonAcadWorksDTOs;
	}

	/**
	 * @param candidateLanguageProficiencies
	 * @param dto
	 */
	public Set<CandidateLanguageProficiencyDTO> convertCandidateLanguageProficiencies(
			Set<CandidateLanguageProficiency> candidateLanguageProficiencies, Boolean withCandidateProfileScore,
			Candidate candidate) {
		Set<CandidateLanguageProficiencyDTO> languageProficiencyDTOs = new HashSet<>();
		if (candidateLanguageProficiencies != null && candidateLanguageProficiencies.isEmpty()) {
			CandidateLanguageProficiencyDTO languageProficiencyDTO = new CandidateLanguageProficiencyDTO();
			if (withCandidateProfileScore) {
				CandidateDTO candidateDTO = new CandidateDTO();
				candidateDTO.setProfileScore(candidate.getProfileScore() != null ? candidate.getProfileScore() : 0d);
				languageProficiencyDTO.setCandidate(candidateDTO);
			}
			languageProficiencyDTOs.add(languageProficiencyDTO);
		} else {
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
		}
		return languageProficiencyDTOs;
	}

	/**
	 * @param candidateLanguageProficiencies
	 * @param dto
	 */
	public CandidateLanguageProficiencyDTO convertCandidateLanguageProficiency(CandidateLanguageProficiency candidateLanguageProficiency) {

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
	public Set<CandidateCertificationDTO> convertCandidateCertifications(Set<CandidateCertification> candidateCertifications,
			Boolean withCandidateProfileScore,Candidate candidate) {
		Set<CandidateCertificationDTO> candidateCertificationDTOs = new HashSet<>();
		if(candidateCertifications != null && candidateCertifications.isEmpty()) {
			CandidateCertificationDTO certificationDTO = new CandidateCertificationDTO();
			if(withCandidateProfileScore) {
				CandidateDTO candidateDTO = new CandidateDTO();
				candidateDTO.setProfileScore(candidate.getProfileScore() != null ? candidate.getProfileScore() : 0d);
				certificationDTO.setCandidate(candidateDTO);
			}
			candidateCertificationDTOs.add(certificationDTO);
		} else {
			candidateCertifications.forEach(certification -> {
				CandidateCertificationDTO certificationDTO = new CandidateCertificationDTO();
				certificationDTO.setId(certification.getId());
				certificationDTO.setCertificationTitle(certification.getCertificationTitle());
				certificationDTO.setCertificationDetails(certification.getCertificationDetails());
				certificationDTO.setCertificationDate(certification.getCertificationDate());
				candidateCertificationDTOs.add(certificationDTO);
				if(withCandidateProfileScore) {
					CandidateDTO candidateDTO = new CandidateDTO();
					candidateDTO.setProfileScore(certification.getCandidate().getProfileScore()!=null?certification.getCandidate().getProfileScore():0d);
					certificationDTO.setCandidate(candidateDTO);
				}
			});
		}
		return candidateCertificationDTOs;
	}

	/**
	 * @param candidateEmployments
	 * @param dto
	 */
	public Set<CandidateEmploymentDTO> convertCandidateEmployments(Set<CandidateEmployment> candidateEmployments, Boolean withCandidateProfileScore, Candidate candidate) {
		Set<CandidateEmploymentDTO> employmentDTOs = new HashSet<>();
		if(candidateEmployments != null && candidateEmployments.isEmpty()) {
			CandidateEmploymentDTO employmentDTO = new CandidateEmploymentDTO();
			if(withCandidateProfileScore) {
				CandidateDTO candidateDTO = new CandidateDTO();
				candidateDTO.setProfileScore(candidate.getProfileScore() != null ? candidate.getProfileScore() : 0d);
				employmentDTO.setCandidate(candidateDTO);
			}
			
			employmentDTOs.add(employmentDTO);
		} else {
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
				employment.getProjects().forEach(project -> {
					CandidateProjectDTO projectDTO = setCandidateProjects(project);
					employmentDTO.getProjects().add(projectDTO);
				});
				employmentDTOs.add(employmentDTO);
				if(withCandidateProfileScore) {
					CandidateDTO candidateDTO = new CandidateDTO();
					candidateDTO.setProfileScore(employment.getCandidate().getProfileScore()!=null?employment.getCandidate().getProfileScore():0d);
					employmentDTO.setCandidate(candidateDTO);
				}
			});
		}
		return employmentDTOs;
	}

	/**
	 * @param project
	 * @return
	 */
	public CandidateProjectDTO setCandidateProjects(CandidateProject project) {
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
	public Set<CandidateEducationDTO> convertCandidateEducations(Set<CandidateEducation> candidateEducations,
			Boolean withCandidateProfileScore, Candidate candidate) {
		Set<CandidateEducationDTO> educationDTOs = new HashSet<>();
		if (candidateEducations != null && candidateEducations.isEmpty()) {
			CandidateEducationDTO educationDTO = new CandidateEducationDTO();
			if (withCandidateProfileScore) {
				CandidateDTO candidateDTO = new CandidateDTO();
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
				education.getProjects().forEach(project -> {
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
			countryDTO.setCountryNiceName(address.getCountry().getCountryNiceName());
			addressDTO.setCountry(countryDTO);
			addressDTO.setState(address.getState());
			addressDTO.setZip(address.getZip());
			// dto.getAddresses().add(addressDTO);
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
		return candidateDetailDTO;
	}
	

}
