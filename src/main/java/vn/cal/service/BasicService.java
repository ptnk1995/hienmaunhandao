package vn.cal.service;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Nullable;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import vn.cal.core.BaseObject;
import vn.cal.model.Quyen;

public class BasicService<T> extends BaseObject<T> {
	private @Nullable Date tuNgay;
	private @Nullable Date denNgay;
	
	public @Nullable Date getTuNgay() {
		return tuNgay;
	}

	public void setTuNgay(Date _tuNgay) {
		this.tuNgay = _tuNgay;
	}

	public @Nullable Date getFixTuNgay() {
		Date fixTuNgay = tuNgay;
		if(fixTuNgay !=null){
			Calendar cal = Calendar.getInstance();
			cal.setTime(fixTuNgay);
			//cal.add(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			fixTuNgay = cal.getTime();
		}
		return fixTuNgay;
	}
	
	public @Nullable Date getDenNgay() {
		return denNgay;
	}
	
	public void setDenNgay(Date _denNgay) {
		this.denNgay = _denNgay;
	}
	
	public @Nullable Date getFixDenNgay() {
		Date fixDenNgay = denNgay;
		if(getDenNgay()!=null){
			Calendar cal = Calendar.getInstance();
			cal.setTime(fixDenNgay);
			//cal.add(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			fixDenNgay = cal.getTime();
		}
		return fixDenNgay;
	}
	
	public final Quyen getQuyen() {
		return new Quyen(super.getQuyen().getRealm(), MapUtils.getString(argDeco(),Labels.getLabel("param.resource"),""));
	}
	
	public final Quyen getQuyen(String resource) {
		return new Quyen(super.getQuyen().getRealm(),resource);
	}

}
