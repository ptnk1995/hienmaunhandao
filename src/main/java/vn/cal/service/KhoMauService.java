package vn.cal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.model.KhoMau;
import vn.cal.model.LoaiMau;
import vn.cal.model.QKhoMau;
import vn.cal.model.QNhomMau;

public class KhoMauService extends BasicService<KhoMau>{
	
	private Map<Long, String> loaiMau = new HashMap<Long, String>();
	
	public JPAQuery<KhoMau> getTargetQuery() {
		String paramTuKhoa = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String publishStatus = MapUtils.getString(argDeco(),Labels.getLabel("param.publishstatus"),"");
		
		System.out.println("paramTuKhoaLoaiMau " +paramTuKhoa);
		System.out.println("publishStatus " +publishStatus);
		JPAQuery<KhoMau> q = find(KhoMau.class)
				.where(QKhoMau.khoMau.trangThai.ne(getCore().TT_DA_XOA));
		q.orderBy(QKhoMau.khoMau.ngayTao.desc());
		
		if(!publishStatus.isEmpty()) {
			boolean status = Boolean.valueOf(publishStatus);
			q.where(QKhoMau.khoMau.publishStatus.eq(status));
		}
		
		if (!paramTuKhoa.isEmpty()) {
			String tukhoa = "%" + paramTuKhoa + "%";
			q.where(QKhoMau.khoMau.nhomMau.tenNhom.eq(tukhoa));
		}
		Long arg = MapUtils.getLong(argDeco(), "tenLoaiMau");
		if(arg != null){
			q.where(QNhomMau.nhomMau.loaiMau.tenLoai.eq(loaiMau.get(arg)));
		}
		return q;
	}
	
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
}
