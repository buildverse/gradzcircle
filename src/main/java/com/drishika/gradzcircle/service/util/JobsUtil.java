/**
 * 
 */
package com.drishika.gradzcircle.service.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drishika.gradzcircle.domain.Corporate;
import com.drishika.gradzcircle.domain.Job;
import com.drishika.gradzcircle.domain.JobFilter;
import com.drishika.gradzcircle.exception.BeanCopyException;

/**
 * @author abhinav
 *
 */
public class JobsUtil {

	private static final Logger log = LoggerFactory.getLogger(JobsUtil.class);

	private JobsUtil() {

	}

	public static void trimCorporateFromJob(Job job) {
		Corporate corporate = new Corporate();
		corporate.setId(job.getCorporate().getId());
		corporate.setEscrowAmount(job.getCorporate().getEscrowAmount());
		corporate.setLogin(job.getCorporate().getLogin());
		job.setCorporate(corporate);
	}

	public static void trimJobFromFilter(Job job) {

		if (job.getJobFilters() != null) {
			job.getJobFilters().forEach(jobFilter -> {
				jobFilter.setJob(null);
			});
		}

	}

	public static void trimJobFromFilter(Set<JobFilter> jobFilters) {

		if (jobFilters != null) {
			jobFilters.forEach(jobFilter -> {
				jobFilter.setJob(null);
			});
		}

	}

	public static void populateHistories(Object destination, Object source) throws BeanCopyException {
		try {
			BeanUtils.copyProperties(destination, source);
		} catch (IllegalAccessException e) {
			log.error("Error creating job history object", e);
			throw new BeanCopyException(e.getMessage(), e.getCause());
		} catch (InvocationTargetException e) {
			log.error("Error creating job history object", e);
			throw new BeanCopyException(e.getMessage(), e.getCause());
		}
	}

}
