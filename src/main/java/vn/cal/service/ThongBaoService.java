package vn.cal.service;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.model.QThongBao;
import vn.cal.model.ThongBao;

public class ThongBaoService extends BasicService<ThongBao> {

	public JPAQuery<ThongBao> getTargetQuery() {
		String paramTuKhoa = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String publishStatus = MapUtils.getString(argDeco(),Labels.getLabel("param.publishstatus"),"");

		JPAQuery<ThongBao> q = find(ThongBao.class)
				.where(QThongBao.thongBao.trangThai.ne(getCore().TT_DA_XOA));
		q.orderBy(QThongBao.thongBao.ngayTao.desc());
		
		if(!publishStatus.isEmpty()) {
			boolean status = Boolean.valueOf(publishStatus);
			q.where(QThongBao.thongBao.publishStatus.eq(status));
		}
		
		if (!paramTuKhoa.isEmpty()) {
			String tukhoa = "%" + paramTuKhoa + "%";
			q.where(QThongBao.thongBao.noiDung.like(tukhoa));
		}
		return q;
	}
}
