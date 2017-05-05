package vn.cal.model;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.Window;

import vn.cal.core.Asset;

@Entity
@Table(name = "chi_tiet_chuong_trinh")
public class ChiTietChuongTrinh extends Asset<ChiTietChuongTrinh> {
	
	private ChuongTrinhHienMau chuongTrinh;
	private ThanhPho thanhPho;
	private QuanHuyen quanHuyen;
	@Column(length=255)
	private String diaChi;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idChuongTrinh")
	public ChuongTrinhHienMau getChuongTrinh() {
		return chuongTrinh;
	}
	public void setChuongTrinh(ChuongTrinhHienMau chuongTrinh) {
		this.chuongTrinh = chuongTrinh;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idThanhPho")
	public ThanhPho getThanhPho() {
		return thanhPho;
	}
	public void setThanhPho(ThanhPho thanhPho) {
		this.thanhPho = thanhPho;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idQuanHuyen")
	public QuanHuyen getQuanHuyen() {
		return quanHuyen;
	}
	public void setQuanHuyen(QuanHuyen quanHuyen) {
		this.quanHuyen = quanHuyen;
	}
	public String getDiaChi() {
		return diaChi;
	}
	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}
	
	@Command
	public void saveChiTiet(@BindingParam("vm") final Object _object, @BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) {
		chuongTrinh = (ChuongTrinhHienMau)_object;
		if(chuongTrinh != null){
			chuongTrinh.getListChiTiet().add(this);
		}
		wdn.detach();
		BindUtils.postNotifyChange(null, null, _object, "listChiTiet");
	}
	
	@Command
	public void selectedThanhPho() {
		System.out.println("thanhPho " +thanhPho);
		BindUtils.postNotifyChange(null, null, this, "thanhPho");
		BindUtils.postNotifyChange(null, null, this, "listQuanHuyen");
	}
	
	@Transient
	public List<QuanHuyen> getListQuanHuyen(){
		System.out.println("getListQuanHuyen1 " +thanhPho);
		List<QuanHuyen> list = new ArrayList<QuanHuyen>();
		if(thanhPho != null) {
			System.out.println("getListQuanHuyen1");
			list = getCore().getQuanHuyenService().getByThanhPho(thanhPho).fetch();
		}
		return list;
	}
	
	@Transient
	public List<ThanhPho> getListThanhPho(){
		return getCore().getThanhPhoService().getTargetQuery().fetch();
	}
}
