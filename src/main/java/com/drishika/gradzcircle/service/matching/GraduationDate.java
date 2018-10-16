/**
 * 
 */
package com.drishika.gradzcircle.service.matching;

import java.time.LocalDate;

/**
 * @author abhinav
 *
 */
public class GraduationDate {

	private int year;
	private int month;
	private int day;

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @param month
	 *            the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * @param day
	 *            the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}

	public LocalDate getGraduationDate() {
		return LocalDate.of(year, month, day);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GraduationDate [year=" + year + ", month=" + month + ", day=" + day + "]";
	}

}
