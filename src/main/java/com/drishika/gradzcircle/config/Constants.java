package com.drishika.gradzcircle.config;

/**
 * Application constants.
 */
public final class Constants {

	// Regex for acceptable logins
	public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

	public static final String SYSTEM_ACCOUNT = "system";
	public static final String ANONYMOUS_USER = "anonymoususer";
	public static final String DEFAULT_LANGUAGE = "en";
	public static final String SUCCESS = "SUCCESS";
	public static final String ERROR = "ERROR";
	public static final String AWS_REGION = "ap-south-1";
	public static final String OTHER = "Other";
	public static final String COURSE = "course";
	public static final String GRADUATION_DATE = "gradDate";
	public static final String SCORE = "score";
	public static final String COLLEGE = "colleges";
	public static final String UNIVERSITY = "universities";
	public static final String LANGUAGE = "languages";
	public static final String GENDER = "gender";
	public static final String SKILL = "skill";
	public static final String QUALIFICATION = "qualification";
	public static final String GRADUATION_DATE_GREATER = "greater";
	public static final String GRADUATION_DATE_LESS = "less";
	public static final String GRADUATION_DATE_BETWEEN = "between";

	public static final String PERCENT = "percent";
	public static final String GPA = "gpa";
	/*
	 * @ TODO check feasibility of moving to database
	 */

	public static final Integer SCORE_THRESHOLD = 2;
	
	public static final String ENTITY_COLLEGE = "college";
	public static final String ENTITY_UNIVERSITY = "university";
	public static final String ENTITY_LANGUAGE = "language";
	public static final String ENTITY_GENDER = "gender";
	public static final String ENTITY_QUALIFICATION = "qualification";
	public static final String ENTITY_JOB_CATEGORY = "jobcategory";
	public static final String ENTITY_INDUSTRY = "industry";
	public static final String ENTITY_MARITAL_STATUS = "maritalstatus";
	public static final String ENTITY_COUNTRY = "country";
	public static final String ENTITY_NATIONALITY = "nationality";
	public static final String ENTITY_COURSE = "course";
	public static final String ENTITY_SKILL = "skill";

	/* PROFILE CATEGORIES */
	
	public static final String CANDIDATE_EDUCATION_PROFILE = "education";
	public static final String CANDIDATE_PERSONAL_DETAIL_PROFILE = "personalDetails";
	public static final String CANDIDATE_EXPERIENCE_PROFILE = "experience";
	public static final String CANDIDATE_CERTIFICATION_PROFILE = "certification";
	public static final String CANDIDATE_LANGUAGE_PROFILE = "language";
	public static final String CANDIDATE_NON_ACADEMIC_PROFILE= "nonAcademic";
	public static final String CANDIDATE_PROFILE_TOTAL_WEIGHT = "totalWeight";
	public static final String CANDIDATE_BASIC_PROFILE = "basicDetails";
	public static final String CANDIDATE_SKILL_PROFILE = "skill";
	
}
