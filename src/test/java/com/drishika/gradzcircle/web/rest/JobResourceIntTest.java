package com.drishika.gradzcircle.web.rest;

import com.drishika.gradzcircle.GradzcircleApp;

import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.repository.JobRepository;
import com.drishika.gradzcircle.repository.search.JobSearchRepository;
import com.drishika.gradzcircle.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.drishika.gradzcircle.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.drishika.gradzcircle.domain.enumeration.PaymentType;
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

    private static final Double DEFAULT_SALARY = 1D;
    private static final Double UPDATED_SALARY = 2D;

    private static final Integer DEFAULT_JOB_STATUS = 1;
    private static final Integer UPDATED_JOB_STATUS = 2;

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

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

    private static final Boolean DEFAULT_CAN_EDIT = false;
    private static final Boolean UPDATED_CAN_EDIT = true;

    private static final ZonedDateTime DEFAULT_UPDATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final Long DEFAULT_UPDATED_BY = 1L;
    private static final Long UPDATED_UPDATED_BY = 2L;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobSearchRepository jobSearchRepository;

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobResource jobResource = new JobResource(jobRepository, jobSearchRepository);
        this.restJobMockMvc = MockMvcBuilders.standaloneSetup(jobResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Job createEntity(EntityManager em) {
        Job job = new Job()
            .jobTitle(DEFAULT_JOB_TITLE)
            .jobDescription(DEFAULT_JOB_DESCRIPTION)
            .noOfApplicants(DEFAULT_NO_OF_APPLICANTS)
            .salary(DEFAULT_SALARY)
            .jobStatus(DEFAULT_JOB_STATUS)
            .createDate(DEFAULT_CREATE_DATE)
            .originalJobCost(DEFAULT_ORIGINAL_JOB_COST)
            .jobCost(DEFAULT_JOB_COST)
            .amountPaid(DEFAULT_AMOUNT_PAID)
            .totalAmountPaid(DEFAULT_TOTAL_AMOUNT_PAID)
            .noOfApplicantsBought(DEFAULT_NO_OF_APPLICANTS_BOUGHT)
            .removedFilterAmount(DEFAULT_REMOVED_FILTER_AMOUNT)
            .additionalFilterAmount(DEFAULT_ADDITIONAL_FILTER_AMOUNT)
            .adminCharge(DEFAULT_ADMIN_CHARGE)
            .adminChargeRate(DEFAULT_ADMIN_CHARGE_RATE)
            .upfrontDiscountRate(DEFAULT_UPFRONT_DISCOUNT_RATE)
            .upfrontDiscountAmount(DEFAULT_UPFRONT_DISCOUNT_AMOUNT)
            .escrowAmountUsed(DEFAULT_ESCROW_AMOUNT_USED)
            .escrowAmountAdded(DEFAULT_ESCROW_AMOUNT_ADDED)
            .paymentType(DEFAULT_PAYMENT_TYPE)
            .hasBeenEdited(DEFAULT_HAS_BEEN_EDITED)
            .everActive(DEFAULT_EVER_ACTIVE)
            .canEdit(DEFAULT_CAN_EDIT)
            .updateDate(DEFAULT_UPDATE_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return job;
    }

    @Before
    public void initTest() {
        jobSearchRepository.deleteAll();
        job = createEntity(em);
    }

    @Test
    @Transactional
    public void createJob() throws Exception {
        int databaseSizeBeforeCreate = jobRepository.findAll().size();

        // Create the Job
        restJobMockMvc.perform(post("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isCreated());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeCreate + 1);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testJob.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
        assertThat(testJob.getNoOfApplicants()).isEqualTo(DEFAULT_NO_OF_APPLICANTS);
        assertThat(testJob.getSalary()).isEqualTo(DEFAULT_SALARY);
        assertThat(testJob.getJobStatus()).isEqualTo(DEFAULT_JOB_STATUS);
        assertThat(testJob.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
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
        assertThat(testJob.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
        assertThat(testJob.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testJob.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);

        // Validate the Job in Elasticsearch
        Job jobEs = jobSearchRepository.findOne(testJob.getId());
        assertThat(jobEs).isEqualToComparingFieldByField(testJob);
    }

    @Test
    @Transactional
    public void createJobWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobRepository.findAll().size();

        // Create the Job with an existing ID
        job.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobMockMvc.perform(post("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJobs() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList
        restJobMockMvc.perform(get("/api/jobs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE.toString())))
            .andExpect(jsonPath("$.[*].jobDescription").value(hasItem(DEFAULT_JOB_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].noOfApplicants").value(hasItem(DEFAULT_NO_OF_APPLICANTS)))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].jobStatus").value(hasItem(DEFAULT_JOB_STATUS)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(sameInstant(DEFAULT_CREATE_DATE))))
            .andExpect(jsonPath("$.[*].originalJobCost").value(hasItem(DEFAULT_ORIGINAL_JOB_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].jobCost").value(hasItem(DEFAULT_JOB_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].amountPaid").value(hasItem(DEFAULT_AMOUNT_PAID.doubleValue())))
            .andExpect(jsonPath("$.[*].totalAmountPaid").value(hasItem(DEFAULT_TOTAL_AMOUNT_PAID.doubleValue())))
            .andExpect(jsonPath("$.[*].noOfApplicantsBought").value(hasItem(DEFAULT_NO_OF_APPLICANTS_BOUGHT)))
            .andExpect(jsonPath("$.[*].removedFilterAmount").value(hasItem(DEFAULT_REMOVED_FILTER_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].additionalFilterAmount").value(hasItem(DEFAULT_ADDITIONAL_FILTER_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].adminCharge").value(hasItem(DEFAULT_ADMIN_CHARGE.doubleValue())))
            .andExpect(jsonPath("$.[*].adminChargeRate").value(hasItem(DEFAULT_ADMIN_CHARGE_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].upfrontDiscountRate").value(hasItem(DEFAULT_UPFRONT_DISCOUNT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].upfrontDiscountAmount").value(hasItem(DEFAULT_UPFRONT_DISCOUNT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].escrowAmountUsed").value(hasItem(DEFAULT_ESCROW_AMOUNT_USED.doubleValue())))
            .andExpect(jsonPath("$.[*].escrowAmountAdded").value(hasItem(DEFAULT_ESCROW_AMOUNT_ADDED.doubleValue())))
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
        jobRepository.saveAndFlush(job);

        // Get the job
        restJobMockMvc.perform(get("/api/jobs/{id}", job.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(job.getId().intValue()))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE.toString()))
            .andExpect(jsonPath("$.jobDescription").value(DEFAULT_JOB_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.noOfApplicants").value(DEFAULT_NO_OF_APPLICANTS))
            .andExpect(jsonPath("$.salary").value(DEFAULT_SALARY.doubleValue()))
            .andExpect(jsonPath("$.jobStatus").value(DEFAULT_JOB_STATUS))
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
    public void getNonExistingJob() throws Exception {
        // Get the job
        restJobMockMvc.perform(get("/api/jobs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);
        jobSearchRepository.save(job);
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Update the job
        Job updatedJob = jobRepository.findOne(job.getId());
        updatedJob
            .jobTitle(UPDATED_JOB_TITLE)
            .jobDescription(UPDATED_JOB_DESCRIPTION)
            .noOfApplicants(UPDATED_NO_OF_APPLICANTS)
            .salary(UPDATED_SALARY)
            .jobStatus(UPDATED_JOB_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .originalJobCost(UPDATED_ORIGINAL_JOB_COST)
            .jobCost(UPDATED_JOB_COST)
            .amountPaid(UPDATED_AMOUNT_PAID)
            .totalAmountPaid(UPDATED_TOTAL_AMOUNT_PAID)
            .noOfApplicantsBought(UPDATED_NO_OF_APPLICANTS_BOUGHT)
            .removedFilterAmount(UPDATED_REMOVED_FILTER_AMOUNT)
            .additionalFilterAmount(UPDATED_ADDITIONAL_FILTER_AMOUNT)
            .adminCharge(UPDATED_ADMIN_CHARGE)
            .adminChargeRate(UPDATED_ADMIN_CHARGE_RATE)
            .upfrontDiscountRate(UPDATED_UPFRONT_DISCOUNT_RATE)
            .upfrontDiscountAmount(UPDATED_UPFRONT_DISCOUNT_AMOUNT)
            .escrowAmountUsed(UPDATED_ESCROW_AMOUNT_USED)
            .escrowAmountAdded(UPDATED_ESCROW_AMOUNT_ADDED)
            .paymentType(UPDATED_PAYMENT_TYPE)
            .hasBeenEdited(UPDATED_HAS_BEEN_EDITED)
            .everActive(UPDATED_EVER_ACTIVE)
            .canEdit(UPDATED_CAN_EDIT)
            .updateDate(UPDATED_UPDATE_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restJobMockMvc.perform(put("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJob)))
            .andExpect(status().isOk());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testJob.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
        assertThat(testJob.getNoOfApplicants()).isEqualTo(UPDATED_NO_OF_APPLICANTS);
        assertThat(testJob.getSalary()).isEqualTo(UPDATED_SALARY);
        assertThat(testJob.getJobStatus()).isEqualTo(UPDATED_JOB_STATUS);
        assertThat(testJob.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testJob.getOriginalJobCost()).isEqualTo(UPDATED_ORIGINAL_JOB_COST);
        assertThat(testJob.getJobCost()).isEqualTo(UPDATED_JOB_COST);
        assertThat(testJob.getAmountPaid()).isEqualTo(UPDATED_AMOUNT_PAID);
        assertThat(testJob.getTotalAmountPaid()).isEqualTo(UPDATED_TOTAL_AMOUNT_PAID);
        assertThat(testJob.getNoOfApplicantsBought()).isEqualTo(UPDATED_NO_OF_APPLICANTS_BOUGHT);
        assertThat(testJob.getRemovedFilterAmount()).isEqualTo(UPDATED_REMOVED_FILTER_AMOUNT);
        assertThat(testJob.getAdditionalFilterAmount()).isEqualTo(UPDATED_ADDITIONAL_FILTER_AMOUNT);
        assertThat(testJob.getAdminCharge()).isEqualTo(UPDATED_ADMIN_CHARGE);
        assertThat(testJob.getAdminChargeRate()).isEqualTo(UPDATED_ADMIN_CHARGE_RATE);
        assertThat(testJob.getUpfrontDiscountRate()).isEqualTo(UPDATED_UPFRONT_DISCOUNT_RATE);
        assertThat(testJob.getUpfrontDiscountAmount()).isEqualTo(UPDATED_UPFRONT_DISCOUNT_AMOUNT);
        assertThat(testJob.getEscrowAmountUsed()).isEqualTo(UPDATED_ESCROW_AMOUNT_USED);
        assertThat(testJob.getEscrowAmountAdded()).isEqualTo(UPDATED_ESCROW_AMOUNT_ADDED);
        assertThat(testJob.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testJob.isHasBeenEdited()).isEqualTo(UPDATED_HAS_BEEN_EDITED);
        assertThat(testJob.isEverActive()).isEqualTo(UPDATED_EVER_ACTIVE);
        assertThat(testJob.isCanEdit()).isEqualTo(UPDATED_CAN_EDIT);
        assertThat(testJob.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testJob.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testJob.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);

        // Validate the Job in Elasticsearch
        Job jobEs = jobSearchRepository.findOne(testJob.getId());
        assertThat(jobEs).isEqualToComparingFieldByField(testJob);
    }

    @Test
    @Transactional
    public void updateNonExistingJob() throws Exception {
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Create the Job

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobMockMvc.perform(put("/api/jobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isCreated());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);
        jobSearchRepository.save(job);
        int databaseSizeBeforeDelete = jobRepository.findAll().size();

        // Get the job
        restJobMockMvc.perform(delete("/api/jobs/{id}", job.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean jobExistsInEs = jobSearchRepository.exists(job.getId());
        assertThat(jobExistsInEs).isFalse();

        // Validate the database is empty
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);
        jobSearchRepository.save(job);

        // Search the job
        restJobMockMvc.perform(get("/api/_search/jobs?query=id:" + job.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE.toString())))
            .andExpect(jsonPath("$.[*].jobDescription").value(hasItem(DEFAULT_JOB_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].noOfApplicants").value(hasItem(DEFAULT_NO_OF_APPLICANTS)))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].jobStatus").value(hasItem(DEFAULT_JOB_STATUS)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(sameInstant(DEFAULT_CREATE_DATE))))
            .andExpect(jsonPath("$.[*].originalJobCost").value(hasItem(DEFAULT_ORIGINAL_JOB_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].jobCost").value(hasItem(DEFAULT_JOB_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].amountPaid").value(hasItem(DEFAULT_AMOUNT_PAID.doubleValue())))
            .andExpect(jsonPath("$.[*].totalAmountPaid").value(hasItem(DEFAULT_TOTAL_AMOUNT_PAID.doubleValue())))
            .andExpect(jsonPath("$.[*].noOfApplicantsBought").value(hasItem(DEFAULT_NO_OF_APPLICANTS_BOUGHT)))
            .andExpect(jsonPath("$.[*].removedFilterAmount").value(hasItem(DEFAULT_REMOVED_FILTER_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].additionalFilterAmount").value(hasItem(DEFAULT_ADDITIONAL_FILTER_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].adminCharge").value(hasItem(DEFAULT_ADMIN_CHARGE.doubleValue())))
            .andExpect(jsonPath("$.[*].adminChargeRate").value(hasItem(DEFAULT_ADMIN_CHARGE_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].upfrontDiscountRate").value(hasItem(DEFAULT_UPFRONT_DISCOUNT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].upfrontDiscountAmount").value(hasItem(DEFAULT_UPFRONT_DISCOUNT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].escrowAmountUsed").value(hasItem(DEFAULT_ESCROW_AMOUNT_USED.doubleValue())))
            .andExpect(jsonPath("$.[*].escrowAmountAdded").value(hasItem(DEFAULT_ESCROW_AMOUNT_ADDED.doubleValue())))
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
}
