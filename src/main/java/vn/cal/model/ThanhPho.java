package vn.cal.model;

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
@Table(name = "thanh_pho")
public class ThanhPho extends Asset<ThanhPho>{
	
	private String tenThanhPho;
	private List<QuanHuyen> listQuanHuyen;
	private List<ChiTietChuongTrinh> listChiTiet;
	private List<BaiDang> listBaiDang;
	
	public String getTenThanhPho() {
		return tenThanhPho;
	}
	public void setTenThanhPho(String tenThanhPho) {
		this.tenThanhPho = tenThanhPho;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "thanhPho", cascade = CascadeType.ALL)
	public List<QuanHuyen> getListQuanHuyen() {
		return listQuanHuyen;
	}
	public void setListQuanHuyen(List<QuanHuyen> listQuanHuyen) {
		this.listQuanHuyen = listQuanHuyen;
	}
	//@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="listThanhPho")
	/*public List<ChuongTrinhHienMau> getListChuongTrinh() {
		return listChuongTrinh;
	}
	public void setListChuongTrinh(List<ChuongTrinhHienMau> listChuongTrinh) {
		this.listChuongTrinh = listChuongTrinh;
	}*/
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "thanhPho", cascade = CascadeType.ALL)
	public List<ChiTietChuongTrinh> getListChiTiet() {
		return listChiTiet;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "thanhPho", cascade = CascadeType.ALL)
	public List<BaiDang> getListBaiDang() {
		return listBaiDang;
	}
	public void setListBaiDang(List<BaiDang> listBaiDang) {
		this.listBaiDang = listBaiDang;
	}
	public void setListChiTiet(List<ChiTietChuongTrinh> listChiTiet) {
		this.listChiTiet = listChiTiet;
	}
	@Command
	public void saveThanhPho(@BindingParam("list") final Object listObject, @BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) {
		saveAsShowNotification();
		wdn.detach();
		BindUtils.postNotifyChange(null, null, listObject, attr);
	}
}
