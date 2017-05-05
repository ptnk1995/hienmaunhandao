package vn.cal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.model.QQuanHuyen;
import vn.cal.model.QuanHuyen;
import vn.cal.model.ThanhPho;

public class QuanHuyenService extends BasicService<QuanHuyen> {

	private Map<Long, String> thanhPho = new HashMap<Long, String>();
	private List<ThanhPho> listThanhPho = new ArrayList<ThanhPho>();

	public JPAQuery<QuanHuyen> getTargetQuery() {
		String paramTuKhoa = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String publishStatus = MapUtils.getString(argDeco(),Labels.getLabel("param.publishstatus"),"");
		
		System.out.println("paramTuKhoa " +paramTuKhoa);
		System.out.println("publishStatus " +publishStatus);
		JPAQuery<QuanHuyen> q = find(QuanHuyen.class)
				.where(QQuanHuyen.quanHuyen.trangThai.ne(getCore().TT_DA_XOA));
		q.orderBy(QQuanHuyen.quanHuyen.ngayTao.desc());
		
		if(!publishStatus.isEmpty()) {
			boolean status = Boolean.valueOf(publishStatus);
			q.where(QQuanHuyen.quanHuyen.publishStatus.eq(status));
		}
		
		if (!paramTuKhoa.isEmpty()) {
			String tukhoa = "%" + paramTuKhoa + "%";
			q.where(QQuanHuyen.quanHuyen.tenQuanHuyen.like(tukhoa));
		}
		Long arg = MapUtils.getLong(argDeco(), "tenThanhPho");
		if(arg != null){
			q.where(QQuanHuyen.quanHuyen.thanhPho.tenThanhPho.eq(thanhPho.get(arg)));
		}
		return q;
	}
	
	public JPAQuery<QuanHuyen> getByThanhPho(ThanhPho tp){
		JPAQuery<QuanHuyen> q = find(QuanHuyen.class)
				.where(QQuanHuyen.quanHuyen.trangThai.ne(getCore().TT_DA_XOA));
		q.orderBy(QQuanHuyen.quanHuyen.ngayTao.desc());
		if(tp != null){
			q.where(QQuanHuyen.quanHuyen.thanhPho.eq(tp));
		}
		return q;
	}
	public List<ThanhPho> getListThanhPho(){
		listThanhPho = getCore().getThanhPhoService().getTargetQuery().fetch();
		return listThanhPho;
	}
	public final Map<Long, String> getThanhPho() {
		if(thanhPho.isEmpty()){
			thanhPho.put(null, "        ");
			List<ThanhPho> list = getCore().getThanhPhoService().getTargetQuery().fetch();
			for (ThanhPho lm : list) {
				thanhPho.put(lm.getId(), lm.getTenThanhPho());
			}
		}
		return thanhPho;
	}
}
