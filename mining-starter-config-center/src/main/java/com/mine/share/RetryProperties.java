

package com.mine.share;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.cloud.config.retry")
public class RetryProperties {

	/**
	 * Initial retry interval in milliseconds.
	 */
	long initialInterval = 1000;
	/**
	 * Multiplier for next interval.
	 */
	double multiplier = 1.1;
	/**
	 * Maximum interval for backoff.
	 */
	long maxInterval = 2000;
	/**
	 * Maximum number of attempts.
	 */
	int maxAttempts = 6;

	public long getInitialInterval() {
		return this.initialInterval;
	}

	public void setInitialInterval(long initialInterval) {
		this.initialInterval = initialInterval;
	}

	public double getMultiplier() {
		return this.multiplier;
	}

	public void setMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}

	public long getMaxInterval() {
		return this.maxInterval;
	}

	public void setMaxInterval(long maxInterval) {
		this.maxInterval = maxInterval;
	}

	public int getMaxAttempts() {
		return this.maxAttempts;
	}

	public void setMaxAttempts(int maxAttempts) {
		this.maxAttempts = maxAttempts;
	}

}
