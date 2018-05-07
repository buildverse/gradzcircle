package com.drishika.gradzcircle.web.rest;

import com.drishika.gradzcircle.GradzcircleApp;

import com.drishika.gradzcircle.domain.JobHistory;
import com.drishika.gradzcircle.repository.JobHistoryRepository;
import com.drishika.gradzcircle.repository.search.JobHistorySearchRepository;
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
 * Test class for the JobHistoryResource REST controller.
 *
 * @see JobHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class JobHistoryResourceIntTest {

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
    private JobHistoryRepository jobHistoryRepository;

    @Autowired
    private JobHistorySearchRepository jobHistorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobHistoryMockMvc;

    private JobHistory jobHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobHistoryResource jobHistoryResource = new JobHistoryResource(jobHistoryRepository, jobHistorySearchRepository);
        this.restJobHistoryMockMvc = MockMvcBuilders.standaloneSetup(jobHistoryResource)
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
    public static JobHistory createEntity(EntityManager em) {
        JobHistory jobHistory = new JobHistory()
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
        return jobHistory;
    }

    @Before
    public void initTest() {
        jobHistorySearchRepository.deleteAll();
        jobHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobHistory() throws Exception {
        int databaseSizeBeforeCreate = jobHistoryRepository.findAll().size();

        // Create the JobHistory
        restJobHistoryMockMvc.perform(post("/api/job-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobHistory)))
            .andExpect(status().isCreated());

        // Validate the JobHistory in the database
        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        JobHistory testJobHistory = jobHistoryList.get(jobHistoryList.size() - 1);
        assertThat(testJobHistory.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testJobHistory.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
        assertThat(testJobHistory.getNoOfApplicants()).isEqualTo(DEFAULT_NO_OF_APPLICANTS);
        assertThat(testJobHistory.getSalary()).isEqualTo(DEFAULT_SALARY);
        assertThat(testJobHistory.getJobStatus()).isEqualTo(DEFAULT_JOB_STATUS);
        assertThat(testJobHistory.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testJobHistory.getOriginalJobCost()).isEqualTo(DEFAULT_ORIGINAL_JOB_COST);
        assertThat(testJobHistory.getJobCost()).isEqualTo(DEFAULT_JOB_COST);
        assertThat(testJobHistory.getAmountPaid()).isEqualTo(DEFAULT_AMOUNT_PAID);
        assertThat(testJobHistory.getTotalAmountPaid()).isEqualTo(DEFAULT_TOTAL_AMOUNT_PAID);
        assertThat(testJobHistory.getNoOfApplicantsBought()).isEqualTo(DEFAULT_NO_OF_APPLICANTS_BOUGHT);
        assertThat(testJobHistory.getRemovedFilterAmount()).isEqualTo(DEFAULT_REMOVED_FILTER_AMOUNT);
        assertThat(testJobHistory.getAdditionalFilterAmount()).isEqualTo(DEFAULT_ADDITIONAL_FILTER_AMOUNT);
        assertThat(testJobHistory.getAdminCharge()).isEqualTo(DEFAULT_ADMIN_CHARGE);
        assertThat(testJobHistory.getAdminChargeRate()).isEqualTo(DEFAULT_ADMIN_CHARGE_RATE);
        assertThat(testJobHistory.getUpfrontDiscountRate()).isEqualTo(DEFAULT_UPFRONT_DISCOUNT_RATE);
        assertThat(testJobHistory.getUpfrontDiscountAmount()).isEqualTo(DEFAULT_UPFRONT_DISCOUNT_AMOUNT);
        assertThat(testJobHistory.getEscrowAmountUsed()).isEqualTo(DEFAULT_ESCROW_AMOUNT_USED);
        assertThat(testJobHistory.getEscrowAmountAdded()).isEqualTo(DEFAULT_ESCROW_AMOUNT_ADDED);
        assertThat(testJobHistory.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
        assertThat(testJobHistory.isHasBeenEdited()).isEqualTo(DEFAULT_HAS_BEEN_EDITED);
        assertThat(testJobHistory.isEverActive()).isEqualTo(DEFAULT_EVER_ACTIVE);
        assertThat(testJobHistory.isCanEdit()).isEqualTo(DEFAULT_CAN_EDIT);
        assertThat(testJobHistory.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
        assertThat(testJobHistory.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testJobHistory.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);

        // Validate the JobHistory in Elasticsearch
        JobHistory jobHistoryEs = jobHistorySearchRepository.findOne(testJobHistory.getId());
        assertThat(jobHistoryEs).isEqualToComparingFieldByField(testJobHistory);
    }

    @Test
    @Transactional
    public void createJobHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobHistoryRepository.findAll().size();

        // Create the JobHistory with an existing ID
        jobHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobHistoryMockMvc.perform(post("/api/job-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobHistory)))
            .andExpect(status().isBadRequest());

        // Validate the JobHistory in the database
        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJobHistories() throws Exception {
        // Initialize the database
        jobHistoryRepository.saveAndFlush(jobHistory);

        // Get all the jobHistoryList
        restJobHistoryMockMvc.perform(get("/api/job-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobHistory.getId().intValue())))
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
    public void getJobHistory() throws Exception {
        // Initialize the database
        jobHistoryRepository.saveAndFlush(jobHistory);

        // Get the jobHistory
        restJobHistoryMockMvc.perform(get("/api/job-histories/{id}", jobHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobHistory.getId().intValue()))
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
    public void getNonExistingJobHistory() throws Exception {
        // Get the jobHistory
        restJobHistoryMockMvc.perform(get("/api/job-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobHistory() throws Exception {
        // Initialize the database
        jobHistoryRepository.saveAndFlush(jobHistory);
        jobHistorySearchRepository.save(jobHistory);
        int databaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();

        // Update the jobHistory
        JobHistory updatedJobHistory = jobHistoryRepository.findOne(jobHistory.getId());
        updatedJobHistory
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

        restJobHistoryMockMvc.perform(put("/api/job-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJobHistory)))
            .andExpect(status().isOk());

        // Validate the JobHistory in the database
        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeUpdate);
        JobHistory testJobHistory = jobHistoryList.get(jobHistoryList.size() - 1);
        assertThat(testJobHistory.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testJobHistory.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
        assertThat(testJobHistory.getNoOfApplicants()).isEqualTo(UPDATED_NO_OF_APPLICANTS);
        assertThat(testJobHistory.getSalary()).isEqualTo(UPDATED_SALARY);
        assertThat(testJobHistory.getJobStatus()).isEqualTo(UPDATED_JOB_STATUS);
        assertThat(testJobHistory.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testJobHistory.getOriginalJobCost()).isEqualTo(UPDATED_ORIGINAL_JOB_COST);
        assertThat(testJobHistory.getJobCost()).isEqualTo(UPDATED_JOB_COST);
        assertThat(testJobHistory.getAmountPaid()).isEqualTo(UPDATED_AMOUNT_PAID);
        assertThat(testJobHistory.getTotalAmountPaid()).isEqualTo(UPDATED_TOTAL_AMOUNT_PAID);
        assertThat(testJobHistory.getNoOfApplicantsBought()).isEqualTo(UPDATED_NO_OF_APPLICANTS_BOUGHT);
        assertThat(testJobHistory.getRemovedFilterAmount()).isEqualTo(UPDATED_REMOVED_FILTER_AMOUNT);
        assertThat(testJobHistory.getAdditionalFilterAmount()).isEqualTo(UPDATED_ADDITIONAL_FILTER_AMOUNT);
        assertThat(testJobHistory.getAdminCharge()).isEqualTo(UPDATED_ADMIN_CHARGE);
        assertThat(testJobHistory.getAdminChargeRate()).isEqualTo(UPDATED_ADMIN_CHARGE_RATE);
        assertThat(testJobHistory.getUpfrontDiscountRate()).isEqualTo(UPDATED_UPFRONT_DISCOUNT_RATE);
        assertThat(testJobHistory.getUpfrontDiscountAmount()).isEqualTo(UPDATED_UPFRONT_DISCOUNT_AMOUNT);
        assertThat(testJobHistory.getEscrowAmountUsed()).isEqualTo(UPDATED_ESCROW_AMOUNT_USED);
        assertThat(testJobHistory.getEscrowAmountAdded()).isEqualTo(UPDATED_ESCROW_AMOUNT_ADDED);
        assertThat(testJobHistory.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testJobHistory.isHasBeenEdited()).isEqualTo(UPDATED_HAS_BEEN_EDITED);
        assertThat(testJobHistory.isEverActive()).isEqualTo(UPDATED_EVER_ACTIVE);
        assertThat(testJobHistory.isCanEdit()).isEqualTo(UPDATED_CAN_EDIT);
        assertThat(testJobHistory.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testJobHistory.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testJobHistory.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);

        // Validate the JobHistory in Elasticsearch
        JobHistory jobHistoryEs = jobHistorySearchRepository.findOne(testJobHistory.getId());
        assertThat(jobHistoryEs).isEqualToComparingFieldByField(testJobHistory);
    }

    @Test
    @Transactional
    public void updateNonExistingJobHistory() throws Exception {
        int databaseSizeBeforeUpdate = jobHistoryRepository.findAll().size();

        // Create the JobHistory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobHistoryMockMvc.perform(put("/api/job-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobHistory)))
            .andExpect(status().isCreated());

        // Validate the JobHistory in the database
        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJobHistory() throws Exception {
        // Initialize the database
        jobHistoryRepository.saveAndFlush(jobHistory);
        jobHistorySearchRepository.save(jobHistory);
        int databaseSizeBeforeDelete = jobHistoryRepository.findAll().size();

        // Get the jobHistory
        restJobHistoryMockMvc.perform(delete("/api/job-histories/{id}", jobHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean jobHistoryExistsInEs = jobHistorySearchRepository.exists(jobHistory.getId());
        assertThat(jobHistoryExistsInEs).isFalse();

        // Validate the database is empty
        List<JobHistory> jobHistoryList = jobHistoryRepository.findAll();
        assertThat(jobHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJobHistory() throws Exception {
        // Initialize the database
        jobHistoryRepository.saveAndFlush(jobHistory);
        jobHistorySearchRepository.save(jobHistory);

        // Search the jobHistory
        restJobHistoryMockMvc.perform(get("/api/_search/job-histories?query=id:" + jobHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobHistory.getId().intValue())))
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
        TestUtil.equalsVerifier(JobHistory.class);
        JobHistory jobHistory1 = new JobHistory();
        jobHistory1.setId(1L);
        JobHistory jobHistory2 = new JobHistory();
        jobHistory2.setId(jobHistory1.getId());
        assertThat(jobHistory1).isEqualTo(jobHistory2);
        jobHistory2.setId(2L);
        assertThat(jobHistory1).isNotEqualTo(jobHistory2);
        jobHistory1.setId(null);
        assertThat(jobHistory1).isNotEqualTo(jobHistory2);
    }
}
