package vn.cal.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.Window;

import vn.cal.core.Asset;

@Entity
@Table(name="tin_tuc")
public class TinTuc extends Asset<TinTuc> {

	private String tieuDe;
	private String noiDung;
	private DanhMucTinTuc danhMuc;
	private String urlBrowseImage=null;
	@Transient
	private List<DanhMucTinTuc> listDanhMuc;
	public String getTieuDe() {
		return tieuDe;
	}
	public void setTieuDe(String tieuDe) {
		this.tieuDe = tieuDe;
	}
	public String getNoiDung() {
		return noiDung;
	}
	@Transient
	public String getCutNoiDung() {
		System.out.println("noi dung"+noiDung.substring(0,100));
		return noiDung.substring(0,10);
	}
	public void setNoiDung(String noiDung) {
		this.noiDung = noiDung;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	public DanhMucTinTuc getDanhMuc() {
		return danhMuc;
	}
	public void setDanhMuc(DanhMucTinTuc danhMuc) {
		this.danhMuc = danhMuc;
	}
	
	public String getUrlBrowseImage() {
		return urlBrowseImage;
	}
	public void setUrlBrowseImage(String urlBrowseImage) {
		this.urlBrowseImage = urlBrowseImage;
	}
	@Command
	public void saveTinTuc(@BindingParam("list") final Object listObject, @BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) {
		this.setNgayTao(new Date());
		saveAsShowNotification();
		wdn.detach();
		BindUtils.postNotifyChange(null, null, listObject, attr);
	}

	@Transient
	public List<DanhMucTinTuc> getListDanhMuc() {
		return getCore().getDanhMucTinTucService().getTargetQuery().fetch();
	}
	@Transient
	public void setListDanhMuc(List<DanhMucTinTuc> listDanhMuc) {
		this.listDanhMuc = listDanhMuc;
	}
	
}
