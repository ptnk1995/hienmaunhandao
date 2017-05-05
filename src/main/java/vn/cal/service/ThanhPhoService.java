package vn.cal.service;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.model.QThanhPho;
import vn.cal.model.ThanhPho;

public class ThanhPhoService extends BasicService<ThanhPho> {
	public JPAQuery<ThanhPho> getTargetQuery() {
		String paramTuKhoa = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String publishStatus = MapUtils.getString(argDeco(),Labels.getLabel("param.publishstatus"),"");
		
		System.out.println("paramTuKhoa " +paramTuKhoa);
		System.out.println("publishStatus " +publishStatus);
		JPAQuery<ThanhPho> q = find(ThanhPho.class)
				.where(QThanhPho.thanhPho.trangThai.ne(getCore().TT_DA_XOA));
		q.orderBy(QThanhPho.thanhPho.ngayTao.desc());
		
		if(!publishStatus.isEmpty()) {
			boolean status = Boolean.valueOf(publishStatus);
			q.where(QThanhPho.thanhPho.publishStatus.eq(status));
		}
		
		if (!paramTuKhoa.isEmpty()) {
			String tukhoa = "%" + paramTuKhoa + "%";
			q.where(QThanhPho.thanhPho.tenThanhPho.like(tukhoa));
		}
		return q;
	}
	
}
