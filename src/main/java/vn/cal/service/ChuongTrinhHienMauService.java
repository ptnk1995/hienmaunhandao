package vn.cal.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.MapUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.model.ChiTietChuongTrinh;
import vn.cal.model.ChuongTrinhHienMau;
import vn.cal.model.QChiTietChuongTrinh;
import vn.cal.model.QChuongTrinhHienMau;
import vn.cal.model.QuanHuyen;
import vn.cal.model.ThanhPho;

public class ChuongTrinhHienMauService extends BasicService<ChuongTrinhHienMau> {
	private ThanhPho selectedThanhPho;
	private QuanHuyen selectedQuanHuyen;
	private List<QuanHuyen> listQuanHuyen;
	private List<ThanhPho> listThanhPho;
	public ThanhPho getSelectedThanhPho() {
		return selectedThanhPho;
	}

	public void setSelectedThanhPho(ThanhPho selectedThanhPho) {
		this.selectedThanhPho = selectedThanhPho;
		selectedQuanHuyen = null;
		BindUtils.postNotifyChange(null, null, this, "listQuanHuyen");
	}

	public QuanHuyen getSelectedQuanHuyen() {
		return selectedQuanHuyen;
	}

	public void setSelectedQuanHuyen(QuanHuyen selectedQuanHuyen) {
		this.selectedQuanHuyen = selectedQuanHuyen;
	}
	public List<ChuongTrinhHienMau> getTrangChu(){
		JPAQuery<ChuongTrinhHienMau> q = find(ChuongTrinhHienMau.class)
				.where(QChuongTrinhHienMau.chuongTrinhHienMau.trangThai.ne(getCore().TT_DA_XOA));
		q.orderBy(QChuongTrinhHienMau.chuongTrinhHienMau.ngayTao.desc()).limit(3);
		System.out.println("getTrangChu: " + q.fetchCount());
		return q.fetch();
	}
	public JPAQuery<ChuongTrinhHienMau> getTargetQuery() {
		String paramTuKhoa = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String publishStatus = MapUtils.getString(argDeco(),Labels.getLabel("param.publishstatus"),"");
		JPAQuery<ChuongTrinhHienMau> q = find(ChuongTrinhHienMau.class)
				.where(QChuongTrinhHienMau.chuongTrinhHienMau.trangThai.ne(getCore().TT_DA_XOA))
				.where(QChuongTrinhHienMau.chuongTrinhHienMau.ngayKetThuc.after(new Date()));
		q.orderBy(QChuongTrinhHienMau.chuongTrinhHienMau.ngayTao.desc());
		
		JPAQuery<ChiTietChuongTrinh> p = find(ChiTietChuongTrinh.class)
				.where(QChiTietChuongTrinh.chiTietChuongTrinh.trangThai.ne(getCore().TT_DA_XOA));
		p.orderBy(QChiTietChuongTrinh.chiTietChuongTrinh.ngayTao.desc());
		
		
		if(!publishStatus.isEmpty()) {
			boolean status = Boolean.valueOf(publishStatus);
			q.where(QChuongTrinhHienMau.chuongTrinhHienMau.publishStatus.eq(status));
		}
		
		if (!paramTuKhoa.isEmpty()) {
			String tukhoa = "%" + paramTuKhoa + "%";
			q.where(QChuongTrinhHienMau.chuongTrinhHienMau.tenChuongTrinh.like(tukhoa));
		}
		if(selectedThanhPho != null && selectedQuanHuyen != null){
			List<ChuongTrinhHienMau> list = new ArrayList<ChuongTrinhHienMau>();
			list = find(ChiTietChuongTrinh.class)
					.where(QChiTietChuongTrinh.chiTietChuongTrinh.quanHuyen.eq(selectedQuanHuyen))
					.where(QChiTietChuongTrinh.chiTietChuongTrinh.thanhPho.eq(selectedThanhPho))
					.groupBy(QChiTietChuongTrinh.chiTietChuongTrinh.chuongTrinh)
					.select(QChiTietChuongTrinh.chiTietChuongTrinh.chuongTrinh)
					.fetch();
			q.where(QChuongTrinhHienMau.chuongTrinhHienMau.in(list));
		} else if (selectedThanhPho != null) {
			List<ChuongTrinhHienMau> list = new ArrayList<ChuongTrinhHienMau>();
			list = find(ChiTietChuongTrinh.class)
					.where(QChiTietChuongTrinh.chiTietChuongTrinh.thanhPho.eq(selectedThanhPho))
					.groupBy(QChiTietChuongTrinh.chiTietChuongTrinh.chuongTrinh)
					.select(QChiTietChuongTrinh.chiTietChuongTrinh.chuongTrinh)
					.fetch();
			q.where(QChuongTrinhHienMau.chuongTrinhHienMau.in(list));
		}
		return q;
	}

	public List<ThanhPho> getListThanhPho(){
		List<ThanhPho> loai = getCore().getThanhPhoService().getTargetQuery().fetch();
		listThanhPho = new ArrayList<ThanhPho>();
		listThanhPho.add(null);
		for (ThanhPho tp : loai ) {
			listThanhPho.add(tp);
		}
		return listThanhPho;
	}
	
	public List<QuanHuyen> getListQuanHuyen(){
		List<QuanHuyen> loai = getCore().getQuanHuyenService().getTargetQuery().fetch();
		listQuanHuyen = new ArrayList<QuanHuyen>();
		listQuanHuyen.add(null);
		if(selectedThanhPho != null){
			for (QuanHuyen quanHuyen : selectedThanhPho.getListQuanHuyen()) {
				listQuanHuyen.add(quanHuyen);
			}
		}
		else{ 
			for (QuanHuyen qh : loai) {
				listQuanHuyen.add(qh);
			}
		}
		return listQuanHuyen;
	}

}
