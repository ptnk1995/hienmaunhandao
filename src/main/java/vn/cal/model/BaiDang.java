package vn.cal.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Default;
import org.zkoss.bind.sys.ValidationMessages;
import org.zkoss.image.AImage;
import org.zkoss.image.Image;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

import vn.cal.core.Asset;
import vn.cal.core.BaseObject;
import vn.cal.enums.LoaiThongBao;

@Entity
@Table(name = "bai_dang")
public class BaiDang extends Asset<BaiDang>{

	private String tieuDe;
	private String noiDung;
	private ThanhPho thanhPho;
	private List<NhomMau> nhomMaus = new ArrayList<NhomMau>();
	private NhomMau nhomMau;
	private List<BinhLuan> listBinhLuan = new ArrayList<BinhLuan>();
	private Image imageContent;
	private String iconName = "";
	private String iconUrl = "";
	private boolean active = false;
	private String wd = "";

	@Transient
	public String getWd() {
		return wd;
	}

	public void setWd(String wd) {
		this.wd = wd;
	}

	@Transient
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	@Transient
	public NhomMau getNhomMau() {
		return nhomMau;
	}
	@Transient
	public void setNhomMau(NhomMau nhomMau) {
		this.nhomMau = nhomMau;
	}
	
	@Transient
	private boolean flagImage = true;
	
	@Transient
	public org.zkoss.image.Image getImageContent() throws FileNotFoundException, IOException {
		if (imageContent == null && !getCore().TT_DA_XOA.equals(getTrangThai())) {
			if (flagImage) {

				loadImageIsView();
			}
		}
		return imageContent;
	}
	
	private void loadImageIsView() throws FileNotFoundException, IOException {
		String imgName = "";
		String path = "";
		path = folderStore() + getIconName();
		if (!"".equals(getIconUrl()) && new File(path).exists()) {
			try (FileInputStream fis = new FileInputStream(new File(path));) {
				setImageContent(new AImage(imgName, fis));
			}
		} 
		/*else {
			String filePath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/backend/assets/img/avatar.jpg");
			try (FileInputStream fis = new FileInputStream(new File(filePath));) {
				setImageContent(new AImage("imge", fis));
			}
		}*/
	}

	@Command
	public void attachImages(@BindingParam("media") final Media media,
			@BindingParam("vmsgs") final ValidationMessages vmsgs) {
		if (media instanceof org.zkoss.image.Image) {
			String tenFile = media.getName();
			tenFile = tenFile.replace(" ", "");
			tenFile = unAccent(tenFile.substring(0, tenFile.lastIndexOf("."))) + "_"
					+ Calendar.getInstance().getTimeInMillis() + tenFile.substring(tenFile.lastIndexOf("."));
			setImageContent((org.zkoss.image.Image) media);
			setIconName(tenFile);
			setWd("50%");
			if (vmsgs != null) {
				vmsgs.clearKeyMessages("errLabel");
			}
			BindUtils.postNotifyChange(null, null, this, "imageContent");
			BindUtils.postNotifyChange(null, null, this, "iconname");
			BindUtils.postNotifyChange(null, null, this, "wd");
		} else {
			showNotification("Không phải hình ảnh", "", "warning");
		}
	}
	
	public void setImageContent(org.zkoss.image.Image _imageContent) {
		this.imageContent = _imageContent;
	}
	
	private boolean beforeSaveImg() throws IOException {
		if (getImageContent() == null) {
			return false;
		}
		saveImageToServer();
		return true;
	}
	protected void saveImageToServer() throws IOException {

		Image imageContent2 = getImageContent();
		if (imageContent2 != null) {
			// luu hinh
			if (getIconName() != null && !getIconName().isEmpty()) {
				setIconUrl(folderImageUrl().concat(getIconName()));
				final File baseDir = new File(folderStore().concat(getIconName()));
				Files.copy(baseDir, imageContent2.getStreamData());
			}
		}
	}
	@Transient
	public String folderImageUrl() {
		return "/" + Labels.getLabel("filestore.folder") + "/baidang/";
	}
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "chi_tiet_bai_dang", joinColumns = {
			@JoinColumn(name = "idBaiDang", nullable = false, updatable = false) },
			inverseJoinColumns = { @JoinColumn(name = "idNhomMau", nullable = false, updatable = false) })
	public List<NhomMau> getNhomMaus() {
		return nhomMaus;
	}

	public void setNhomMaus(List<NhomMau> nhomMaus) {
		this.nhomMaus = nhomMaus;
	}
	
	private boolean flagLoad;
	
	
	@Transient
	public boolean isFlagLoad() {
		return flagLoad;
	}

	public void setFlagLoad(boolean flagLoad) {
		this.flagLoad = flagLoad;
	}

	@Transient
	public List<BinhLuan> getListBinhLuan() {
		if(!noId()) {
			if(!flagLoad){
				flagLoad = true;
				listBinhLuan = find(BinhLuan.class)
						.where(QBinhLuan.binhLuan.trangThai.eq(getCore().TT_AP_DUNG))
						.where(QBinhLuan.binhLuan.baiDang.eq(this))
						.fetch();
			}
		}		
		return listBinhLuan;
	}
	
	public void setListBinhLuan(List<BinhLuan> listBinhLuan) {
		this.listBinhLuan = listBinhLuan;
	}
	
	public String getTieuDe() {
		return tieuDe;
	}
	
	public void setTieuDe(String tieuDe) {
		this.tieuDe = tieuDe;
	}
	
	public String getNoiDung() {
		return noiDung;
	}
	
	public void setNoiDung(String noiDung) {
		this.noiDung = noiDung;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idThanhPho")
	public ThanhPho getThanhPho() {
		return thanhPho;
	}
	
	public void setThanhPho(ThanhPho thanhPho) {
		this.thanhPho = thanhPho ;
	}
	
	@Command
	public void saveBaiDang(@BindingParam("list") final Object Object, 
			@BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn,
			@BindingParam("count") final Object count) throws IOException {
			beforeSaveImg();
			setNgayTao(new Date());
			saveAsShowNotification();
			for(BinhLuan bl:listBinhLuan){
				bl.saveNotShowNotification();
			}
			wdn.detach();
			BindUtils.postNotifyChange(null, null, Object, attr);
			BindUtils.postNotifyChange(null, null, Object, "count");
	}
	
	@Command
	public void saveBaiDangMoi(@BindingParam("list") final Object object, 
			@BindingParam("nguoi") final Object nv, 
			@BindingParam("attr") final String attr,
			@BindingParam("res") final String res) throws IOException {
		beforeSaveImg();
		setNgayTao(new Date());
		NhanVien nhanVien = (NhanVien)nv;
		setNguoiTao(nhanVien);
		setPublishStatus(false);
		saveNotShowNotification();
		count++;
		showNotification("Bài đăng của bạn đang đợi kiểm duyệt !", "", "success");
		if(nhomMaus.size() > 0){
			System.out.println(nhomMaus.size());
			this.setActive(true);
			BindUtils.postNotifyChange(null, null, this, "active");
		}
		for(BinhLuan bl:listBinhLuan){
			bl.saveNotShowNotification();
		}
		BindUtils.postNotifyChange(null, null, this, "listBaiDang");
		BindUtils.postNotifyChange(null, null, count, "count");
	}
	
	@Command
	public void saveChiTiet(@BindingParam("vm") final Object listObject, @BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn){
		BaiDang bd = (BaiDang)listObject;
		if(bd != null)
		bd.setNhomMaus(nhomMaus);
		wdn.detach();
		BindUtils.postNotifyChange(null, null, bd, attr);
	}
	
	@Transient
	public List<NhomMau> getListNhomMau(){
		return getCore().getNhomMauService().getTenNhomMau().fetch();
	}
	
	@Command
	public void selectNhomMau(){
		if(nhomMaus.contains(getNhomMau())){
			nhomMaus.remove(getNhomMau());
		}
	}
	@Command
	public void deleteChiTiet(final @BindingParam("notify") Object beanObject,
			final @BindingParam("attr") @Default(value = "*") String attr) {
		final NhomMau nm = (NhomMau) beanObject;
		this.nhomMaus.remove(nm);
		BindUtils.postNotifyChange(null, null, this, attr);
	}
	@Command
	public void deleteBinhLuan(final @BindingParam("notify") Object beanObject,
			final @BindingParam("attr") @Default(value = "*") String attr) {
		final BinhLuan bl = (BinhLuan) beanObject;
		bl.setDaXoa(true);
		bl.setTrangThai("da_xoa");
		if (!bl.noId()) {
			bl.saveNotShowNotification();
		}
		this.getListBinhLuan().remove(bl);
		BindUtils.postNotifyChange(null, null, this, attr);
	}
	
}
