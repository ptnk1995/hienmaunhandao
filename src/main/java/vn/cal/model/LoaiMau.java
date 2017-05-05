package vn.cal.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.Window;

import vn.cal.core.Asset;

@Entity
@Table(name="loai_mau")
public class LoaiMau extends Asset<LoaiMau>{
	
	@Column(length = 255)
	private String tenLoai;
	private String ghiChu;
	private List<DeNghi> listDeNghi = new ArrayList<DeNghi>();

	private List<NhomMau> listNhomMau;
	
	public String getTenLoai() {
		return tenLoai;
	}
	public void setTenLoai(String tenLoai) {
		this.tenLoai = tenLoai;
	}
	public String getGhiChu() {
		return ghiChu;
	}
	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "loaiMau", cascade = CascadeType.ALL)
	public List<NhomMau> getListNhomMau() {
		return listNhomMau;
	}
	public void setListNhomMau(List<NhomMau> listNhomMau) {
		this.listNhomMau = listNhomMau;
	}
	
	@Transient
	public List<DeNghi> getListDeNghi() {
		listDeNghi = find(DeNghi.class).where(QDeNghi.deNghi.loaiMau.eq(this)).fetch();
		return listDeNghi;
	}
	public void setListDeNghi(List<DeNghi> listDeNghi) {
		this.listDeNghi = listDeNghi;
	}
	@Command
	public void saveLoaiMau(@BindingParam("list") final Object listObject, @BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn){
		saveAsShowNotification();
		wdn.detach();
		BindUtils.postNotifyChange(null, null, listObject, attr);
	}
}
