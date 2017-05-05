package vn.cal.core;

import java.util.Date;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Asset<T extends Asset<T>> extends Model<T> {
	private Date publishBeginTime;
	private Date publishEndTime;
	
	public  Date getPublishBeginTime() {
		return publishBeginTime;
	}

	public void setPublishBeginTime( Date publishBeginTime1) {
		this.publishBeginTime = publishBeginTime1;
	}

	public  Date getPublishEndTime() {
		return publishEndTime;
	}

	public void setPublishEndTime( Date publishEndTime1) {
		this.publishEndTime = publishEndTime1;
	}
}
