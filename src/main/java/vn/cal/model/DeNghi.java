package vn.cal.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.Window;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.Main;
import vn.cal.core.Asset;

@Entity
@Table(name = "de_nghi")
public class DeNghi extends Asset<DeNghi>{

	private NhanVien nguoiNhan;
	private NhanVien nguoiGui;
	private String trangThaiDeNghi = "waiting";
	private NhomMau nhomMau;
	private LoaiMau loaiMau;
	private String style;
	private String button="Gửi đề nghị";
	
	public void setStyle(String style) {
		this.style = style;
	}

	@Transient
	public String getButton() {
		return button;
	}
	
	public void setButton(String button) {
		this.button = button;
	}
	
	@ManyToOne
	public NhanVien getNguoiNhan() {
		return nguoiNhan;
	}
	
	public void setNguoiNhan(NhanVien nguoiNhan) {
		this.nguoiNhan = nguoiNhan;
	}
	
	@ManyToOne
	public NhanVien getNguoiGui() {
		return nguoiGui;
	}
	
	public void setNguoiGui(NhanVien nguoiGui) {
		this.nguoiGui = nguoiGui;
	}

	public String getTrangThaiDeNghi() {
		return trangThaiDeNghi;
	}

	public void setTrangThaiDeNghi(String trangThaiDeNghi) {
		this.trangThaiDeNghi = trangThaiDeNghi;
	}
	
	@ManyToOne
	public NhomMau getNhomMau() {
		return nhomMau;
	}

	public void setNhomMau(NhomMau nhomMau) {
		this.nhomMau = nhomMau;
	}
	
	@ManyToOne
	public LoaiMau getLoaiMau() {
		return loaiMau;
	}

	public void setLoaiMau(LoaiMau loaiMau) {
		this.loaiMau = loaiMau;
	}
	
	@Transient
	public String getStyle() {
		String out = "";
		if (("rejected").equals(getTrangThaiDeNghi())) {
			out = "btn btn-danger";
		} else if (("accept").equals(getTrangThaiDeNghi())) {
			out = "btn btn-success";
		} else if (("waiting").equals(getTrangThaiDeNghi())) {
			out = "btn btn-info";
		}
		return out;
	}

	@Command
	public void saveDeNghi(@BindingParam("list") final Object listObject, @BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) {
		setNgayTao(new Date());
		saveAsShowNotification();
		wdn.detach();
		BindUtils.postNotifyChange(null, null, listObject, attr);
	}
	
	@Command
	public void newDeNghi(@BindingParam("vm") final Object vm, @BindingParam("nhomMau") final Object nhomMau, @BindingParam("attr") final String attr, @BindingParam("nguoiGui") final Object nguoiGui,
			@BindingParam("nguoiNhan") final Object nguoiNhan,
			@BindingParam("button") final String button){
		DeNghi dn = (DeNghi)vm;
		NhomMau nMau = (NhomMau)nhomMau;
		NhanVien nGui = (NhanVien) nguoiGui;
		NhanVien nNhan = (NhanVien) nguoiNhan;
		
		if(button == "Gửi đề nghị"){
			
			this.setNhomMau(nMau);
			this.setNguoiGui(nGui);
			this.setNguoiNhan(nNhan);
			this.setNguoiTao(nGui);
			this.setTrangThaiDeNghi("waiting");
			this.setNgayTao(new Date());
			saveNotShowNotification();
			showNotification("Đề nghị thành công", "", "success");
			NhatKy nhatKy = new NhatKy();
			nhatKy.setDoiTuongId(this.getId());
			nhatKy.setLoaiDoiTuong("đề nghị cho "+nNhan.getHoVaTen());
			nhatKy.setHanhDong("gửi");
			nhatKy.setNguoiTao(nGui);
			nhatKy.setNgayTao(new Date());
			nhatKy.saveNotShowNotification();
			this.setButton("Hủy đề nghị");
			BindUtils.postNotifyChange(null, null, nhatKy, "listNhatKy");
		}
		else{
			DeNghi capNhatDeNghi = find(DeNghi.class)
					.where(QDeNghi.deNghi.nguoiGui.eq(nGui))
					.where(QDeNghi.deNghi.nguoiNhan.eq(nNhan))
					.where(QDeNghi.deNghi.trangThaiDeNghi.eq("waiting")).fetchOne();
			capNhatDeNghi.setTrangThaiDeNghi("rejected");
			capNhatDeNghi.saveNotShowNotification();
			showNotification("Đã hủy đề nghị", "", "success");
			NhatKy nhatKy = new NhatKy();
			nhatKy.setDoiTuongId(this.getId());
			nhatKy.setLoaiDoiTuong("đề nghị cho "+nNhan.getHoVaTen());
			nhatKy.setHanhDong("hủy");
			nhatKy.setNguoiTao(nGui);
			nhatKy.setNgayTao(new Date());
			nhatKy.saveNotShowNotification();
			this.setButton("Gửi đăng ký");
			dn = capNhatDeNghi;
			BindUtils.postNotifyChange(null, null, nhatKy, "listNhatKy");
		}
		
		
		BindUtils.postNotifyChange(null, null, dn, "button");
		BindUtils.postNotifyChange(null, null, this, attr);
	}
	@Command
	public void saveTuChoi(@BindingParam("list") final Object listObject, @BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn){
		this.setTrangThaiDeNghi("rejected");
		setNgaySua(new Date());
		saveAsShowNotification();
		String title="Hiến máu nhân đạo_ Xác nhận đề nghị";
		String body="<div style='background-color:#EA2E4F; color: white;'>Lời đề nghị của bạn cho "+this.getNguoiNhan().getHoVaTen()+" gửi vào lúc "+this.getNgayTao()+" đã bị hủy bỏ bởi "+this.getNguoiNhan().getHoVaTen()+
						".<br />Vui lòng xem thêm thông tin tại www.hienmaunhandaodn.com.vn"+
						" <br />Chúc bạn luôn vui vẻ !</div>";
		String header = " <p>Chào bạn,</p>";
		String footer = "<div>"
              +"<table border='0' cellpadding='0' cellspacing='0' >"
              + "<tr><td>"
              +      "Liên hệ với chúng tôi tại 459 Tôn Đức Thắng, Đà Nẵng."
              +    "</td></tr>"
              +"</table></div>";
		String content = header + body + footer;
		try {
			Main.send("smtp.gmail.com", "tranlesa22@gmail.com", "hienmaunhandaodn@gmail.com", "hienmaunhandaodn",
					title, content);
		} catch (Exception e) {
			showNotification("email không tồn tại", "", "warning");
			e.printStackTrace();
		}
		if(wdn != null)
			wdn.detach();
		BindUtils.postNotifyChange(null, null, listObject, attr);
		BindUtils.postNotifyChange(null, null, this, "style");
	}
	@Command
	public void saveChapNhan(@BindingParam("list") final Object listObject, @BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn){
		this.setTrangThaiDeNghi("accept");
		DangKy dk = getCore().getNhanVienService().getDangKy(this.getNguoiNhan()).fetchOne();
		if(dk != null){
			dk.setTrangThaiDangKy("done");
			dk.saveNotShowNotification();
			String title="Hiến máu nhân đạo_ Xác nhận đề nghị";
			String body="<div style='background-color:#EA2E4F; color: white;'>Lời đề nghị của bạn cho "+this.getNguoiNhan().getHoVaTen()+" gửi vào lúc "+this.getNgayTao()+" đã được chấp nhận bởi "+this.getNguoiNhan().getHoVaTen()+
							".<br />Vui lòng xem thêm thông tin tại www.hienmaunhandaodn.comn.vn"+
							" <br />Chúc bạn luôn vui vẻ !</div>";
			String header = " <p>Chào bạn,</p>";
			String footer = "<div>"
	              +"<table border='0' cellpadding='0' cellspacing='0'>"
	              + "<tr><td class='content-block powered-by'>"
	              +      "Liên hệ với chúng tôi tại 459 Tôn Đức Thắng, Đà Nẵng."
	              +    "</td></tr>"
	              +"</table></div>";
			String content = header + body + footer;
			try {
				Main.send("smtp.gmail.com", "tranlesa22@gmail.com", "hienmaunhandaodn@gmail.com", "hienmaunhandaodn",
						title, content);
			} catch (Exception e) {
				showNotification("email không tồn tại", "", "warning");
				e.printStackTrace();
			}
			setNgaySua(new Date());
			saveAsShowNotification();
		}
		else{
			showNotification("Bạn chỉ được chấp nhận một lời đề nghị !", "", "warning");
		}
		if(wdn != null)
			wdn.detach();
		BindUtils.postNotifyChange(null, null, listObject, attr);
		BindUtils.postNotifyChange(null, null, dk, "trangThaiDangKy");
		BindUtils.postNotifyChange(null, null, this, "style");
	}
	
	@Command
	public void deleteDeNghi(@BindingParam("vm") final Object listObject, @BindingParam("attr") String attr,
			@BindingParam("nguoiGui") final Object nguoiGui,
			@BindingParam("nguoiNhan") final Object nguoiNhan){
		DeNghi dn = (DeNghi)listObject;
		NhanVien nGui = (NhanVien) nguoiGui;
		NhanVien nNhan = (NhanVien) nguoiNhan;
		System.out.println(dn.getTrangThaiDeNghi());
		dn.setDaXoa(true);
		dn.setTrangThai("da_xoa");
		NhatKy nhatKy = new NhatKy();
		nhatKy.setDoiTuongId(this.getId());
		nhatKy.setLoaiDoiTuong("đề nghị cho "+nNhan.getHoVaTen());
		nhatKy.setHanhDong("hủy");
		nhatKy.setNguoiTao(nGui);
		nhatKy.setNgayTao(new Date());
		nhatKy.saveNotShowNotification();
		BindUtils.postNotifyChange(null, null, dn, attr);
		BindUtils.postNotifyChange(null, null, nhatKy, "listNhatKy");
	}
	
	public void isDeNghi(NhanVien nv1, NhanVien nv2, Date ngayTaoDangKy){
		JPAQuery<DeNghi> q = find(DeNghi.class)
				.where(QDeNghi.deNghi.trangThai.ne(getCore().TT_DA_XOA))
				.where(QDeNghi.deNghi.nguoiGui.eq(nv1))
				.where(QDeNghi.deNghi.ngayTao.after(ngayTaoDangKy));
				q.orderBy(QDeNghi.deNghi.ngayTao.desc());
				q.where(QDeNghi.deNghi.trangThaiDeNghi.eq("waiting"));
		List<DeNghi> listDeNghi = q.fetch();
		for (DeNghi deNghi : listDeNghi) {
			System.out.println("deNghi"+deNghi.getNguoiGui().getHoVaTen());
			if(deNghi.getNguoiNhan().equals(nv2)){
				button = "Hủy đề nghị";
				BindUtils.postNotifyChange(null, null, button, "button");
			}
		}
	}
}
