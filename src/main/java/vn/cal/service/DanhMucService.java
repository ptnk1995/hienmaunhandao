package vn.cal.service;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.model.DanhMuc;
import vn.cal.model.QDanhMuc;

public class DanhMucService extends BasicService<DanhMuc> {
	public JPAQuery<DanhMuc> getTargetQuery() {
		String paramTuKhoa = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String publishStatus = MapUtils.getString(argDeco(),Labels.getLabel("param.publishstatus"),"");
		
		System.out.println("paramTuKhoa " +paramTuKhoa);
		System.out.println("publishStatus " +publishStatus);
		JPAQuery<DanhMuc> q = find(DanhMuc.class)
				.where(QDanhMuc.danhMuc.trangThai.ne(getCore().TT_DA_XOA));
		q.orderBy(QDanhMuc.danhMuc.ngayTao.desc());
		
		if(!publishStatus.isEmpty()) {
			boolean status = Boolean.valueOf(publishStatus);
			q.where(QDanhMuc.danhMuc.publishStatus.eq(status));
		}
		
		if (!paramTuKhoa.isEmpty()) {
			String tukhoa = "%" + paramTuKhoa + "%";
			q.where(QDanhMuc.danhMuc.name.like(tukhoa));
		}
		return q;
	}
}
