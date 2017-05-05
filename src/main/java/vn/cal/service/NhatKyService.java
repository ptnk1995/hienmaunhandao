package vn.cal.service;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.model.NhatKy;
import vn.cal.model.QNhatKy;
public class NhatKyService extends BasicService<NhatKy>{
	
	public JPAQuery<NhatKy> getTargetQuery() {
		String publishStatus = MapUtils.getString(argDeco(),Labels.getLabel("param.publishstatus"),"");
		
		JPAQuery<NhatKy> q = find(NhatKy.class)
				.where(QNhatKy.nhatKy.trangThai.ne(getCore().TT_DA_XOA));
		q.orderBy(QNhatKy.nhatKy.ngayTao.desc()).limit(6);
		
		if(!publishStatus.isEmpty()) {
			boolean status = Boolean.valueOf(publishStatus);
			q.where(QNhatKy.nhatKy.publishStatus.eq(status));
		}
		
		return q;
	}
	
}
