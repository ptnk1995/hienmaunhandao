package vn.cal.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.Window;

import vn.cal.core.Asset;

@Entity
@Table(name = "quan_huyen")
public class QuanHuyen extends Asset<QuanHuyen> {

	private String tenQuanHuyen;
	private ThanhPho thanhPho;
	private List<ChiTietChuongTrinh> listChiTiet;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idThanhPho")
	public ThanhPho getThanhPho() {
		return thanhPho;
	}

	public void setThanhPho(ThanhPho thanhPho) {
		this.thanhPho = thanhPho;
	}
	
	public String getTenQuanHuyen() {
		return tenQuanHuyen;
	}

	public void setTenQuanHuyen(String tenQuanHuyen) {
		this.tenQuanHuyen = tenQuanHuyen;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "quanHuyen", cascade = CascadeType.ALL)
	public List<ChiTietChuongTrinh> getListChiTiet() {
		return listChiTiet;
	}
	public void setListChiTiet(List<ChiTietChuongTrinh> listChiTiet) {
		this.listChiTiet = listChiTiet;
	}
	@Command
	public void saveQuanHuyen(@BindingParam("list") final Object listObject, @BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) {
		saveAsShowNotification();
		wdn.detach();
		BindUtils.postNotifyChange(null, null, listObject, attr);
	}
}
