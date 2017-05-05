package vn.cal.service;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.model.BaiDang;
import vn.cal.model.BinhLuan;
import vn.cal.model.QBinhLuan;

public class BinhLuanService extends BasicService<BinhLuan> {
	public JPAQuery<BaiDang> getTargetQuery() {
		String publishStatus = MapUtils.getString(argDeco(),Labels.getLabel("param.publishstatus"),"");
		
		JPAQuery<BaiDang> q = find(BaiDang.class)
				.where(QBinhLuan.binhLuan.trangThai.ne(getCore().TT_DA_XOA));
		q.orderBy(QBinhLuan.binhLuan.ngayTao.desc());
		
		if(!publishStatus.isEmpty()) {
			boolean status = Boolean.valueOf(publishStatus);
			q.where(QBinhLuan.binhLuan.publishStatus.eq(status));
		}
		
		return q;
	}
}
