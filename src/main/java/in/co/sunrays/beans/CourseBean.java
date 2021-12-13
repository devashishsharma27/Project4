/**
 * 
 */
package in.co.sunrays.beans;

/**
*
* Course JavaBean Encapsulates Course attributes.
* 
* @author SunilOS
* 
* @version 1.0
* 
* @Copyright (c) SunilOS
*
*/
public class CourseBean extends BaseBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Course name 
	 */
	private String name;
	/**
	 * Course duration
	 */
	private String duration;
	/**
	 * Course description 
	 */
	private String description;
	
	
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
	 * @return the duration
	 */
	public String getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(String duration) {
		this.duration = duration;
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
	
	
	@Override
	public String getKey() {
		
		return id+ "";
	}
	
	@Override
	public String getValue() {
		
		return name;
	}
	

}
