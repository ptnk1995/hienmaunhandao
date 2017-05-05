package vn.cal.service;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.model.DanhMucTinTuc;
import vn.cal.model.QDanhMucTinTuc;

public class DanhMucTinTucService extends BasicService<DanhMucTinTuc> {
	public JPAQuery<DanhMucTinTuc> getTargetQuery() {
		String paramTuKhoa = MapUtils.getString(argDeco(), Labels.getLabel("param.tukhoa"), "").trim();
		String publishStatus = MapUtils.getString(argDeco(), Labels.getLabel("param.publishstatus"), "");

		JPAQuery<DanhMucTinTuc> q = find(DanhMucTinTuc.class)
				.where(QDanhMucTinTuc.danhMucTinTuc.trangThai.ne(getCore().TT_DA_XOA));
		q.orderBy(QDanhMucTinTuc.danhMucTinTuc.ngayTao.desc());

		if (!publishStatus.isEmpty()) {
			boolean status = Boolean.valueOf(publishStatus);
			q.where(QDanhMucTinTuc.danhMucTinTuc.publishStatus.eq(status));
		}

		if (!paramTuKhoa.isEmpty()) {
			String tukhoa = "%" + paramTuKhoa + "%";
			q.where(QDanhMucTinTuc.danhMucTinTuc.tenDanhMuc.like(tukhoa));
		} /*
			 * Long arg = MapUtils.getLong(argDeco(), "tenLoaiMau"); if(arg !=
			 * null){
			 * q.where(QDanhMucTinTuc.danhMucTinTuc.tenDanhMuc.eq(loaiMau.get(
			 * arg))); }
			 */
		return q;
	}
}
