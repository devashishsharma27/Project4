/**
 * 
 */
package in.co.sunrays.beans;
/**
*
* Subject JavaBean Encapsulates Subject attributes.
* 
* @author SunilOS
* 
* @version 1.0
* 
* @Copyright (c) SunilOS
*
*/
public class SubjectBean extends BaseBean{
	/**
	 *Subject description 
	 */
	private String description;
	/**
	 *Subject coursename 
	 */
	private String courseName;
	/**
	 *Subject courseId 
	 */
	private long courseId;
	
	/**
	 * subject name 
	 */
	private String name;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
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
	
	@Override
    public String getKey() {
        return id + "";
    }

    @Override
    public String getValue() {
        return name;
    }
}
