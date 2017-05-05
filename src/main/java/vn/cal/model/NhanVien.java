package vn.cal.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jasypt.util.password.BasicPasswordEncryptor;
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

import com.google.common.base.Strings;

import vn.cal.core.Model;

@Entity
@Table(name = "nhanvien")
public class NhanVien extends Model<NhanVien> {
	public static transient final Logger LOG = LogManager.getLogger(NhanVien.class.getName());
/*	public static final String TONGBIENTAP = "tongbientap";
	public static final String CONGTACVIEN = "congtacvien";
	public static final String BIENTAPVIEN = "bientapvien";
	public static final String QUANTRIVIEN = "quantrivien";*/
	
	public List<DeNghi> listDeNghiNhan = new ArrayList<DeNghi>();
	public List<DeNghi> listDeNghiGui = new ArrayList<DeNghi>();
	private List<ThongBao> listThongBao = new ArrayList<ThongBao>();

	private String chucVu = "";
	private String diaChi = "";
	private int soThuTu;
	private Set<String> quyens = new HashSet<>();
	private Set<String> tatCaQuyens = new HashSet<>();
	
	private String email = "";

	private Image imageContent;
	
	private String iconName = "";
	
	private String iconUrl = "";

	private String hoVaTen = "";

	private String matKhau = "";

	private  Date ngaySinh;

	private String soDienThoai = "";
	
	private String gioiTinh = "";
	
	private String tenCoQuan = "";

	private String matKhau2 = "";
	
	private String emailCoQuan = "";

	private Set<VaiTro> vaiTros = new HashSet<>();
	
	private List<BaiDang> listBaiDang = new ArrayList<BaiDang>();
	
	private List<DangKy> listDangKy = new ArrayList<DangKy>();
	
	private List<NhatKy> listNhatKy = new ArrayList<NhatKy>();
	
	private List<BinhLuan> listBinhLuan = new ArrayList<BinhLuan>();
	
	private boolean flagChange = false;
	private boolean checkKichHoat;

	public boolean isCheckKichHoat() {
		return checkKichHoat;
	}

	public void setCheckKichHoat(boolean checkKichHoat) {
		this.checkKichHoat = checkKichHoat;
	}
	@Transient
	public boolean isFlagChange() {
		return flagChange;
	}

	public void setFlagChange(boolean flagChange) {
		this.flagChange = flagChange;
	}
	
	private NhomMau nhomMau;
	
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
		} else {
			String filePath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/backend/assets/img/avatar.jpg");
			try (FileInputStream fis = new FileInputStream(new File(filePath));) {
				setImageContent(new AImage("imge", fis));
			}
		}
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
			if (vmsgs != null) {
				vmsgs.clearKeyMessages("errLabel");
			}
			BindUtils.postNotifyChange(null, null, this, "imageContent");
			BindUtils.postNotifyChange(null, null, this, "iconname");
		} else {
			showNotification("Không phải hình ảnh", "", "warning");
		}
	}
	
	public void setImageContent(org.zkoss.image.Image _imageContent) {
		this.imageContent = _imageContent;
	}
	
	public int getSoThuTu() {
		return soThuTu;
	}

	public void setSoThuTu(int soThuTu) {
		this.soThuTu = soThuTu;
	}

	@Transient
	public String getVaiTroText() {
		String result = "";
		for (VaiTro vt : getVaiTros()) {
			result += (result.isEmpty() ? "" : ", ") + vt.getTenVaiTro();
		}
		return result;
	}

	public NhanVien() {
		super();
	}

	public NhanVien(String email_, String hoTen) {
		super();
		email = email_;
		hoVaTen = hoTen;
	}

	@Override
	public void doSave() {
		super.doSave();
	}

	public String getChucVu() {
		return chucVu;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public String getEmail() {
		return email;
	}


	public String getHoVaTen() {
		return hoVaTen;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public  Date getNgaySinh() {
		return ngaySinh;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}
	
	public String getGioiTinh() {
		return gioiTinh;
	}

	public void setGioiTinh(String gioiTinh) {
		this.gioiTinh = gioiTinh;
	}

	public String getTenCoQuan() {
		return tenCoQuan;
	}

	public void setTenCoQuan(String tenCoQuan) {
		this.tenCoQuan = tenCoQuan;
	}

	public String getEmailCoQuan() {
		return emailCoQuan;
	}

	public void setEmailCoQuan(String emailCoQuan) {
		this.emailCoQuan = emailCoQuan;
	}
	
	@Transient
	public List<NhatKy> getListNhatKy() {
		listNhatKy = find(NhatKy.class).where(QNhatKy.nhatKy.trangThai.eq(getCore().TT_AP_DUNG))
				.where(QNhatKy.nhatKy.nguoiTao.eq(this))
				.orderBy(QNhatKy.nhatKy.ngayTao.desc()).limit(6)
				.fetch();
		return listNhatKy;
	}

	public void setListNhatKy(List<NhatKy> listNhatKy) {
		this.listNhatKy = listNhatKy;
	}
	
	@Transient
	public List<ThongBao> getListThongBao() {
		listThongBao = find(ThongBao.class).where(QThongBao.thongBao.trangThai.eq(getCore().TT_AP_DUNG))
				.where(QThongBao.thongBao.nguoiTao.eq(this))
				.orderBy(QThongBao.thongBao.ngayTao.desc()).limit(6)
				.fetch();
		return listThongBao;
	}

	public void setListThongBao(List<ThongBao> listThongBao) {
		this.listThongBao = listThongBao;
	}

	@Transient
	public List<BaiDang> getListBaiDang() {
		listBaiDang = find(BaiDang.class).where(QBaiDang.baiDang.trangThai.eq(getCore().TT_AP_DUNG))
				.where(QBaiDang.baiDang.nguoiTao.eq(this))
				.orderBy(QBaiDang.baiDang.ngayTao.desc())
				.fetch();
		return listBaiDang;
	}
	
	public void setListBaiDang(List<BaiDang> listBaiDang) {
		this.listBaiDang = listBaiDang;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "nguoiDung", cascade = CascadeType.ALL)
	public List<BinhLuan> getListBinhLuan() {
		return listBinhLuan;
	}

	public void setListBinhLuan(List<BinhLuan> listBinhLuan) {
		this.listBinhLuan = listBinhLuan;
	}
	
	@Transient
	public List<DangKy> getListDangKy() {
		listDangKy = find(DangKy.class).where(QDangKy.dangKy.trangThai.eq(getCore().TT_AP_DUNG))
				.where(QDangKy.dangKy.nguoiTao.eq(this))
				.orderBy(QDangKy.dangKy.ngayTao.desc()).limit(4)
				.fetch();
		return listDangKy;
	}

	public void setListDangKy(List<DangKy> listDangKy) {
		this.listDangKy = listDangKy;
	}
	
	@ManyToOne
	public NhomMau getNhomMau() {
		return nhomMau;
	}

	public void setNhomMau(NhomMau nhomMau) {
		this.nhomMau = nhomMau;
	}

	@Transient
	public String getMatKhau2() {
		return matKhau2;
	}

	public void setMatKhau2(String matKhau2) {
		this.matKhau2 = matKhau2;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "nhanvien_vaitro", joinColumns = {
			@JoinColumn(name = "nhanVien_id") }, inverseJoinColumns = { @JoinColumn(name = "vaiTros_id") })
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	public Set<VaiTro> getVaiTros() {
		return vaiTros;
	}
	
	@Transient
	public List<DeNghi> getListDeNghiNhan() {
		List<DeNghi> list = new ArrayList<>();
		list = find(DeNghi.class).where(QDeNghi.deNghi.trangThai.eq(getCore().TT_AP_DUNG))
				.where(QDeNghi.deNghi.nguoiNhan.eq(this))
				.orderBy(QDeNghi.deNghi.ngayTao.desc()).limit(4)
				.fetch();
		return list;
	}
	
	public void setListDeNghiNhan(List<DeNghi> listDeNghiNhan) {
		this.listDeNghiNhan = listDeNghiNhan;
	}
	
	@Transient
	public List<DeNghi> getListDeNghiGui() {
		List<DeNghi> list = new ArrayList<>();
		list = find(DeNghi.class).where(QDeNghi.deNghi.trangThai.eq(getCore().TT_AP_DUNG))				
				.where(QDeNghi.deNghi.nguoiGui.eq(this))
				.orderBy(QDeNghi.deNghi.ngayTao.desc()).limit(4)
				.fetch();
		return list;
	}
	
	public void setListDeNghiGui(List<DeNghi> listDeNghiGui) {
		this.listDeNghiGui = listDeNghiGui;
	}

	@Transient
	public Set<String> getTatCaQuyens() {
		if (tatCaQuyens.isEmpty()) {
			tatCaQuyens.addAll(quyens);
			for (VaiTro vaiTro : vaiTros) {
				if (!vaiTro.getAlias().isEmpty()) {
					tatCaQuyens.add(vaiTro.getAlias());
				}
				tatCaQuyens.addAll(vaiTro.getQuyens());
			}
			if (Labels.getLabel("email.superuser").equals(email)) {
				tatCaQuyens.add("*");
			}
		}
		return tatCaQuyens;
	}

	public void setQuyens(final Set<String> dsChoPhep) {
		quyens = dsChoPhep;
	}
	
	public void setChucVu(final  String _chuVu) {
		chucVu = Strings.nullToEmpty(_chuVu);
	}

	public void setDiaChi(final  String _diaChi) {
		diaChi = Strings.nullToEmpty(_diaChi);
	}

	public void setEmail(final  String _email) {
		email = Strings.nullToEmpty(_email);
	}

	public void setHoVaTen(final  String _hoVaTen) {
		hoVaTen = Strings.nullToEmpty(_hoVaTen);
	}

	public void setMatKhau(final  String _matKhau) {
		matKhau = Strings.nullToEmpty(_matKhau);
	}

	public void setNgaySinh(final Date _ngaySinh) {
		ngaySinh = _ngaySinh;
	}

	public void setSoDienThoai(final  String _soDienThoai) {
		soDienThoai = Strings.nullToEmpty(_soDienThoai);
	}

	public void setVaiTros(final Set<VaiTro> vaiTros1) {
		vaiTros = vaiTros1;
	}
	
	@Transient
	public Quyen getTatCaQuyen() {
		return quyen;
	}
	
	private Quyen quyen = new Quyen(new SimpleAccountRealm() {
		@Override
		protected AuthorizationInfo getAuthorizationInfo(
				final  PrincipalCollection arg0) {
			final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			info.setStringPermissions(getTatCaQuyens());
			return info;
		}
	});
	
	private String salkey;
	
	public String getSalkey() {
        return salkey;
    }
    
    public void setSalkey(String salkey) {
        this.salkey = salkey;
    }
	 
	public String getCookieToken(long expire) {
        String token = getId() + ":" + expire +":";
        return Base64.encodeBase64String(token.concat(DigestUtils.md5Hex(token + matKhau + ":" + salkey)).getBytes());
    }
	
	@Transient
	public String getTen(){
		String[] str = getHoVaTen().split(" ");
		return str[str.length-1];
	}

    public void updatePassword(String pass){
        BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
        String salkey = getSalkey();
        if(salkey==null || salkey.equals("")){
            salkey = encryptor.encryptPassword((new Date()).toString());
        }
        String passNoHash = pass + salkey;
        String passHash = encryptor.encryptPassword(passNoHash);
        setSalkey(salkey);
        setMatKhau(passHash);
    }
    
    @Command
    public void saveNguoiDung(@BindingParam("list") final Object listObject,
			@BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) throws IOException{
    	if (matKhau2 != null && !matKhau2.isEmpty()) {
			updatePassword(matKhau2);
		}
    	beforeSaveImg();
    	saveAsShowNotification();
    	if(wdn != null)
    		wdn.detach();
    	Executions.sendRedirect("/nguoidung");
		BindUtils.postNotifyChange(null, null, this, "*");
		BindUtils.postNotifyChange(null, null, listObject, attr);
    }
    
    @Command
    public void saveNguoiDungCu(@BindingParam("res") final String res,
			@BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) throws IOException{
    	if (matKhau2 != null && !matKhau2.isEmpty()) {
			updatePassword(matKhau2);
		}
    	beforeSaveImg();
    	saveAsShowNotification();
		NhatKy nhatKy = new NhatKy();
		nhatKy.setDoiTuongId(this.getId());
		nhatKy.setLoaiDoiTuong("tài khoản");
		nhatKy.setHanhDong("cập nhật");
		nhatKy.setNguoiTao(this);
		nhatKy.saveNotShowNotification();
    	if(wdn != null)
    		wdn.detach();
    	Executions.sendRedirect("/"+res+"/xem/"+getId());
		BindUtils.postNotifyChange(null, null, this, "*");
		BindUtils.postNotifyChange(null, null, this, attr);
    }
    
    @Command
    public void saveCaNhan(@BindingParam("res") final String res,
			@BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) throws IOException{
    	if (matKhau2 != null && !matKhau2.isEmpty()) {
			updatePassword(matKhau2);
		}
    	beforeSaveImg();
    	saveAsShowNotification();
		NhatKy nhatKy = new NhatKy();
		nhatKy.setDoiTuongId(this.getId());
		nhatKy.setLoaiDoiTuong("tài khoản");
		nhatKy.setHanhDong("cập nhật");
		nhatKy.setNguoiTao(this);
		nhatKy.setNgayTao(new Date());
		nhatKy.saveNotShowNotification();
    	if(wdn != null)
    		wdn.detach();
    	Executions.sendRedirect("/"+res);
		BindUtils.postNotifyChange(null, null, this, "*");
		BindUtils.postNotifyChange(null, null, this, attr);
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
		return "/" + Labels.getLabel("filestore.folder") + "/doituong/";
	}
    
    @Command
	public void cancel(@BindingParam("res") final String res) 
			throws IOException {	
		Executions.sendRedirect("/" + res);
	}
    
    @Command
	public void deleteBaiDang(final @BindingParam("notify") Object beanObject,
			final @BindingParam("attr") @Default(value = "*") String attr) {
		final BaiDang bl = (BaiDang) beanObject;
		bl.setDaXoa(true);
		bl.setTrangThai("da_xoa");
		if (!bl.noId()) {
			bl.saveNotShowNotification();
		}
		this.getListBaiDang().remove(bl);
		BindUtils.postNotifyChange(null, null, this, attr);
	}
}
