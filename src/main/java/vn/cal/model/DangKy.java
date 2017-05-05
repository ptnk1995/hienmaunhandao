package vn.cal.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

import vn.cal.core.Asset;

@Entity
@Table(name = "dang_ky")
public class DangKy extends Asset<DangKy>{

	private String hoTen;
	private String email;
	private String soDienThoai;
	private int soLanHienTruoc;
	private Date thoiGianVuaHien;
	private Date thoiGianSapHien;
	private float theTich;
	private NhomMau nhomMau;
	private ChuongTrinhHienMau chuongTrinhHienMau;
	private String trangThaiDangKy = "waiting";
	private long noDay = 0 ;
	
	public String getTrangThaiDangKy() {
		return trangThaiDangKy;
	}
	public void setTrangThaiDangKy(String trangThaiDangKy) {
		this.trangThaiDangKy = trangThaiDangKy;
	}
	public String getHoTen() {
		return hoTen;
	}
	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSoDienThoai() {
		return soDienThoai;
	}
	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}
	public int getSoLanHienTruoc() {
		return soLanHienTruoc;
	}
	public void setSoLanHienTruoc(int soLanHienTruoc) {
		this.soLanHienTruoc = soLanHienTruoc;
		System.out.println("getSoLanHienTruoc()"+ soLanHienTruoc);
	}
	@Temporal(TemporalType.DATE)
	@Column(length = 10)
	public Date getThoiGianVuaHien() {
		return thoiGianVuaHien;
	}
	public void setThoiGianVuaHien(Date thoiGianVuaHien) {
		this.thoiGianVuaHien = thoiGianVuaHien;
	}
	@Temporal(TemporalType.DATE)
	@Column(length = 10)
	public Date getThoiGianSapHien() {
		return thoiGianSapHien;
	}
	public void setThoiGianSapHien(Date thoiGianSapHien) {
		this.thoiGianSapHien = thoiGianSapHien;
		Calendar c1 = Calendar.getInstance();
	    Calendar c2 = Calendar.getInstance();
		System.out.println("thoiGianSapHien"+thoiGianSapHien);
		c1.setTime(thoiGianSapHien);
		if(getThoiGianVuaHien() != null && getThoiGianVuaHien().after(thoiGianSapHien)){
			c2.setTime(getThoiGianVuaHien());
			noDay = (c1.getTime().getTime() - c2.getTime().getTime())
			            / (24 * 3600 * 1000);
		}
	}
	public float getTheTich() {
		return theTich;
	}
	public void setTheTich(float theTich) {
		this.theTich = theTich;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	public NhomMau getNhomMau() {
		return nhomMau;
	}
	public void setNhomMau(NhomMau nhomMau) {
		this.nhomMau = nhomMau;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	public ChuongTrinhHienMau getChuongTrinhHienMau() {
		return chuongTrinhHienMau;
	}
	public void setChuongTrinhHienMau(ChuongTrinhHienMau chuongTrinhHienMau) {
		this.chuongTrinhHienMau = chuongTrinhHienMau;
	}
	@Command
	public void saveDangKy(@BindingParam("list") final Object listObject, @BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn){
		if(noDay >= 84){
			saveAsShowNotification();
		}
		else{
			showNotification("Bạn chưa đủ điều kiện để đăng ký hiến máu vào ngày "+getThoiGianSapHien(), "", "warning");
		}
		wdn.detach();
		BindUtils.postNotifyChange(null, null, listObject, attr);
	}
	

    @Command
    public void saveDangKyMoi(@BindingParam("vm") final Object vm, @BindingParam("ngTao") final Object nv){
    	DangKy dk = (DangKy) vm;
    	NhanVien nhanVien = (NhanVien) nv;
    	DangKy dkCu = getCore().getNhanVienService().getDangKy(nhanVien).fetchOne();
		Calendar c1 = Calendar.getInstance();
	    Calendar c2 = Calendar.getInstance();
		if(getSoLanHienTruoc() > 0){
			if(getThoiGianVuaHien() != null && getThoiGianSapHien() != null){
				System.out.println("thoiGianVuaHien"+thoiGianVuaHien);
				c2.setTime(getThoiGianVuaHien());
				c1.setTime(getThoiGianSapHien());
				noDay = (c1.getTime().getTime() - c2.getTime().getTime())
				            / (24 * 3600 * 1000);
			}
	    	if(nhanVien != null){
	    		dk.setHoTen(nhanVien.getHoVaTen());
	    		dk.setEmail(nhanVien.getEmail());
	    		dk.setSoDienThoai(nhanVien.getSoDienThoai());
	    		dk.setNguoiTao(nhanVien);
				if(noDay < 84){
					showNotification("Bạn chưa đủ điều kiện để đăng ký hiến máu, khoảng cách 2 lần hiến máu tối thiểu là 84 ngày, hãy chọn lại ngày phù hợp !", "", "warning");
				}
				else if(dkCu != null){
					if(dkCu.getTrangThaiDangKy() != "waiting")
						showNotification("Đơn đăng ký trước của bạn chưa được cập nhập !", "", "warning");
				}
				else{
					dk.save();
					NhatKy nhatKy = new NhatKy();
					nhatKy.setDoiTuongId(this.getId());
					nhatKy.setLoaiDoiTuong("hiến máu");
					nhatKy.setHanhDong("đăng ký");
					nhatKy.setNgayTao(new Date());
					nhatKy.setNguoiTao(nhanVien);
					nhatKy.saveNotShowNotification();
			    	Executions.sendRedirect("/c/thongtincanhan");
			    	showNotification("Đăng ký thành công!", "", "success");
				}
	    	}
	    	else{
	    		showNotification("Bạn cần đăng nhập để thực hiện chức năng này !", "", "warning");
	    		Executions.sendRedirect("/c/signin");
	    	}
		}
		else{
	    	if(nhanVien != null){
	    		dk.setHoTen(nhanVien.getHoVaTen());
	    		dk.setEmail(nhanVien.getEmail());
	    		dk.setSoDienThoai(nhanVien.getSoDienThoai());
	    		dk.setNguoiTao(nhanVien);
				if(dkCu != null){
					if(dkCu.getTrangThaiDangKy() != "waiting")
						showNotification("Đơn đăng ký trước của bạn chưa được cập nhập !", "", "warning");
				}
				else{
					dk.save();
					NhatKy nhatKy = new NhatKy();
					nhatKy.setDoiTuongId(this.getId());
					nhatKy.setLoaiDoiTuong("hiến máu");
					nhatKy.setHanhDong("đăng ký");
					nhatKy.setNgayTao(new Date());
					nhatKy.setNguoiTao(nhanVien);
					nhatKy.saveNotShowNotification();
			    	Executions.sendRedirect("/c/thongtincanhan");
			    	showNotification("Đăng ký thành công!", "", "success");
				}
	    	}
	    	else{
	    		showNotification("Bạn cần đăng nhập để thực hiện chức năng này !", "", "warning");
	    		Executions.sendRedirect("/c/signin");
	    	}
		}
    }
    
    @Command
    public void saveDangKyTam(@BindingParam("vm") final Object vm, @BindingParam("ngTao") final Object nv,
    		@BindingParam("chuongTrinh") final Object chuongTrinh){
    	DangKy dk = (DangKy) vm;
    	NhanVien nhanVien = (NhanVien) nv;
    	ChuongTrinhHienMau ctr = (ChuongTrinhHienMau) chuongTrinh;
    	DangKy dkCu = getCore().getNhanVienService().getDangKy(nhanVien).fetchOne();
		Calendar c1 = Calendar.getInstance();
	    Calendar c2 = Calendar.getInstance();
		
		if(getThoiGianVuaHien() != null && getThoiGianSapHien() != null){
			System.out.println("thoiGianVuaHien"+thoiGianVuaHien);
			c2.setTime(getThoiGianVuaHien());
			c1.setTime(getThoiGianSapHien());
			noDay = (c1.getTime().getTime() - c2.getTime().getTime())
			            / (24 * 3600 * 1000);
		}
    	if(nhanVien != null){
    		dk.setHoTen(nhanVien.getHoVaTen());
    		dk.setEmail(nhanVien.getEmail());
    		dk.setChuongTrinhHienMau(ctr);
    		dk.setSoDienThoai(nhanVien.getSoDienThoai());
    		dk.setNguoiTao(nhanVien);
			if(noDay < 84){
				showNotification("Bạn chưa đủ điều kiện để đăng ký hiến máu, khoảng cách 2 lần hiến máu tối thiểu là 84 ngày, hãy chọn lại ngày phù hợp !", "", "warning");
			}
			else if(dkCu != null){
				if(dkCu.getTrangThaiDangKy() != "waiting")
					showNotification("Đơn đăng ký trước của bạn chưa được cập nhập !", "", "warning");
			}
			else{
				dk.save();
				NhatKy nhatKy = new NhatKy();
				nhatKy.setDoiTuongId(this.getId());
				nhatKy.setLoaiDoiTuong("hiến máu");
				nhatKy.setHanhDong("đăng ký");
				nhatKy.setNgayTao(new Date());
				nhatKy.setNguoiTao(nhanVien);
				nhatKy.saveNotShowNotification();
		    	Executions.sendRedirect("/c/thongtincanhan");
		    	showNotification("Đăng ký thành công!", "", "success");
			}
    	}
    	else{
    		showNotification("Bạn cần đăng nhập để thực hiện chức năng này !", "", "warning");
    		Executions.sendRedirect("/c/signin");
    	}
    }
    
	@Transient
	public String getStyle() {
		String out = "";
		if (("rejected").equals(getTrangThaiDangKy())) {
			out = "btn btn-danger";
		} else if (("done").equals(getTrangThaiDangKy())) {
			out = "btn btn-success";
		} else if (("waiting").equals(getTrangThaiDangKy())) {
			out = "btn btn-info";
		}
		return out;
	}
}
