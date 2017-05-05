package vn.cal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.model.DeNghi;
import vn.cal.model.LoaiMau;
import vn.cal.model.NhanVien;
import vn.cal.model.NhomMau;
import vn.cal.model.QDeNghi;
import vn.cal.model.QNhanVien;
import vn.cal.model.QNhomMau;

public class DeNghiService extends BasicService<DeNghi>{
	
	public NhomMau selectedNhomMau;
	public LoaiMau selectedLoaiMau;
	public NhanVien selectedNguoiGui;
	public List<NhomMau> listNhomMau = new ArrayList<NhomMau>();
	public List<NhanVien> listNguoiDungAndNM = new ArrayList<NhanVien>();
	private Map<String, String> listStatusNull = new HashMap<>();

	
	public NhanVien getSelectedNguoiGui() {
		return selectedNguoiGui;
	}

	public void setSelectedNguoiGui(NhanVien selectedNguoiGui) {
		this.selectedNguoiGui = selectedNguoiGui;
		BindUtils.postNotifyChange(null, null, this, "listNguoiDungAndNM");
	}

	public NhomMau getSelectedNhomMau() {
		return selectedNhomMau;
	}

	public void setSelectedNhomMau(NhomMau selectedNhomMau) {
		this.selectedNhomMau = selectedNhomMau;
		BindUtils.postNotifyChange(null, null, this, "listNguoiDungAndNM");
	}

	public LoaiMau getSelectedLoaiMau() {
		return selectedLoaiMau;
	}

	public void setSelectedLoaiMau(LoaiMau selectedLoaiMau) {
		this.selectedLoaiMau = selectedLoaiMau;
		System.out.println("selectedLoaiMau"+selectedLoaiMau.getTenLoai());
		BindUtils.postNotifyChange(null, null, this, "listNhomMau");
	}
	
	public JPAQuery<DeNghi> getTargetQuery() {
		String paramTuKhoa = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String publishStatus = MapUtils.getString(argDeco(),Labels.getLabel("param.publishstatus"),"");
		String statusDeNghi = MapUtils.getString(argDeco(),Labels.getLabel("param.status"),"");
		
		JPAQuery<DeNghi> q = find(DeNghi.class)
				.where(QDeNghi.deNghi.trangThai.ne(getCore().TT_DA_XOA));
		q.orderBy(QDeNghi.deNghi.ngayTao.desc());
		
		if(!publishStatus.isEmpty()) {
			boolean status = Boolean.valueOf(publishStatus);
			q.where(QDeNghi.deNghi.publishStatus.eq(status));
		}
		
		if(!statusDeNghi.isEmpty()){
			String stt = statusDeNghi;
			q.where(QDeNghi.deNghi.trangThaiDeNghi.eq(stt));
		}
		
		if (!paramTuKhoa.isEmpty()) {
			String tukhoa = "%" + paramTuKhoa + "%";
			q.where(QDeNghi.deNghi.nguoiGui.hoVaTen.like(tukhoa).and(QDeNghi.deNghi.nguoiNhan.hoVaTen.like(tukhoa)));
		}
		return q;
	}
	
	public List<NhomMau> getListNhomMau(){
		if(selectedLoaiMau != null)
			return find(NhomMau.class).where(QNhomMau.nhomMau.loaiMau.eq(selectedLoaiMau)).fetch();
		else 
			return getCore().getNhomMauService().getTenNhomMau().fetch();
	}
	
	public List<NhanVien> getListNguoiDung(){ 
		return getCore().getNhanVienService().getTargetQuery().fetch();
	}
	
	public List<LoaiMau> getListLoaiMau(){
		return getCore().getLoaiMauService().getTargetQuery().fetch();
	}
	
	public List<NhanVien> getListNguoiDungAndNM(){
		if(selectedNguoiGui != null){
			System.out.println("selectedNguoiGui"+selectedNguoiGui.getHoVaTen());
		if (selectedNhomMau != null && selectedLoaiMau != null) {
			System.out.println("khac");
			listNguoiDungAndNM = find(NhanVien.class)
					.where(QNhanVien.nhanVien.nhomMau.eq(selectedNhomMau))
					.where(QNhanVien.nhanVien.ne(selectedNguoiGui))
					.fetch();
		}
		else if (selectedNhomMau != null) { 
			System.out.println("tim ten");
			listNguoiDungAndNM = find(NhanVien.class)
					.where(QNhanVien.nhanVien.nhomMau.tenNhom.eq(selectedNhomMau.getTenNhom()))
					.where(QNhanVien.nhanVien.ne(selectedNguoiGui))
					.fetch();
		}
		else {
			listNguoiDungAndNM = getCore().getNhanVienService().getTargetQuery().fetch();
			System.out.println("không");
		}
		}
		return listNguoiDungAndNM;
	}
	
	public final Map<String, String> getListStatus() {
		if(listStatusNull.isEmpty()){
			listStatusNull.put(null, "Tất cả");
			listStatusNull.put("rejected", "Rejected");
			listStatusNull.put("waiting","Waiting");
			listStatusNull.put("accept","Accept");
		}
		return listStatusNull;
	}
}
