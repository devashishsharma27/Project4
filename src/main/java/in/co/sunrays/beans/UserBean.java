package in.co.sunrays.beans;

import java.util.Date;

public class UserBean extends BaseBean{
	
	
	private String firstName ;
	private String lastName ;
	private String loginId ;
	private String password ;
	private String confirmPassword ;
	private Date dob ;
	private String mobNo ;
	private String gender ;
	private long roleId ;
	
	
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lname) {
		this.lastName = lname;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getMobNo() {
		return mobNo;
	}
	public void setMobNo(String mobNo) {
		this.mobNo = mobNo;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getValue() {
		
		return null;
	}
	public String getKey() {
		
		return null;
	}
	@Override
	public String toString() {
		return "UserBean [firstName=" + firstName + ", lastName=" + lastName + ", loginId=" + loginId + ", password="
				+ password + ", dob=" + dob + ", mobNo=" + mobNo + ", gender=" + gender + ", roleId=" + roleId + "]";
	}

}
