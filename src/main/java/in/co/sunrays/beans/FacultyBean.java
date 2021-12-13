/**
 * 
 */
package in.co.sunrays.beans;

import java.util.Date;

/**
*
* Faculty JavaBean Encapsulates Faculty attributes.
* 
* @author SunilOS
* 
* @version 1.0
* 
* @Copyright (c) SunilOS
*
*/
public class FacultyBean extends BaseBean{
	
	
	private long Id;
	private long CollegeId;
	private String FirstName;
	private String LastName;
	private String CollegeName;
	private Date DOB;
	private String EmailId;
	private String MobileNo;
	private String Subject;
	private String Qualification;
	private String Gender;
	private Long SubjectId;
	private Long CourseId;
	private String CourseName;
	/**
	 * @return the id
	 */
	public long getId() {
		return Id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		Id = id;
	}
	/**
	 * @return the collegeId
	 */
	public long getCollegeId() {
		return CollegeId;
	}
	/**
	 * @param collegeId the collegeId to set
	 */
	public void setCollegeId(long collegeId) {
		CollegeId = collegeId;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return FirstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return LastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	/**
	 * @return the collegeName
	 */
	public String getCollegeName() {
		return CollegeName;
	}
	/**
	 * @param collegeName the collegeName to set
	 */
	public void setCollegeName(String collegeName) {
		CollegeName = collegeName;
	}
	/**
	 * @return the dOB
	 */
	public Date getDOB() {
		return DOB;
	}
	/**
	 * @param string the dOB to set
	 */
	public void setDOB(Date dob) {
		DOB =dob;
	}
	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return EmailId;
	}
	/**
	 * @param emailId the emailId to set
	 */
	public void setEmailId(String emailId) {
		EmailId = emailId;
	}
	/**
	 * @return the mobileNo
	 */
	public String getMobileNo() {
		return MobileNo;
	}
	/**
	 * @param mobileNo the mobileNo to set
	 */
	public void setMobileNo(String mobileNo) {
		MobileNo = mobileNo;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return Subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		Subject = subject;
	}
	/**
	 * @return the qualification
	 */
	public String getQualification() {
		return Qualification;
	}
	/**
	 * @param qualification the qualification to set
	 */
	public void setQualification(String qualification) {
		Qualification = qualification;
	}
	/**
	 * @return the gender
	 */
	public String getGender() {
		return Gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		Gender = gender;
	}
	/**
	 * @return the subjectId
	 */
	public Long getSubjectId() {
		return SubjectId;
	}
	/**
	 * @param subjectId the subjectId to set
	 */
	public void setSubjectId(Long subjectId) {
		SubjectId = subjectId;
	}
	/**
	 * @return the courseId
	 */
	public Long getCourseId() {
		return CourseId;
	}
	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(Long courseId) {
		CourseId = courseId;
	}
	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return CourseName;
	}
	/**
	 * @param courseName the courseName to set
	 */
	public void setCourseName(String courseName) {
		CourseName = courseName;
	}
	
	@Override
	public String getValue() {
		
		return Id+"";
	}
	
	
	@Override
	public String getKey() {
		
		return FirstName+" "+LastName;
	}
}
