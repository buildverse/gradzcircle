package com.drishika.gradzcircle.web.rest;

import com.drishika.gradzcircle.GradzcircleApp;

import com.drishika.gradzcircle.domain.Skills;
import com.drishika.gradzcircle.repository.SkillsRepository;
import com.drishika.gradzcircle.repository.search.SkillsSearchRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SkillsResource REST controller.
 *
 * @see SkillsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradzcircleApp.class)
public class SkillsResourceIntTest {

    private static final String DEFAULT_SKILL = "AAAAAAAAAA";
    private static final String UPDATED_SKILL = "BBBBBBBBBB";

    @Autowired
    private SkillsRepository skillsRepository;

    @Autowired
    private SkillsSearchRepository skillsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSkillsMockMvc;

    private Skills skills;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SkillsResource skillsResource = new SkillsResource(skillsRepository, skillsSearchRepository);
        this.restSkillsMockMvc = MockMvcBuilders.standaloneSetup(skillsResource)
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
    public static Skills createEntity(EntityManager em) {
        Skills skills = new Skills()
            .skill(DEFAULT_SKILL);
        return skills;
    }

    @Before
    public void initTest() {
        skillsSearchRepository.deleteAll();
        skills = createEntity(em);
    }

    @Test
    @Transactional
    public void createSkills() throws Exception {
        int databaseSizeBeforeCreate = skillsRepository.findAll().size();

        // Create the Skills
        restSkillsMockMvc.perform(post("/api/skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skills)))
            .andExpect(status().isCreated());

        // Validate the Skills in the database
        List<Skills> skillsList = skillsRepository.findAll();
        assertThat(skillsList).hasSize(databaseSizeBeforeCreate + 1);
        Skills testSkills = skillsList.get(skillsList.size() - 1);
        assertThat(testSkills.getSkill()).isEqualTo(DEFAULT_SKILL);

        // Validate the Skills in Elasticsearch
        Skills skillsEs = skillsSearchRepository.findOne(testSkills.getId());
        assertThat(skillsEs).isEqualToComparingFieldByField(testSkills);
    }

    @Test
    @Transactional
    public void createSkillsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = skillsRepository.findAll().size();

        // Create the Skills with an existing ID
        skills.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSkillsMockMvc.perform(post("/api/skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skills)))
            .andExpect(status().isBadRequest());

        // Validate the Skills in the database
        List<Skills> skillsList = skillsRepository.findAll();
        assertThat(skillsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSkills() throws Exception {
        // Initialize the database
        skillsRepository.saveAndFlush(skills);

        // Get all the skillsList
        restSkillsMockMvc.perform(get("/api/skills?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skills.getId().intValue())))
            .andExpect(jsonPath("$.[*].skill").value(hasItem(DEFAULT_SKILL.toString())));
    }

    @Test
    @Transactional
    public void getSkills() throws Exception {
        // Initialize the database
        skillsRepository.saveAndFlush(skills);

        // Get the skills
        restSkillsMockMvc.perform(get("/api/skills/{id}", skills.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(skills.getId().intValue()))
            .andExpect(jsonPath("$.skill").value(DEFAULT_SKILL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSkills() throws Exception {
        // Get the skills
        restSkillsMockMvc.perform(get("/api/skills/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSkills() throws Exception {
        // Initialize the database
        skillsRepository.saveAndFlush(skills);
        skillsSearchRepository.save(skills);
        int databaseSizeBeforeUpdate = skillsRepository.findAll().size();

        // Update the skills
        Skills updatedSkills = skillsRepository.findOne(skills.getId());
        updatedSkills
            .skill(UPDATED_SKILL);

        restSkillsMockMvc.perform(put("/api/skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSkills)))
            .andExpect(status().isOk());

        // Validate the Skills in the database
        List<Skills> skillsList = skillsRepository.findAll();
        assertThat(skillsList).hasSize(databaseSizeBeforeUpdate);
        Skills testSkills = skillsList.get(skillsList.size() - 1);
        assertThat(testSkills.getSkill()).isEqualTo(UPDATED_SKILL);

        // Validate the Skills in Elasticsearch
        Skills skillsEs = skillsSearchRepository.findOne(testSkills.getId());
        assertThat(skillsEs).isEqualToComparingFieldByField(testSkills);
    }

    @Test
    @Transactional
    public void updateNonExistingSkills() throws Exception {
        int databaseSizeBeforeUpdate = skillsRepository.findAll().size();

        // Create the Skills

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSkillsMockMvc.perform(put("/api/skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skills)))
            .andExpect(status().isCreated());

        // Validate the Skills in the database
        List<Skills> skillsList = skillsRepository.findAll();
        assertThat(skillsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSkills() throws Exception {
        // Initialize the database
        skillsRepository.saveAndFlush(skills);
        skillsSearchRepository.save(skills);
        int databaseSizeBeforeDelete = skillsRepository.findAll().size();

        // Get the skills
        restSkillsMockMvc.perform(delete("/api/skills/{id}", skills.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean skillsExistsInEs = skillsSearchRepository.exists(skills.getId());
        assertThat(skillsExistsInEs).isFalse();

        // Validate the database is empty
        List<Skills> skillsList = skillsRepository.findAll();
        assertThat(skillsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSkills() throws Exception {
        // Initialize the database
        skillsRepository.saveAndFlush(skills);
        skillsSearchRepository.save(skills);

        // Search the skills
        restSkillsMockMvc.perform(get("/api/_search/skills?query=id:" + skills.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skills.getId().intValue())))
            .andExpect(jsonPath("$.[*].skill").value(hasItem(DEFAULT_SKILL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Skills.class);
        Skills skills1 = new Skills();
        skills1.setId(1L);
        Skills skills2 = new Skills();
        skills2.setId(skills1.getId());
        assertThat(skills1).isEqualTo(skills2);
        skills2.setId(2L);
        assertThat(skills1).isNotEqualTo(skills2);
        skills1.setId(null);
        assertThat(skills1).isNotEqualTo(skills2);
    }
}
