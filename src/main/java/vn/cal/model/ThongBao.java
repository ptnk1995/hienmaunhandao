package vn.cal.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import vn.cal.core.Asset;
import vn.cal.enums.LoaiThongBao;


@Entity
@Table(name = "thong_bao")
public class ThongBao extends Asset<ThongBao>{

	private NhanVien nguoiNhan;
	private String noiDung;
	private LoaiThongBao loaiThongBao;
	
	public ThongBao() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ThongBao(NhanVien nguoiNhan, String noiDung, LoaiThongBao loaiThongBao) {
		super();
		this.nguoiNhan = nguoiNhan;
		this.noiDung = noiDung;
		this.loaiThongBao = loaiThongBao;
	}
	@ManyToOne
	public NhanVien getNguoiNhan() {
		return nguoiNhan;
	}
	public void setNguoiNhan(NhanVien nguoiNhan) {
		this.nguoiNhan = nguoiNhan;
	}
	public String getNoiDung() {
		return noiDung;
	}
	public void setNoiDung(String noiDung) {
		this.noiDung = noiDung;
	}
	public LoaiThongBao getLoaiThongBao() {
		return loaiThongBao;
	}
	public void setLoaiThongBao(LoaiThongBao loaiThongBao) {
		this.loaiThongBao = loaiThongBao;
	}
	
}
