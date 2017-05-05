package vn.cal.service;

import java.util.Date;

import javax.persistence.Transient;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

import vn.cal.model.QuanHuyen;
import vn.cal.model.ThanhPho;

public class ThongKeService extends BasicService<Object> {
	private ThanhPho selectedTinhThanhTK;
	private QuanHuyen selectedQuanHuyenTK;
	private Date tuNgayTK;
	private Date denNgayTK;
	private boolean thongKe;
	
	public ThanhPho getSelectedTinhThanhTK() {
		return selectedTinhThanhTK;
	}
	public void setSelectedTinhThanhTK(ThanhPho selectedTinhThanhTK) {
		this.selectedTinhThanhTK = selectedTinhThanhTK;
	}
	public QuanHuyen getSelectedQuanHuyenTK() {
		return selectedQuanHuyenTK;
	}
	public void setSelectedQuanHuyenTK(QuanHuyen selectedQuanHuyenTK) {
		this.selectedQuanHuyenTK = selectedQuanHuyenTK;
	}
	public Date getTuNgayTK() {
		return tuNgayTK;
	}
	public void setTuNgayTK(Date tuNgayTK) {
		this.tuNgayTK = tuNgayTK;
	}
	public Date getDenNgayTK() {
		return denNgayTK;
	}
	public void setDenNgayTK(Date denNgayTK) {
		this.denNgayTK = denNgayTK;
	}
	public boolean isThongKe() {
		return thongKe;
	}
	public void setThongKe(boolean thongKe) {
		this.thongKe = thongKe;
	}
	
	@Transient
	public AbstractValidator getValidator() {
		return new AbstractValidator() {
			@Override
			public void validate(final ValidationContext ctx) {				
				if (getTuNgayTK() != null && getDenNgayTK() != null && getDenNgayTK().before(getTuNgayTK())) {
					addInvalidMessage(ctx, "lblErrDenNgayTK","Đến ngày thống kê không được lớn hơn từ ngày thống kê.");
				}  
			}
		};
	}
}
