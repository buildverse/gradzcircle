package com.drishika.gradzcircle.config;

/**
 * Application constants.
 */
public final class Constants {

    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String SUCCESS ="SUCCESS";
    public static final String ERROR ="ERROR";
    public static final String AWS_REGION="us-west-1";
    public static final String OTHER = "Other";
    public static final String COURSE = "course";
    public static final String GRADUATION_DATE = "gradDate";
    public static final String SCORE = "score";
    public static final String COLLEGE = "colleges";
    public static final String UNIVERSITY = "universities";
    public static final String LANGUAGE = "languages";
    public static final String GENDER = "gender";
    public static final String QUALIFICATION = "qualification";
    public static final String GRADUATION_DATE_GREATER ="greater";
    public static final String GRADUATION_DATE_LESS ="less";
    public static final String GRADUATION_DATE_BETWEEN ="between";
    /*@ TODO
     * check feasibility of moving to database
     */
    
    public static final Integer SCORE_THRESHOLD = 2;
 
}
