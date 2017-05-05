package vn.cal.service;

import java.util.List;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.model.ChuongTrinhHienMau;
import vn.cal.model.DangKy;
import vn.cal.model.NhanVien;
import vn.cal.model.NhomMau;
import vn.cal.model.QDangKy;

public class DangKyService extends BasicService<DangKy>{
	
	public JPAQuery<DangKy> getTargetQuery() {
		String paramTuKhoa = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String publishStatus = MapUtils.getString(argDeco(),Labels.getLabel("param.publishstatus"),"");

		JPAQuery<DangKy> q = find(DangKy.class)
				.where(QDangKy.dangKy.trangThai.ne(getCore().TT_DA_XOA));
		q.orderBy(QDangKy.dangKy.ngayTao.desc());
		
		if(!publishStatus.isEmpty()) {
			boolean status = Boolean.valueOf(publishStatus);
			q.where(QDangKy.dangKy.publishStatus.eq(status));
		}
		
		if (!paramTuKhoa.isEmpty()) {
			String tukhoa = "%" + paramTuKhoa + "%";
			q.where(QDangKy.dangKy.nhomMau.tenNhom.like(tukhoa));
		}
		return q;
	}
	public List<NhomMau> getListNhomMau(){
		return getCore().getNhomMauService().getTenNhomMau().fetch();
	}
	public List<ChuongTrinhHienMau> getListChuongTrinh(){
		return getCore().getChuongTrinhHienMauService().getTargetQuery().fetch();
	}
	
	public List<DangKy> getSoLanHienMau(){
		JPAQuery<DangKy> q = find(DangKy.class)
				.where(QDangKy.dangKy.trangThai.ne(getCore().TT_DA_XOA))
				.groupBy(QDangKy.dangKy.nguoiTao);
		q.orderBy(QDangKy.dangKy.soLanHienTruoc.desc()).limit(4);
		return q.fetch();
	}
}
