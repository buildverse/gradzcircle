/**
 * 
 */
package com.drishika.gradzcircle.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;

import io.github.jhipster.config.JHipsterConstants;
import io.github.jhipster.config.liquibase.AsyncSpringLiquibase;
import liquibase.exception.LiquibaseException;

/**
 * @author abhinav
 *
 */
public class AsynchSpringLiquibaseGradzcircle extends AsyncSpringLiquibase {
	
	  private final Logger logger = LoggerFactory.getLogger(AsynchSpringLiquibaseGradzcircle.class);

	    private final TaskExecutor taskExecutor;

	    private final Environment env;

	/**
	 * @param taskExecutor
	 * @param env
	 */
	public AsynchSpringLiquibaseGradzcircle(TaskExecutor taskExecutor, Environment env) {
		
		super(taskExecutor, env);
		 this.taskExecutor = taskExecutor;
	        this.env = env;
		// TODO Auto-generated constructor stub
	}
	
	 @Override
	    public void afterPropertiesSet() throws LiquibaseException {
	        if (!env.acceptsProfiles(JHipsterConstants.SPRING_PROFILE_NO_LIQUIBASE)) {
	            if (env.acceptsProfiles(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)) {
	                taskExecutor.execute(() -> {
	                    try {
	                        logger.warn("Starting Liquibase asynchronously, your database might not be ready at startup!");
	                        initDb();
	                    } catch (LiquibaseException e) {
	                        logger.error("Liquibase could not start correctly, your database is NOT ready: {}", e
	                            .getMessage(), e);
	                    }
	                });
	            } else {
	                logger.info("Starting Liquibase synchronously");
	                initDb();
	            }
	        } else {
	            logger.debug("Liquibase is disabled");
	        }
	    }


}
