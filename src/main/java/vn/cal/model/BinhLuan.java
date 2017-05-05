package vn.cal.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Default;
import org.zkoss.zul.Window;

import vn.cal.core.Asset;

@Entity
@Table(name = "binh_luan")
public class BinhLuan extends Asset<BinhLuan>{
	
	private NhanVien nguoiDung;
	private String noiDung;
	private BaiDang baiDang;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idNguoiDung")
	public NhanVien getNguoiDung() {
		return nguoiDung;
	}
	
	public void setNguoiDung(NhanVien nguoiDung) {
		this.nguoiDung = nguoiDung;
	}
	public String getNoiDung() {
		return noiDung;
	}
	public void setNoiDung(String noiDung) {
		this.noiDung = noiDung;
	}
	
	@ManyToOne
	public BaiDang getBaiDang() {
		return baiDang;
	}
	public void setBaiDang(BaiDang baiDang) {
		this.baiDang = baiDang;
	}
	@Command
	public void saveBinhLuan(@BindingParam("vm") final Object listObject, @BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn){
		BaiDang baiDang = (BaiDang)listObject;
		setBaiDang(baiDang);
		saveAsShowNotification();
		wdn.detach();
		BindUtils.postNotifyChange(null, null, baiDang, attr);
	}
	
	@Transient
	public List<BinhLuan> getListBinhLuanByBaiDang(BaiDang baiDang) {
		List<BinhLuan> listBinhLuan = new ArrayList<BinhLuan>();
		listBinhLuan.addAll(find(BinhLuan.class).where(QBinhLuan.binhLuan.trangThai.eq(getCore().TT_AP_DUNG))
				.where(QBinhLuan.binhLuan.baiDang.eq(baiDang)).fetch());

		return listBinhLuan;
	}
	
	@Command
	public void saveBinhLuanMoi(@BindingParam("nguoi") final Object nhanVien, @BindingParam("attr") final String attr,
			@BindingParam("baiDang") final Object baiDang, @BindingParam("vm") final Object vm) {
		
		BaiDang bd = (BaiDang)baiDang;
		NhanVien nv = (NhanVien)nhanVien;
		System.out.println("bd " +bd.getTieuDe());
		setBaiDang(bd);
		setNguoiTao(nv);
		setNguoiDung(bd.getNguoiTao());
		saveNotShowNotification();
		bd.setListBinhLuan(getListBinhLuanByBaiDang(bd));
		BindUtils.postNotifyChange(null, null, baiDang, "listBinhLuan");
		BindUtils.postNotifyChange(null, null, this, attr);
		BindUtils.postNotifyChange(null, null, vm, "listBaiDang");
	}
	
	@Command
	public void deleteBinhLuan(final @BindingParam("notify") Object beanObject,
			final @BindingParam("baidang") Object baiDang,
			final @BindingParam("attr") @Default(value = "*") String attr,
			@BindingParam("vm") final Object vm){
		final BinhLuan bl = (BinhLuan) beanObject;
		final BaiDang bd = (BaiDang)  baiDang;
		bl.setDaXoa(true);
		bl.setTrangThai("da_xoa");
		bl.saveNotShowNotification();
		bd.getListBinhLuan().remove(bl);
		BindUtils.postNotifyChange(null, null, baiDang, attr);
		BindUtils.postNotifyChange(null, null, vm, "listBaiDang");
	}
}
