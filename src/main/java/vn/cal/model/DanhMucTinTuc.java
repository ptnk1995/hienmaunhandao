package vn.cal.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.Window;

import vn.cal.core.Asset;

@Entity
@Table(name="danh_muc_tin_tuc")
public class DanhMucTinTuc extends Asset<DanhMucTinTuc>{
	
	private String tenDanhMuc;
	private List<TinTuc> listTinTuc = new ArrayList<TinTuc>(); 

	public String getTenDanhMuc() {
		return tenDanhMuc;
	}

	public void setTenDanhMuc(String tenDanhMuc) {
		this.tenDanhMuc = tenDanhMuc;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "danhMuc", cascade = CascadeType.ALL)
	public List<TinTuc> getListTinTuc() {
		return listTinTuc;
	}

	public void setListTinTuc(List<TinTuc> listTinTuc) {
		this.listTinTuc = listTinTuc;
	}
	
	@Command
	public void saveDanhMuc(@BindingParam("list") final Object listObject, @BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) {
		saveAsShowNotification();
		wdn.detach();
		BindUtils.postNotifyChange(null, null, listObject, attr);
	}
}
