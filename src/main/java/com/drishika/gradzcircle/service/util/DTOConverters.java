/**
 * 
 */
package com.drishika.gradzcircle.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateAppliedJobs;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateJob;
import com.drishika.gradzcircle.domain.CorporateCandidate;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.service.dto.CandidateJobDTO;
import com.drishika.gradzcircle.service.dto.CandidateProfileListDTO;
import com.drishika.gradzcircle.service.dto.CorporateJobDTO;

/**
 * @author abhinav
 *
 */
@Component
public class DTOConverters {

	private final Logger logger = LoggerFactory.getLogger(DTOConverters.class);

	public CandidateProfileListDTO convertToCandidateProfileListingDTO(CandidateAppliedJobs candidateAppliedJob,
			Candidate candidate) {
		CandidateProfileListDTO dto = new CandidateProfileListDTO();
		dto.setFirstName(candidate.getFirstName());
		dto.setLastName(candidate.getLastName());
		dto.setLogin(candidate.getLogin());
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
		// jobListingData.setNoOfMatchedCandidates(job.getCandidateJobs().stream()
		// .filter(candidateJob ->
		// !candidateJob.getReviewed()).collect(Collectors.toSet()).size());
		jobListingData.setNoOfMatchedCandidates(job.getCandidateJobs().size());
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
		jobListingData.setJobStatus(job.getJobStatus());
		jobListingData.setJobTitle(job.getJobTitle());
		jobListingData.setEmploymentType(job.getEmploymentType());
		jobListingData.setJobType(job.getJobType());
		jobListingData.setMatchScore(job.getCandidateJobs().stream()
				.filter(candidateJob -> candidateJob.getCandidate().getId().equals(candidateId)).findAny().get()
				.getMatchScore());
		if (!appliedJobs)
			job.getAppliedCandidates().stream()
					.filter(appliedCandidate -> !appliedCandidate.getId().equals(candidateId));
		else
			job.getAppliedCandidates().stream()
					.filter(appliedCandidate -> appliedCandidate.getId().equals(candidateId));
		/*
		 * if(job.getAppliedCandidates().stream().filter(appliedCandidate ->
		 * appliedCandidate.getId().equals(candidateId)).findAny().isPresent())
		 * jobListingData.setHasCandidateApplied(true); else
		 * jobListingData.setHasCandidateApplied(false);
		 */
		jobListingData.setId(job.getId());
		return jobListingData;
	}

}
