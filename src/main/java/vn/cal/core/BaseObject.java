package vn.cal.core;

import java.io.IOException;
import java.text.Normalizer;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.persistence.Transient;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.math.NumberUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Default;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.model.BaiDang;
import vn.cal.model.NhanVien;
import vn.cal.model.QNhanVien;
import vn.cal.model.Quyen;
import vn.cal.service.NhanVienService;

public class BaseObject<T> extends CoreObject<T> {
	
	private List<BaiDang> listDuyet;
	public BaseObject() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Map<Object, Object> getArg() {
		Map<Object, Object> arg = super.getArg();
				
		return arg;
	}
	
	public Quyen getQuyen() {
		return getNhanVien().getTatCaQuyen();
	}
	
	public NhanVien getNhanVien(boolean isSave, HttpServletRequest req, HttpServletResponse res) {
		NhanVien nhanVien = null;

		String key = getClass() + "." + NhanVien.class;
		nhanVien = (NhanVien) req.getAttribute(key);
		if (nhanVien == null || nhanVien.noId()) {
			Object token = null;
			Cookie[] cookies = req.getCookies();
			if (cookies != null) {
				for (Cookie c : cookies) {
					if ("email".equals(c.getName())) {
						token = c.getValue();
						break;
					}
				}
			}
			if (token == null) {
				HttpSession ses = req.getSession();
				token = ses.getAttribute("email");
			}
			if (token != null) {
				String[] parts = new String(Base64.decodeBase64(token.toString())).split(":");
				NhanVien nhanVienLogin = em().find(NhanVien.class, NumberUtils.toLong(parts[0], 0));
				if (parts.length == 3 && nhanVienLogin != null) {
					long expire = NumberUtils.toLong(parts[1], 0);
					if (expire > System.currentTimeMillis() && token.equals(nhanVienLogin.getCookieToken(expire))) {
						nhanVien = nhanVienLogin;
					}
				}
			}
			if (!isSave && (nhanVien == null)) {
				if (nhanVien == null) {
					bootstrapNhanVien();
				}
				nhanVien = new NhanVien();
				if (token != null) {
					req.getSession().removeAttribute("email");
				}
				StringBuilder url = new StringBuilder();
				url.append(req.getScheme()) // http (https)
						.append("://") // ://
						.append(req.getServerName()); // localhost (domain name)
				if (req.getServerPort() != 80 && req.getServerPort() != 443) {
					url.append(":").append(req.getServerPort()); // app name
				}
				try {
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			req.setAttribute(key, nhanVien);
		}

		return isSave && nhanVien != null && nhanVien.noId() ? null : nhanVien;
	}
	
	public String unAccent(String s) {
		String temp = Normalizer.normalize(s.toLowerCase(), Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(temp).replaceAll("").replaceAll("đ", "d").replaceAll(" ", "")
				.replaceAll("[^a-zA-Z0-9 -]", "");
	}
	
	@Transient
	public boolean isNhanVienDaKhoa() {
		return !getNhanVien().isCheckApDung();
	}
	

	@Transient
	public boolean isNhanVienDaKichHoat() {
		return !getNhanVien().isCheckKichHoat();
	}
	
	public void bootstrapNhanVien() {
		JPAQuery<NhanVien> q = find(NhanVien.class)
				.where(QNhanVien.nhanVien.daXoa.isFalse())
				.where(QNhanVien.nhanVien.trangThai.eq(getCore().TT_AP_DUNG));
		if (q.fetchCount() == 0) {
			final NhanVien NhanVien = new NhanVien("admin", "Super Admin");
			//NhanVien.getQuyens().add("*");
			NhanVien.updatePassword("tcx@123");
			NhanVien.saveNotShowNotification();
		}
	} 
    
	public NhanVien getNhanVien() {
		return fetchNhanVien(false);
	}
	
	public NhanVien fetchNhanVienSaving() {
		return fetchNhanVien(true);
	}
	
	public int count = 0;
	
	public int getCount() {
		return (int)getCore().getBaiDangService().getBaiDangChuaDuyet().fetchCount();
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public NhanVien fetchNhanVien(boolean saving) {
		if (Executions.getCurrent() == null) {
			return null;
		}
		return getNhanVien(saving, (HttpServletRequest) Executions.getCurrent().getNativeRequest(),
				(HttpServletResponse) Executions.getCurrent().getNativeResponse());
	}
	
	public  Date date(Object key) {
		Object result = argDeco().get(key);
		if (!(result instanceof Date) && result != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					CoreObject.DATE_FORMAT);
			result = simpleDateFormat.parse(result.toString(),
					new ParsePosition(0));
		}
		if (result != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime((Date) result);
			cal.add(Calendar.HOUR, 7);
			result = cal.getTime();
		}
		return (Date) result;
	}
	
	@Transient
	public NhanVienService getNhanViens() {
		return new NhanVienService();
	}
	@Command
	public final void cmd(
			@BindingParam("ten") @Default(value = "") final String ten,
			@BindingParam("notify")  Object beanObject,
			@BindingParam("attr") @Default(value = "*") String fields)
			{
		invoke(null, ten, null, beanObject, fields, null, false);
	}
	
	@Command
	public void cancelPopup(@BindingParam("list") final Object listObject,
			@BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) {
		wdn.detach();
		BindUtils.postNotifyChange(null, null, listObject, attr);
	}
	
	@Command
	public void close(@BindingParam("wdn") final Window wdn) {
		wdn.detach();
	}
	
	@Command
	public void redirectPage(@BindingParam("zul") String zul,
			@BindingParam("vmArgs") Object vmArgs, @BindingParam("vm") Object vm) {
		System.out.println("redirect");
		Map<String, Object> args = new HashMap<>();
		args.put("vmArgs", vmArgs);
		args.put("vm", vm);
		Executions.createComponents(zul, null, args);
	}
	
	@Command
	public void rdPage(@BindingParam("zul") String zul,
			@BindingParam("vmArgs") Object vmArgs, @BindingParam("vm") Object vm) {
		System.out.println("rdPage");
		Map<String, Object> args = new HashMap<>();
		args.put("vmArgs", vmArgs);
		args.put("vm", vm);
		Executions.sendRedirect(zul);
	}
	
	public void showNotification(String content, String title, String type) {
		switch (type) {
			case "success":
				Clients.evalJavaScript("toastrSuccess('" + content + "', '" + title + "');");
				break;
			case "info":
				Clients.evalJavaScript("toastrInfo('" + content + "', '" + title + "');");
				break;
			case "warning":
				Clients.evalJavaScript("toastrWarning('" + content + "', '" + title + "');");
				break;
			case "error":
				Clients.evalJavaScript("toastrError('" + content + "', '" + title + "');");
				break;
			default:
				break;
		}
	}
	
	public List<BaiDang> getListDuyet(){
		listDuyet = getCore().getBaiDangService().getBaiDangChuaDuyet().fetch();
		return listDuyet;
	}
	
	public void setListDuyet(List<BaiDang> listDuyet){
		this.listDuyet = listDuyet;
	}
	
	@Command
	public void saveBaiDangS(@BindingParam("list") final Object Object, @BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn,
			@BindingParam("vm") final Object vm, 
			@BindingParam("c") final String c) throws IOException {
		BaiDang bd = (BaiDang)vm;
		if(attr.equals("listDuyet")){
			bd.setNgaySua(new Date());
			bd.saveAsShowNotification();
			wdn.detach();
			//	ThongBao tb = new ThongBao(this.getNguoiTao(), "đã duyệt", LoaiThongBao.TBDENGHI);
		//	tb.saveNotShowNotification();
			setCount( bd.getCount());
			System.out.println("getCount"+getCount());
			BindUtils.postNotifyChange(null, null, Object, attr);
			BindUtils.postNotifyChange(null, null, Object , c);
		}
		
	}
}
