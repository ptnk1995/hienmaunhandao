package vn.cal.service;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.model.PhanHoi;
import vn.cal.model.QPhanHoi;

public class PhanHoiService extends BasicService<PhanHoi>{

	public JPAQuery<PhanHoi> getTargetQuery() {
		String paramTuKhoa = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String publishStatus = MapUtils.getString(argDeco(),Labels.getLabel("param.publishstatus"),"");
		
		JPAQuery<PhanHoi> q = find(PhanHoi.class)
				.where(QPhanHoi.phanHoi.trangThai.ne(getCore().TT_DA_XOA));
		q.orderBy(QPhanHoi.phanHoi.ngayTao.desc());
		
		if(!publishStatus.isEmpty()) {
			boolean status = Boolean.valueOf(publishStatus);
			q.where(QPhanHoi.phanHoi.publishStatus.eq(status));
		}
		
		if (!paramTuKhoa.isEmpty()) {
			String tukhoa = "%" + paramTuKhoa + "%";
			q.where(QPhanHoi.phanHoi.hoTen.like(tukhoa));
		}
		return q;
	}
}
