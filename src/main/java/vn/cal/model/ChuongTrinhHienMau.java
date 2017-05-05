package vn.cal.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Default;
import org.zkoss.zul.Window;

import vn.cal.core.Asset;

@Entity
@Table(name = "chuong_trinh_hien_mau")
public class ChuongTrinhHienMau extends Asset<ChuongTrinhHienMau> {

	private String tenChuongTrinh;
	private List<ChiTietChuongTrinh> listChiTiet = new ArrayList<ChiTietChuongTrinh>();
	private Date ngayBatDau;
	private Date ngayKetThuc;
	private String moTa;
	private String cutMoTa;
	private ThanhPho thanhPho;
	private QuanHuyen quanHuyen;
	private List<DangKy> dangKys = new ArrayList<DangKy>();
	private List<ThanhPho> lThanhPho = new ArrayList<ThanhPho>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "chuongTrinhHienMau", cascade = CascadeType.ALL)
	public List<DangKy> getDangKys() {
		return dangKys;
	}

	public void setDangKys(List<DangKy> dangKys) {
		this.dangKys = dangKys;
	}

	@Transient
	public ThanhPho getThanhPho() {
		return thanhPho;
	}

	public void setThanhPho(ThanhPho thanhPho) {
		this.thanhPho = thanhPho;
	}
	
	@Transient
	public QuanHuyen getQuanHuyen() {
		return quanHuyen;
	}

	public void setQuanHuyen(QuanHuyen quanHuyen) {
		this.quanHuyen = quanHuyen;
	}

	@Column(length = 255)
	public String getTenChuongTrinh() {
		return tenChuongTrinh;
	}

	public void setTenChuongTrinh(String tenChuongTrinh) {
		this.tenChuongTrinh = tenChuongTrinh;
	}

	private boolean flagLoad;
	
	@Transient
	public List<ChiTietChuongTrinh> getListChiTiet() {
		if(!noId()){
			if(!flagLoad){
				flagLoad = true;
				listChiTiet = find(ChiTietChuongTrinh.class)
						.where(QChiTietChuongTrinh.chiTietChuongTrinh.trangThai.eq(getCore().TT_AP_DUNG))
						.where(QChiTietChuongTrinh.chiTietChuongTrinh.chuongTrinh.eq(this))
						.fetch();
			}
		}
		return listChiTiet;
	}

	public void setListChiTiet(List<ChiTietChuongTrinh> listChiTiet) {
		this.listChiTiet = listChiTiet;
	}

	@Temporal(TemporalType.DATE)
	@Column(length = 10)
	public Date getNgayBatDau() {
		return ngayBatDau;
	}

	public void setNgayBatDau(Date ngayBatDau) {
		this.ngayBatDau = ngayBatDau;
	}

	@Temporal(TemporalType.DATE)
	@Column(length = 10)
	public Date getNgayKetThuc() {
		return ngayKetThuc;
	}

	public void setNgayKetThuc(Date ngayKetThuc) {
		this.ngayKetThuc = ngayKetThuc;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}
	
	@Transient
	public String getCutMoTa() {
		return moTa.substring(0, 90);
	}

	public void setCutMoTa(String cutMoTa) {
		this.cutMoTa = cutMoTa;
	}

	@Command
	public void saveChuongTrinh(@BindingParam("list") final Object listObject, @BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) {
		setNgayTao(new Date());
		saveAsShowNotification();
		for (ChiTietChuongTrinh chiTiet : listChiTiet) {
			chiTiet.saveNotShowNotification();
		}
		wdn.detach();
		BindUtils.postNotifyChange(null, null, listObject, attr);
	}

	@Command
	public void deleteChiTiet(final @BindingParam("notify") Object beanObject,
			final @BindingParam("attr") @Default(value = "*") String attr) {
		final ChiTietChuongTrinh ctct = (ChiTietChuongTrinh) beanObject;
		ctct.setDaXoa(true);
		ctct.setTrangThai("da_xoa");
		if (!ctct.noId()) {
			ctct.saveNotShowNotification();
		}
		this.getListChiTiet().remove(ctct);
		BindUtils.postNotifyChange(null, null, this, attr);
	}
	
	public ChuongTrinhHienMau findById(Long id){
		ChuongTrinhHienMau cthm = new ChuongTrinhHienMau();
		cthm = find(ChuongTrinhHienMau.class)
				.where(QChuongTrinhHienMau.chuongTrinhHienMau.trangThai.ne(getCore().TT_DA_XOA))
				.where(QChuongTrinhHienMau.chuongTrinhHienMau.id.eq(id)).fetchFirst();
		return cthm;
	}
	
	@Transient
	public List<ThanhPho> getLThanhPho(){
		lThanhPho = find(ThanhPho.class)
				.where(QThanhPho.thanhPho.listChiTiet.any().chuongTrinh.eq(this))
				.fetch();
		return lThanhPho;
	}
}
