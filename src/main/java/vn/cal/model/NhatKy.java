package vn.cal.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;

import vn.cal.core.Asset;

@Entity
@Table(name = "nhat_ky")
public class NhatKy extends Asset<NhatKy>{
	
	private Long doiTuongId;
	private String loaiDoiTuong;
	private String hanhDong;
	
	public Long getDoiTuongId() {
		return doiTuongId;
	}
	public void setDoiTuongId(Long doiTuongId) {
		this.doiTuongId = doiTuongId;
	}
	public String getLoaiDoiTuong() {
		return loaiDoiTuong;
	}
	public void setLoaiDoiTuong(String loaiDoiTuong) {
		this.loaiDoiTuong = loaiDoiTuong;
	}
	public String getHanhDong() {
		return hanhDong;
	}
	public void setHanhDong(String hanhDong) {
		this.hanhDong = hanhDong;
	}

	public NhatKy() {
	}
	
	public NhatKy(Long doiTuongId, String loaiDoiTuong, String hanhDong) {
		super();
		this.doiTuongId = doiTuongId;
		this.loaiDoiTuong = loaiDoiTuong;
		this.hanhDong = hanhDong;
	}
	
	@Command
	public void saveNhatKy(@BindingParam("list") final Object listObject, @BindingParam("attr") final String attr) {
		saveNotShowNotification();
		BindUtils.postNotifyChange(null, null, listObject, attr);
	}
	
	public String toString(){
		return "Bạn đã "+getHanhDong()+" "+getLoaiDoiTuong()+" vào lúc ";
	}
}
