
package com.drishika.gradzcircle.web.rest;

import static com.drishika.gradzcircle.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.drishika.gradzcircle.GradzcircleApp;
import com.drishika.gradzcircle.domain.Candidate;
import com.drishika.gradzcircle.domain.CandidateAppliedJobs;
import com.drishika.gradzcircle.domain.CandidateEducation;
import com.drishika.gradzcircle.domain.CandidateJob;
import com.drishika.gradzcircle.domain.Corporate;
import com.drishika.gradzcircle.domain.Course;
import com.drishika.gradzcircle.domain.EmploymentType;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.domain.JobFilter;
import com.drishika.gradzcircle.domain.JobFilterHistory;
import com.drishika.gradzcircle.domain.JobHistory;
import com.drishika.gradzcircle.domain.JobType;
import com.drishika.gradzcircle.domain.Qualification;
import com.drishika.gradzcircle.domain.enumeration.PaymentType;
import com.drishika.gradzcircle.repository.CandidateRepository;
import com.drishika.gradzcircle.repository.CorporateRepository;
import com.drishika.gradzcircle.repository.CourseRepository;
import com.drishika.gradzcircle.repository.EmploymentTypeRepository;
import com.drishika.gradzcircle.repository.JobFilterHistoryRepository;
import com.drishika.gradzcircle.repository.JobFilterRepository;
import com.drishika.gradzcircle.repository.JobHistoryRepository;
import com.drishika.gradzcircle.repository.JobRepository;
import com.drishika.gradzcircle.repository.JobTypeRepository;
import com.drishika.gradzcircle.repository.QualificationRepository;
import com.drishika.gradzcircle.repository.search.JobSearchRepository;
import com.drishika.gradzcircle.service.JobService;
import com.drishika.gradzcircle.service.util.JobsUtil;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the JobResource REST controller.
 *
 * @see JobResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class JobResourceIntTest {

	private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
	private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

	private static final String DEFAULT_JOB_DESCRIPTION = "AAAAAAAAAA";
	private static final String UPDATED_JOB_DESCRIPTION = "BBBBBBBBBB";

	private static final Integer DEFAULT_NO_OF_APPLICANTS = 1;
	private static final Integer UPDATED_NO_OF_APPLICANTS = 2;

	private static final String DEFAULT_FILTER = "{\"basic\":true,\"premium\":true,\"scoreType\":\"percent\",\"percentage\":78,\"addOn\":true,\"gender\":{\"id\":1,\"gender\":\"Male\"}}";
	private static final String UPDATED_FILTER = "{\"basic\":true,\"premium\":true,\"scoreType\":\"percent\",\"percentage\":78,\"addOn\":true,\"gender\":{\"id\":1,\"gender\":\"Female\"}}";
	private static final String BASIC_FILTER = "{\"basic\":true}";

	private static final Double DEFAULT_SALARY = 1D;
	private static final Double UPDATED_SALARY = 2D;

	private static final Integer DRAFT_JOB_STATUS = 0;
	private static final Integer ACTIVE_JOB_STATUS = 1;
	private static final Integer ARCHIVE_JOB_STATUS = -1;

	private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
	private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));

	private static final Double DEFAULT_ORIGINAL_JOB_COST = 1D;
	private static final Double UPDATED_ORIGINAL_JOB_COST = 2D;

	private static final Double DEFAULT_JOB_COST = 1D;
	private static final Double UPDATED_JOB_COST = 2D;

	private static final Double DEFAULT_AMOUNT_PAID = 1D;
	private static final Double UPDATED_AMOUNT_PAID = 2D;

	private static final Double DEFAULT_TOTAL_AMOUNT_PAID = 1D;
	private static final Double UPDATED_TOTAL_AMOUNT_PAID = 2D;

	private static final Integer DEFAULT_NO_OF_APPLICANTS_BOUGHT = 1;
	private static final Integer UPDATED_NO_OF_APPLICANTS_BOUGHT = 2;

	private static final Double DEFAULT_REMOVED_FILTER_AMOUNT = 1D;
	private static final Double UPDATED_REMOVED_FILTER_AMOUNT = 2D;

	private static final Double DEFAULT_ADDITIONAL_FILTER_AMOUNT = 1D;
	private static final Double UPDATED_ADDITIONAL_FILTER_AMOUNT = 2D;

	private static final Double DEFAULT_ADMIN_CHARGE = 1D;
	private static final Double UPDATED_ADMIN_CHARGE = 2D;

	private static final Double DEFAULT_ADMIN_CHARGE_RATE = 1D;
	private static final Double UPDATED_ADMIN_CHARGE_RATE = 2D;

	private static final Double DEFAULT_UPFRONT_DISCOUNT_RATE = 1D;
	private static final Double UPDATED_UPFRONT_DISCOUNT_RATE = 2D;

	private static final Double DEFAULT_UPFRONT_DISCOUNT_AMOUNT = 1D;
	private static final Double UPDATED_UPFRONT_DISCOUNT_AMOUNT = 2D;

	private static final Double DEFAULT_ESCROW_AMOUNT_USED = 1D;
	private static final Double UPDATED_ESCROW_AMOUNT_USED = 2D;

	private static final Double DEFAULT_ESCROW_AMOUNT_ADDED = 1D;
	private static final Double UPDATED_ESCROW_AMOUNT_ADDED = 2D;

	private static final PaymentType DEFAULT_PAYMENT_TYPE = PaymentType.UPFRONT;
	private static final PaymentType UPDATED_PAYMENT_TYPE = PaymentType.AS_YOU_GO;

	private static final Boolean DEFAULT_HAS_BEEN_EDITED = false;
	private static final Boolean UPDATED_HAS_BEEN_EDITED = true;

	private static final Boolean DEFAULT_EVER_ACTIVE = false;
	private static final Boolean UPDATED_EVER_ACTIVE = true;

	private static final Boolean DEFAULT_CAN_EDIT = true;
	private static final Boolean UPDATED_CAN_EDIT = false;

	private static final ZonedDateTime DEFAULT_UPDATE_DATE = ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).withNano(0);
	private static final ZonedDateTime UPDATED_UPDATE_DATE = ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).withNano(0);

	private static final Long DEFAULT_CREATED_BY = 1L;
	private static final Long UPDATED_CREATED_BY = 2L;

	private static final Long DEFAULT_UPDATED_BY = 1L;
	private static final Long UPDATED_UPDATED_BY = 2L;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private JobHistoryRepository jobHistoryRepository;

	// @Autowired
	// private JobHistorySearchRepository jobHistorySearchRepository;

	@Autowired
	private JobFilterRepository jobFilterRepository;

	// @Autowired
	// private JobFilterSearchRepository jobFilterSearchRepository;

	@Autowired
	private JobFilterHistoryRepository jobFilterHistoryRepository;

	@Autowired
	private CorporateRepository corporateRepository;

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private JobSearchRepository jobSearchRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private JobTypeRepository jobTypeRepository;
	
	@Autowired
	private EmploymentTypeRepository employmentTypeRepository;
	
	@Autowired
	private QualificationRepository qualificationRepository;

	@Autowired
	private JobService jobService;;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restJobMockMvc;

	private Job job;

	private Job jobA;

	private Corporate corporate;

	private Candidate candidateA, candidateB, candidateC;
	
	private JobType jobType;
	
	private EmploymentType employmentType;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final JobResource jobResource = new JobResource(jobRepository, jobSearchRepository, jobService);

		this.restJobMockMvc = MockMvcBuilders.standaloneSetup(jobResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Job createDraftJob(EntityManager em) {

		Job job = new Job().jobTitle(DEFAULT_JOB_TITLE).jobDescription(DEFAULT_JOB_DESCRIPTION)
				.noOfApplicants(DEFAULT_NO_OF_APPLICANTS).salary(DEFAULT_SALARY).jobStatus(DRAFT_JOB_STATUS)
				.createDate(DEFAULT_CREATE_DATE).originalJobCost(DEFAULT_ORIGINAL_JOB_COST).jobCost(DEFAULT_JOB_COST)
				.amountPaid(DEFAULT_AMOUNT_PAID).totalAmountPaid(DEFAULT_TOTAL_AMOUNT_PAID)
				.noOfApplicantsBought(DEFAULT_NO_OF_APPLICANTS_BOUGHT)
				.removedFilterAmount(DEFAULT_REMOVED_FILTER_AMOUNT)
				.additionalFilterAmount(DEFAULT_ADDITIONAL_FILTER_AMOUNT).adminCharge(DEFAULT_ADMIN_CHARGE)
				.adminChargeRate(DEFAULT_ADMIN_CHARGE_RATE).upfrontDiscountRate(DEFAULT_UPFRONT_DISCOUNT_RATE)
				.upfrontDiscountAmount(DEFAULT_UPFRONT_DISCOUNT_AMOUNT).escrowAmountUsed(DEFAULT_ESCROW_AMOUNT_USED)
				.escrowAmountAdded(DEFAULT_ESCROW_AMOUNT_ADDED).paymentType(DEFAULT_PAYMENT_TYPE)
				.hasBeenEdited(DEFAULT_HAS_BEEN_EDITED).everActive(DEFAULT_EVER_ACTIVE).canEdit(DEFAULT_CAN_EDIT)
				.updateDate(DEFAULT_UPDATE_DATE).createdBy(DEFAULT_CREATED_BY).updatedBy(DEFAULT_UPDATED_BY);

		return job;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Job createJobDraftEditedCannotEdit(EntityManager em) {

		Job job = new Job().jobTitle(DEFAULT_JOB_TITLE).jobDescription(DEFAULT_JOB_DESCRIPTION)
				.noOfApplicants(DEFAULT_NO_OF_APPLICANTS).salary(DEFAULT_SALARY).jobStatus(DRAFT_JOB_STATUS)
				.createDate(DEFAULT_CREATE_DATE).originalJobCost(DEFAULT_ORIGINAL_JOB_COST).jobCost(DEFAULT_JOB_COST)
				.amountPaid(DEFAULT_AMOUNT_PAID).totalAmountPaid(DEFAULT_TOTAL_AMOUNT_PAID)
				.noOfApplicantsBought(DEFAULT_NO_OF_APPLICANTS_BOUGHT)
				.removedFilterAmount(DEFAULT_REMOVED_FILTER_AMOUNT)
				.additionalFilterAmount(DEFAULT_ADDITIONAL_FILTER_AMOUNT).adminCharge(DEFAULT_ADMIN_CHARGE)
				.adminChargeRate(DEFAULT_ADMIN_CHARGE_RATE).upfrontDiscountRate(DEFAULT_UPFRONT_DISCOUNT_RATE)
				.upfrontDiscountAmount(DEFAULT_UPFRONT_DISCOUNT_AMOUNT).escrowAmountUsed(DEFAULT_ESCROW_AMOUNT_USED)
				.escrowAmountAdded(DEFAULT_ESCROW_AMOUNT_ADDED).paymentType(DEFAULT_PAYMENT_TYPE)
				.hasBeenEdited(DEFAULT_HAS_BEEN_EDITED).everActive(DEFAULT_EVER_ACTIVE).canEdit(DEFAULT_CAN_EDIT)
				.updateDate(DEFAULT_UPDATE_DATE).createdBy(DEFAULT_CREATED_BY).updatedBy(DEFAULT_UPDATED_BY);

		return job;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Job createJobDraftNotEditedCanEdit(EntityManager em) {

		Job job = new Job().jobTitle(DEFAULT_JOB_TITLE).jobDescription(UPDATED_JOB_DESCRIPTION).salary(DEFAULT_SALARY)
				.jobStatus(DRAFT_JOB_STATUS).hasBeenEdited(DEFAULT_HAS_BEEN_EDITED).everActive(DEFAULT_EVER_ACTIVE)
				.canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY).updatedBy(UPDATED_UPDATED_BY);

		return job;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Set<JobFilter> createJobFilter(EntityManager em) {
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		JobFilter jobFilter = new JobFilter().filterDescription(DEFAULT_FILTER);
		jobFilters.add(jobFilter);
		return jobFilters;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Set<JobFilter> createNewJobFilter(EntityManager em) {
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		JobFilter jobFilter = new JobFilter().filterDescription(UPDATED_FILTER);
		jobFilters.add(jobFilter);
		return jobFilters;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Set<JobFilter> createUpdatedJobFilter(EntityManager em) {
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		JobFilter jobFilter = new JobFilter().filterDescription(DEFAULT_FILTER);
		jobFilters.add(jobFilter);
		return jobFilters;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Set<JobFilterHistory> createJobFilterHistories(EntityManager em) {
		Set<JobFilterHistory> jobFilterHistories = new HashSet<JobFilterHistory>();
		JobFilterHistory jobFilterHistory1 = new JobFilterHistory().filterDescription(DEFAULT_FILTER);
		JobFilterHistory jobFilterHistory2 = new JobFilterHistory().filterDescription(DEFAULT_FILTER);
		JobFilterHistory jobFilterHistory3 = new JobFilterHistory().filterDescription(DEFAULT_FILTER);
		jobFilterHistories.add(jobFilterHistory1);
		jobFilterHistories.add(jobFilterHistory2);
		jobFilterHistories.add(jobFilterHistory3);
		return jobFilterHistories;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static JobHistory createDraftJobHistory(EntityManager em) {

		JobHistory jobHistory = new JobHistory().jobTitle(DEFAULT_JOB_TITLE).jobDescription(DEFAULT_JOB_DESCRIPTION)
				.salary(DEFAULT_SALARY).jobStatus(DRAFT_JOB_STATUS).hasBeenEdited(DEFAULT_HAS_BEEN_EDITED)
				.everActive(DEFAULT_EVER_ACTIVE).canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY)
				.updatedBy(UPDATED_UPDATED_BY);

		return jobHistory;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Set<JobHistory> createActiveJobHistories(EntityManager em) {
		Set<JobHistory> jobHistories = new HashSet<JobHistory>();
		JobHistory jobHistory1 = new JobHistory().jobTitle(DEFAULT_JOB_TITLE).jobDescription(DEFAULT_JOB_DESCRIPTION)
				.salary(DEFAULT_SALARY).jobStatus(DRAFT_JOB_STATUS).hasBeenEdited(DEFAULT_HAS_BEEN_EDITED)
				.everActive(DEFAULT_EVER_ACTIVE).canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY)
				.updatedBy(UPDATED_UPDATED_BY);
		JobHistory jobHistory2 = new JobHistory().jobTitle(DEFAULT_JOB_TITLE).jobDescription(UPDATED_JOB_DESCRIPTION)
				.salary(DEFAULT_SALARY).jobStatus(DRAFT_JOB_STATUS).hasBeenEdited(DEFAULT_HAS_BEEN_EDITED)
				.everActive(DEFAULT_EVER_ACTIVE).canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY)
				.updatedBy(UPDATED_UPDATED_BY);
		JobHistory jobHistory3 = new JobHistory().jobTitle(DEFAULT_JOB_TITLE).jobDescription(UPDATED_JOB_DESCRIPTION)
				.salary(DEFAULT_SALARY).jobStatus(ACTIVE_JOB_STATUS).hasBeenEdited(DEFAULT_HAS_BEEN_EDITED)
				.everActive(DEFAULT_EVER_ACTIVE).canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY)
				.updatedBy(UPDATED_UPDATED_BY);
		jobHistories.add(jobHistory1);
		jobHistories.add(jobHistory2);
		jobHistories.add(jobHistory3);
		return jobHistories;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Job createJobDraftEditedCanEdit(EntityManager em) {

		Job job = new Job().jobTitle(DEFAULT_JOB_TITLE).jobDescription(DEFAULT_JOB_DESCRIPTION)
				.noOfApplicants(DEFAULT_NO_OF_APPLICANTS).salary(DEFAULT_SALARY).jobStatus(DRAFT_JOB_STATUS)
				.createDate(DEFAULT_CREATE_DATE).originalJobCost(DEFAULT_ORIGINAL_JOB_COST).jobCost(DEFAULT_JOB_COST)
				.amountPaid(DEFAULT_AMOUNT_PAID).totalAmountPaid(DEFAULT_TOTAL_AMOUNT_PAID)
				.noOfApplicantsBought(DEFAULT_NO_OF_APPLICANTS_BOUGHT)
				.removedFilterAmount(DEFAULT_REMOVED_FILTER_AMOUNT)
				.additionalFilterAmount(DEFAULT_ADDITIONAL_FILTER_AMOUNT).adminCharge(DEFAULT_ADMIN_CHARGE)
				.adminChargeRate(DEFAULT_ADMIN_CHARGE_RATE).upfrontDiscountRate(DEFAULT_UPFRONT_DISCOUNT_RATE)
				.upfrontDiscountAmount(DEFAULT_UPFRONT_DISCOUNT_AMOUNT).escrowAmountUsed(DEFAULT_ESCROW_AMOUNT_USED)
				.escrowAmountAdded(DEFAULT_ESCROW_AMOUNT_ADDED).paymentType(DEFAULT_PAYMENT_TYPE)
				.hasBeenEdited(DEFAULT_HAS_BEEN_EDITED).everActive(DEFAULT_EVER_ACTIVE).canEdit(DEFAULT_CAN_EDIT)
				.updateDate(DEFAULT_UPDATE_DATE).createdBy(DEFAULT_CREATED_BY).updatedBy(DEFAULT_UPDATED_BY);

		return job;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Job createJobActiveNotEditedCanEdit(EntityManager em) {

		Job job = new Job().jobTitle(DEFAULT_JOB_TITLE).jobDescription(DEFAULT_JOB_DESCRIPTION)
				.hasBeenEdited(DEFAULT_HAS_BEEN_EDITED).everActive(UPDATED_EVER_ACTIVE).canEdit(DEFAULT_CAN_EDIT)
				.updateDate(DEFAULT_UPDATE_DATE).createdBy(DEFAULT_CREATED_BY).updatedBy(DEFAULT_UPDATED_BY)
				.jobStatus(ACTIVE_JOB_STATUS);
		return job;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Job createJobActiveEditedCanEdit(EntityManager em) {

		Job job = new Job().jobTitle(DEFAULT_JOB_TITLE).jobDescription(DEFAULT_JOB_DESCRIPTION)
				.noOfApplicants(DEFAULT_NO_OF_APPLICANTS).salary(DEFAULT_SALARY).jobStatus(ACTIVE_JOB_STATUS)
				.createDate(DEFAULT_CREATE_DATE).originalJobCost(DEFAULT_ORIGINAL_JOB_COST).jobCost(DEFAULT_JOB_COST)
				.amountPaid(DEFAULT_AMOUNT_PAID).totalAmountPaid(DEFAULT_TOTAL_AMOUNT_PAID)
				.noOfApplicantsBought(DEFAULT_NO_OF_APPLICANTS_BOUGHT)
				.removedFilterAmount(DEFAULT_REMOVED_FILTER_AMOUNT)
				.additionalFilterAmount(DEFAULT_ADDITIONAL_FILTER_AMOUNT).adminCharge(DEFAULT_ADMIN_CHARGE)
				.adminChargeRate(DEFAULT_ADMIN_CHARGE_RATE).upfrontDiscountRate(DEFAULT_UPFRONT_DISCOUNT_RATE)
				.upfrontDiscountAmount(DEFAULT_UPFRONT_DISCOUNT_AMOUNT).escrowAmountUsed(DEFAULT_ESCROW_AMOUNT_USED)
				.escrowAmountAdded(DEFAULT_ESCROW_AMOUNT_ADDED).paymentType(DEFAULT_PAYMENT_TYPE)
				.hasBeenEdited(UPDATED_HAS_BEEN_EDITED).everActive(UPDATED_EVER_ACTIVE).canEdit(DEFAULT_CAN_EDIT)
				.updateDate(DEFAULT_UPDATE_DATE).createdBy(DEFAULT_CREATED_BY).updatedBy(DEFAULT_UPDATED_BY);

		return job;
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Job createJobActiveEditedCannotEdit(EntityManager em) {

		Job job = new Job().jobTitle(DEFAULT_JOB_TITLE).jobDescription(DEFAULT_JOB_DESCRIPTION)
				.noOfApplicants(DEFAULT_NO_OF_APPLICANTS).salary(DEFAULT_SALARY).jobStatus(DRAFT_JOB_STATUS)
				.createDate(DEFAULT_CREATE_DATE).originalJobCost(DEFAULT_ORIGINAL_JOB_COST).jobCost(DEFAULT_JOB_COST)
				.amountPaid(DEFAULT_AMOUNT_PAID).totalAmountPaid(DEFAULT_TOTAL_AMOUNT_PAID)
				.noOfApplicantsBought(DEFAULT_NO_OF_APPLICANTS_BOUGHT)
				.removedFilterAmount(DEFAULT_REMOVED_FILTER_AMOUNT)
				.additionalFilterAmount(DEFAULT_ADDITIONAL_FILTER_AMOUNT).adminCharge(DEFAULT_ADMIN_CHARGE)
				.adminChargeRate(DEFAULT_ADMIN_CHARGE_RATE).upfrontDiscountRate(DEFAULT_UPFRONT_DISCOUNT_RATE)
				.upfrontDiscountAmount(DEFAULT_UPFRONT_DISCOUNT_AMOUNT).escrowAmountUsed(DEFAULT_ESCROW_AMOUNT_USED)
				.escrowAmountAdded(DEFAULT_ESCROW_AMOUNT_ADDED).paymentType(DEFAULT_PAYMENT_TYPE)
				.hasBeenEdited(DEFAULT_HAS_BEEN_EDITED).everActive(DEFAULT_EVER_ACTIVE).canEdit(DEFAULT_CAN_EDIT)
				.updateDate(DEFAULT_UPDATE_DATE).createdBy(DEFAULT_CREATED_BY).updatedBy(DEFAULT_UPDATED_BY);

		return job;
	}

	public static Corporate createCorporate(EntityManager em) {
		Corporate corporate = new Corporate();
		return corporate;
	}
	
	public static EmploymentType createEmploymentType(EntityManager em) {
		EmploymentType employmentType = new EmploymentType();
		return employmentType;
	}
	
	public static JobType createJobType(EntityManager em) {
		JobType jobType = new JobType();
		return jobType;
	}

	@Before
	public void initTest() {
		// jobSearchRepository.deleteAll();
		job = createDraftJob(em);
		corporate = createCorporate(em);
		employmentType = createEmploymentType(em);
		jobType = createJobType(em);
		employmentTypeRepository.saveAndFlush(employmentType);
		jobTypeRepository.saveAndFlush(jobType);
	}

	@Test
	@Transactional
	public void disableFutureEditsIfAlreadyEdited() throws Exception {
		Job job = createJobActiveEditedCanEdit(em);
		jobRepository.saveAndFlush(job);
		int jobDatabaseSizeBeforeUpdate = jobRepository.findAll().size();
		int jobHistoryDatabaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();
		Job initializedJob = jobRepository.findOne(job.getId());
		Job toBeUpdated = new Job();
		JobsUtil.populateHistories(toBeUpdated, initializedJob);
		toBeUpdated.setAdditionalFilterAmount(UPDATED_ADDITIONAL_FILTER_AMOUNT);
		toBeUpdated.setJobTitle(UPDATED_JOB_TITLE);
		restJobMockMvc.perform(put("/api/jobs").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(toBeUpdated))).andExpect(status().isOk());
		List<Job> jobList = jobRepository.findAll();
		List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
		assertThat(jobList).hasSize(jobDatabaseSizeBeforeUpdate);
		assertThat(jobList).hasSize(1);
		assertThat(jobHistoryList).hasSize(jobHistoryDatabaseSizeBeforeUpdate + 1);
		assertThat(jobHistoryList).hasSize(1);
		Job testJob = jobList.get(0);
		assertThat(testJob.getCanEdit()).isEqualTo(false);

	}

	@Test
	@Transactional
	public void createJob() throws Exception {
		int jobDatabaseSizeBeforeCreate = jobRepository.findAll().size();
		job.setCorporate(corporateRepository.save(corporate));
		// Create the Job
		restJobMockMvc.perform(post("/api/jobs").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(job))).andExpect(status().isCreated());

		// Validate the Job in the database
		List<Job> jobList = jobRepository.findAll();
		List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
		List<JobFilterHistory> jobFilterHistoryList = jobFilterHistoryRepository.findAll();
		List<JobFilter> jobFilterList = jobFilterRepository.findAll();
		assertThat(jobList).hasSize(jobDatabaseSizeBeforeCreate + 1);
		assertThat(jobHistoryList).hasSize(0);
		assertThat(jobFilterHistoryList).hasSize(0);
		assertThat(jobFilterList).hasSize(0);
		Job testJob = jobList.get(jobList.size() - 1);
		assertThat(testJob.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
		assertThat(testJob.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
		assertThat(testJob.getNoOfApplicants()).isEqualTo(DEFAULT_NO_OF_APPLICANTS);
		assertThat(testJob.getSalary()).isEqualTo(DEFAULT_SALARY);
		assertThat(testJob.getJobStatus()).isEqualTo(DRAFT_JOB_STATUS);
		// assertThat(testJob.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
		assertThat(testJob.getOriginalJobCost()).isEqualTo(DEFAULT_ORIGINAL_JOB_COST);
		assertThat(testJob.getJobCost()).isEqualTo(DEFAULT_JOB_COST);
		assertThat(testJob.getAmountPaid()).isEqualTo(DEFAULT_AMOUNT_PAID);
		assertThat(testJob.getTotalAmountPaid()).isEqualTo(DEFAULT_TOTAL_AMOUNT_PAID);
		assertThat(testJob.getNoOfApplicantsBought()).isEqualTo(DEFAULT_NO_OF_APPLICANTS_BOUGHT);
		assertThat(testJob.getRemovedFilterAmount()).isEqualTo(DEFAULT_REMOVED_FILTER_AMOUNT);
		assertThat(testJob.getAdditionalFilterAmount()).isEqualTo(DEFAULT_ADDITIONAL_FILTER_AMOUNT);
		assertThat(testJob.getAdminCharge()).isEqualTo(DEFAULT_ADMIN_CHARGE);
		assertThat(testJob.getAdminChargeRate()).isEqualTo(DEFAULT_ADMIN_CHARGE_RATE);
		assertThat(testJob.getUpfrontDiscountRate()).isEqualTo(DEFAULT_UPFRONT_DISCOUNT_RATE);
		assertThat(testJob.getUpfrontDiscountAmount()).isEqualTo(DEFAULT_UPFRONT_DISCOUNT_AMOUNT);
		assertThat(testJob.getEscrowAmountUsed()).isEqualTo(DEFAULT_ESCROW_AMOUNT_USED);
		assertThat(testJob.getEscrowAmountAdded()).isEqualTo(DEFAULT_ESCROW_AMOUNT_ADDED);
		assertThat(testJob.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
		assertThat(testJob.isHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJob.isEverActive()).isEqualTo(DEFAULT_EVER_ACTIVE);
		assertThat(testJob.isCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		// assertThat(testJob.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJob.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);

		// Validate the Job in Elasticsearch
		/*
		 * Job jobEs = jobSearchRepository.findOne(testJob.getId());
		 * assertThat(jobEs.getJobTitle()).isEqualTo(testJob.getJobTitle());
		 * assertThat(jobEs.getJobDescription()).isEqualTo(testJob.getJobDescription());
		 * assertThat(jobEs.getNoOfApplicants()).isEqualTo(testJob.getNoOfApplicants());
		 * assertThat(jobEs.getSalary()).isEqualTo(testJob.getSalary());
		 * assertThat(jobEs.getJobStatus()).isEqualTo(testJob.getJobStatus()); //
		 * assertThat(testJob.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
		 * assertThat(jobEs.getOriginalJobCost()).isEqualTo(testJob.getOriginalJobCost()
		 * ); assertThat(jobEs.getJobCost()).isEqualTo(testJob.getJobCost());
		 * assertThat(jobEs.getAmountPaid()).isEqualTo(testJob.getAmountPaid());
		 * assertThat(jobEs.getTotalAmountPaid()).isEqualTo(testJob.getTotalAmountPaid()
		 * ); assertThat(jobEs.getNoOfApplicantsBought()).isEqualTo(testJob.
		 * getNoOfApplicantsBought());
		 * assertThat(jobEs.getRemovedFilterAmount()).isEqualTo(testJob.
		 * getRemovedFilterAmount());
		 * assertThat(jobEs.getAdditionalFilterAmount()).isEqualTo(testJob.
		 * getAdditionalFilterAmount());
		 * assertThat(jobEs.getAdminCharge()).isEqualTo(testJob.getAdminCharge());
		 * assertThat(jobEs.getAdminChargeRate()).isEqualTo(testJob.getAdminChargeRate()
		 * ); assertThat(jobEs.getUpfrontDiscountRate()).isEqualTo(testJob.
		 * getUpfrontDiscountRate());
		 * assertThat(jobEs.getUpfrontDiscountAmount()).isEqualTo(testJob.
		 * getUpfrontDiscountAmount());
		 * assertThat(jobEs.getEscrowAmountUsed()).isEqualTo(testJob.getEscrowAmountUsed
		 * ()); assertThat(jobEs.getEscrowAmountAdded()).isEqualTo(testJob.
		 * getEscrowAmountAdded());
		 * assertThat(jobEs.getPaymentType()).isEqualTo(testJob.getPaymentType());
		 * assertThat(jobEs.isHasBeenEdited()).isEqualTo(testJob.isHasBeenEdited());
		 * assertThat(jobEs.isEverActive()).isEqualTo(testJob.isEverActive());
		 * assertThat(jobEs.isCanEdit()).isEqualTo(testJob.isCanEdit()); //
		 * assertThat(testJob.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
		 * assertThat(jobEs.getCreatedBy()).isEqualTo(testJob.getCreatedBy());
		 * assertThat(jobEs.getUpdatedBy()).isEqualTo(testJob.getUpdatedBy());
		 */
	}

	@Test
	@Transactional
	public void createJobWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = jobRepository.findAll().size();

		// Create the Job with an existing ID
		job.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restJobMockMvc.perform(post("/api/jobs").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(job))).andExpect(status().isBadRequest());

		// Validate the Job in the database
		List<Job> jobList = jobRepository.findAll();
		assertThat(jobList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllJobs() throws Exception {
		// Initialize the database
		corporateRepository.saveAndFlush(corporate);
		jobRepository.saveAndFlush(job);

		// Get all the jobList
		restJobMockMvc.perform(get("/api/jobs?sort=id,desc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
				.andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE.toString())))
				.andExpect(jsonPath("$.[*].jobDescription").value(hasItem(DEFAULT_JOB_DESCRIPTION.toString())))
				.andExpect(jsonPath("$.[*].noOfApplicants").value(hasItem(DEFAULT_NO_OF_APPLICANTS)))
				.andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY.doubleValue())))
				.andExpect(jsonPath("$.[*].jobStatus").value(hasItem(DRAFT_JOB_STATUS)))
				.andExpect(jsonPath("$.[*].createDate").value(hasItem(sameInstant(DEFAULT_CREATE_DATE))))
				.andExpect(jsonPath("$.[*].originalJobCost").value(hasItem(DEFAULT_ORIGINAL_JOB_COST.doubleValue())))
				.andExpect(jsonPath("$.[*].jobCost").value(hasItem(DEFAULT_JOB_COST.doubleValue())))
				.andExpect(jsonPath("$.[*].amountPaid").value(hasItem(DEFAULT_AMOUNT_PAID.doubleValue())))
				.andExpect(jsonPath("$.[*].totalAmountPaid").value(hasItem(DEFAULT_TOTAL_AMOUNT_PAID.doubleValue())))
				.andExpect(jsonPath("$.[*].noOfApplicantsBought").value(hasItem(DEFAULT_NO_OF_APPLICANTS_BOUGHT)))
				.andExpect(jsonPath("$.[*].removedFilterAmount")
						.value(hasItem(DEFAULT_REMOVED_FILTER_AMOUNT.doubleValue())))
				.andExpect(jsonPath("$.[*].additionalFilterAmount")
						.value(hasItem(DEFAULT_ADDITIONAL_FILTER_AMOUNT.doubleValue())))
				.andExpect(jsonPath("$.[*].adminCharge").value(hasItem(DEFAULT_ADMIN_CHARGE.doubleValue())))
				.andExpect(jsonPath("$.[*].adminChargeRate").value(hasItem(DEFAULT_ADMIN_CHARGE_RATE.doubleValue())))
				.andExpect(jsonPath("$.[*].upfrontDiscountRate")
						.value(hasItem(DEFAULT_UPFRONT_DISCOUNT_RATE.doubleValue())))
				.andExpect(jsonPath("$.[*].upfrontDiscountAmount")
						.value(hasItem(DEFAULT_UPFRONT_DISCOUNT_AMOUNT.doubleValue())))
				.andExpect(jsonPath("$.[*].escrowAmountUsed").value(hasItem(DEFAULT_ESCROW_AMOUNT_USED.doubleValue())))
				.andExpect(
						jsonPath("$.[*].escrowAmountAdded").value(hasItem(DEFAULT_ESCROW_AMOUNT_ADDED.doubleValue())))
				.andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE.toString())))
				.andExpect(jsonPath("$.[*].hasBeenEdited").value(hasItem(DEFAULT_HAS_BEEN_EDITED.booleanValue())))
				.andExpect(jsonPath("$.[*].everActive").value(hasItem(DEFAULT_EVER_ACTIVE.booleanValue())))
				.andExpect(jsonPath("$.[*].canEdit").value(hasItem(DEFAULT_CAN_EDIT.booleanValue())))
				.andExpect(jsonPath("$.[*].updateDate").value(hasItem(sameInstant(DEFAULT_UPDATE_DATE))))
				.andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
				.andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.intValue())));
	}

	@Test
	@Transactional
	public void getJob() throws Exception {
		// Initialize the database
		corporateRepository.saveAndFlush(corporate);
		job.setCorporate(corporate);
		jobRepository.saveAndFlush(job);

		// Get the job
		restJobMockMvc.perform(get("/api/jobs/{id}", job.getId())).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(job.getId().intValue()))
				.andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE.toString()))
				.andExpect(jsonPath("$.jobDescription").value(DEFAULT_JOB_DESCRIPTION.toString()))
				.andExpect(jsonPath("$.noOfApplicants").value(DEFAULT_NO_OF_APPLICANTS))
				.andExpect(jsonPath("$.salary").value(DEFAULT_SALARY.doubleValue()))
				.andExpect(jsonPath("$.jobStatus").value(DRAFT_JOB_STATUS))
				.andExpect(jsonPath("$.createDate").value(sameInstant(DEFAULT_CREATE_DATE)))
				.andExpect(jsonPath("$.originalJobCost").value(DEFAULT_ORIGINAL_JOB_COST.doubleValue()))
				.andExpect(jsonPath("$.jobCost").value(DEFAULT_JOB_COST.doubleValue()))
				.andExpect(jsonPath("$.amountPaid").value(DEFAULT_AMOUNT_PAID.doubleValue()))
				.andExpect(jsonPath("$.totalAmountPaid").value(DEFAULT_TOTAL_AMOUNT_PAID.doubleValue()))
				.andExpect(jsonPath("$.noOfApplicantsBought").value(DEFAULT_NO_OF_APPLICANTS_BOUGHT))
				.andExpect(jsonPath("$.removedFilterAmount").value(DEFAULT_REMOVED_FILTER_AMOUNT.doubleValue()))
				.andExpect(jsonPath("$.additionalFilterAmount").value(DEFAULT_ADDITIONAL_FILTER_AMOUNT.doubleValue()))
				.andExpect(jsonPath("$.adminCharge").value(DEFAULT_ADMIN_CHARGE.doubleValue()))
				.andExpect(jsonPath("$.adminChargeRate").value(DEFAULT_ADMIN_CHARGE_RATE.doubleValue()))
				.andExpect(jsonPath("$.upfrontDiscountRate").value(DEFAULT_UPFRONT_DISCOUNT_RATE.doubleValue()))
				.andExpect(jsonPath("$.upfrontDiscountAmount").value(DEFAULT_UPFRONT_DISCOUNT_AMOUNT.doubleValue()))
				.andExpect(jsonPath("$.escrowAmountUsed").value(DEFAULT_ESCROW_AMOUNT_USED.doubleValue()))
				.andExpect(jsonPath("$.escrowAmountAdded").value(DEFAULT_ESCROW_AMOUNT_ADDED.doubleValue()))
				.andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE.toString()))
				.andExpect(jsonPath("$.hasBeenEdited").value(DEFAULT_HAS_BEEN_EDITED.booleanValue()))
				.andExpect(jsonPath("$.everActive").value(DEFAULT_EVER_ACTIVE.booleanValue()))
				.andExpect(jsonPath("$.canEdit").value(DEFAULT_CAN_EDIT.booleanValue()))
				.andExpect(jsonPath("$.updateDate").value(sameInstant(DEFAULT_UPDATE_DATE)))
				.andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
				.andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY.intValue()));
	}

	@Test
	@Transactional
	@Ignore
	public void getActiveJob() throws Exception {
		// Initialize the database
		corporateRepository.saveAndFlush(corporate);
		job.setCorporate(corporate);
		jobRepository.saveAndFlush(job);

		// Get the job
		restJobMockMvc.perform(get("/api/activeJobs/{id}", corporate.getId())).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$[0].id").value(job.getId().intValue()))
				.andExpect(jsonPath("$[0].jobTitle").value(DEFAULT_JOB_TITLE.toString()))
				.andExpect(jsonPath("$[0].jobDescription").value(DEFAULT_JOB_DESCRIPTION.toString()))
				.andExpect(jsonPath("$[0].noOfApplicants").value(DEFAULT_NO_OF_APPLICANTS))
				.andExpect(jsonPath("$[0].salary").value(DEFAULT_SALARY.doubleValue()))
				.andExpect(jsonPath("$[0].jobStatus").value(DRAFT_JOB_STATUS))
				.andExpect(jsonPath("$[0].createDate").value(sameInstant(DEFAULT_CREATE_DATE)))
				.andExpect(jsonPath("$[0].originalJobCost").value(DEFAULT_ORIGINAL_JOB_COST.doubleValue()))
				.andExpect(jsonPath("$[0].jobCost").value(DEFAULT_JOB_COST.doubleValue()))
				.andExpect(jsonPath("$[0].amountPaid").value(DEFAULT_AMOUNT_PAID.doubleValue()))
				.andExpect(jsonPath("$[0].totalAmountPaid").value(DEFAULT_TOTAL_AMOUNT_PAID.doubleValue()))
				.andExpect(jsonPath("$[0].noOfApplicantsBought").value(DEFAULT_NO_OF_APPLICANTS_BOUGHT))
				.andExpect(jsonPath("$[0].removedFilterAmount").value(DEFAULT_REMOVED_FILTER_AMOUNT.doubleValue()))
				.andExpect(
						jsonPath("$[0].additionalFilterAmount").value(DEFAULT_ADDITIONAL_FILTER_AMOUNT.doubleValue()))
				.andExpect(jsonPath("$[0].adminCharge").value(DEFAULT_ADMIN_CHARGE.doubleValue()))
				.andExpect(jsonPath("$[0].adminChargeRate").value(DEFAULT_ADMIN_CHARGE_RATE.doubleValue()))
				.andExpect(jsonPath("$[0].upfrontDiscountRate").value(DEFAULT_UPFRONT_DISCOUNT_RATE.doubleValue()))
				.andExpect(jsonPath("$[0].upfrontDiscountAmount").value(DEFAULT_UPFRONT_DISCOUNT_AMOUNT.doubleValue()))
				.andExpect(jsonPath("$[0].escrowAmountUsed").value(DEFAULT_ESCROW_AMOUNT_USED.doubleValue()))
				.andExpect(jsonPath("$[0].escrowAmountAdded").value(DEFAULT_ESCROW_AMOUNT_ADDED.doubleValue()))
				.andExpect(jsonPath("$[0].paymentType").value(DEFAULT_PAYMENT_TYPE.toString()))
				.andExpect(jsonPath("$[0].hasBeenEdited").value(DEFAULT_HAS_BEEN_EDITED.booleanValue()))
				.andExpect(jsonPath("$[0].everActive").value(DEFAULT_EVER_ACTIVE.booleanValue()))
				.andExpect(jsonPath("$[0].canEdit").value(DEFAULT_CAN_EDIT.booleanValue()))
				.andExpect(jsonPath("$[0].updateDate").value(sameInstant(DEFAULT_UPDATE_DATE)))
				.andExpect(jsonPath("$[0].createdBy").value(DEFAULT_CREATED_BY.intValue()))
				.andExpect(jsonPath("$[0].updatedBy").value(DEFAULT_UPDATED_BY.intValue()));
	}

	@Test
	@Transactional
	public void getNonExistingJob() throws Exception {
		// Get the job
		restJobMockMvc.perform(get("/api/jobs/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void getActiveJobsListForCorporatesFilteredByReviewStatusTrue() throws Exception {
		Candidate candidate1 = new Candidate();
		Candidate candidate2 = new Candidate();
		candidateRepository.saveAndFlush(candidate1);
		candidateRepository.saveAndFlush(candidate2);
		corporateRepository.saveAndFlush(corporate);
		job.setCorporate(corporate);
		jobRepository.saveAndFlush(job);
		CandidateJob cJ1 = new CandidateJob(candidate1, job);
		cJ1.setMatchScore(100d);
		CandidateJob cJ2 = new CandidateJob(candidate2, job);
		cJ2.setMatchScore(10d);
		job.addCandidateJob(cJ2).addCandidateJob(cJ1);
		jobRepository.saveAndFlush(job);
		restJobMockMvc.perform(get("/api/activeJobsForCorporate/{id}", corporate.getId()))
				.andDo(MockMvcResultHandlers.print()).andExpect(jsonPath("$", hasSize(1)));
				
	}

	@Test
	@Transactional
	public void updateNonExistingJob() throws Exception {
		int databaseSizeBeforeUpdate = jobRepository.findAll().size();

		// Create the Job
		job.setCorporate(corporateRepository.save(corporate));
		// If the entity doesn't have an ID, it will be created instead of just being
		// updated
		restJobMockMvc
				.perform(put("/api/jobs").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(job)))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isCreated());

		// Validate the Job in the database
		List<Job> jobList = jobRepository.findAll();
		assertThat(jobList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteJob() throws Exception {
		// Initialize the database
		jobRepository.saveAndFlush(job);
		// jobSearchRepository.save(job);
		int databaseSizeBeforeDelete = jobRepository.findAll().size();

		// Get the job
		restJobMockMvc.perform(delete("/api/jobs/{id}", job.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate Elasticsearch is empty
		// boolean jobExistsInEs = jobSearchRepository.exists(job.getId());
		// assertThat(jobExistsInEs).isFalse();

		// Validate the database is empty
		List<Job> jobList = jobRepository.findAll();
		assertThat(jobList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void searchJob() throws Exception {
		// Initialize the database
		corporateRepository.saveAndFlush(corporate);
		jobRepository.saveAndFlush(job);
		jobSearchRepository.save(job);

		// Search the job
		restJobMockMvc.perform(get("/api/_search/jobs?query=id:" + job.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
				.andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE.toString())))
				.andExpect(jsonPath("$.[*].jobDescription").value(hasItem(DEFAULT_JOB_DESCRIPTION.toString())))
				.andExpect(jsonPath("$.[*].noOfApplicants").value(hasItem(DEFAULT_NO_OF_APPLICANTS)))
				.andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY.doubleValue())))
				.andExpect(jsonPath("$.[*].jobStatus").value(hasItem(DRAFT_JOB_STATUS)))
				.andExpect(jsonPath("$.[*].createDate").value(hasItem(sameInstant(DEFAULT_CREATE_DATE))))
				.andExpect(jsonPath("$.[*].originalJobCost").value(hasItem(DEFAULT_ORIGINAL_JOB_COST.doubleValue())))
				.andExpect(jsonPath("$.[*].jobCost").value(hasItem(DEFAULT_JOB_COST.doubleValue())))
				.andExpect(jsonPath("$.[*].amountPaid").value(hasItem(DEFAULT_AMOUNT_PAID.doubleValue())))
				.andExpect(jsonPath("$.[*].totalAmountPaid").value(hasItem(DEFAULT_TOTAL_AMOUNT_PAID.doubleValue())))
				.andExpect(jsonPath("$.[*].noOfApplicantsBought").value(hasItem(DEFAULT_NO_OF_APPLICANTS_BOUGHT)))
				.andExpect(jsonPath("$.[*].removedFilterAmount")
						.value(hasItem(DEFAULT_REMOVED_FILTER_AMOUNT.doubleValue())))
				.andExpect(jsonPath("$.[*].additionalFilterAmount")
						.value(hasItem(DEFAULT_ADDITIONAL_FILTER_AMOUNT.doubleValue())))
				.andExpect(jsonPath("$.[*].adminCharge").value(hasItem(DEFAULT_ADMIN_CHARGE.doubleValue())))
				.andExpect(jsonPath("$.[*].adminChargeRate").value(hasItem(DEFAULT_ADMIN_CHARGE_RATE.doubleValue())))
				.andExpect(jsonPath("$.[*].upfrontDiscountRate")
						.value(hasItem(DEFAULT_UPFRONT_DISCOUNT_RATE.doubleValue())))
				.andExpect(jsonPath("$.[*].upfrontDiscountAmount")
						.value(hasItem(DEFAULT_UPFRONT_DISCOUNT_AMOUNT.doubleValue())))
				.andExpect(jsonPath("$.[*].escrowAmountUsed").value(hasItem(DEFAULT_ESCROW_AMOUNT_USED.doubleValue())))
				.andExpect(
						jsonPath("$.[*].escrowAmountAdded").value(hasItem(DEFAULT_ESCROW_AMOUNT_ADDED.doubleValue())))
				.andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE.toString())))
				.andExpect(jsonPath("$.[*].hasBeenEdited").value(hasItem(DEFAULT_HAS_BEEN_EDITED.booleanValue())))
				.andExpect(jsonPath("$.[*].everActive").value(hasItem(DEFAULT_EVER_ACTIVE.booleanValue())))
				.andExpect(jsonPath("$.[*].canEdit").value(hasItem(DEFAULT_CAN_EDIT.booleanValue())))
				.andExpect(jsonPath("$.[*].updateDate").value(hasItem(sameInstant(DEFAULT_UPDATE_DATE))))
				.andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
				.andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY.intValue())));
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Job.class);
		Job job1 = new Job();
		job1.setId(1L);

		Job job2 = new Job();
		job2.setId(job1.getId());

		assertThat(job1).isEqualTo(job2);
		job2.setId(2L);

		assertThat(job1).isNotEqualTo(job2);

		job1.setId(null);
		assertThat(job1).isNotEqualTo(job2);
	}

	@Test
	@Transactional
	public void updateDraftJobToActiveWithDefaultFilter() throws Exception {
		// Create Draft Job
		Job job = createDraftJob(em);
		job.employmentType(employmentType);
		job.jobType(jobType);
		jobRepository.saveAndFlush(job);
		// jobSearchRepository.save(job);
		int jobDatabaseSizeBeforeUpdate = jobRepository.findAll().size();
		int jobHistoryDatabaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();
		int jobFiltersSizeBeforeUpdate = jobFilterRepository.findAll().size();
		int jobFilterHistorySizeBeforeUpdate = jobFilterHistoryRepository.findAll().size();
		Job initializedJob = jobRepository.findOne(job.getId());
		// Job prevJob = new Job ();
		// BeanUtils.copyProperties(prevJob, initializedJob);

		Job toBeUpdated = new Job();
		toBeUpdated.jobTitle(UPDATED_JOB_TITLE).jobDescription(UPDATED_JOB_DESCRIPTION).salary(UPDATED_SALARY)
				.jobStatus(ACTIVE_JOB_STATUS).hasBeenEdited(DEFAULT_HAS_BEEN_EDITED).everActive(DEFAULT_EVER_ACTIVE)
				.canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY).updatedBy(UPDATED_UPDATED_BY);
		toBeUpdated.setId(initializedJob.getId());
		Job prevJob = new Job();
		BeanUtils.copyProperties(prevJob, initializedJob);
		// Create Basic Filter
		JobFilter jobFilter = new JobFilter();
		jobFilter.setFilterDescription(BASIC_FILTER);
		jobFilter.job(toBeUpdated);

		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		jobFilters.add(jobFilter);
		toBeUpdated.setJobFilters(jobFilters);
		// No Prior History
		// Execute Update
		restJobMockMvc.perform(put("/api/jobs").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(toBeUpdated))).andExpect(status().isOk());

		List<Job> jobList = jobRepository.findAll();
		List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
		List<JobFilter> jobFilterList = jobFilterRepository.findAll();
		List<JobFilterHistory> jobFilterHistoryList = jobFilterHistoryRepository.findAll();
		assertThat(jobList).hasSize(jobDatabaseSizeBeforeUpdate);
		assertThat(jobHistoryList).hasSize(jobHistoryDatabaseSizeBeforeUpdate + 1);
		assertThat(jobHistoryList).hasSize(1);
		assertThat(jobFilterList).hasSize(jobFiltersSizeBeforeUpdate + 1);
		assertThat(jobFilterList).hasSize(1);
		// should not have any filter history
		assertThat(jobFilterHistoryList).hasSize(jobFilterHistorySizeBeforeUpdate);
		assertThat(jobFilterHistoryList).hasSize(0);

		Job testJob = jobList.get(jobList.size() - 1);
		// hasBeenEdited must be false
		// canEdit must be true
		// everActive must be true
		// should move job to active
		assertThat(testJob.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
		assertThat(testJob.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
		assertThat(testJob.getSalary()).isEqualTo(UPDATED_SALARY);
		assertThat(testJob.getJobStatus()).isEqualTo(ACTIVE_JOB_STATUS);
		assertThat(testJob.isCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJob.isHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJob.isEverActive()).isEqualTo(UPDATED_EVER_ACTIVE);
		assertThat(testJob.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		// validate in ES
		/*
		 * Job jobEs = jobSearchRepository.findOne(testJob.getId());
		 * assertThat(testJob.getJobTitle()).isEqualTo(jobEs.getJobTitle());
		 * assertThat(testJob.getJobDescription()).isEqualTo(jobEs.getJobDescription());
		 * assertThat(testJob.getSalary()).isEqualTo(jobEs.getSalary());
		 * assertThat(testJob.getJobStatus()).isEqualTo(jobEs.getJobStatus());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.isCanEdit()).isEqualTo(jobEs.isCanEdit());
		 * assertThat(testJob.isHasBeenEdited()).isEqualTo(jobEs.isHasBeenEdited());
		 * assertThat(testJob.isEverActive()).isEqualTo(jobEs.isEverActive());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.getUpdatedBy()).isEqualTo(jobEs.getUpdatedBy());
		 */
		// Validate history data
		// should have history with the draft job in
		List<JobHistory> jobHistories = jobHistoryRepository.findByJob(testJob);
		JobHistory jobHistory = jobHistories.get(jobHistories.size() - 1);
		assertThat(jobHistory.getJobTitle()).isEqualTo(prevJob.getJobTitle());
		assertThat(jobHistory.getJobDescription()).isEqualTo(prevJob.getJobDescription());
		assertThat(jobHistory.getSalary()).isEqualTo(prevJob.getSalary());
		assertThat(jobHistory.getJobStatus()).isEqualTo(prevJob.getJobStatus());
		assertThat(jobHistory.getCreatedBy()).isEqualTo(prevJob.getCreatedBy());
		assertThat(jobHistory.isCanEdit()).isEqualTo(prevJob.isCanEdit());
		assertThat(jobHistory.isHasBeenEdited()).isEqualTo(prevJob.isHasBeenEdited());
		assertThat(jobHistory.isEverActive()).isEqualTo(prevJob.isEverActive());
		assertThat(jobHistory.getCreatedBy()).isEqualTo(prevJob.getCreatedBy());
		assertThat(jobHistory.getUpdatedBy()).isEqualTo(prevJob.getUpdatedBy());

		// Validate Filters
		// should have filter data
		JobFilter testJobFilter = jobFilterRepository.findByJob(testJob);
		assertThat(testJobFilter.getFilterDescription()).isEqualTo(BASIC_FILTER);
		assertThat(testJobFilter.getJob()).isEqualTo(testJob);
	}

	@Test
	@Transactional
	@Ignore
	public void updateActiveJobWithoutChangingAnything() throws Exception {
		// create a job that would have been output from
		// updateDraftJobToActiveWithDefaultFilter
		// nothng should change post update
		// Create Job from previous Save
		Job job = new Job().jobTitle(UPDATED_JOB_TITLE).jobDescription(UPDATED_JOB_DESCRIPTION).salary(UPDATED_SALARY)
				.jobStatus(ACTIVE_JOB_STATUS).hasBeenEdited(DEFAULT_HAS_BEEN_EDITED).everActive(UPDATED_EVER_ACTIVE)
				.canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY).updatedBy(UPDATED_UPDATED_BY);

		JobFilter jobFilter = new JobFilter();
		jobFilter.setFilterDescription(BASIC_FILTER);
		jobFilter.job(job);
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		jobFilters.add(jobFilter);
		job.setJobFilters(jobFilters);

		JobHistory jobHistory = new JobHistory().jobTitle(DEFAULT_JOB_TITLE).jobDescription(DEFAULT_JOB_DESCRIPTION)
				.salary(DEFAULT_SALARY).jobStatus(DRAFT_JOB_STATUS).hasBeenEdited(DEFAULT_HAS_BEEN_EDITED)
				.everActive(UPDATED_EVER_ACTIVE).canEdit(DEFAULT_CAN_EDIT).updateDate(DEFAULT_UPDATE_DATE)
				.createdBy(DEFAULT_CREATED_BY).updatedBy(DEFAULT_UPDATED_BY).job(job);
		Set<JobHistory> jobHistories = new HashSet<JobHistory>();
		jobHistories.add(jobHistory);
		job.setHistories(jobHistories);

		jobRepository.saveAndFlush(job);
		// jobSearchRepository.save(job);

		int jobDatabaseSizeBeforeUpdate = jobRepository.findAll().size();
		int jobHistoryDatabaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();
		int jobFiltersSizeBeforeUpdate = jobFilterRepository.findAll().size();
		int jobFilterHistorySizeBeforeUpdate = jobFilterHistoryRepository.findAll().size();
		Job initializedJob = jobRepository.findOne(job.getId());
		JobHistory initialzedJobHistory = jobHistoryRepository.findOne(jobHistory.getId());
		JobHistory prevJobHistory = new JobHistory();
		BeanUtils.copyProperties(prevJobHistory, initialzedJobHistory);
		Job prevJob = new Job();
		BeanUtils.copyProperties(prevJob, initializedJob);
		// to be updated
		Job toBeUpdated = new Job();
		toBeUpdated.setId(initializedJob.getId());
		toBeUpdated.jobTitle(UPDATED_JOB_TITLE).jobDescription(UPDATED_JOB_DESCRIPTION).salary(UPDATED_SALARY)
				.jobStatus(ACTIVE_JOB_STATUS).hasBeenEdited(DEFAULT_HAS_BEEN_EDITED).everActive(UPDATED_EVER_ACTIVE)
				.canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY).updatedBy(UPDATED_UPDATED_BY);
		JobFilter initializedJobFilter = jobFilterRepository.findByJob(initializedJob);
		JobFilter prevJobFilter = new JobFilter();
		BeanUtils.copyProperties(prevJobFilter, initializedJobFilter);
		JobFilter jobFilterUpdated = new JobFilter();
		jobFilterUpdated.setId(initializedJobFilter.getId());
		jobFilterUpdated.setFilterDescription(BASIC_FILTER);
		jobFilterUpdated.job(toBeUpdated);
		Set<JobFilter> jobFiltersUpdated = new HashSet<JobFilter>();
		jobFiltersUpdated.add(jobFilterUpdated);
		toBeUpdated.setJobFilters(jobFiltersUpdated);
		// Execute Update
		restJobMockMvc.perform(put("/api/jobs").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(toBeUpdated))).andExpect(status().isOk());

		List<Job> jobList = jobRepository.findAll();
		List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
		List<JobFilter> jobFilterList = jobFilterRepository.findAll();
		List<JobFilterHistory> jobFilterHistoryList = jobFilterHistoryRepository.findAll();
		assertThat(jobList).hasSize(jobDatabaseSizeBeforeUpdate);
		assertThat(jobList).hasSize(1);
		assertThat(jobHistoryList).hasSize(jobHistoryDatabaseSizeBeforeUpdate);
		assertThat(jobHistoryList).hasSize(1);
		assertThat(jobFilterList).hasSize(jobFiltersSizeBeforeUpdate);
		assertThat(jobFilterList).hasSize(1);
		// should not have any filter history
		assertThat(jobFilterHistoryList).hasSize(jobFilterHistorySizeBeforeUpdate);
		assertThat(jobFilterHistoryList).hasSize(0);

		Job testJob = jobList.get(jobList.size() - 1);
		// hasBeenEdited must be false
		// canEdit must be true
		// everActive must be true
		// should move job to active
		assertThat(testJob.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
		assertThat(testJob.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
		assertThat(testJob.getSalary()).isEqualTo(UPDATED_SALARY);
		assertThat(testJob.getJobStatus()).isEqualTo(ACTIVE_JOB_STATUS);
		assertThat(testJob.isCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJob.isHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJob.isEverActive()).isEqualTo(UPDATED_EVER_ACTIVE);
		assertThat(testJob.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		// validate in ES
		/*
		 * Job jobEs = jobSearchRepository.findOne(testJob.getId());
		 * assertThat(testJob.getJobTitle()).isEqualTo(jobEs.getJobTitle());
		 * assertThat(testJob.getJobDescription()).isEqualTo(jobEs.getJobDescription());
		 * assertThat(testJob.getSalary()).isEqualTo(jobEs.getSalary());
		 * assertThat(testJob.getJobStatus()).isEqualTo(jobEs.getJobStatus());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.isCanEdit()).isEqualTo(jobEs.isCanEdit());
		 * assertThat(testJob.isHasBeenEdited()).isEqualTo(jobEs.isHasBeenEdited());
		 * assertThat(testJob.isEverActive()).isEqualTo(jobEs.isEverActive());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.getUpdatedBy()).isEqualTo(jobEs.getUpdatedBy());
		 */
		// Validate history data
		// should have history with the draft job in
		List<JobHistory> testJobHistories = jobHistoryRepository.findByJob(testJob);
		JobHistory testJobHistory = testJobHistories.get(testJobHistories.size() - 1);

		assertThat(testJobHistory.getJobTitle()).isEqualTo(prevJobHistory.getJobTitle());
		assertThat(testJobHistory.getJobDescription()).isEqualTo(prevJobHistory.getJobDescription());
		assertThat(testJobHistory.getSalary()).isEqualTo(prevJobHistory.getSalary());
		assertThat(testJobHistory.getJobStatus()).isEqualTo(prevJobHistory.getJobStatus());
		assertThat(testJobHistory.getCreatedBy()).isEqualTo(prevJobHistory.getCreatedBy());
		assertThat(testJobHistory.isCanEdit()).isEqualTo(prevJobHistory.isCanEdit());
		assertThat(testJobHistory.isHasBeenEdited()).isEqualTo(prevJobHistory.isHasBeenEdited());
		assertThat(testJobHistory.isEverActive()).isEqualTo(prevJobHistory.isEverActive());
		assertThat(testJobHistory.getCreatedBy()).isEqualTo(prevJobHistory.getCreatedBy());
		assertThat(testJobHistory.getUpdatedBy()).isEqualTo(prevJobHistory.getUpdatedBy());

		// Validate Filters
		// should have filter data
		JobFilter testJobFilter = jobFilterRepository.findByJob(testJob);
		assertThat(testJobFilter).isEqualTo(prevJobFilter);
		assertThat(testJobFilter.getJob()).isEqualTo(testJob);

	}

	@Test
	@Transactional
	public void updateActiveJobOnlyToDraft() throws Exception {
		// create a job that would have been output from
		// updateDraftJobToActiveWithDefaultFilter
		// Job shud move to Dfraft State
		// hasBeenEdited must be false
		// canEdit must be true
		// everActive must be true
		// JobHistroy should have the previous Active record.
		Job job = new Job().jobTitle(UPDATED_JOB_TITLE).jobDescription(UPDATED_JOB_DESCRIPTION).salary(UPDATED_SALARY)
				.jobStatus(ACTIVE_JOB_STATUS).hasBeenEdited(DEFAULT_HAS_BEEN_EDITED).everActive(UPDATED_EVER_ACTIVE)
				.canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY).updatedBy(UPDATED_UPDATED_BY);

		JobFilter jobFilter = new JobFilter();
		jobFilter.setFilterDescription(BASIC_FILTER);
		jobFilter.job(job);
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		jobFilters.add(jobFilter);
		job.setJobFilters(jobFilters);
		JobHistory jobHistory = new JobHistory().jobTitle(DEFAULT_JOB_TITLE).jobDescription(DEFAULT_JOB_DESCRIPTION)
				.salary(DEFAULT_SALARY).jobStatus(DRAFT_JOB_STATUS).hasBeenEdited(DEFAULT_HAS_BEEN_EDITED)
				.everActive(UPDATED_EVER_ACTIVE).canEdit(DEFAULT_CAN_EDIT).updateDate(DEFAULT_UPDATE_DATE)
				.createdBy(DEFAULT_CREATED_BY).updatedBy(DEFAULT_UPDATED_BY).job(job);
		Set<JobHistory> jobHistories = new HashSet<JobHistory>();
		jobHistories.add(jobHistory);
		job.setHistories(jobHistories);
		jobRepository.saveAndFlush(job);
		// jobSearchRepository.save(job);
		int jobDatabaseSizeBeforeUpdate = jobRepository.findAll().size();
		int jobHistoryDatabaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();
		int jobFiltersSizeBeforeUpdate = jobFilterRepository.findAll().size();
		int jobFilterHistorySizeBeforeUpdate = jobFilterHistoryRepository.findAll().size();
		Job initializedJob = jobRepository.findOne(job.getId());
		Job prevJob = new Job();
		BeanUtils.copyProperties(prevJob, initializedJob);
		// to be updated
		Job toBeUpdated = new Job();
		toBeUpdated.setId(initializedJob.getId());
		toBeUpdated.jobTitle(UPDATED_JOB_TITLE).jobDescription(UPDATED_JOB_DESCRIPTION).salary(UPDATED_SALARY)
				.jobStatus(DRAFT_JOB_STATUS).hasBeenEdited(DEFAULT_HAS_BEEN_EDITED).everActive(UPDATED_EVER_ACTIVE)
				.canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY).updatedBy(UPDATED_UPDATED_BY);
		toBeUpdated.setJobFilters(jobFilters);
		// Execute Update
		restJobMockMvc.perform(put("/api/jobs").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(toBeUpdated))).andExpect(status().isOk());

		List<Job> jobList = jobRepository.findAll();
		List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
		List<JobFilter> jobFilterList = jobFilterRepository.findAll();
		List<JobFilterHistory> jobFilterHistoryList = jobFilterHistoryRepository.findAll();
		assertThat(jobList).hasSize(jobDatabaseSizeBeforeUpdate);
		assertThat(jobHistoryList).hasSize(jobHistoryDatabaseSizeBeforeUpdate + 1);
		assertThat(jobHistoryList).hasSize(2);
		assertThat(jobFilterList).hasSize(jobFiltersSizeBeforeUpdate);
		assertThat(jobFilterList).hasSize(1);
		// should not have any filter history
		assertThat(jobFilterHistoryList).hasSize(jobFilterHistorySizeBeforeUpdate);
		assertThat(jobFilterHistoryList).hasSize(0);

		Job testJob = jobList.get(jobList.size() - 1);
		// hasBeenEdited must be false
		// canEdit must be true
		// everActive must be true
		// should move job to active
		assertThat(testJob.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
		assertThat(testJob.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
		assertThat(testJob.getSalary()).isEqualTo(UPDATED_SALARY);
		assertThat(testJob.getJobStatus()).isEqualTo(DRAFT_JOB_STATUS);
		assertThat(testJob.isCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJob.isHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJob.isEverActive()).isEqualTo(UPDATED_EVER_ACTIVE);
		assertThat(testJob.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		// validate in ES
		/*
		 * Job jobEs = jobSearchRepository.findOne(testJob.getId());
		 * assertThat(testJob.getJobTitle()).isEqualTo(jobEs.getJobTitle());
		 * assertThat(testJob.getJobDescription()).isEqualTo(jobEs.getJobDescription());
		 * assertThat(testJob.getSalary()).isEqualTo(jobEs.getSalary());
		 * assertThat(testJob.getJobStatus()).isEqualTo(jobEs.getJobStatus());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.isCanEdit()).isEqualTo(jobEs.isCanEdit());
		 * assertThat(testJob.isHasBeenEdited()).isEqualTo(jobEs.isHasBeenEdited());
		 * assertThat(testJob.isEverActive()).isEqualTo(jobEs.isEverActive());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.getUpdatedBy()).isEqualTo(jobEs.getUpdatedBy());
		 */
		// Validate history data
		// should have history with the draft job in
		List<JobHistory> testJobHistories = jobHistoryRepository.findByJobOrderByIdDesc(testJob);
		JobHistory testJobHistory1 = testJobHistories.get(0);
		JobHistory testJobHistory2 = testJobHistories.get(1);

		assertThat(testJobHistory1.getJobTitle()).isEqualTo(prevJob.getJobTitle());
		assertThat(testJobHistory1.getJobDescription()).isEqualTo(prevJob.getJobDescription());
		assertThat(testJobHistory1.getSalary()).isEqualTo(prevJob.getSalary());
		assertThat(testJobHistory1.getJobStatus()).isEqualTo(prevJob.getJobStatus());
		assertThat(testJobHistory1.getCreatedBy()).isEqualTo(prevJob.getCreatedBy());
		assertThat(testJobHistory1.isCanEdit()).isEqualTo(prevJob.isCanEdit());
		assertThat(testJobHistory1.isHasBeenEdited()).isEqualTo(prevJob.isHasBeenEdited());
		assertThat(testJobHistory1.isEverActive()).isEqualTo(prevJob.isEverActive());
		assertThat(testJobHistory1.getCreatedBy()).isEqualTo(prevJob.getCreatedBy());
		assertThat(testJobHistory1.getUpdatedBy()).isEqualTo(prevJob.getUpdatedBy());

		assertThat(testJobHistory2.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
		assertThat(testJobHistory2.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
		assertThat(testJobHistory2.getSalary()).isEqualTo(DEFAULT_SALARY);
		assertThat(testJobHistory2.getJobStatus()).isEqualTo(DRAFT_JOB_STATUS);
		assertThat(testJobHistory2.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJobHistory2.isCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJobHistory2.isHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJobHistory2.isEverActive()).isEqualTo(UPDATED_EVER_ACTIVE);
		assertThat(testJobHistory2.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);

		// Validate Filters
		// should have filter data
		JobFilter testJobFilter = jobFilterRepository.findByJob(testJob);
		assertThat(testJobFilter.getFilterDescription()).isEqualTo(BASIC_FILTER);
		assertThat(testJobFilter.getJob()).isEqualTo(testJob);

	}

	@Test
	@Transactional
	public void updateDraftJobToActiveByOnlyChangingJobNotFilters() throws Exception {
		// create a job from output of updateActiveJobOnlyToDraft()
		// Update Job Only save as Active
		// Should have basic Filter and History from above transitions
		// After update
		// Job must move to Active
		// History should have previous draftrecord
		// no chnage in filter histiry
		// hasBeenEdited should be true
		// canEdit must be true
		// everctive is true
		Job job = new Job().jobTitle(UPDATED_JOB_TITLE).jobDescription(UPDATED_JOB_DESCRIPTION).salary(UPDATED_SALARY)
				.jobStatus(DRAFT_JOB_STATUS).hasBeenEdited(DEFAULT_HAS_BEEN_EDITED).everActive(UPDATED_EVER_ACTIVE)
				.canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY).updatedBy(UPDATED_UPDATED_BY).employmentType(employmentType).jobType(jobType);
		JobFilter jobFilter = new JobFilter();
		jobFilter.setFilterDescription(BASIC_FILTER);
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		jobFilters.add(jobFilter);
		jobFilter.job(job);
		job.setJobFilters(jobFilters);
		JobHistory jobHistory1 = new JobHistory().jobTitle(DEFAULT_JOB_TITLE).jobDescription(DEFAULT_JOB_DESCRIPTION)
				.salary(DEFAULT_SALARY).jobStatus(DRAFT_JOB_STATUS).hasBeenEdited(DEFAULT_HAS_BEEN_EDITED)
				.everActive(DEFAULT_EVER_ACTIVE).canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY)
				.updatedBy(DEFAULT_UPDATED_BY).job(job);
		JobHistory jobHistory2 = new JobHistory().jobTitle(UPDATED_JOB_DESCRIPTION)
				.jobDescription(UPDATED_JOB_DESCRIPTION).salary(UPDATED_SALARY).jobStatus(ACTIVE_JOB_STATUS)
				.hasBeenEdited(DEFAULT_HAS_BEEN_EDITED).everActive(UPDATED_EVER_ACTIVE).canEdit(DEFAULT_CAN_EDIT)
				.createdBy(UPDATED_CREATED_BY).updatedBy(UPDATED_UPDATED_BY).job(job);
		Set<JobHistory> jobHistories = new HashSet<JobHistory>();
		jobHistories.add(jobHistory1);
		jobHistories.add(jobHistory2);
		job.setHistories(jobHistories);
		jobRepository.saveAndFlush(job);
		// jobSearchRepository.save(job);
		int jobDatabaseSizeBeforeUpdate = jobRepository.findAll().size();
		int jobHistoryDatabaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();
		int jobFiltersSizeBeforeUpdate = jobFilterRepository.findAll().size();
		int jobFilterHistorySizeBeforeUpdate = jobFilterHistoryRepository.findAll().size();
		Job initializedJob = jobRepository.findOne(job.getId());
		Job prevJob = new Job();
		BeanUtils.copyProperties(prevJob, initializedJob);
		// to be updated
		Job toBeUpdated = new Job();
		toBeUpdated.setId(initializedJob.getId());
		toBeUpdated.jobTitle(UPDATED_JOB_TITLE).jobDescription(UPDATED_JOB_DESCRIPTION).salary(DEFAULT_SALARY)
				.jobStatus(ACTIVE_JOB_STATUS).hasBeenEdited(DEFAULT_HAS_BEEN_EDITED).everActive(UPDATED_EVER_ACTIVE)
				.canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY).updatedBy(UPDATED_UPDATED_BY);
		toBeUpdated.setJobFilters(jobFilters);
		// Execute Update
		restJobMockMvc.perform(put("/api/jobs").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(toBeUpdated))).andExpect(status().isOk());

		List<Job> jobList = jobRepository.findAll();
		List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
		List<JobFilter> jobFilterList = jobFilterRepository.findAll();
		List<JobFilterHistory> jobFilterHistoryList = jobFilterHistoryRepository.findAll();
		assertThat(jobList).hasSize(jobDatabaseSizeBeforeUpdate);
		assertThat(jobHistoryList).hasSize(jobHistoryDatabaseSizeBeforeUpdate + 1);
		assertThat(jobHistoryList).hasSize(3);
		assertThat(jobHistoryList.get(2).getEmploymentType()).isEqualTo(employmentType);
		assertThat(jobHistoryList.get(2).getJobType()).isEqualTo(jobType);
	
		assertThat(jobFilterList).hasSize(jobFiltersSizeBeforeUpdate);
		assertThat(jobFilterList).hasSize(1);
		// should not have any filter history
		assertThat(jobFilterHistoryList).hasSize(jobFilterHistorySizeBeforeUpdate);
		assertThat(jobFilterHistoryList).hasSize(0);
		// jobHistoryList.forEach(action-> System.out.println(action.getJob()));
		Job testJob = jobList.get(jobList.size() - 1);
		// hasBeenEdited must be false
		// canEdit must be true
		// everActive must be true
		// should move job to active
		assertThat(testJob.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
		assertThat(testJob.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
		assertThat(testJob.getSalary()).isEqualTo(DEFAULT_SALARY);
		assertThat(testJob.getJobStatus()).isEqualTo(ACTIVE_JOB_STATUS);
		assertThat(testJob.isCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJob.isHasBeenEdited()).isEqualTo(UPDATED_HAS_BEEN_EDITED);
		assertThat(testJob.isEverActive()).isEqualTo(UPDATED_EVER_ACTIVE);
		assertThat(testJob.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		// validate in ES
		/*
		 * Job jobEs = jobSearchRepository.findOne(testJob.getId());
		 * assertThat(testJob.getJobTitle()).isEqualTo(jobEs.getJobTitle());
		 * assertThat(testJob.getJobDescription()).isEqualTo(jobEs.getJobDescription());
		 * assertThat(testJob.getSalary()).isEqualTo(jobEs.getSalary());
		 * assertThat(testJob.getJobStatus()).isEqualTo(jobEs.getJobStatus());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.isCanEdit()).isEqualTo(jobEs.isCanEdit());
		 * assertThat(testJob.isHasBeenEdited()).isEqualTo(jobEs.isHasBeenEdited());
		 * assertThat(testJob.isEverActive()).isEqualTo(jobEs.isEverActive());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.getUpdatedBy()).isEqualTo(jobEs.getUpdatedBy());
		 */
		// Validate history data
		// should have history with the draft job in
		List<JobHistory> testJobHistories = jobHistoryRepository.findByJobIdOrderByIdDesc(testJob.getId());
		assertThat(testJobHistories.size()).isEqualTo(3);
		JobHistory testJobHistory1 = testJobHistories.get(0);
		JobHistory testJobHistory2 = testJobHistories.get(1);
		JobHistory testJobHistory3 = testJobHistories.get(2);
		assertThat(testJobHistory1.getJobTitle()).isEqualTo(prevJob.getJobTitle());
		assertThat(testJobHistory1.getJobDescription()).isEqualTo(prevJob.getJobDescription());
		assertThat(testJobHistory1.getSalary()).isEqualTo(prevJob.getSalary());
		assertThat(testJobHistory1.getJobStatus()).isEqualTo(prevJob.getJobStatus());
		assertThat(testJobHistory1.getCreatedBy()).isEqualTo(prevJob.getCreatedBy());
		assertThat(testJobHistory1.isCanEdit()).isEqualTo(prevJob.isCanEdit());
		assertThat(testJobHistory1.isHasBeenEdited()).isEqualTo(prevJob.isHasBeenEdited());
		assertThat(testJobHistory1.isEverActive()).isEqualTo(prevJob.isEverActive());
		assertThat(testJobHistory1.getCreatedBy()).isEqualTo(prevJob.getCreatedBy());
		assertThat(testJobHistory1.getUpdatedBy()).isEqualTo(prevJob.getUpdatedBy());

		assertThat(testJobHistory2.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
		assertThat(testJobHistory2.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
		assertThat(testJobHistory2.getSalary()).isEqualTo(UPDATED_SALARY);
		assertThat(testJobHistory2.getJobStatus()).isEqualTo(ACTIVE_JOB_STATUS);
		assertThat(testJobHistory2.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
		assertThat(testJobHistory2.isCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJobHistory2.isHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJobHistory2.isEverActive()).isEqualTo(UPDATED_EVER_ACTIVE);
		assertThat(testJobHistory2.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);

		assertThat(testJobHistory3.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
		assertThat(testJobHistory3.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
		assertThat(testJobHistory3.getSalary()).isEqualTo(DEFAULT_SALARY);
		assertThat(testJobHistory3.getJobStatus()).isEqualTo(DRAFT_JOB_STATUS);
		assertThat(testJobHistory3.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJobHistory3.isCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJobHistory3.isHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJobHistory3.isEverActive()).isEqualTo(DEFAULT_EVER_ACTIVE);
		assertThat(testJobHistory3.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);

		// Validate Filters
		// should have filter data
		JobFilter testJobFilter = jobFilterRepository.findByJob(testJob);
		assertThat(testJobFilter.getFilterDescription()).isEqualTo(BASIC_FILTER);
	}

	@Test
	@Transactional
	public void updateActiveJobFilterAndNotJob() throws Exception {
		// cretaeJob from output of the previous test case
		// -updateDraftJobToActiveByOnlyChnagingJobNotFilters()
		// update the Filters Only
		// Filter History shud be updated
		// Job History must be updated
		// hasBeenEdited must be true
		// canEdit must be False
		// everActive must be true
		Job job = new Job().jobTitle(UPDATED_JOB_TITLE).jobDescription(UPDATED_JOB_DESCRIPTION).salary(UPDATED_SALARY)
				.jobStatus(ACTIVE_JOB_STATUS).hasBeenEdited(UPDATED_HAS_BEEN_EDITED).everActive(UPDATED_EVER_ACTIVE)
				.canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY).updatedBy(UPDATED_UPDATED_BY);
		;
		JobFilter jobFilter = new JobFilter();
		jobFilter.setFilterDescription(BASIC_FILTER);
		jobFilter.job(job);
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		jobFilters.add(jobFilter);
		job.setJobFilters(jobFilters);
		JobHistory jobHistory1 = new JobHistory().jobTitle(DEFAULT_JOB_TITLE).jobDescription(DEFAULT_JOB_DESCRIPTION)
				.salary(DEFAULT_SALARY).jobStatus(DRAFT_JOB_STATUS).hasBeenEdited(DEFAULT_HAS_BEEN_EDITED)
				.everActive(DEFAULT_EVER_ACTIVE).canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY)
				.updatedBy(DEFAULT_UPDATED_BY).job(job);
		JobHistory jobHistory2 = new JobHistory().jobTitle(UPDATED_JOB_DESCRIPTION)
				.jobDescription(UPDATED_JOB_DESCRIPTION).salary(UPDATED_SALARY).jobStatus(ACTIVE_JOB_STATUS)
				.hasBeenEdited(DEFAULT_HAS_BEEN_EDITED).everActive(UPDATED_EVER_ACTIVE).canEdit(DEFAULT_CAN_EDIT)
				.createdBy(UPDATED_CREATED_BY).updatedBy(UPDATED_UPDATED_BY).job(job);
		Set<JobHistory> jobHistories = new HashSet<JobHistory>();
		jobHistories.add(jobHistory1);
		jobHistories.add(jobHistory2);
		job.setHistories(jobHistories);
		jobRepository.saveAndFlush(job);
		// jobSearchRepository.save(job);
		int jobDatabaseSizeBeforeUpdate = jobRepository.findAll().size();
		int jobHistoryDatabaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();
		int jobFiltersSizeBeforeUpdate = jobFilterRepository.findAll().size();
		int jobFilterHistorySizeBeforeUpdate = jobFilterHistoryRepository.findAll().size();
		Job initializedJob = jobRepository.findOne(job.getId());
		Job prevJob = new Job();
		BeanUtils.copyProperties(prevJob, initializedJob);
		JobFilter filter = jobFilterRepository.findOne(jobFilter.getId());
		JobFilter prevJobFilter = new JobFilter();
		BeanUtils.copyProperties(prevJobFilter, filter);
		// to be updated
		JobFilter toBeUpdatedFilter = new JobFilter();
		toBeUpdatedFilter.setFilterDescription(UPDATED_FILTER);
		toBeUpdatedFilter.setId(filter.getId());
		Job toBeUpdated = new Job();
		toBeUpdated.setId(initializedJob.getId());
		toBeUpdated.jobTitle(UPDATED_JOB_TITLE).jobDescription(UPDATED_JOB_DESCRIPTION).salary(UPDATED_SALARY)
				.jobStatus(ACTIVE_JOB_STATUS).hasBeenEdited(UPDATED_HAS_BEEN_EDITED).everActive(UPDATED_EVER_ACTIVE)
				.canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY).updatedBy(UPDATED_UPDATED_BY);
		Set<JobFilter> toBeUpdatedFilterSet = new HashSet<JobFilter>();
		toBeUpdatedFilterSet.add(toBeUpdatedFilter.job(toBeUpdated));
		toBeUpdated.setJobFilters(toBeUpdatedFilterSet);
		// Execute Update
		restJobMockMvc.perform(put("/api/jobs").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(toBeUpdated))).andExpect(status().isOk());

		List<Job> jobList = jobRepository.findAll();
		List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
		List<JobFilter> jobFilterList = jobFilterRepository.findAll();
		List<JobFilterHistory> jobFilterHistoryList = jobFilterHistoryRepository.findAll();
		assertThat(jobList).hasSize(jobDatabaseSizeBeforeUpdate);
		assertThat(jobHistoryList).hasSize(jobHistoryDatabaseSizeBeforeUpdate + 1);
		assertThat(jobHistoryList).hasSize(3);
		assertThat(jobFilterList).hasSize(jobFiltersSizeBeforeUpdate);
		assertThat(jobFilterList).hasSize(1);
		// should have filter history
		assertThat(jobFilterHistoryList).hasSize(jobFilterHistorySizeBeforeUpdate + 1);
		assertThat(jobFilterHistoryList).hasSize(1);

		Job testJob = jobList.get(jobList.size() - 1);
		// hasBeenEdited must be false
		// canEdit must be true
		// everActive must be true
		// should move job to active
		assertThat(testJob.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
		assertThat(testJob.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
		assertThat(testJob.getSalary()).isEqualTo(UPDATED_SALARY);
		assertThat(testJob.getJobStatus()).isEqualTo(ACTIVE_JOB_STATUS);
		assertThat(testJob.isCanEdit()).isEqualTo(UPDATED_CAN_EDIT);
		assertThat(testJob.isHasBeenEdited()).isEqualTo(UPDATED_HAS_BEEN_EDITED);
		assertThat(testJob.isEverActive()).isEqualTo(UPDATED_EVER_ACTIVE);
		assertThat(testJob.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		// validate in ES
		/*
		 * Job jobEs = jobSearchRepository.findOne(testJob.getId());
		 * assertThat(testJob.getJobTitle()).isEqualTo(jobEs.getJobTitle());
		 * assertThat(testJob.getJobDescription()).isEqualTo(jobEs.getJobDescription());
		 * assertThat(testJob.getSalary()).isEqualTo(jobEs.getSalary());
		 * assertThat(testJob.getJobStatus()).isEqualTo(jobEs.getJobStatus());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.isCanEdit()).isEqualTo(jobEs.isCanEdit());
		 * assertThat(testJob.isHasBeenEdited()).isEqualTo(jobEs.isHasBeenEdited());
		 * assertThat(testJob.isEverActive()).isEqualTo(jobEs.isEverActive());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.getUpdatedBy()).isEqualTo(jobEs.getUpdatedBy());
		 */
		// Validate history data
		// should have history with the draft job in
		List<JobHistory> testJobHistories = jobHistoryRepository.findByJobOrderByIdDesc(testJob);
		JobHistory testJobHistory1 = testJobHistories.get(0);
		JobHistory testJobHistory2 = testJobHistories.get(1);
		JobHistory testJobHistory3 = testJobHistories.get(2);
		assertThat(testJobHistory1.getJobTitle()).isEqualTo(prevJob.getJobTitle());
		assertThat(testJobHistory1.getJobDescription()).isEqualTo(prevJob.getJobDescription());
		assertThat(testJobHistory1.getSalary()).isEqualTo(prevJob.getSalary());
		assertThat(testJobHistory1.getJobStatus()).isEqualTo(prevJob.getJobStatus());
		assertThat(testJobHistory1.getCreatedBy()).isEqualTo(prevJob.getCreatedBy());
		assertThat(testJobHistory1.isCanEdit()).isEqualTo(prevJob.isCanEdit());
		assertThat(testJobHistory1.isHasBeenEdited()).isEqualTo(prevJob.isHasBeenEdited());
		assertThat(testJobHistory1.isEverActive()).isEqualTo(prevJob.isEverActive());
		assertThat(testJobHistory1.getCreatedBy()).isEqualTo(prevJob.getCreatedBy());
		assertThat(testJobHistory1.getUpdatedBy()).isEqualTo(prevJob.getUpdatedBy());

		assertThat(testJobHistory2.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
		assertThat(testJobHistory2.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
		assertThat(testJobHistory2.getSalary()).isEqualTo(UPDATED_SALARY);
		assertThat(testJobHistory2.getJobStatus()).isEqualTo(ACTIVE_JOB_STATUS);
		assertThat(testJobHistory2.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
		assertThat(testJobHistory2.isCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJobHistory2.isHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJobHistory2.isEverActive()).isEqualTo(UPDATED_EVER_ACTIVE);
		assertThat(testJobHistory2.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);

		assertThat(testJobHistory3.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
		assertThat(testJobHistory3.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
		assertThat(testJobHistory3.getSalary()).isEqualTo(DEFAULT_SALARY);
		assertThat(testJobHistory3.getJobStatus()).isEqualTo(DRAFT_JOB_STATUS);
		assertThat(testJobHistory3.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJobHistory3.isCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJobHistory3.isHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJobHistory3.isEverActive()).isEqualTo(DEFAULT_EVER_ACTIVE);
		assertThat(testJobHistory3.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);

		// Validate Filters
		// should have filter data

		JobFilter testJobFilter = jobFilterRepository.findByJob(testJob);
		assertThat(testJobFilter.getFilterDescription()).isEqualTo(UPDATED_FILTER);
		assertThat(testJobFilter.getJob()).isEqualTo(testJob);

		List<JobFilterHistory> testJobfilterHistories = jobFilterHistoryRepository.findAll();
		JobFilterHistory jobFilterHistory = testJobfilterHistories.get(testJobfilterHistories.size() - 1);
		assertThat(jobFilterHistory.getFilterDescription()).isEqualTo(prevJobFilter.getFilterDescription());
		assertThat(jobFilterHistory.getJobFilter()).isEqualTo(testJobFilter);
	}

	@Test
	@Transactional
	public void updateJobFilterAndJob() throws Exception {
		// cretaeJob from output of the previous test case
		// -updateDraftJobToActiveByOnlyChnagingJobNotFilters()
		// update the Filters Only
		// Filter History shud be updated
		// Job History must be updated
		// hasBeenEdited must be true
		// canEdit must be False
		// everActive must be true

		Job job = new Job().jobTitle(DEFAULT_JOB_TITLE).jobDescription(UPDATED_JOB_DESCRIPTION).salary(UPDATED_SALARY)
				.jobStatus(ACTIVE_JOB_STATUS).hasBeenEdited(UPDATED_HAS_BEEN_EDITED).everActive(UPDATED_EVER_ACTIVE)
				.canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY).updatedBy(UPDATED_UPDATED_BY);
		;
		JobFilter jobFilter = new JobFilter();
		jobFilter.setFilterDescription(DEFAULT_FILTER);
		jobFilter.job(job);
		Set<JobFilter> jobFilters = new HashSet<JobFilter>();
		jobFilters.add(jobFilter);
		JobFilterHistory jobFilterHistory = new JobFilterHistory();
		jobFilterHistory.setFilterDescription(BASIC_FILTER);
		jobFilterHistory.jobFilter(jobFilter);
		jobFilter.addHistory(jobFilterHistory);
		job.setJobFilters(jobFilters);
		JobHistory jobHistory1 = new JobHistory().jobTitle(DEFAULT_JOB_TITLE).jobDescription(DEFAULT_JOB_DESCRIPTION)
				.salary(DEFAULT_SALARY).jobStatus(DRAFT_JOB_STATUS).hasBeenEdited(DEFAULT_HAS_BEEN_EDITED)
				.everActive(DEFAULT_EVER_ACTIVE).canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY)
				.updatedBy(DEFAULT_UPDATED_BY).job(job);
		JobHistory jobHistory2 = new JobHistory().jobTitle(UPDATED_JOB_DESCRIPTION)
				.jobDescription(UPDATED_JOB_DESCRIPTION).salary(UPDATED_SALARY).jobStatus(ACTIVE_JOB_STATUS)
				.hasBeenEdited(DEFAULT_HAS_BEEN_EDITED).everActive(UPDATED_EVER_ACTIVE).canEdit(DEFAULT_CAN_EDIT)
				.createdBy(UPDATED_CREATED_BY).updatedBy(UPDATED_UPDATED_BY).job(job);
		Set<JobHistory> jobHistories = new HashSet<JobHistory>();
		jobHistories.add(jobHistory1);
		jobHistories.add(jobHistory2);
		job.setHistories(jobHistories);
		jobRepository.saveAndFlush(job);
		// jobSearchRepository.save(job);
		int jobDatabaseSizeBeforeUpdate = jobRepository.findAll().size();
		int jobHistoryDatabaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();
		int jobFiltersSizeBeforeUpdate = jobFilterRepository.findAll().size();
		int jobFilterHistorySizeBeforeUpdate = jobFilterHistoryRepository.findAll().size();
		Job initializedJob = jobRepository.findOne(job.getId());
		Job prevJob = new Job();
		BeanUtils.copyProperties(prevJob, initializedJob);
		JobFilter filter = jobFilterRepository.findOne(jobFilter.getId());
		JobFilter prevJobFilter = new JobFilter();
		BeanUtils.copyProperties(prevJobFilter, filter);
		// to be updated
		JobFilter toBeUpdatedFilter = new JobFilter();
		toBeUpdatedFilter.setFilterDescription(UPDATED_FILTER);
		toBeUpdatedFilter.setId(filter.getId());
		Job toBeUpdated = new Job();
		toBeUpdated.setId(initializedJob.getId());
		toBeUpdated.jobTitle(UPDATED_JOB_TITLE).jobDescription(UPDATED_JOB_DESCRIPTION).salary(UPDATED_SALARY)
				.jobStatus(ACTIVE_JOB_STATUS).hasBeenEdited(UPDATED_HAS_BEEN_EDITED).everActive(UPDATED_EVER_ACTIVE)
				.canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY).updatedBy(UPDATED_UPDATED_BY);
		Set<JobFilter> toBeUpdatedFilterSet = new HashSet<JobFilter>();
		toBeUpdatedFilterSet.add(toBeUpdatedFilter.job(toBeUpdated));
		toBeUpdated.setJobFilters(toBeUpdatedFilterSet);
		// Execute Update
		ResultActions sction = restJobMockMvc.perform(put("/api/jobs").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(toBeUpdated))).andExpect(status().isOk());
		System.out.println("++++++++++++++++++" + sction.andReturn().getResponse().getContentAsString());
		List<Job> jobList = jobRepository.findAll();
		List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
		List<JobFilter> jobFilterList = jobFilterRepository.findAll();
		List<JobFilterHistory> jobFilterHistoryList = jobFilterHistoryRepository.findAll();
		assertThat(jobList).hasSize(jobDatabaseSizeBeforeUpdate);
		assertThat(jobHistoryList).hasSize(jobHistoryDatabaseSizeBeforeUpdate + 1);
		assertThat(jobHistoryList).hasSize(3);
		assertThat(jobFilterList).hasSize(jobFiltersSizeBeforeUpdate);
		assertThat(jobFilterList).hasSize(1);
		// should have filter history
		assertThat(jobFilterHistoryList).hasSize(jobFilterHistorySizeBeforeUpdate + 1);
		assertThat(jobFilterHistoryList).hasSize(2);

		Job testJob = jobList.get(jobList.size() - 1);
		// hasBeenEdited must be false
		// canEdit must be true
		// everActive must be true
		// should move job to active
		assertThat(testJob.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
		assertThat(testJob.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
		assertThat(testJob.getSalary()).isEqualTo(UPDATED_SALARY);
		assertThat(testJob.getJobStatus()).isEqualTo(ACTIVE_JOB_STATUS);
		assertThat(testJob.isCanEdit()).isEqualTo(UPDATED_CAN_EDIT);
		assertThat(testJob.isHasBeenEdited()).isEqualTo(UPDATED_HAS_BEEN_EDITED);
		assertThat(testJob.isEverActive()).isEqualTo(UPDATED_EVER_ACTIVE);
		assertThat(testJob.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJob.getJobFilters()).hasSize(1);
		// validate in ES
		/*
		 * Job jobEs = jobSearchRepository.findOne(testJob.getId());
		 * assertThat(testJob.getJobTitle()).isEqualTo(jobEs.getJobTitle());
		 * assertThat(testJob.getJobDescription()).isEqualTo(jobEs.getJobDescription());
		 * assertThat(testJob.getSalary()).isEqualTo(jobEs.getSalary());
		 * assertThat(testJob.getJobStatus()).isEqualTo(jobEs.getJobStatus());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.isCanEdit()).isEqualTo(jobEs.isCanEdit());
		 * assertThat(testJob.isHasBeenEdited()).isEqualTo(jobEs.isHasBeenEdited());
		 * assertThat(testJob.isEverActive()).isEqualTo(jobEs.isEverActive());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.getUpdatedBy()).isEqualTo(jobEs.getUpdatedBy());
		 */
		// Validate history data
		// should have history with the draft job in
		List<JobHistory> testJobHistories = jobHistoryRepository.findByJobOrderByIdDesc(testJob);
		JobHistory testJobHistory1 = testJobHistories.get(0);
		JobHistory testJobHistory2 = testJobHistories.get(1);
		JobHistory testJobHistory3 = testJobHistories.get(2);
		assertThat(testJobHistory1.getJobTitle()).isEqualTo(prevJob.getJobTitle());
		assertThat(testJobHistory1.getJobDescription()).isEqualTo(prevJob.getJobDescription());
		assertThat(testJobHistory1.getSalary()).isEqualTo(prevJob.getSalary());
		assertThat(testJobHistory1.getJobStatus()).isEqualTo(prevJob.getJobStatus());
		assertThat(testJobHistory1.getCreatedBy()).isEqualTo(prevJob.getCreatedBy());
		assertThat(testJobHistory1.isCanEdit()).isEqualTo(prevJob.isCanEdit());
		assertThat(testJobHistory1.isHasBeenEdited()).isEqualTo(prevJob.isHasBeenEdited());
		assertThat(testJobHistory1.isEverActive()).isEqualTo(prevJob.isEverActive());
		assertThat(testJobHistory1.getCreatedBy()).isEqualTo(prevJob.getCreatedBy());
		assertThat(testJobHistory1.getUpdatedBy()).isEqualTo(prevJob.getUpdatedBy());

		assertThat(testJobHistory2.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
		assertThat(testJobHistory2.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
		assertThat(testJobHistory2.getSalary()).isEqualTo(UPDATED_SALARY);
		assertThat(testJobHistory2.getJobStatus()).isEqualTo(ACTIVE_JOB_STATUS);
		assertThat(testJobHistory2.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
		assertThat(testJobHistory2.isCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJobHistory2.isHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJobHistory2.isEverActive()).isEqualTo(UPDATED_EVER_ACTIVE);
		assertThat(testJobHistory2.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);

		assertThat(testJobHistory3.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
		assertThat(testJobHistory3.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
		assertThat(testJobHistory3.getSalary()).isEqualTo(DEFAULT_SALARY);
		assertThat(testJobHistory3.getJobStatus()).isEqualTo(DRAFT_JOB_STATUS);
		assertThat(testJobHistory3.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJobHistory3.isCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJobHistory3.isHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJobHistory3.isEverActive()).isEqualTo(DEFAULT_EVER_ACTIVE);
		assertThat(testJobHistory3.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);

		// Validate Filters
		// should have filter data

		JobFilter testJobFilter = jobFilterRepository.findByJob(testJob);
		assertThat(testJobFilter.getFilterDescription()).isEqualTo(UPDATED_FILTER);
		assertThat(testJobFilter.getJob()).isEqualTo(testJob);

		List<JobFilterHistory> testJobfilterHistories = jobFilterHistoryRepository
				.findByJobFilterOrderByIdDesc(testJobFilter);
		JobFilterHistory jbFilterHistory1 = testJobfilterHistories.get(testJobfilterHistories.size() - 1);
		JobFilterHistory jbFilterHistory2 = testJobfilterHistories.get(testJobfilterHistories.size() - 2);
		assertThat(jbFilterHistory1.getFilterDescription()).isEqualTo(BASIC_FILTER);
		assertThat(jbFilterHistory2.getFilterDescription()).isEqualTo(DEFAULT_FILTER);
		assertThat(jbFilterHistory1.getJobFilter()).isEqualTo(testJobFilter);
		assertThat(jbFilterHistory2.getJobFilter()).isEqualTo(testJobFilter);
	}

	@Transactional
	@Test
	public void shouldNotBeAbleToEditJobIfNotEdiatable() throws Exception {
		Job job = new Job().jobTitle(DEFAULT_JOB_TITLE).jobDescription(UPDATED_JOB_DESCRIPTION).salary(UPDATED_SALARY)
				.jobStatus(ACTIVE_JOB_STATUS).hasBeenEdited(UPDATED_HAS_BEEN_EDITED).everActive(UPDATED_EVER_ACTIVE)
				.canEdit(UPDATED_CAN_EDIT).createdBy(DEFAULT_CREATED_BY).updatedBy(UPDATED_UPDATED_BY);
		jobRepository.saveAndFlush(job);
		// jobSearchRepository.save(job);
		Job initializedJob = jobRepository.findOne(job.getId());
		Job toBeUpdated = new Job();
		toBeUpdated.setId(initializedJob.getId());
		toBeUpdated.jobTitle(UPDATED_JOB_TITLE).jobDescription(UPDATED_JOB_DESCRIPTION).salary(UPDATED_SALARY)
				.jobStatus(ACTIVE_JOB_STATUS).hasBeenEdited(UPDATED_HAS_BEEN_EDITED).everActive(UPDATED_EVER_ACTIVE)
				.canEdit(UPDATED_CAN_EDIT).createdBy(DEFAULT_CREATED_BY).updatedBy(UPDATED_UPDATED_BY);

		restJobMockMvc.perform(put("/api/jobs").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(toBeUpdated))).andExpect(status().isBadRequest());
		// assertThat(toBeUpdated.getJobDescription()).isEqualTo("Cannot Edit job
		// anymmore");

	}

	@Test
	@Transactional
	@Ignore
	public void updateDraftTransisitonToDraftJob() throws Exception {

		// Initialize the database
		Job job = createJobDraftNotEditedCanEdit(em);
		
		jobRepository.saveAndFlush(job);
		// jobSearchRepository.save(job);
		int jobDatabaseSizeBeforeUpdate = jobRepository.findAll().size();
		int jobHistoryDatabaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();

		// Update the job
		Job initializedJob = jobRepository.findOne(job.getId());
		// Job prevJob = new Job ();
		// JobsUtil.populateHistories(prevJob, initializedJob);

		Job toBeUpdated = new Job();
		toBeUpdated.jobTitle(UPDATED_JOB_TITLE).jobDescription(UPDATED_JOB_DESCRIPTION).salary(UPDATED_SALARY)
				.jobStatus(DRAFT_JOB_STATUS).hasBeenEdited(DEFAULT_HAS_BEEN_EDITED).everActive(DEFAULT_EVER_ACTIVE)
				.canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY).updatedBy(UPDATED_UPDATED_BY);
		toBeUpdated.setId(initializedJob.getId());
		restJobMockMvc.perform(put("/api/jobs").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(toBeUpdated))).andExpect(status().isOk());

		// Validate Results
		List<Job> jobList = jobRepository.findAll();
		List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
		List<JobFilter> jobFilterList = jobFilterRepository.findAll();
		List<JobFilterHistory> jobFilterHistoryList = jobFilterHistoryRepository.findAll();
		assertThat(jobList).hasSize(jobDatabaseSizeBeforeUpdate);
		assertThat(jobHistoryList).hasSize(jobHistoryDatabaseSizeBeforeUpdate + 1);
		assertThat(jobFilterList).hasSize(0);
		assertThat(jobFilterHistoryList).hasSize(0);

		Job testJob = jobList.get(jobList.size() - 1);
		assertThat(testJob.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
		assertThat(testJob.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
		assertThat(testJob.getSalary()).isEqualTo(UPDATED_SALARY);
		assertThat(testJob.getJobStatus()).isEqualTo(DRAFT_JOB_STATUS);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJob.isCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJob.isHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJob.isEverActive()).isEqualTo(DEFAULT_EVER_ACTIVE);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJob.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);

		// validate in ES
		/*
		 * Job jobEs = jobSearchRepository.findOne(testJob.getId());
		 * assertThat(testJob.getJobTitle()).isEqualTo(jobEs.getJobTitle());
		 * assertThat(testJob.getJobDescription()).isEqualTo(jobEs.getJobDescription());
		 * assertThat(testJob.getSalary()).isEqualTo(jobEs.getSalary());
		 * assertThat(testJob.getJobStatus()).isEqualTo(jobEs.getJobStatus());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.isCanEdit()).isEqualTo(jobEs.isCanEdit());
		 * assertThat(testJob.isHasBeenEdited()).isEqualTo(jobEs.isHasBeenEdited());
		 * assertThat(testJob.isEverActive()).isEqualTo(jobEs.isEverActive());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.getUpdatedBy()).isEqualTo(jobEs.getUpdatedBy());
		 */
		// Validate history data
		List<JobHistory> jobHistories = jobHistoryRepository.findByJob(testJob);
		JobHistory jobHistory = jobHistories.get(jobHistories.size() - 1);

		assertThat(jobHistory.getJobTitle()).isEqualTo(initializedJob.getJobTitle());
		assertThat(jobHistory.getJobDescription()).isEqualTo(initializedJob.getJobDescription());
		assertThat(jobHistory.getSalary()).isEqualTo(initializedJob.getSalary());
		assertThat(jobHistory.getJobStatus()).isEqualTo(initializedJob.getJobStatus());
		assertThat(jobHistory.getCreatedBy()).isEqualTo(initializedJob.getCreatedBy());
		assertThat(jobHistory.isCanEdit()).isEqualTo(initializedJob.isCanEdit());
		assertThat(jobHistory.isHasBeenEdited()).isEqualTo(initializedJob.isHasBeenEdited());
		assertThat(jobHistory.isEverActive()).isEqualTo(initializedJob.isEverActive());
		assertThat(jobHistory.getCreatedBy()).isEqualTo(initializedJob.getCreatedBy());
		assertThat(jobHistory.getUpdatedBy()).isEqualTo(initializedJob.getUpdatedBy());

	}

	@Test
	@Transactional
	public void updateDraftTransisitonToDraftWhereJobIsSame() throws Exception {

		// Initialize the database
		Job job = createJobDraftNotEditedCanEdit(em);
		jobRepository.saveAndFlush(job);
		// jobSearchRepository.save(job);
		int jobDatabaseSizeBeforeUpdate = jobRepository.findAll().size();
		// Update the job
		Job initializedJob = jobRepository.findOne(job.getId());
		Job toBeUpdated = new Job();
		BeanUtils.copyProperties(toBeUpdated, initializedJob);
		Job prevJob = new Job();
		BeanUtils.copyProperties(prevJob, initializedJob);
		toBeUpdated.setId(initializedJob.getId());
		toBeUpdated.setJobTitle(UPDATED_JOB_TITLE);
		restJobMockMvc.perform(put("/api/jobs").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(toBeUpdated))).andExpect(status().isOk());

		// Validate Results
		List<Job> jobList = jobRepository.findAll();
		List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
		List<JobFilter> jobFilterList = jobFilterRepository.findAll();
		List<JobFilterHistory> jobFilterHistoryList = jobFilterHistoryRepository.findAll();
		assertThat(jobList).hasSize(jobDatabaseSizeBeforeUpdate);
		assertThat(jobHistoryList).hasSize(1);
		assertThat(jobFilterList).hasSize(0);
		assertThat(jobFilterHistoryList).hasSize(0);
		Job testJob = jobList.get(jobList.size() - 1);
		assertThat(testJob.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
		assertThat(testJob.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
		assertThat(testJob.getSalary()).isEqualTo(DEFAULT_SALARY);
		assertThat(testJob.getJobStatus()).isEqualTo(DRAFT_JOB_STATUS);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJob.isCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJob.isHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJob.isEverActive()).isEqualTo(DEFAULT_EVER_ACTIVE);
		assertThat(testJob.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);

		// validate in ES
		/*
		 * Job jobEs = jobSearchRepository.findOne(testJob.getId());
		 * assertThat(testJob.getJobTitle()).isEqualTo(jobEs.getJobTitle());
		 * assertThat(testJob.getJobDescription()).isEqualTo(jobEs.getJobDescription());
		 * assertThat(testJob.getSalary()).isEqualTo(jobEs.getSalary());
		 * assertThat(testJob.getJobStatus()).isEqualTo(jobEs.getJobStatus());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.isCanEdit()).isEqualTo(jobEs.isCanEdit());
		 * assertThat(testJob.isHasBeenEdited()).isEqualTo(jobEs.isHasBeenEdited());
		 * assertThat(testJob.isEverActive()).isEqualTo(jobEs.isEverActive());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.getUpdatedBy()).isEqualTo(jobEs.getUpdatedBy());
		 */
		// Validate history data

		List<JobHistory> testJobHistoryList = jobHistoryRepository.findAll();
		assertThat(testJobHistoryList.size()).isEqualTo(1);

		JobHistory testJobHistory = testJobHistoryList.get(0);
		assertThat(testJobHistory.getJobTitle()).isEqualTo(prevJob.getJobTitle());
		assertThat(testJobHistory.getJobDescription()).isEqualTo(prevJob.getJobDescription());
		assertThat(testJobHistory.getJobStatus()).isEqualTo(prevJob.getJobStatus());
		assertThat(testJobHistory.getCanEdit()).isEqualTo(prevJob.getCanEdit());
		assertThat(testJobHistory.getHasBeenEdited()).isEqualTo(prevJob.getHasBeenEdited());
		assertThat(testJobHistory.getEverActive()).isEqualTo(prevJob.getEverActive());
		assertThat(testJobHistory.getCreatedBy()).isEqualTo(prevJob.getCreatedBy());

	}

	@Test
	@Transactional
	@Ignore
	public void updateDraftTransisitonToActiveJob() throws Exception {

		Job job = createJobDraftNotEditedCanEdit(em);
		JobHistory jobHistory = createDraftJobHistory(em);
		jobHistory.job(job);
		Set<JobHistory> histories = new HashSet<JobHistory>();
		histories.add(jobHistory);
		job.setHistories(histories);
		jobRepository.saveAndFlush(job);
		// jobSearchRepository.save(job);
		// jobHistorySearchRepository.save(jobHistory);
		int jobDatabaseSizeBeforeUpdate = jobRepository.findAll().size();
		int jobHistoryDatabaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();
		int jobFilterSizeBeforeUpdate = jobFilterRepository.findAll().size();
		Set<JobFilter> jobFilters = createJobFilter(em);
		// Update the job
		Job initializedJob = jobRepository.findOne(job.getId());

		Job toBeUpdated = new Job();
		toBeUpdated.jobTitle(UPDATED_JOB_TITLE).jobDescription(UPDATED_JOB_DESCRIPTION).salary(UPDATED_SALARY)
				.jobStatus(ACTIVE_JOB_STATUS).hasBeenEdited(DEFAULT_HAS_BEEN_EDITED).everActive(DEFAULT_EVER_ACTIVE)
				.canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY).updatedBy(UPDATED_UPDATED_BY);
		toBeUpdated.setId(initializedJob.getId());
		toBeUpdated.setJobFilters(jobFilters);

		restJobMockMvc.perform(put("/api/jobs").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(toBeUpdated))).andExpect(status().isOk());

		// Validate Results
		List<Job> jobList = jobRepository.findAll();
		List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
		List<JobFilter> jobFilterList = jobFilterRepository.findAll();
		List<JobFilterHistory> jobFilterHistoryList = jobFilterHistoryRepository.findAll();
		assertThat(jobList).hasSize(jobDatabaseSizeBeforeUpdate);
		assertThat(jobHistoryList).hasSize(jobHistoryDatabaseSizeBeforeUpdate + 1);
		assertThat(jobHistoryList).hasSize(2);
		assertThat(jobFilterList).hasSize(jobFilterSizeBeforeUpdate + 1);
		assertThat(jobFilterList).hasSize(1);
		assertThat(jobFilterHistoryList).hasSize(0);
		Job testJob = jobList.get(jobList.size() - 1);
		assertThat(testJob.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
		assertThat(testJob.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
		assertThat(testJob.getSalary()).isEqualTo(UPDATED_SALARY);
		assertThat(testJob.getJobStatus()).isEqualTo(ACTIVE_JOB_STATUS);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJob.isCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJob.isHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJob.isEverActive()).isEqualTo(UPDATED_EVER_ACTIVE);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJob.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);

		JobFilter testJobFilter = jobFilterList.get(jobFilterList.size() - 1);
		assertThat(testJobFilter.getFilterDescription()).isEqualTo(DEFAULT_FILTER);
		System.out.println("Test job filter id is " + testJobFilter);
		JobHistory testJobHistory = jobHistoryRepository.findTopByOrderByIdDesc();
		assertThat(testJobHistory.getJobTitle()).isEqualTo(initializedJob.getJobTitle());
		assertThat(testJobHistory.getJobDescription()).isEqualTo(initializedJob.getJobDescription());
		assertThat(testJobHistory.getSalary()).isEqualTo(initializedJob.getSalary());
		assertThat(testJobHistory.getJobStatus()).isEqualTo(initializedJob.getJobStatus());
		assertThat(testJobHistory.getCreatedBy()).isEqualTo(initializedJob.getCreatedBy());
		assertThat(testJobHistory.isCanEdit()).isEqualTo(initializedJob.isCanEdit());
		assertThat(testJobHistory.isHasBeenEdited()).isEqualTo(initializedJob.isHasBeenEdited());
		assertThat(testJobHistory.isEverActive()).isEqualTo(initializedJob.isEverActive());
		assertThat(testJobHistory.getCreatedBy()).isEqualTo(initializedJob.getCreatedBy());
		assertThat(testJobHistory.getUpdatedBy()).isEqualTo(initializedJob.getUpdatedBy());

		// Validate ES not saving Filter to ES

		/*
		 * Job jobEs = jobSearchRepository.findOne(testJob.getId());
		 * assertThat(testJob.getJobTitle()).isEqualTo(jobEs.getJobTitle());
		 * assertThat(testJob.getJobDescription()).isEqualTo(jobEs.getJobDescription());
		 * assertThat(testJob.getSalary()).isEqualTo(jobEs.getSalary());
		 * assertThat(testJob.getJobStatus()).isEqualTo(jobEs.getJobStatus());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.isCanEdit()).isEqualTo(jobEs.isCanEdit());
		 * assertThat(testJob.isHasBeenEdited()).isEqualTo(jobEs.isHasBeenEdited());
		 * assertThat(testJob.isEverActive()).isEqualTo(jobEs.isEverActive());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.getUpdatedBy()).isEqualTo(jobEs.getUpdatedBy());
		 * JobHistory jobHistoryEs =
		 * jobHistorySearchRepository.findOne(testJobHistory.getId());
		 * assertThat(testJobHistory.getJobTitle()).isEqualTo(jobHistoryEs.getJobTitle()
		 * ); assertThat(testJobHistory.getJobDescription()).isEqualTo(jobHistoryEs.
		 * getJobDescription());
		 * assertThat(testJobHistory.getSalary()).isEqualTo(jobHistoryEs.getSalary());
		 * assertThat(testJobHistory.getJobStatus()).isEqualTo(jobHistoryEs.getJobStatus
		 * ());
		 * assertThat(testJobHistory.getCreatedBy()).isEqualTo(jobHistoryEs.getCreatedBy
		 * ());
		 * assertThat(testJobHistory.isCanEdit()).isEqualTo(jobHistoryEs.isCanEdit());
		 * assertThat(testJobHistory.isHasBeenEdited()).isEqualTo(jobHistoryEs.
		 * isHasBeenEdited());
		 * assertThat(testJobHistory.isEverActive()).isEqualTo(jobHistoryEs.isEverActive
		 * ());
		 * assertThat(testJobHistory.getCreatedBy()).isEqualTo(jobHistoryEs.getCreatedBy
		 * ());
		 * assertThat(testJobHistory.getUpdatedBy()).isEqualTo(jobHistoryEs.getUpdatedBy
		 * ());
		 */

	}

	@Test
	@Transactional
	@Ignore
	public void updateActiveTransisitonToActiveJobSecondAndLastEdit() throws Exception {

		Job job = createJobActiveNotEditedCanEdit(em);
		Set<JobHistory> jobHistories = createActiveJobHistories(em);
		jobHistories.forEach(jobHistory -> jobHistory.job(job));
		job.setHistories(jobHistories);
		jobRepository.saveAndFlush(job);
		// jobSearchRepository.save(job);
		// jobHistorySearchRepository.save(jobHistories);
		int jobDatabaseSizeBeforeUpdate = jobRepository.findAll().size();
		int jobHistoryDatabaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();
		int jobFilterSizeBeforeUpdate = jobFilterRepository.findAll().size();
		int jobFilterHistorySizeBeforeUpdate = jobFilterHistoryRepository.findAll().size();
		Set<JobFilter> jobFilters = createNewJobFilter(em);
		jobFilterRepository.save(jobFilters);
		jobFilterRepository.flush();
		// Update the job
		Job initializedJob = jobRepository.findOne(job.getId());
		// Job prevJob = new Job ();
		// BeanUtils.copyProperties(prevJob, initializedJob);

		Job toBeUpdated = new Job();
		toBeUpdated.jobTitle(UPDATED_JOB_TITLE).jobDescription(UPDATED_JOB_DESCRIPTION).salary(UPDATED_SALARY)
				.jobStatus(ACTIVE_JOB_STATUS).hasBeenEdited(DEFAULT_HAS_BEEN_EDITED).everActive(UPDATED_EVER_ACTIVE)
				.canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY).updatedBy(UPDATED_UPDATED_BY);
		toBeUpdated.setId(initializedJob.getId());
		toBeUpdated.setJobFilters(jobFilters);

		restJobMockMvc.perform(put("/api/jobs").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(toBeUpdated))).andExpect(status().isOk());

		// Validate Results
		List<Job> jobList = jobRepository.findAll();
		List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
		List<JobFilter> jobFilterList = jobFilterRepository.findAll();
		List<JobFilterHistory> jobFilterHistoryList = jobFilterHistoryRepository.findAll();
		assertThat(jobList).hasSize(jobDatabaseSizeBeforeUpdate);
		assertThat(jobHistoryList).hasSize(jobHistoryDatabaseSizeBeforeUpdate + 1);
		assertThat(jobHistoryList).hasSize(4);
		assertThat(jobFilterList).hasSize(jobFilterSizeBeforeUpdate + 1);
		assertThat(jobFilterList).hasSize(1);
		assertThat(jobFilterHistoryList).hasSize(jobFilterHistorySizeBeforeUpdate);
		assertThat(jobFilterHistoryList).hasSize(0);
		Job testJob = jobList.get(jobList.size() - 1);
		assertThat(testJob.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
		assertThat(testJob.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
		assertThat(testJob.getSalary()).isEqualTo(UPDATED_SALARY);
		assertThat(testJob.getJobStatus()).isEqualTo(ACTIVE_JOB_STATUS);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJob.isCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJob.isHasBeenEdited()).isEqualTo(UPDATED_HAS_BEEN_EDITED);
		assertThat(testJob.isEverActive()).isEqualTo(UPDATED_EVER_ACTIVE);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJob.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);

		JobFilter testJobFilter = jobFilterList.get(jobFilterList.size() - 1);
		assertThat(testJobFilter.getFilterDescription()).isEqualTo(UPDATED_FILTER);

		JobHistory testJobHistory = jobHistoryRepository.findTopByOrderByIdDesc();
		assertThat(testJobHistory.getJobTitle()).isEqualTo(initializedJob.getJobTitle());
		assertThat(testJobHistory.getJobDescription()).isEqualTo(initializedJob.getJobDescription());
		assertThat(testJobHistory.getSalary()).isEqualTo(initializedJob.getSalary());
		assertThat(testJobHistory.getJobStatus()).isEqualTo(initializedJob.getJobStatus());
		assertThat(testJobHistory.getCreatedBy()).isEqualTo(initializedJob.getCreatedBy());
		assertThat(testJobHistory.isCanEdit()).isEqualTo(initializedJob.isCanEdit());
		assertThat(testJobHistory.isHasBeenEdited()).isEqualTo(initializedJob.isHasBeenEdited());
		assertThat(testJobHistory.isEverActive()).isEqualTo(initializedJob.isEverActive());
		assertThat(testJobHistory.getCreatedBy()).isEqualTo(initializedJob.getCreatedBy());
		assertThat(testJobHistory.getUpdatedBy()).isEqualTo(initializedJob.getUpdatedBy());

		// Validate ES not saving Filter to ES

		/*
		 * Job jobEs = jobSearchRepository.findOne(testJob.getId());
		 * assertThat(testJob.getJobTitle()).isEqualTo(jobEs.getJobTitle());
		 * assertThat(testJob.getJobDescription()).isEqualTo(jobEs.getJobDescription());
		 * assertThat(testJob.getSalary()).isEqualTo(jobEs.getSalary());
		 * assertThat(testJob.getJobStatus()).isEqualTo(jobEs.getJobStatus());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.isCanEdit()).isEqualTo(jobEs.isCanEdit());
		 * assertThat(testJob.isHasBeenEdited()).isEqualTo(jobEs.isHasBeenEdited());
		 * assertThat(testJob.isEverActive()).isEqualTo(jobEs.isEverActive());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.getUpdatedBy()).isEqualTo(jobEs.getUpdatedBy());
		 * 
		 * JobHistory jobHistoryEs =
		 * jobHistorySearchRepository.findOne(testJobHistory.getId());
		 * assertThat(testJobHistory.getJobTitle()).isEqualTo(jobHistoryEs.getJobTitle()
		 * ); assertThat(testJobHistory.getJobDescription()).isEqualTo(jobHistoryEs.
		 * getJobDescription());
		 * assertThat(testJobHistory.getSalary()).isEqualTo(jobHistoryEs.getSalary());
		 * assertThat(testJobHistory.getJobStatus()).isEqualTo(jobHistoryEs.getJobStatus
		 * ());
		 * assertThat(testJobHistory.getCreatedBy()).isEqualTo(jobHistoryEs.getCreatedBy
		 * ());
		 * assertThat(testJobHistory.isCanEdit()).isEqualTo(jobHistoryEs.isCanEdit());
		 * assertThat(testJobHistory.isHasBeenEdited()).isEqualTo(jobHistoryEs.
		 * isHasBeenEdited());
		 * assertThat(testJobHistory.isEverActive()).isEqualTo(jobHistoryEs.isEverActive
		 * ());
		 * assertThat(testJobHistory.getCreatedBy()).isEqualTo(jobHistoryEs.getCreatedBy
		 * ());
		 * assertThat(testJobHistory.getUpdatedBy()).isEqualTo(jobHistoryEs.getUpdatedBy
		 * ());
		 */

	}

	@Test
	@Transactional
	@Ignore
	public void updateActiveTransisitonToActiveJobFirstEdit() throws Exception {

		Job job = createJobActiveNotEditedCanEdit(em);
		Set<JobHistory> jobHistories = createActiveJobHistories(em);
		jobHistories.forEach(jobHistory -> jobHistory.job(job));
		job.setHistories(jobHistories);
		jobRepository.saveAndFlush(job);
		// jobSearchRepository.save(job);
		// jobHistorySearchRepository.save(jobHistories);
		int jobDatabaseSizeBeforeUpdate = jobRepository.findAll().size();
		int jobHistoryDatabaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();
		int jobFilterSizeBeforeUpdate = jobFilterRepository.findAll().size();
		int jobFilterHistorySizeBeforeUpdate = jobFilterHistoryRepository.findAll().size();
		Set<JobFilter> jobFilters = createNewJobFilter(em);
		jobFilterRepository.save(jobFilters);
		jobFilterRepository.flush();
		// Update the job
		Job initializedJob = jobRepository.findOne(job.getId());

		Job toBeUpdated = new Job();

		toBeUpdated.jobTitle(UPDATED_JOB_TITLE).jobDescription(UPDATED_JOB_DESCRIPTION).salary(UPDATED_SALARY)
				.jobStatus(ACTIVE_JOB_STATUS).hasBeenEdited(DEFAULT_HAS_BEEN_EDITED).everActive(UPDATED_EVER_ACTIVE)
				.canEdit(DEFAULT_CAN_EDIT).createdBy(DEFAULT_CREATED_BY).updatedBy(UPDATED_UPDATED_BY);
		toBeUpdated.setId(initializedJob.getId());
		toBeUpdated.setJobFilters(jobFilters);

		restJobMockMvc.perform(put("/api/jobs").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(toBeUpdated))).andExpect(status().isOk());

		// Validate Results
		List<Job> jobList = jobRepository.findAll();
		List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
		List<JobFilter> jobFilterList = jobFilterRepository.findAll();
		List<JobFilterHistory> jobFilterHistoryList = jobFilterHistoryRepository.findAll();
		assertThat(jobList).hasSize(jobDatabaseSizeBeforeUpdate);
		assertThat(jobHistoryList).hasSize(jobHistoryDatabaseSizeBeforeUpdate + 1);
		assertThat(jobHistoryList).hasSize(4);
		assertThat(jobFilterList).hasSize(jobFilterSizeBeforeUpdate + 1);
		assertThat(jobFilterList).hasSize(1);
		assertThat(jobFilterHistoryList).hasSize(jobFilterHistorySizeBeforeUpdate);
		assertThat(jobFilterHistoryList).hasSize(0);
		Job testJob = jobList.get(jobList.size() - 1);
		assertThat(testJob.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
		assertThat(testJob.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
		assertThat(testJob.getSalary()).isEqualTo(UPDATED_SALARY);
		assertThat(testJob.getJobStatus()).isEqualTo(ACTIVE_JOB_STATUS);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJob.isCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJob.isHasBeenEdited()).isEqualTo(UPDATED_HAS_BEEN_EDITED);
		assertThat(testJob.isEverActive()).isEqualTo(UPDATED_EVER_ACTIVE);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJob.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);

		JobFilter testJobFilter = jobFilterList.get(jobFilterList.size() - 1);
		assertThat(testJobFilter.getFilterDescription()).isEqualTo(UPDATED_FILTER);

		JobHistory testJobHistory = jobHistoryRepository.findTopByOrderByIdDesc();
		assertThat(testJobHistory.getJobTitle()).isEqualTo(initializedJob.getJobTitle());
		assertThat(testJobHistory.getJobDescription()).isEqualTo(initializedJob.getJobDescription());
		assertThat(testJobHistory.getSalary()).isEqualTo(initializedJob.getSalary());
		assertThat(testJobHistory.getJobStatus()).isEqualTo(initializedJob.getJobStatus());
		assertThat(testJobHistory.getCreatedBy()).isEqualTo(initializedJob.getCreatedBy());
		assertThat(testJobHistory.isCanEdit()).isEqualTo(initializedJob.isCanEdit());
		assertThat(testJobHistory.isHasBeenEdited()).isEqualTo(initializedJob.isHasBeenEdited());
		assertThat(testJobHistory.isEverActive()).isEqualTo(initializedJob.isEverActive());
		assertThat(testJobHistory.getCreatedBy()).isEqualTo(initializedJob.getCreatedBy());
		assertThat(testJobHistory.getUpdatedBy()).isEqualTo(initializedJob.getUpdatedBy());

		// Validate ES not saving Filter to ES

		/*
		 * Job jobEs = jobSearchRepository.findOne(testJob.getId());
		 * assertThat(testJob.getJobTitle()).isEqualTo(jobEs.getJobTitle());
		 * assertThat(testJob.getJobDescription()).isEqualTo(jobEs.getJobDescription());
		 * assertThat(testJob.getSalary()).isEqualTo(jobEs.getSalary());
		 * assertThat(testJob.getJobStatus()).isEqualTo(jobEs.getJobStatus());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.isCanEdit()).isEqualTo(jobEs.isCanEdit());
		 * assertThat(testJob.isHasBeenEdited()).isEqualTo(jobEs.isHasBeenEdited());
		 * assertThat(testJob.isEverActive()).isEqualTo(jobEs.isEverActive());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.getUpdatedBy()).isEqualTo(jobEs.getUpdatedBy());
		 * 
		 * JobHistory jobHistoryEs =
		 * jobHistorySearchRepository.findOne(testJobHistory.getId());
		 * assertThat(testJobHistory.getJobTitle()).isEqualTo(jobHistoryEs.getJobTitle()
		 * ); assertThat(testJobHistory.getJobDescription()).isEqualTo(jobHistoryEs.
		 * getJobDescription());
		 * assertThat(testJobHistory.getSalary()).isEqualTo(jobHistoryEs.getSalary());
		 * assertThat(testJobHistory.getJobStatus()).isEqualTo(jobHistoryEs.getJobStatus
		 * ());
		 * assertThat(testJobHistory.getCreatedBy()).isEqualTo(jobHistoryEs.getCreatedBy
		 * ());
		 * assertThat(testJobHistory.isCanEdit()).isEqualTo(jobHistoryEs.isCanEdit());
		 * assertThat(testJobHistory.isHasBeenEdited()).isEqualTo(jobHistoryEs.
		 * isHasBeenEdited());
		 * assertThat(testJobHistory.isEverActive()).isEqualTo(jobHistoryEs.isEverActive
		 * ());
		 * assertThat(testJobHistory.getCreatedBy()).isEqualTo(jobHistoryEs.getCreatedBy
		 * ());
		 * assertThat(testJobHistory.getUpdatedBy()).isEqualTo(jobHistoryEs.getUpdatedBy
		 * ());
		 */

	}

	@Test
	@Transactional
	public void updateActiveTransisitonToArchive() throws Exception {

		Job job = createJobActiveNotEditedCanEdit(em);
		Set<JobHistory> jobHistories = createActiveJobHistories(em);
		jobHistories.forEach(jobHistory -> jobHistory.job(job));
		job.setHistories(jobHistories);
		jobRepository.saveAndFlush(job);
		// jobSearchRepository.save(job);
		// jobHistorySearchRepository.save(jobHistories);
		int jobDatabaseSizeBeforeUpdate = jobRepository.findAll().size();
		int jobHistoryDatabaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();

		// Update the job
		Job initializedJob = jobRepository.findOne(job.getId());
		Job prevJob = new Job();
		BeanUtils.copyProperties(prevJob, initializedJob);

		restJobMockMvc.perform(delete("/api/deActivateJob/{id}", job.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate Results
		List<Job> jobList = jobRepository.findAll();
		List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();

		assertThat(jobList).hasSize(jobDatabaseSizeBeforeUpdate);
		assertThat(jobHistoryList).hasSize(jobHistoryDatabaseSizeBeforeUpdate + 1);
		assertThat(jobHistoryList).hasSize(4);

		Job testJob = jobList.get(jobList.size() - 1);
		assertThat(testJob.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
		assertThat(testJob.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
		assertThat(testJob.getJobStatus()).isEqualTo(ARCHIVE_JOB_STATUS);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJob.isCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJob.isHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJob.isEverActive()).isEqualTo(UPDATED_EVER_ACTIVE);
		assertThat(testJob.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);

		JobHistory testJobHistory = jobHistoryRepository.findTopByOrderByIdDesc();
		assertThat(testJobHistory.getJobTitle()).isEqualTo(prevJob.getJobTitle());
		assertThat(testJobHistory.getJobDescription()).isEqualTo(prevJob.getJobDescription());
		assertThat(testJobHistory.getSalary()).isEqualTo(prevJob.getSalary());
		assertThat(testJobHistory.getJobStatus()).isEqualTo(prevJob.getJobStatus());
		assertThat(testJobHistory.getCreatedBy()).isEqualTo(prevJob.getCreatedBy());
		assertThat(testJobHistory.isCanEdit()).isEqualTo(prevJob.isCanEdit());
		assertThat(testJobHistory.isHasBeenEdited()).isEqualTo(prevJob.isHasBeenEdited());
		assertThat(testJobHistory.isEverActive()).isEqualTo(prevJob.isEverActive());
		assertThat(testJobHistory.getCreatedBy()).isEqualTo(prevJob.getCreatedBy());
		assertThat(testJobHistory.getUpdatedBy()).isEqualTo(prevJob.getUpdatedBy());

		// Validate ES not saving Filter to ES

		/*
		 * Job jobEs = jobSearchRepository.findOne(testJob.getId());
		 * assertThat(testJob.getJobTitle()).isEqualTo(jobEs.getJobTitle());
		 * assertThat(testJob.getJobDescription()).isEqualTo(jobEs.getJobDescription());
		 * assertThat(testJob.getSalary()).isEqualTo(jobEs.getSalary());
		 * assertThat(testJob.getJobStatus()).isEqualTo(jobEs.getJobStatus());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.isCanEdit()).isEqualTo(jobEs.isCanEdit());
		 * assertThat(testJob.isHasBeenEdited()).isEqualTo(jobEs.isHasBeenEdited());
		 * assertThat(testJob.isEverActive()).isEqualTo(jobEs.isEverActive());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.getUpdatedBy()).isEqualTo(jobEs.getUpdatedBy());
		 * 
		 * JobHistory jobHistoryEs =
		 * jobHistorySearchRepository.findOne(testJobHistory.getId());
		 * assertThat(testJobHistory.getJobTitle()).isEqualTo(jobHistoryEs.getJobTitle()
		 * ); assertThat(testJobHistory.getJobDescription()).isEqualTo(jobHistoryEs.
		 * getJobDescription());
		 * assertThat(testJobHistory.getSalary()).isEqualTo(jobHistoryEs.getSalary());
		 * assertThat(testJobHistory.getJobStatus()).isEqualTo(jobHistoryEs.getJobStatus
		 * ());
		 * assertThat(testJobHistory.getCreatedBy()).isEqualTo(jobHistoryEs.getCreatedBy
		 * ());
		 * assertThat(testJobHistory.isCanEdit()).isEqualTo(jobHistoryEs.isCanEdit());
		 * assertThat(testJobHistory.isHasBeenEdited()).isEqualTo(jobHistoryEs.
		 * isHasBeenEdited());
		 * assertThat(testJobHistory.isEverActive()).isEqualTo(jobHistoryEs.isEverActive
		 * ());
		 * assertThat(testJobHistory.getCreatedBy()).isEqualTo(jobHistoryEs.getCreatedBy
		 * ());
		 * assertThat(testJobHistory.getUpdatedBy()).isEqualTo(jobHistoryEs.getUpdatedBy
		 * ());
		 */

	}

	@Test
	@Transactional
	@Ignore
	public void updateActiveTransisitonToActiveWithSameJobAndFilter() throws Exception {

		Job job = createJobActiveNotEditedCanEdit(em);
		Set<JobHistory> jobHistories = createActiveJobHistories(em);
		jobHistories.forEach(jobHistory -> jobHistory.job(job));
		Set<JobFilter> jobFilters = createJobFilter(em);
		jobFilters.forEach(jobFilter -> jobFilter.job(job));
		job.setJobFilters(jobFilters);
		job.setHistories(jobHistories);
		jobRepository.saveAndFlush(job);
		// jobSearchRepository.save(job);
		int jobDatabaseSizeBeforeUpdate = jobRepository.findAll().size();
		int jobHistoryDatabaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();
		int jobFilterSizeBeforeUpdate = jobFilterRepository.findAll().size();
		int jobFilterHistorySizeBeforeUpdate = jobFilterHistoryRepository.findAll().size();
		// Update the job
		Job initializedJob = jobRepository.findOne(job.getId());
		Job prevJob = new Job();
		BeanUtils.copyProperties(prevJob, initializedJob);

		Job toBeUpdated = new Job();
		BeanUtils.copyProperties(toBeUpdated, initializedJob);
		// JobFilter initializedJobFilters = jobFilterRepository.findAll();
		JobFilter filter = jobFilterRepository.findByJob(initializedJob);
		HashSet<JobFilter> set = new HashSet<JobFilter>();
		set.add(filter);
		toBeUpdated.setJobFilters(set);
		// toBeUpdated.setJobFilters(new HashSet(jobFilterRepository.findAll()));

		restJobMockMvc.perform(put("/api/jobs").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(toBeUpdated))).andExpect(status().isOk());

		// Validate Results
		List<Job> jobList = jobRepository.findAll();
		List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
		List<JobFilter> jobFilterList = jobFilterRepository.findAll();
		List<JobFilterHistory> jobFilterHistoryList = jobFilterHistoryRepository.findAll();

		assertThat(jobList).hasSize(jobDatabaseSizeBeforeUpdate);
		assertThat(jobHistoryList).hasSize(jobHistoryDatabaseSizeBeforeUpdate);
		assertThat(jobHistoryList).hasSize(3);
		assertThat(jobFilterList).hasSize(jobFilterSizeBeforeUpdate);
		assertThat(jobFilterList).hasSize(1);
		assertThat(jobFilterHistoryList).hasSize(jobFilterHistorySizeBeforeUpdate);
		assertThat(jobFilterHistoryList).hasSize(0);
		Job testJob = jobList.get(jobList.size() - 1);
		assertThat(testJob.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
		assertThat(testJob.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
		// assertThat(testJob.getSalary()).isEqualTo(UPDATED_SALARY);
		assertThat(testJob.getJobStatus()).isEqualTo(ACTIVE_JOB_STATUS);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJob.isCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJob.isHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJob.isEverActive()).isEqualTo(UPDATED_EVER_ACTIVE);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJob.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);

		JobFilter testJobFilter = jobFilterList.get(jobFilterList.size() - 1);
		assertThat(testJobFilter.getFilterDescription()).isEqualTo(DEFAULT_FILTER);

		// Validate ES not saving Filter to ES

		/*
		 * Job jobEs = jobSearchRepository.findOne(testJob.getId());
		 * assertThat(testJob.getJobTitle()).isEqualTo(jobEs.getJobTitle());
		 * assertThat(testJob.getJobDescription()).isEqualTo(jobEs.getJobDescription());
		 * assertThat(testJob.getSalary()).isEqualTo(jobEs.getSalary());
		 * assertThat(testJob.getJobStatus()).isEqualTo(jobEs.getJobStatus());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.isCanEdit()).isEqualTo(jobEs.isCanEdit());
		 * assertThat(testJob.isHasBeenEdited()).isEqualTo(jobEs.isHasBeenEdited());
		 * assertThat(testJob.isEverActive()).isEqualTo(jobEs.isEverActive());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.getUpdatedBy()).isEqualTo(jobEs.getUpdatedBy());
		 */

	}

	@Test
	@Transactional
	public void updateActiveTransisitonToActiveWithEqualJobAndUnEqualFilter() throws Exception {

		Job job = createJobActiveNotEditedCanEdit(em);
		Set<JobHistory> jobHistories = createActiveJobHistories(em);
		jobHistories.forEach(jobHistory -> jobHistory.job(job));
		Set<JobFilter> jobFilters = createJobFilter(em);
		jobFilters.forEach(jobFilter -> jobFilter.job(job));
		job.setJobFilters(jobFilters);
		job.setHistories(jobHistories);
		jobRepository.saveAndFlush(job);
		// jobSearchRepository.save(job);
		int jobDatabaseSizeBeforeUpdate = jobRepository.findAll().size();
		int jobHistoryDatabaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();
		int jobFilterSizeBeforeUpdate = jobFilterRepository.findAll().size();
		int jobFilterHistorySizeBeforeUpdate = jobFilterHistoryRepository.findAll().size();

		// Update the job
		Job initializedJob = jobRepository.findOne(job.getId());

		Job prevJob = new Job();
		BeanUtils.copyProperties(prevJob, initializedJob);

		Job toBeUpdated = new Job();
		BeanUtils.copyProperties(toBeUpdated, initializedJob);
		JobFilter initializedJobFilter = jobFilterRepository.findByJob(initializedJob);
		Set<JobFilter> updatedJobFilters = createNewJobFilter(em);
		JobFilter filterToBeUpdated = updatedJobFilters.iterator().next();
		filterToBeUpdated.setId(initializedJobFilter.getId());
		filterToBeUpdated.job(toBeUpdated);
		toBeUpdated.setJobFilters(updatedJobFilters);

		restJobMockMvc.perform(put("/api/jobs").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(toBeUpdated))).andExpect(status().isOk());

		// Validate Results
		List<Job> jobList = jobRepository.findAll();
		List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
		List<JobFilter> jobFilterList = jobFilterRepository.findAll();
		List<JobFilterHistory> jobFilterHistoryList = jobFilterHistoryRepository.findAll();

		assertThat(jobList).hasSize(jobDatabaseSizeBeforeUpdate);
		assertThat(jobHistoryList).hasSize(jobHistoryDatabaseSizeBeforeUpdate + 1);
		assertThat(jobHistoryList).hasSize(4);
		assertThat(jobFilterList).hasSize(jobFilterSizeBeforeUpdate);
		assertThat(jobFilterList).hasSize(1);
		assertThat(jobFilterHistoryList).hasSize(jobFilterHistorySizeBeforeUpdate + 1);
		assertThat(jobFilterHistoryList).hasSize(1);
		Job testJob = jobList.get(jobList.size() - 1);
		assertThat(testJob.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
		assertThat(testJob.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
		// assertThat(testJob.getSalary()).isEqualTo(UPDATED_SALARY);
		assertThat(testJob.getJobStatus()).isEqualTo(ACTIVE_JOB_STATUS);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJob.isCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJob.isHasBeenEdited()).isEqualTo(UPDATED_HAS_BEEN_EDITED);
		assertThat(testJob.isEverActive()).isEqualTo(UPDATED_EVER_ACTIVE);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJob.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);

		JobFilter testJobFilter = jobFilterList.get(jobFilterList.size() - 1);
		assertThat(testJobFilter.getFilterDescription()).isEqualTo(UPDATED_FILTER);
		assertThat(testJobFilter.getId()).isEqualTo(initializedJobFilter.getId());

		// Validate ES not saving Filter to ES

		/*
		 * Job jobEs = jobSearchRepository.findOne(testJob.getId());
		 * assertThat(testJob.getJobTitle()).isEqualTo(jobEs.getJobTitle());
		 * assertThat(testJob.getJobDescription()).isEqualTo(jobEs.getJobDescription());
		 * assertThat(testJob.getSalary()).isEqualTo(jobEs.getSalary());
		 * assertThat(testJob.getJobStatus()).isEqualTo(jobEs.getJobStatus());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.isCanEdit()).isEqualTo(jobEs.isCanEdit());
		 * assertThat(testJob.isHasBeenEdited()).isEqualTo(jobEs.isHasBeenEdited());
		 * assertThat(testJob.isEverActive()).isEqualTo(jobEs.isEverActive());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.getUpdatedBy()).isEqualTo(jobEs.getUpdatedBy());
		 */

		List<JobHistory> testJobHistories = jobHistoryRepository.findByJobOrderByIdDesc(testJob);
		assertThat(testJobHistories.size()).isEqualTo(4);

		assertThat(testJobHistories.get(0).getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
		assertThat(testJobHistories.get(0).getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
		assertThat(testJobHistories.get(0).getJobStatus()).isEqualTo(ACTIVE_JOB_STATUS);
		// assertThat(testJobHistories.get(0).getSalary()).isEqualTo(DEFAULT_SALARY);
		assertThat(testJobHistories.get(0).getHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJobHistories.get(0).getCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJobHistories.get(0).getEverActive()).isEqualTo(UPDATED_EVER_ACTIVE);
		assertThat(testJobHistories.get(0).getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJobHistories.get(0).getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
		assertThat(testJobHistories.get(0).getJob()).isEqualTo(testJob);

		assertThat(testJobHistories.get(1).getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
		assertThat(testJobHistories.get(1).getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
		assertThat(testJobHistories.get(1).getJobStatus()).isEqualTo(ACTIVE_JOB_STATUS);
		assertThat(testJobHistories.get(1).getSalary()).isEqualTo(DEFAULT_SALARY);
		assertThat(testJobHistories.get(1).getHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJobHistories.get(1).getCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJobHistories.get(1).getEverActive()).isEqualTo(DEFAULT_EVER_ACTIVE);
		assertThat(testJobHistories.get(1).getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJobHistories.get(1).getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
		assertThat(testJobHistories.get(1).getJob()).isEqualTo(testJob);

		assertThat(testJobHistories.get(2).getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
		assertThat(testJobHistories.get(2).getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
		assertThat(testJobHistories.get(2).getJobStatus()).isEqualTo(DRAFT_JOB_STATUS);
		assertThat(testJobHistories.get(2).getSalary()).isEqualTo(DEFAULT_SALARY);
		assertThat(testJobHistories.get(2).getHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJobHistories.get(2).getCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJobHistories.get(2).getEverActive()).isEqualTo(DEFAULT_EVER_ACTIVE);
		assertThat(testJobHistories.get(2).getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJobHistories.get(2).getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
		assertThat(testJobHistories.get(2).getJob()).isEqualTo(testJob);

		assertThat(testJobHistories.get(3).getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
		assertThat(testJobHistories.get(3).getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
		assertThat(testJobHistories.get(3).getJobStatus()).isEqualTo(DRAFT_JOB_STATUS);
		assertThat(testJobHistories.get(3).getSalary()).isEqualTo(DEFAULT_SALARY);
		assertThat(testJobHistories.get(3).getHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJobHistories.get(3).getCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJobHistories.get(3).getEverActive()).isEqualTo(DEFAULT_EVER_ACTIVE);
		assertThat(testJobHistories.get(3).getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJobHistories.get(3).getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
		assertThat(testJobHistories.get(3).getJob()).isEqualTo(testJob);

	}

	@Test
	@Transactional
	public void updateActiveTransisitonToActiveWithUnEqualJobAndUnEqualFilter() throws Exception {

		Job job = createJobActiveNotEditedCanEdit(em);
		Set<JobHistory> jobHistories = createActiveJobHistories(em);
		jobHistories.forEach(jobHistory -> jobHistory.job(job));
		Set<JobFilter> jobFilters = createJobFilter(em);
		jobFilters.forEach(jobFilter -> jobFilter.job(job));
		job.setJobFilters(jobFilters);
		job.setHistories(jobHistories);
		jobRepository.saveAndFlush(job);
		// jobSearchRepository.save(job);
		int jobDatabaseSizeBeforeUpdate = jobRepository.findAll().size();
		int jobHistoryDatabaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();
		int jobFilterSizeBeforeUpdate = jobFilterRepository.findAll().size();
		int jobFilterHistorySizeBeforeUpdate = jobFilterHistoryRepository.findAll().size();

		// Update the job
		Job initializedJob = jobRepository.findOne(job.getId());

		Job prevJob = new Job();
		BeanUtils.copyProperties(prevJob, initializedJob);

		Job toBeUpdated = new Job();
		BeanUtils.copyProperties(toBeUpdated, initializedJob);
		toBeUpdated.setJobDescription(UPDATED_JOB_DESCRIPTION);
		JobFilter initializedJobFilter = jobFilterRepository.findByJob(initializedJob);
		Set<JobFilter> updatedJobFilters = createNewJobFilter(em);
		JobFilter filterToBeUpdated = updatedJobFilters.iterator().next();
		filterToBeUpdated.setId(initializedJobFilter.getId());
		filterToBeUpdated.job(toBeUpdated);
		toBeUpdated.setJobFilters(updatedJobFilters);

		restJobMockMvc.perform(put("/api/jobs").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(toBeUpdated))).andExpect(status().isOk());

		// Validate Results
		List<Job> jobList = jobRepository.findAll();
		List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
		List<JobFilter> jobFilterList = jobFilterRepository.findAll();
		List<JobFilterHistory> jobFilterHistoryList = jobFilterHistoryRepository.findAll();

		assertThat(jobList).hasSize(jobDatabaseSizeBeforeUpdate);
		assertThat(jobHistoryList).hasSize(jobHistoryDatabaseSizeBeforeUpdate + 1);
		assertThat(jobHistoryList).hasSize(4);
		assertThat(jobFilterList).hasSize(jobFilterSizeBeforeUpdate);
		assertThat(jobFilterList).hasSize(1);
		assertThat(jobFilterHistoryList).hasSize(jobFilterHistorySizeBeforeUpdate + 1);
		assertThat(jobFilterHistoryList).hasSize(1);
		Job testJob = jobList.get(jobList.size() - 1);
		assertThat(testJob.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
		assertThat(testJob.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
		// assertThat(testJob.getSalary()).isEqualTo(UPDATED_SALARY);
		assertThat(testJob.getJobStatus()).isEqualTo(ACTIVE_JOB_STATUS);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJob.isCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJob.isHasBeenEdited()).isEqualTo(UPDATED_HAS_BEEN_EDITED);
		assertThat(testJob.isEverActive()).isEqualTo(UPDATED_EVER_ACTIVE);
		assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJob.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);

		JobFilter testJobFilter = jobFilterList.get(jobFilterList.size() - 1);
		assertThat(testJobFilter.getFilterDescription()).isEqualTo(UPDATED_FILTER);
		assertThat(testJobFilter.getId()).isEqualTo(initializedJobFilter.getId());

		// Validate ES not saving Filter to ES

		/*
		 * Job jobEs = jobSearchRepository.findOne(testJob.getId());
		 * assertThat(testJob.getJobTitle()).isEqualTo(jobEs.getJobTitle());
		 * assertThat(testJob.getJobDescription()).isEqualTo(jobEs.getJobDescription());
		 * assertThat(testJob.getSalary()).isEqualTo(jobEs.getSalary());
		 * assertThat(testJob.getJobStatus()).isEqualTo(jobEs.getJobStatus());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.isCanEdit()).isEqualTo(jobEs.isCanEdit());
		 * assertThat(testJob.isHasBeenEdited()).isEqualTo(jobEs.isHasBeenEdited());
		 * assertThat(testJob.isEverActive()).isEqualTo(jobEs.isEverActive());
		 * assertThat(testJob.getCreatedBy()).isEqualTo(jobEs.getCreatedBy());
		 * assertThat(testJob.getUpdatedBy()).isEqualTo(jobEs.getUpdatedBy());
		 */

		List<JobHistory> testJobHistories = jobHistoryRepository.findByJobOrderByIdDesc(testJob);
		assertThat(testJobHistories.size()).isEqualTo(4);

		assertThat(testJobHistories.get(0).getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
		assertThat(testJobHistories.get(0).getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
		assertThat(testJobHistories.get(0).getJobStatus()).isEqualTo(ACTIVE_JOB_STATUS);
		// assertThat(testJobHistories.get(0).getSalary()).isEqualTo(DEFAULT_SALARY);
		assertThat(testJobHistories.get(0).getHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJobHistories.get(0).getCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJobHistories.get(0).getEverActive()).isEqualTo(UPDATED_EVER_ACTIVE);
		assertThat(testJobHistories.get(0).getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJobHistories.get(0).getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
		assertThat(testJobHistories.get(0).getJob()).isEqualTo(testJob);

		assertThat(testJobHistories.get(1).getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
		assertThat(testJobHistories.get(1).getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
		assertThat(testJobHistories.get(1).getJobStatus()).isEqualTo(ACTIVE_JOB_STATUS);
		assertThat(testJobHistories.get(1).getSalary()).isEqualTo(DEFAULT_SALARY);
		assertThat(testJobHistories.get(1).getHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJobHistories.get(1).getCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJobHistories.get(1).getEverActive()).isEqualTo(DEFAULT_EVER_ACTIVE);
		assertThat(testJobHistories.get(1).getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJobHistories.get(1).getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
		assertThat(testJobHistories.get(1).getJob()).isEqualTo(testJob);

		assertThat(testJobHistories.get(2).getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
		assertThat(testJobHistories.get(2).getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
		assertThat(testJobHistories.get(2).getJobStatus()).isEqualTo(DRAFT_JOB_STATUS);
		assertThat(testJobHistories.get(2).getSalary()).isEqualTo(DEFAULT_SALARY);
		assertThat(testJobHistories.get(2).getHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJobHistories.get(2).getCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJobHistories.get(2).getEverActive()).isEqualTo(DEFAULT_EVER_ACTIVE);
		assertThat(testJobHistories.get(2).getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJobHistories.get(2).getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
		assertThat(testJobHistories.get(2).getJob()).isEqualTo(testJob);

		assertThat(testJobHistories.get(3).getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
		assertThat(testJobHistories.get(3).getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
		assertThat(testJobHistories.get(3).getJobStatus()).isEqualTo(DRAFT_JOB_STATUS);
		assertThat(testJobHistories.get(3).getSalary()).isEqualTo(DEFAULT_SALARY);
		assertThat(testJobHistories.get(3).getHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
		assertThat(testJobHistories.get(3).getCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
		assertThat(testJobHistories.get(3).getEverActive()).isEqualTo(DEFAULT_EVER_ACTIVE);
		assertThat(testJobHistories.get(3).getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
		assertThat(testJobHistories.get(3).getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
		assertThat(testJobHistories.get(3).getJob()).isEqualTo(testJob);

	}

	@Test
	@Transactional
	public void testGetMatchedCandidatesForJob() throws Exception {
		Job job = new Job();
		job.jobTitle("Test Job");
		job.jobStatus(1);
		Candidate c1 = new Candidate().firstName("Abhinav");
		Candidate c2 = new Candidate().firstName("Aveer");
		CandidateEducation ce1 = new CandidateEducation();
		CandidateEducation ce2 = new CandidateEducation();
		Course course = new Course();
		course.course("Computer");
		Qualification qual = new Qualification();
		qual.qualification("Master");
		Course course2 = new Course();
		course.course("Arts");
		Qualification qual2 = new Qualification();
		qual.qualification("Bach");
		ce1.qualification(qual).course(course).highestQualification(true);
		ce2.qualification(qual2).course(course2).highestQualification(false);
		candidateRepository.saveAndFlush(c1);
		candidateRepository.saveAndFlush(c2);
		jobRepository.saveAndFlush(job);
		courseRepository.saveAndFlush(course);
		courseRepository.saveAndFlush(course2);
		qualificationRepository.saveAndFlush(qual);
		qualificationRepository.saveAndFlush(qual2);
		c1.addEducation(ce1).addEducation(ce2);
		c2.addEducation(ce1).addEducation(ce2);
		candidateRepository.saveAndFlush(c1);
		candidateRepository.saveAndFlush(c2);
		CandidateJob cJ1 = new CandidateJob(c1, job);
		CandidateJob cJ2 = new CandidateJob(c2, job);
		c1.addCandidateJob(cJ1);
		c2.addCandidateJob(cJ2);
		candidateRepository.saveAndFlush(c1);
		candidateRepository.saveAndFlush(c2);
		// Get all the jobList
		// ResultActions resultActions =
		// restJobMockMvc.perform(get("/api/matchedCandiatesForJob/{jobId}",job.getId())).andExpect(status().isOk())
		restJobMockMvc.perform(get("/api/matchedCandiatesForJob/{jobId}", job.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[*].firstName", hasItem("Abhinav")))
				.andExpect(jsonPath("$[*].firstName", hasItem("Aveer")))
				.andExpect(jsonPath("$[*].qualificationWithHighestCourse", hasItem("Bach in Arts")));
		// resultActions.andDo(MockMvcResultHandlers.print());

	}
	
	@Test
	@Transactional
	public void testGetAllJobsForCandidateWithNoEducation() throws Exception {
		Job job1 = new Job();
		job1.jobTitle("Test Job1");
		job1.jobStatus(1);
		Job job2 = new Job();
		job2.jobTitle("Test Job2");
		job2.jobStatus(1);
		Job job3 = new Job();
		job3.jobTitle("Test Job3");
		job3.jobStatus(1);
		Candidate c1 = new Candidate().firstName("Abhinav");
		candidateRepository.saveAndFlush(c1);
		jobRepository.saveAndFlush(job1);
		jobRepository.saveAndFlush(job2);
		jobRepository.saveAndFlush(job3);
		c1.addAppliedJob(job1);
		candidateRepository.saveAndFlush(c1);
		
		// Get all the jobList
		// ResultActions resultActions =
		// restJobMockMvc.perform(get("/api/matchedCandiatesForJob/{jobId}",job.getId())).andExpect(status().isOk())
		restJobMockMvc.perform(get("/api/newActiveJobsForCandidate/{candidateId}", c1.getId())).andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[*].jobTitle", hasItem("Test Job2")))
				.andExpect(jsonPath("$[*].jobTitle", hasItem("Test Job3")))
				.andExpect(jsonPath("$[*].matchScore", hasItem(0d)));
		// resultActions.andDo(MockMvcResultHandlers.print());

	}
	
	@Test
	@Transactional
	public void testGetAllJobsForCandidateWithEducation() throws Exception {
		Job job1 = new Job();
		job1.jobTitle("Test Job1");
		job1.jobStatus(1);
		Job job2 = new Job();
		job2.jobTitle("Test Job2");
		job2.jobStatus(1);
		Job job3 = new Job();
		job3.jobTitle("Test Job3");
		job3.jobStatus(1);
		Candidate c1 = new Candidate().firstName("Abhinav");
		candidateRepository.saveAndFlush(c1);
		jobRepository.saveAndFlush(job1);
		jobRepository.saveAndFlush(job2);
		jobRepository.saveAndFlush(job3);
		CandidateEducation ce1 = new CandidateEducation();
		CandidateEducation ce2 = new CandidateEducation();
		Course course = new Course();
		course.course("Computer");
		Qualification qual = new Qualification();
		qual.qualification("Master");
		Course course2 = new Course();
		course.course("Arts");
		Qualification qual2 = new Qualification();
		qual.qualification("Bach");
		ce1.qualification(qual).course(course).highestQualification(true);
		ce2.qualification(qual2).course(course2).highestQualification(false);
		courseRepository.saveAndFlush(course);
		courseRepository.saveAndFlush(course2);
		qualificationRepository.saveAndFlush(qual);
		qualificationRepository.saveAndFlush(qual2);
		c1.addEducation(ce1).addEducation(ce2);
		candidateRepository.saveAndFlush(c1);
		c1.addAppliedJob(job1);
		CandidateJob cj1 = new CandidateJob(c1,job1);
		CandidateJob cj2 = new CandidateJob(c1,job2);
		CandidateJob cj3 = new CandidateJob(c1,job3);
		cj1.setMatchScore(30d);
		cj2.setMatchScore(40d);
		cj3.setMatchScore(50d);
	//c1.addCandidateJob(cj1);c1.addCandidateJob(cj2);c1.addCandidateJob(cj3);
		job1.addCandidateJob(cj1);job2.addCandidateJob(cj2);job3.addCandidateJob(cj3);
		
		candidateRepository.saveAndFlush(c1);
		
		
		// Get all the jobList
		// ResultActions resultActions =
		// restJobMockMvc.perform(get("/api/matchedCandiatesForJob/{jobId}",job.getId())).andExpect(status().isOk())
		restJobMockMvc.perform(get("/api/newActiveJobsForCandidate/{candidateId}", c1.getId())).andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[*].jobTitle", hasItem("Test Job2")))
				.andExpect(jsonPath("$[*].jobTitle", hasItem("Test Job3")))
				.andExpect(jsonPath("$[*].matchScore", hasItem(40d)))
				.andExpect(jsonPath("$[*].matchScore", hasItem(50d)));
		// resultActions.andDo(MockMvcResultHandlers.print());

	}
	
	

	@Test
	@Transactional
	public void testGetAppliedCandidatesForJob() throws Exception {
		Job job = new Job();
		job.jobTitle("Test Job");
		//job.jobStatus(1);
		Candidate c1 = new Candidate().firstName("Abhinav");
		Candidate c2 = new Candidate().firstName("Aveer");
		CandidateEducation ce1 = new CandidateEducation();
		CandidateEducation ce2 = new CandidateEducation();
		Course course = new Course();
		course.course("Computer");
		Qualification qual = new Qualification();
		qual.qualification("Master");
		Course course2 = new Course();
		course.course("Arts");
		Qualification qual2 = new Qualification();
		qual.qualification("Bach");
		ce1.qualification(qual).course(course).highestQualification(true);
		ce2.qualification(qual2).course(course2).highestQualification(false);
		candidateRepository.saveAndFlush(c1);
		candidateRepository.saveAndFlush(c2);
		jobRepository.saveAndFlush(job);
		courseRepository.saveAndFlush(course);
		courseRepository.saveAndFlush(course2);
		qualificationRepository.saveAndFlush(qual);
		qualificationRepository.saveAndFlush(qual2);
		c1.addEducation(ce1).addEducation(ce2);
		c2.addEducation(ce1).addEducation(ce2);
		candidateRepository.saveAndFlush(c1);
		candidateRepository.saveAndFlush(c2);
		CandidateJob cJ1 = new CandidateJob(c1, job);
		CandidateJob cJ2 = new CandidateJob(c2, job);
		c1.addCandidateJob(cJ1);
		c2.addCandidateJob(cJ2);
		c1.addAppliedJob(job);
		c2.addAppliedJob(job);
		candidateRepository.saveAndFlush(c1);
		candidateRepository.saveAndFlush(c2);
		// Get all the jobList
		// ResultActions resultActions =
		// restJobMockMvc.perform(get("/api/matchedCandiatesForJob/{jobId}",job.getId())).andExpect(status().isOk())
		restJobMockMvc.perform(get("/api/appliedCandiatesForJob/{jobId}", job.getId())).andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[*].firstName", hasItem("Abhinav")))
				.andExpect(jsonPath("$[*].firstName", hasItem("Aveer")))
				.andExpect(jsonPath("$[*].qualificationWithHighestCourse", hasItem("Bach in Arts")));
	}

	
	@Test
	@Transactional
	public void testGetAppliedCandidatesForAllJobsByCorporate() throws Exception {
		Corporate corp = new Corporate();
		corp.name("Drishika");
		Job job1 = new Job();
		job1.jobTitle("Test Job 1");
		Job job2 = new Job();
		job1.jobTitle("Test Job 2");
		
		//job.jobStatus(1);
		Candidate c1 = new Candidate().firstName("Abhinav");
		Candidate c2 = new Candidate().firstName("Aveer");
		CandidateEducation ce1 = new CandidateEducation();
		CandidateEducation ce2 = new CandidateEducation();
		Course course = new Course();
		course.course("Computer");
		Qualification qual = new Qualification();
		qual.qualification("Master");
		Course course2 = new Course();
		course.course("Arts");
		Qualification qual2 = new Qualification();
		qual.qualification("Bach");
		ce1.qualification(qual).course(course).highestQualification(true);
		ce2.qualification(qual2).course(course2).highestQualification(false);
		corporateRepository.saveAndFlush(corp);
		candidateRepository.saveAndFlush(c1);
		candidateRepository.saveAndFlush(c2);
		job1.corporate(corp);job2.corporate(corp);
		jobRepository.saveAndFlush(job1);
		jobRepository.saveAndFlush(job2);
		courseRepository.saveAndFlush(course);
		courseRepository.saveAndFlush(course2);
		qualificationRepository.saveAndFlush(qual);
		qualificationRepository.saveAndFlush(qual2);
		c1.addEducation(ce1).addEducation(ce2);
		c2.addEducation(ce1).addEducation(ce2);
		candidateRepository.saveAndFlush(c1);
		candidateRepository.saveAndFlush(c2);
		CandidateJob cJ1 = new CandidateJob(c1, job1);
		CandidateJob cJ2 = new CandidateJob(c2, job1);
		CandidateJob cJ3 = new CandidateJob(c2, job2);
		c1.addCandidateJob(cJ1);
		c2.addCandidateJob(cJ2);
		c2.addCandidateJob(cJ3);
		c1.addAppliedJob(job1);
		c2.addAppliedJob(job2);
		c2.addAppliedJob(job1);
		candidateRepository.saveAndFlush(c1);
		candidateRepository.saveAndFlush(c2);
		// Get all the jobList
		// ResultActions resultActions =
		// restJobMockMvc.perform(get("/api/matchedCandiatesForJob/{jobId}",job.getId())).andExpect(status().isOk())
		restJobMockMvc.perform(get("/api/appliedCandidatesForJobsByCorporate/{corporateId}", corp.getId()))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$").value(3));
	}
	
	
	@Test
	@Transactional
	public void testGetTotalJobsByCorporate() throws Exception {
		Corporate corp = new Corporate();
		corp.name("Drishika");
		Job job1 = new Job();
		job1.jobTitle("Test Job 1");
		Job job2 = new Job();
		job1.jobTitle("Test Job 2");
		corporateRepository.saveAndFlush(corp);
		
		job1.corporate(corp);job2.corporate(corp);
		jobRepository.saveAndFlush(job1);
		jobRepository.saveAndFlush(job2);
		
		// Get all the jobList
		// ResultActions resultActions =
		// restJobMockMvc.perform(get("/api/matchedCandiatesForJob/{jobId}",job.getId())).andExpect(status().isOk())
		restJobMockMvc.perform(get("/api/totalJobsByCorporate/{corporateId}", corp.getId()))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$").value(2));
	}
	
	
	@Test
	@Transactional
	public void testGetJobsPostedLastMonthByCorporate() throws Exception {
		Corporate corp = new Corporate();
		corp.name("Drishika");
		Job job1 = new Job();
		job1.jobTitle("Test Job 1");
	//	ZonedDateTime createDate = ZonedDateTime.parse("2018-10-30T12:30:40Z[GMT]");
		LocalDate createDate = LocalDate.now(ZoneId.of("Asia/Kolkata"));
		job1.createDate(createDate.atStartOfDay(ZoneId.of("Asia/Kolkata")));
		Job job2 = new Job();
		job1.jobTitle("Test Job 2");
		LocalDate createDate2 = LocalDate.of(2017,Month.JANUARY,1);
	//	ZonedDateTime createDate2 = ZonedDateTime.parse("2018-09-30T12:30:40Z[GMT]");
		job2.setCreateDate(createDate2.atStartOfDay(ZoneId.of("Asia/Kolkata")));
		corporateRepository.saveAndFlush(corp);
		job1.corporate(corp);job2.corporate(corp);
		jobRepository.saveAndFlush(job1);
		jobRepository.saveAndFlush(job2);
		
		// Get all the jobList
		// ResultActions resultActions =
		// restJobMockMvc.perform(get("/api/matchedCandiatesForJob/{jobId}",job.getId())).andExpect(status().isOk())
		restJobMockMvc.perform(get("/api/totalJobsPostedLastMonthByCorporate/{corporateId}", corp.getId()))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$").value(1));
	}
	
	@Test
	@Transactional
	public void testGetJobsPostedLastMonthByCorporateFromDecToJan() throws Exception {
		Corporate corp = new Corporate();
		corp.name("Drishika");
		Job job1 = new Job();
		job1.jobTitle("Test Job 1");
	//	ZonedDateTime createDate = ZonedDateTime.parse("2018-10-30T12:30:40Z[GMT]");
		LocalDate createDate = LocalDate.of(2018,Month.DECEMBER,22);
		job1.createDate(createDate.atStartOfDay(ZoneId.of("Asia/Kolkata")));
		Job job2 = new Job();
		job1.jobTitle("Test Job 2");
		LocalDate createDate2 = LocalDate.of(2017,Month.JANUARY,1);
	//	ZonedDateTime createDate2 = ZonedDateTime.parse("2018-09-30T12:30:40Z[GMT]");
		job2.setCreateDate(createDate2.atStartOfDay(ZoneId.of("Asia/Kolkata")));
		corporateRepository.saveAndFlush(corp);
		job1.corporate(corp);job2.corporate(corp);
		jobRepository.saveAndFlush(job1);
		jobRepository.saveAndFlush(job2);
		
		// Get all the jobList
		// ResultActions resultActions =
		// restJobMockMvc.perform(get("/api/matchedCandiatesForJob/{jobId}",job.getId())).andExpect(status().isOk())
		restJobMockMvc.perform(get("/api/totalJobsPostedLastMonthByCorporate/{corporateId}", corp.getId()))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$").value(1));
	}
	
	@Test
	@Transactional
	public void testGetJobsPostedLastMonthByCorporateFromDec1stToJanCurrent() throws Exception {
		Corporate corp = new Corporate();
		corp.name("Drishika");
		Job job1 = new Job();
		job1.jobTitle("Test Job 1");
	//	ZonedDateTime createDate = ZonedDateTime.parse("2018-10-30T12:30:40Z[GMT]");
		LocalDate createDate = LocalDate.of(2018,Month.DECEMBER,1);
		job1.createDate(createDate.atStartOfDay(ZoneId.of("Asia/Kolkata")));
		Job job2 = new Job();
		job1.jobTitle("Test Job 2");
		LocalDate createDate2 = LocalDate.of(2017,Month.JANUARY,1);
	//	ZonedDateTime createDate2 = ZonedDateTime.parse("2018-09-30T12:30:40Z[GMT]");
		job2.setCreateDate(createDate2.atStartOfDay(ZoneId.of("Asia/Kolkata")));
		corporateRepository.saveAndFlush(corp);
		job1.corporate(corp);job2.corporate(corp);
		jobRepository.saveAndFlush(job1);
		jobRepository.saveAndFlush(job2);
		
		// Get all the jobList
		// ResultActions resultActions =
		// restJobMockMvc.perform(get("/api/matchedCandiatesForJob/{jobId}",job.getId())).andExpect(status().isOk())
		restJobMockMvc.perform(get("/api/totalJobsPostedLastMonthByCorporate/{corporateId}", corp.getId()))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$").value(1));
	}


	

}
