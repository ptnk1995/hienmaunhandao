package vn.cal.service;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.model.DeNghi;
import vn.cal.model.QDeNghi;
import vn.cal.model.thongke.NguoiThamGiaModel;
import vn.cal.model.thongke.ThongKePhanTichModel;

public class ThongKeNguoiThamGia extends ThongKeService {
	
	private ThongKePhanTichModel soLieu = new ThongKePhanTichModel();
	private List<NguoiThamGiaModel> listNguoiThamGia = new ArrayList<NguoiThamGiaModel>();

	public ThongKePhanTichModel getSoLieu() {
		return soLieu;
	}

	public void setSoLieu(ThongKePhanTichModel soLieu) {
		this.soLieu = soLieu;
	}

	@Command
	public void thongKeKetQua() {
		setThongKe(true);
		BindUtils.postNotifyChange(null, null, this, "thongKe");
		soLieu.setSoLieuBietNhomMau(getQueryBietNhomMau()
				.fetchCount());
		soLieu.setSoLieuKhongBietNhomMau(getQueryChuaBietNhomMau()
				.fetchCount());
		soLieu.setSoLieuDKNhieuLan(getQueryThongTin()
				.fetchCount());
		soLieu.setSoLieuDNDoi(getTrangThaiDoi().fetchCount());
		soLieu.setSoLieuDNHuy(getTrangThaiHuyBo().fetchCount());
		soLieu.setSoLieuDNChapNhan(getTrangThaiChapNhan().fetchCount());
		soLieu.setSoLieuDNAll(getCore().getDeNghiService().getTargetQuery().fetchCount());
		BindUtils.postNotifyChange(null, null, this, "soLieu");
	}
	
	public JPAQuery<DeNghi> getQueryThongTin() {
		JPAQuery<DeNghi> q = find(DeNghi.class)
				.where(QDeNghi.deNghi.trangThai.ne(getCore().TT_DA_XOA))
				.where(QDeNghi.deNghi.trangThaiDeNghi.eq("accept"));
		if (getTuNgayTK() != null && getDenNgayTK() !=null) {
			q.where(QDeNghi.deNghi.ngayTao.between(getTuNgayTK(), getDenNgayTK()));
		} else if (getTuNgayTK() != null) {
			q.where(QDeNghi.deNghi.ngayTao.after(getTuNgayTK()));
		} else if (getDenNgayTK() != null) {
			q.where(QDeNghi.deNghi.ngayTao.before(getDenNgayTK()));
		}
		return q;
	}
	public JPAQuery<DeNghi> getQueryChuaBietNhomMau() {
		JPAQuery<DeNghi> q = find(DeNghi.class)
				.where(QDeNghi.deNghi.trangThai.ne(getCore().TT_DA_XOA))
				.where(QDeNghi.deNghi.trangThaiDeNghi.eq("accept"))
				.where(QDeNghi.deNghi.nhomMau.isNull());
		if (getTuNgayTK() != null && getDenNgayTK() !=null) {
			q.where(QDeNghi.deNghi.ngayTao.between(getTuNgayTK(), getDenNgayTK()));
		} else if (getTuNgayTK() != null) {
			q.where(QDeNghi.deNghi.ngayTao.after(getTuNgayTK()));
		} else if (getDenNgayTK() != null) {
			q.where(QDeNghi.deNghi.ngayTao.before(getDenNgayTK()));
		}
		return q;
	}
	public JPAQuery<DeNghi> getQueryBietNhomMau() {
		JPAQuery<DeNghi> q = find(DeNghi.class)
				.where(QDeNghi.deNghi.trangThai.ne(getCore().TT_DA_XOA))
				.where(QDeNghi.deNghi.trangThaiDeNghi.eq("accept"))
				.where(QDeNghi.deNghi.nhomMau.isNotNull());
		if (getTuNgayTK() != null && getDenNgayTK() !=null) {
			q.where(QDeNghi.deNghi.ngayTao.between(getTuNgayTK(), getDenNgayTK()));
		} else if (getTuNgayTK() != null) {
			q.where(QDeNghi.deNghi.ngayTao.after(getTuNgayTK()));
		} else if (getDenNgayTK() != null) {
			q.where(QDeNghi.deNghi.ngayTao.before(getDenNgayTK()));
		}
		return q;
	}
	
	public JPAQuery<DeNghi> getTrangThaiChapNhan() {
		JPAQuery<DeNghi> q = find(DeNghi.class)
				.where(QDeNghi.deNghi.trangThai.ne(getCore().TT_DA_XOA))
				.where(QDeNghi.deNghi.trangThaiDeNghi.eq("accept"));
		if (getTuNgayTK() != null && getDenNgayTK() !=null) {
			q.where(QDeNghi.deNghi.ngayTao.between(getTuNgayTK(), getDenNgayTK()));
		} else if (getTuNgayTK() != null) {
			q.where(QDeNghi.deNghi.ngayTao.after(getTuNgayTK()));
		} else if (getDenNgayTK() != null) {
			q.where(QDeNghi.deNghi.ngayTao.before(getDenNgayTK()));
		}
		return q;
	}
	
	public JPAQuery<DeNghi> getTrangThaiHuyBo() {
		JPAQuery<DeNghi> q = find(DeNghi.class)
				.where(QDeNghi.deNghi.trangThai.ne(getCore().TT_DA_XOA))
				.where(QDeNghi.deNghi.trangThaiDeNghi.eq("rejected"));
		if (getTuNgayTK() != null && getDenNgayTK() !=null) {
			q.where(QDeNghi.deNghi.ngayTao.between(getTuNgayTK(), getDenNgayTK()));
		} else if (getTuNgayTK() != null) {
			q.where(QDeNghi.deNghi.ngayTao.after(getTuNgayTK()));
		} else if (getDenNgayTK() != null) {
			q.where(QDeNghi.deNghi.ngayTao.before(getDenNgayTK()));
		}
		return q;
	}
	public JPAQuery<DeNghi> getTrangThaiDoi() {
		JPAQuery<DeNghi> q = find(DeNghi.class)
				.where(QDeNghi.deNghi.trangThai.ne(getCore().TT_DA_XOA))
				.where(QDeNghi.deNghi.trangThaiDeNghi.eq("waiting"));
		if (getTuNgayTK() != null && getDenNgayTK() !=null) {
			q.where(QDeNghi.deNghi.ngayTao.between(getTuNgayTK(), getDenNgayTK()));
		} else if (getTuNgayTK() != null) {
			q.where(QDeNghi.deNghi.ngayTao.after(getTuNgayTK()));
		} else if (getDenNgayTK() != null) {
			q.where(QDeNghi.deNghi.ngayTao.before(getDenNgayTK()));
		}
		return q;
	}
}
