package vn.cal.service;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.model.LoaiMau;
import vn.cal.model.QLoaiMau;

public class LoaiMauService extends BasicService<LoaiMau> {
	
	public JPAQuery<LoaiMau> getTargetQuery() {
		String paramTuKhoa = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String publishStatus = MapUtils.getString(argDeco(),Labels.getLabel("param.publishstatus"),"");
		
		System.out.println("paramTuKhoaLoaiMau " +paramTuKhoa);
		System.out.println("publishStatus " +publishStatus);
		JPAQuery<LoaiMau> q = find(LoaiMau.class)
				.where(QLoaiMau.loaiMau.trangThai.ne(getCore().TT_DA_XOA));
		q.orderBy(QLoaiMau.loaiMau.ngayTao.desc());
		
		if(!publishStatus.isEmpty()) {
			boolean status = Boolean.valueOf(publishStatus);
			q.where(QLoaiMau.loaiMau.publishStatus.eq(status));
		}
		
		if (!paramTuKhoa.isEmpty()) {
			String tukhoa = "%" + paramTuKhoa + "%";
			q.where(QLoaiMau.loaiMau.tenLoai.like(tukhoa));
		}
		return q;
	}
	
	
	
}
