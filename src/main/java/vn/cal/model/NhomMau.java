package vn.cal.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.Window;

import vn.cal.core.Asset;

@Entity
@Table(name="nhom_mau")
public class NhomMau extends Asset<NhomMau> {
	
	@Column(length = 255)
	private String tenNhom;

	private LoaiMau loaiMau;
	
	private List<NhanVien> listNhanVien = new ArrayList<NhanVien>();
	
	private List<DeNghi> listDeNghi = new ArrayList<DeNghi>();
	
	private List<BaiDang> baiDangs = new ArrayList<BaiDang>();
	
	private List<DangKy> dangKys = new ArrayList<DangKy>();
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy ="nhomMau")
	public List<DangKy> getDangKys() {
		return dangKys;
	}
	
	public void setDangKys(List<DangKy> dangKys) {
		this.dangKys = dangKys;
	}
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "nhomMaus", cascade = CascadeType.ALL)
	public List<BaiDang> getBaiDangs() {
		return baiDangs;
	}
	
	public void setBaiDangs(List<BaiDang> baiDangs) {
		this.baiDangs = baiDangs;
	}
	
	public String getTenNhom() {
		return tenNhom;
	}
	
	public void setTenNhom(String tenNhom) {
		this.tenNhom = tenNhom;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idLoaiMau")
	public LoaiMau getLoaiMau() {
		return loaiMau;
	}
	
	public void setLoaiMau(LoaiMau loaiMau) {
		this.loaiMau = loaiMau;
	}
	
	@Transient
	public List<NhanVien> getListNhanVien() {
		listNhanVien = find(NhanVien.class).where(QNhanVien.nhanVien.trangThai.eq(getCore().TT_AP_DUNG))
				.where(QNhanVien.nhanVien.nhomMau.eq(this))
				.fetch();
		return listNhanVien;
	}
	public void setListNhanVien(List<NhanVien> listNhanVien) {
		this.listNhanVien = listNhanVien;
	}
	
	@Transient
	public List<DeNghi> getListDeNghi() {
		listDeNghi = find(DeNghi.class).where(QDeNghi.deNghi.nhomMau.eq(this)).fetch();
		return listDeNghi;
	}
	public void setListDeNghi(List<DeNghi> listDeNghi) {
		this.listDeNghi = listDeNghi;
	}
	
	@Command
	public void saveNhomMau(@BindingParam("list") final Object listObject, @BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn){
		saveAsShowNotification();
		wdn.detach();
		BindUtils.postNotifyChange(null, null, listObject, attr);
	}
	
}
