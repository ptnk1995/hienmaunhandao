package vn.cal.service;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.model.BaiDang;
import vn.cal.model.QBaiDang;

public class ChiTietBaiDangService extends BasicService<BaiDang>{
	
	public JPAQuery<BaiDang> getTargetQuery() {
		String paramTuKhoa = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String publishStatus = MapUtils.getString(argDeco(),Labels.getLabel("param.publishstatus"),"");
		
		JPAQuery<BaiDang> q = find(BaiDang.class)
				.where(QBaiDang.baiDang.trangThai.ne(getCore().TT_DA_XOA));
		q.orderBy(QBaiDang.baiDang.ngayTao.desc());
		
		if(!publishStatus.isEmpty()) {
			boolean status = Boolean.valueOf(publishStatus);
			q.where(QBaiDang.baiDang.publishStatus.eq(status));
		}
		
		if (!paramTuKhoa.isEmpty()) {
			String tukhoa = "%" + paramTuKhoa + "%";
			q.where(QBaiDang.baiDang.tieuDe.like(tukhoa));
		}
		return q;
	}
}
