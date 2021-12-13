/**
 * 
 */
package in.co.sunrays.beans;

import java.util.Date;

/**
 *
 * Timetable JavaBean Encapsulates Timetable attributes.
 * 
 * @author SunilOS
 * 
 * @version 1.0
 * 
 * @Copyright (c) SunilOS
 *
 */

public class TimetableBean extends BaseBean{
	

	/**
	 * 
	 * Course name 
	 */
	private String courseName;
	/**
	 * 
	 * Course Id 
	 */
	private long courseId;
	
	/**
	 * 
	 * Subject name 
	 */
	private String subjectName;

	/**
	 * 
	 * Subject Id 
	 */
	private long subjectId;
	
	/**
	 * 
	 * Exam Date
	 */
    private Date examDate;
	
    /**
	 * 
	 * Exam Time
	 */
    private String examTime;
    
    /**
	 * 
	 * semester
	 */
    private int semester;

	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}

	/**
	 * @param courseName the courseName to set
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	/**
	 * @return the courseId
	 */
	public long getCourseId() {
		return courseId;
	}

	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}

	/**
	 * @return the subjectName
	 */
	public String getSubjectName() {
		return subjectName;
	}

	/**
	 * @param subjectName the subjectName to set
	 */
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	/**
	 * @return the subjectId
	 */
	public long getSubjectId() {
		return subjectId;
	}

	/**
	 * @param subjectId the subjectId to set
	 */
	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

	/**
	 * @return the examDate
	 */
	public Date getExamDate() {
		return examDate;
	}

	/**
	 * @param examDate the examDate to set
	 */
	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}

	/**
	 * @return the examTime
	 */
	public String getExamTime() {
		return examTime;
	}

	/**
	 * @param examTime the examTime to set
	 */
	public void setExamTime(String examTime) {
		this.examTime = examTime;
	}

	/**
	 * @return the semester
	 */
	public int getSemester() {
		return semester;
	}

	/**
	 * @param semester the semester to set
	 */
	public void setSemester(int semester) {
		this.semester = semester;
	}

	/* (non-Javadoc)
	 * @see in.co.sunrays.proj4.bean.DropdownListBean#getKey()
	 */
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see in.co.sunrays.proj4.bean.DropdownListBean#getValue()
	 */
	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "TimetableBean [courseName=" + courseName + ", courseId=" + courseId + ", subjectName=" + subjectName
				+ ", subjectId=" + subjectId + ", examDate=" + examDate + ", examTime=" + examTime + ", semester="
				+ semester + "]";
	}

}