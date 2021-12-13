package in.co.sunrays.beans;

import java.util.Date;
import java.io.Serializable;
import java.sql.Timestamp;

public abstract class BaseBean implements Serializable, DropdownListBean, Comparable<BaseBean> {

	private static final long serialVersionUID = 1L;
	protected long id;
	protected String createdBy;
	protected String modifiedBy;
	protected Timestamp createdDateTime;
	protected Timestamp modifiedDateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Timestamp createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public Timestamp getModifiedDateTime() {
		return modifiedDateTime;
	}

	public void setModifiedDateTime(Timestamp modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}
	
	 public int compareTo(BaseBean next) {
	        return getValue().compareTo(next.getValue());
	    }
	 
	 /**
		 * @return
		 */
		public String getKey() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * @return
		 */
		public String getValue() {
			// TODO Auto-generated method stub
			return null;
		}
}
