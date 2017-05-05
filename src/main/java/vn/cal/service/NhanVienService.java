package vn.cal.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.model.DangKy;
import vn.cal.model.NhanVien;
import vn.cal.model.QDangKy;
import vn.cal.model.QNhanVien;
public class NhanVienService extends BasicService<NhanVien>{
	
	private List<Map<String, Object>> listResult = new ArrayList<Map<String, Object>>();
	private String titleDate = "";
		
	public List<Map<String, Object>> getListResult() {
		return listResult;
	}

	public void setListResult(List<Map<String, Object>> listResult) {
		this.listResult = listResult;
	}
	
	public JPAQuery<NhanVien> getTargetQuery(){
		String paramTuKhoa = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String publishStatus = MapUtils.getString(argDeco(),Labels.getLabel("param.publishstatus"),"");
		JPAQuery<NhanVien> q = find(NhanVien.class)
				.where(QNhanVien.nhanVien.trangThai.ne(getCore().TT_DA_XOA));
		q.orderBy(QNhanVien.nhanVien.ngayTao.desc());
		
		if(!publishStatus.isEmpty()) {
			boolean status = Boolean.valueOf(publishStatus);
			q.where(QNhanVien.nhanVien.publishStatus.eq(status));
		}
		
		if (!paramTuKhoa.isEmpty()) {
			String tukhoa = "%" + paramTuKhoa + "%";
			q.where(QNhanVien.nhanVien.hoVaTen.like(tukhoa));
		}
		return q;
	}
	public NhanVien findById(Long id){
		NhanVien q = find(NhanVien.class)
				.where(QNhanVien.nhanVien.trangThai.ne(getCore().TT_DA_XOA))
				.where(QNhanVien.nhanVien.id.eq(id)).limit(1).fetchOne();
		return q;
	}
	@Command 
	public void login(@BindingParam("email") final String email, @BindingParam("password") String password){
		System.out.println("Email: "+email);
		System.out.println("Password: "+password);
		NhanVien nhanVien = new JPAQuery<NhanVien>(em()).from(QNhanVien.nhanVien)
				.where(QNhanVien.nhanVien.daXoa.isFalse()).where(QNhanVien.nhanVien.trangThai.ne(getCore().TT_DA_XOA))
				.where(QNhanVien.nhanVien.email.eq(email)).fetchFirst();
		BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
		if (nhanVien != null && encryptor.checkPassword(password.trim() + nhanVien.getSalkey(), nhanVien.getMatKhau())) {
			System.out.println("nhan vien != null");
			String cookieToken = nhanVien
					.getCookieToken(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
			Session zkSession = Sessions.getCurrent();
			zkSession.setAttribute("email", cookieToken);
			HttpServletResponse res = (HttpServletResponse) Executions.getCurrent().getNativeResponse();
			Cookie cookie = new Cookie("email", cookieToken);
			cookie.setPath("/");
			cookie.setMaxAge(1000000000);
			res.addCookie(cookie);
			System.out.print("nhanVien "+nhanVien.getChucVu());
			if(nhanVien.getChucVu().equals("admin"))	
				Executions.sendRedirect("/nguoidung");
			else 
				Executions.sendRedirect("/");
		} else {
			showNotification("Đăng nhập không thành công", "", "error");
		}
	}
	
	public void actionLogout() {
		System.out.println("logout");
		NhanVien NhanVienLogin = getNhanVien(true);
		if (NhanVienLogin != null && !NhanVienLogin.noId()) {
			Session zkSession = Sessions.getCurrent();
			zkSession.removeAttribute("email");
			HttpServletResponse res = (HttpServletResponse) Executions.getCurrent().getNativeResponse();
			Cookie cookie = new Cookie("email", null);
			cookie.setPath("/");
			cookie.setMaxAge(0);
			res.addCookie(cookie);
			Executions.sendRedirect("/");
		}
	}
	
	@Command
	public void logout(){
		Messagebox.show("Bạn chắc chắn muốn đăng xuất", "Xác nhận",
				Messagebox.CANCEL | Messagebox.OK, Messagebox.QUESTION, new EventListener<Event>() {
				
				@Override
				public void onEvent(final Event event) {
					if (Messagebox.ON_OK.equals(event.getName())) {
						actionLogout();
					}
				}
			});
	}
	
	public NhanVien getNhanVien(boolean saving) {
		if (Executions.getCurrent() == null) {
			return null;
		}
		return getNhanVien(saving, (HttpServletRequest) Executions.getCurrent().getNativeRequest(),
				(HttpServletResponse) Executions.getCurrent().getNativeResponse());
	}

	public JPAQuery<DangKy> getDangKy(NhanVien nv){
		JPAQuery<DangKy> q = find(DangKy.class)
				.where(QDangKy.dangKy.trangThai.ne(getCore().TT_DA_XOA))
				.where(QDangKy.dangKy.nguoiTao.eq(nv));
				q.orderBy(QDangKy.dangKy.ngayTao.desc()).limit(1);
				q.where(QDangKy.dangKy.trangThaiDangKy.eq("waiting"));
		return q;
	}
	
	@Command
	public void xuatExcel() throws IOException {
		List<NhanVien> listNhanVien = getTargetQuery().fetch();
		ExcelUtil.exportThongKe("Danh sách người dùng "+ titleDate, "danhsachnguoidung", "danhsachnguoidung", listNhanVien);
	}
}
