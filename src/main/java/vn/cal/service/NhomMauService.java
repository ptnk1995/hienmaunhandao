package vn.cal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.model.LoaiMau;
import vn.cal.model.NhomMau;
import vn.cal.model.QNhomMau;

public class NhomMauService extends BasicService<NhomMau> {
	
	private Map<Long, String> loaiMau = new HashMap<Long, String>();
	
	public final Map<Long, String> getLoaiMau() {
		if(loaiMau.isEmpty()){
			loaiMau.put(null, "        ");
			List<LoaiMau> list = getCore().getLoaiMauService().getTargetQuery().fetch();
			for (LoaiMau lm : list) {
				loaiMau.put(lm.getId(), lm.getTenLoai());
			}
		}
		return loaiMau;
	}

	public JPAQuery<NhomMau> getTargetQuery() {
		String paramTuKhoa = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String publishStatus = MapUtils.getString(argDeco(),Labels.getLabel("param.publishstatus"),"");
		
		JPAQuery<NhomMau> q = find(NhomMau.class)
				.where(QNhomMau.nhomMau.trangThai.ne(getCore().TT_DA_XOA));
		q.orderBy(QNhomMau.nhomMau.ngayTao.desc());
		
		if(!publishStatus.isEmpty()) {
			boolean status = Boolean.valueOf(publishStatus);
			q.where(QNhomMau.nhomMau.publishStatus.eq(status));
		}
		
		if (!paramTuKhoa.isEmpty()) {
			String tukhoa = "%" + paramTuKhoa + "%";
			q.where(QNhomMau.nhomMau.tenNhom.like(tukhoa));
		}
		Long arg = MapUtils.getLong(argDeco(), "tenLoaiMau");
		if(arg != null){
			q.where(QNhomMau.nhomMau.loaiMau.tenLoai.eq(loaiMau.get(arg)));
		}
		return q;
	}
	
	public JPAQuery<NhomMau> getTenNhomMau() {
		JPAQuery<NhomMau> q = find(NhomMau.class)
				.where(QNhomMau.nhomMau.trangThai.ne(getCore().TT_DA_XOA));
		q.orderBy(QNhomMau.nhomMau.ngayTao.desc());
		q.groupBy(QNhomMau.nhomMau.tenNhom);
		return q;
	}
}
