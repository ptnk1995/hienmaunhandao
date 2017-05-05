package vn.cal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.model.DanhMucTinTuc;
import vn.cal.model.QTinTuc;
import vn.cal.model.TinTuc;


public class TinTucService extends BasicService<TinTuc> {

	private Map<Long, String> danhMucTin = new HashMap<Long, String>();
	public JPAQuery<TinTuc> getTargetQuery() {
		String paramTuKhoa = MapUtils.getString(argDeco(), Labels.getLabel("param.tukhoa"), "").trim();
		String publishStatus = MapUtils.getString(argDeco(), Labels.getLabel("param.publishstatus"), "");

		JPAQuery<TinTuc> q = find(TinTuc.class)
				.where(QTinTuc.tinTuc.trangThai.ne(getCore().TT_DA_XOA));
		q.orderBy(QTinTuc.tinTuc.ngayTao.desc());

		if (!publishStatus.isEmpty()) {
			boolean status = Boolean.valueOf(publishStatus);
			q.where(QTinTuc.tinTuc.publishStatus.eq(status));
		}

		if (!paramTuKhoa.isEmpty()) {
			String tukhoa = "%" + paramTuKhoa + "%";
			q.where(QTinTuc.tinTuc.tieuDe.like(tukhoa));
		}
		Long arg = MapUtils.getLong(argDeco(), "tenDanhMuc");
		if (arg != null) {
			q.where(QTinTuc.tinTuc.danhMuc.tenDanhMuc.eq(danhMucTin.get(arg)));
		}

		return q;
	}
	
	public List<DanhMucTinTuc> listDanhMucTin(){
		return getCore().getDanhMucTinTucService().getTargetQuery().fetch();
	}
	
	public final Map<Long, String> getDanhMucTin() {
		if(danhMucTin.isEmpty()){
			danhMucTin.put(null, "        ");
			for (DanhMucTinTuc lm : listDanhMucTin()) {
				danhMucTin.put(lm.getId(), lm.getTenDanhMuc());
			}
		}
		return danhMucTin;
	}
}
