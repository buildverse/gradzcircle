/**
 * 
 */
package com.drishika.gradzcircle.service.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author abhinav
 *
 */
@Service
public class JobStatisticsCacheManager<K, V> {
	
	private final Logger log = LoggerFactory.getLogger(JobStatisticsCacheManager.class);

	private final ConcurrentMap<K, Future<V>> jobStatCache = new ConcurrentHashMap<>();

	private Future<V> createFutureIfAbsent(final K key, final Callable<V> callable) {
		Future<V> future = jobStatCache.get(key);
		if (future == null) {
			log.info("Loading data from Database for {} " ,key);
			final FutureTask<V> futureTask = new FutureTask<V>(callable);
			future = jobStatCache.putIfAbsent(key, futureTask);
			if (future == null) {
				future = futureTask;
				futureTask.run();
			}
		}
		return future;
	}

	public V getValue(final K key, final Callable<V> callable) throws InterruptedException, ExecutionException {
		try {
			final Future<V> future = createFutureIfAbsent(key, callable);
			return future.get();
		} catch (final InterruptedException e) {
			jobStatCache.remove(key);
			throw e;
		} catch (final ExecutionException e) {
			jobStatCache.remove(key);
			throw e;
		} catch (final RuntimeException e) {
			jobStatCache.remove(key);
			throw e;
		}
	}

	public void setValueIfAbsent(final K key, final V value) {
		createFutureIfAbsent(key, new Callable<V>() {
			@Override
			public V call() throws Exception {
				return value;
			}
		});
	}
	
	public void removeFromCache(final K key) {
		jobStatCache.remove(key);
	}
	
	public void clearCache() {
		jobStatCache.clear();
	}

}
