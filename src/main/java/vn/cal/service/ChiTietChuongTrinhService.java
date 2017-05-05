package vn.cal.service;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.model.ChiTietChuongTrinh;
import vn.cal.model.QChiTietChuongTrinh;

public class ChiTietChuongTrinhService extends BasicService<ChiTietChuongTrinh>{
	
	public JPAQuery<ChiTietChuongTrinh> getTargetQuery() {
		String paramTuKhoa = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String publishStatus = MapUtils.getString(argDeco(),Labels.getLabel("param.publishstatus"),"");
		JPAQuery<ChiTietChuongTrinh> q = find(ChiTietChuongTrinh.class)
				.where(QChiTietChuongTrinh.chiTietChuongTrinh.trangThai.ne(getCore().TT_DA_XOA));
		q.orderBy(QChiTietChuongTrinh.chiTietChuongTrinh.ngayTao.desc());
		
		if(!publishStatus.isEmpty()) {
			boolean status = Boolean.valueOf(publishStatus);
			q.where(QChiTietChuongTrinh.chiTietChuongTrinh.publishStatus.eq(status));
		}
		
		if (!paramTuKhoa.isEmpty()) {
			String tukhoa = "%" + paramTuKhoa + "%";
			q.where(QChiTietChuongTrinh.chiTietChuongTrinh.diaChi.like(tukhoa));
		}
		return q;
	}
}
