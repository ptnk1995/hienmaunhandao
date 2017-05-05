package vn.cal.core;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Transient;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.AbstractMapDecorator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.GenericTypeResolver;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Default;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.google.common.base.Strings;
import com.querydsl.core.Fetchable;
import com.querydsl.core.SimpleQuery;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.ComparablePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.service.BaiDangService;
import vn.cal.service.BinhLuanService;
import vn.cal.service.ChiTietChuongTrinhService;
import vn.cal.service.ChuongTrinhHienMauService;
import vn.cal.service.DangKyService;
import vn.cal.service.DanhMucService;
import vn.cal.service.DanhMucTinTucService;
import vn.cal.service.DeNghiService;
import vn.cal.service.KhoMauService;
import vn.cal.service.LoaiMauService;
import vn.cal.service.NhanVienService;
import vn.cal.service.NhomMauService;
import vn.cal.service.PhanHoiService;
import vn.cal.service.QuanHuyenService;
import vn.cal.service.ThanhPhoService;
import vn.cal.service.TinTucService;
import vn.cal.service.VaiTroService;


interface ModelIntf {
	Object getId();
	boolean inUse();
	boolean noId();
	void setDaXoa(final boolean deleted);
	void setTrangThai(final String deleted);
}

interface UserIntf extends ModelIntf {
	
}

interface HasDaXoa {
	boolean isDaXoa();
	void setDaXoa(final boolean deleted);
}

interface HasNguoiTao {
	String getNguoiTao();
	void setNguoiTao( final String nguoiTao1);
}

interface HasNguoiSua {
	String getNguoiSua();
	void setNguoiSua( final String nguoiSua1);
}

interface HasNgayTao {
	Date getNgayTao();
	void setNgayTao( final Date ngayTao1);
}

interface HasNgaySua {
	Date getNgaySua();
	void setNgaySua( final Date ngaySua1);
}

public class CoreObject<T> implements ApplicationContextAware, ModelIntf {

	@Value("${trangthai.daxoa}")
    public String TT_DA_XOA = "";
	@Value("${trangthai.apdung}")
    public String TT_AP_DUNG = "";
	@Value("${trangthai.khongapdung}")
    public String TT_KHONG_AP_DUNG = "";
    
    public static final String DATE_FORMAT = "dd-MM-yy";
	public static final String PH_DEFPAGESIZE = "${conf.defaultpagesize:20}";
	public static final String PH_DEFNOTIFYPOS= "${conf.defnotifyposition:top_center}";
	public static final String PH_KEYPAGE = "${conf.pageName:page}";
	public static final String PH_KEYPAGESIZE = "${conf.pagesizename:pagesize}";
	public static final String PH_KEYID = "${conf.idname:id}";
	public static final String PH_FIELDMA = "${conf.idfield:ma}";
	public static final String PH_FIELDID = "${conf.idfield:id}";
	
	// ATION
	@Value("${action.xem}")
	public String XEM = ""; // duoc xem bat ky
	@Value("${action.lietke}")
	public String LIST = ""; // duoc vao trang list search url
	@Value("${action.sua}")
	public String SUA = ""; // duoc sua bat ky
	@Value("${action.xoa}")
	public String XOA = ""; // duoc xoa bat ky
	@Value("${action.them}")
	public String THEM = ""; // duoc them
	@Value("${action.gui}")
	public String GUI = ""; // duoc gui
	@Value("${action.duyet}")
	public String DUYET = ""; // duoc duyet bai
		
	/* URL */
	@Value("${url.danhmuc}")
	public String DANHMUC = "";
	
	@Value("${url.loaimau}")
	public String LOAIMAU = "";
	
	@Value("${url.nhommau}")
	public String NHOMMAU = "";
	
	@Value("${url.thanhpho}")
	public String THANHPHO = "";
	
	@Value("${url.quanhuyen}")
	public String QUANHUYEN = "";
	
	@Value("${url.chuongtrinhhienmau}")
	public String CHUONGTRINHHIENMAU = "";
	
	@Value("${url.nguoidung}")
	public String NGUOIDUNG = "";
	
	@Value("${url.vaitro}")
	public String VAITRO = "";
	
	@Value("${url.baidang}")
	public String BAIDANG = "";
	
	@Value("${url.dangky}")
	public String DANGKY = "";
	
	@Value("${url.danhmuctintuc}")
	public String DANHMUCTINTUC = "";
	
	@Value("${url.tintuc}")
	public String TINTUC = "";
	
	@Value("${url.khomau}")
	public String KHOMAU = "";
	
	@Value("${url.phanhoi}")
	public String PHANHOI = "";
	
	@Value("${url.denghi}")
	public String DENGHI = "";
	
	@Value("$url.signup")
	public String SIGNUP = "";
	
	@Value("$url.thongke")
	public String THONGKE = "";
	
    /* VAI TRO */
	@Value("${url.danhmuc}" + ":" + "${action.xem}")
	public String DANHMUCXEM = "";
	@Value("${url.danhmuc}" + ":" + "${action.them}")
	public String DANHMUCTHEM = "";
	@Value("${url.danhmuc}" + ":" + "${action.lietke}")
	public String DANHMUCLIST = "";
	@Value("${url.danhmuc}" + ":" + "${action.xoa}")
	public String DANHMUCXOA = "";
	@Value("${url.danhmuc}" + ":" + "${action.sua}")
	public String DANHMUCSUA = "";
	
	@Value("${url.loaimau}" + ":" + "${action.xem}")
	public String LOAIMAUXEM = "";
	@Value("${url.loaimau}" + ":" + "${action.them}")
	public String LOAIMAUTHEM = "";
	@Value("${url.loaimau}" + ":" + "${action.lietke}")
	public String LOAIMAULIST = "";
	@Value("${url.loaimau}" + ":" + "${action.xoa}")
	public String LOAIMAUXOA = "";
	@Value("${url.loaimau}" + ":" + "${action.sua}")
	public String LOAIMAUSUA = "";
	
	@Value("${url.vaitro}" + ":" + "${action.xem}")
	public String VAITROXEM = "";
	@Value("${url.vaitro}" + ":" + "${action.them}")
	public String VAITROTHEM = "";
	@Value("${url.vaitro}" + ":" + "${action.lietke}")
	public String VAITROLIST = "";
	@Value("${url.vaitro}" + ":" + "${action.xoa}")
	public String VAITROXOA = "";
	@Value("${url.vaitro}" + ":" + "${action.sua}")
	public String VAITROSUA = "";
	
	@Value("${url.nhommau}" + ":" + "${action.xem}")
	public String NHOMMAUXEM = "";
	@Value("${url.nhommau}" + ":" + "${action.them}")
	public String NHOMMAUTHEM = "";
	@Value("${url.nhommau}" + ":" + "${action.lietke}")
	public String NHOMMAULIST = "";
	@Value("${url.nhommau}" + ":" + "${action.xoa}")
	public String NHOMMAUXOA = "";
	@Value("${url.nhommau}" + ":" + "${action.sua}")
	public String NHOMMAUSUA = "";
	
	@Value("${url.chuongtrinhhienmau}" + ":" + "${action.xem}")
	public String CHUONGTRINHHIENMAUXEM = "";
	@Value("${url.chuongtrinhhienmau}" + ":" + "${action.them}")
	public String CHUONGTRINHHIENMAUTHEM = "";
	@Value("${url.chuongtrinhhienmau}" + ":" + "${action.lietke}")
	public String CHUONGTRINHHIENMAULIST = "";
	@Value("${url.chuongtrinhhienmau}" + ":" + "${action.xoa}")
	public String CHUONGTRINHHIENMAUXOA = "";
	@Value("${url.chuongtrinhhienmau}" + ":" + "${action.sua}")
	public String CHUONGTRINHHIENMAUSUA = "";
	
	@Value("${url.nguoidung}" + ":" + "${action.xem}")
	public String NGUOIDUNGXEM = "";
	@Value("${url.nguoidung}" + ":" + "${action.them}")
	public String NGUOIDUNGTHEM = "";
	@Value("${url.nguoidung}" + ":" + "${action.lietke}")
	public String NGUOIDUNGLIST = "";
	@Value("${url.nguoidung}" + ":" + "${action.xoa}")
	public String NGUOIDUNGXOA = "";
	@Value("${url.nguoidung}" + ":" + "${action.sua}")
	public String NGUOIDUNGSUA = "";
	
	@Value("${url.thanhpho}" + ":" + "${action.xem}")
	public String THANHPHOXEM = "";
	@Value("${url.thanhpho}" + ":" + "${action.them}")
	public String THANHPHOTHEM = "";
	@Value("${url.thanhpho}" + ":" + "${action.lietke}")
	public String THANHPHOLIST = "";
	@Value("${url.thanhpho}" + ":" + "${action.xoa}")
	public String THANHPHOXOA = "";
	@Value("${url.thanhpho}" + ":" + "${action.sua}")
	public String THANHPHOSUA = "";
	
	@Value("${url.quanhuyen}" + ":" + "${action.xem}")
	public String QUANHUYENXEM = "";
	@Value("${url.quanhuyen}" + ":" + "${action.them}")
	public String QUANHUYENTHEM = "";
	@Value("${url.quanhuyen}" + ":" + "${action.lietke}")
	public String QUANHUYENLIST = "";
	@Value("${url.quanhuyen}" + ":" + "${action.xoa}")
	public String QUANHUYENXOA = "";
	@Value("${url.quanhuyen}" + ":" + "${action.sua}")
	public String QUANHUYENSUA = "";
	
	@Value("${url.baidang}" + ":" + "${action.xem}")
	public String BAIDANGXEM = "";
	@Value("${url.baidang}" + ":" + "${action.them}")
	public String BAIDANGTHEM = "";
	@Value("${url.baidang}" + ":" + "${action.lietke}")
	public String BAIDANGLIST = "";
	@Value("${url.baidang}" + ":" + "${action.xoa}")
	public String BAIDANGXOA = "";
	@Value("${url.baidang}" + ":" + "${action.sua}")
	public String BAIDANGSUA = "";
	
	public String[] getRESOURCES() {
		return new String[] { DANHMUC, LOAIMAU, NHOMMAU, CHUONGTRINHHIENMAU, NGUOIDUNG, VAITRO, THANHPHO, QUANHUYEN,
				BAIDANG, DANGKY, DANHMUCTINTUC, TINTUC, KHOMAU, SIGNUP, THONGKE };
	}
	
	public String[] getACTIONS() {
		return new String[] { LIST, XEM, THEM, SUA, XOA, GUI, DUYET};
	}
	
    private static ApplicationContext appContext;
    private static CoreObject<?> env;
    public static final String TRANGTHAI = "trangthai";

	public static final String VIEW = "xem";

	public static final String ACTION = "_action";

	public static final String RESOURCE = "_resource";

	public static final String DEN = "den";

	public static final String FROM = "tu";

	public static final String ADD = "them";

	public static final String NGAYTAO = "ngaytao";
	
	public final Date fromDate() {
		Object result = getArg().get(FROM);
		if (!(result instanceof Date) && result != null) {
			result = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
					.parse(String.valueOf(result),
							new ParsePosition(0));
		}
		return (Date) result;
	}

	public final Date toDate() {
		Object result = getArg().get(DEN);
		if (!(result instanceof Date) && result != null) {
			result = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
					.parse(String.valueOf(result),
							new ParsePosition(0));
		}
		return (Date) result;
	}
	
	protected void setCore() {
		if (env == null) {
			env = this;
		}	
	}
	
	public CoreObject<?> getCore() {
		assert env != null;
		return env;
	}
	
	@Override
	public String toString() {
		return super.toString() + " : " + getId();
	}
	
	private transient Map<Object, Object> arg = Collections.emptyMap();
	
	private Map<String, String> listTrangThai = new HashMap<>();
	private Map<String, String> trangThaiSoanList = new HashMap<>();
	private Map<Boolean, String> listPublishStatusNull = new HashMap<>();
	
	public final Map<Boolean, String> getListPublishStatus() {
		if(listPublishStatusNull.isEmpty()){
			listPublishStatusNull.put(null, "Tất cả");
			listPublishStatusNull.put(true, "Áp dụng");
			listPublishStatusNull.put(false,"Không áp dụng");
		}
		return listPublishStatusNull;
	}
	
	public final Map<String, String> getListTrangThai() {
		if (listTrangThai.isEmpty()) {
			listTrangThai.put("ap_dung", "Áp dụng");
			listTrangThai.put("khong_ap_dung", "Không áp dụng");
		}
		return listTrangThai;
	}
	
	public Map<String, String> getTrangThaiSoanList() {
		if(trangThaiSoanList.isEmpty()){
			trangThaiSoanList.put(null, "      ");
			trangThaiSoanList.put("dang_soan", "Đang soạn");
			trangThaiSoanList.put("cho_duyet", "Chờ duyệt");
			trangThaiSoanList.put("da_duyet", "Đã duyệt");
			trangThaiSoanList.put("tu_choi", "Từ chối");
		}
		return trangThaiSoanList;
	}
	
	@Transient
	public final Map<String, String> getTrangThaiListAndNull() {
		HashMap<String, String> result = new HashMap<>();
		result.put(null, "");
		result.put("khong_ap_dung", "Không áp dụng");
		result.put("ap_dung", "Áp dụng");
		return result;
	}
	
	@Transient
	public Map<Object, Object> getArg() {
		//System.out.println("set arg");
		if (arg == Collections.emptyMap()) {
			arg = new HashMap<Object, Object>();
			arg.put(SystemPropertyUtils.resolvePlaceholders(PH_KEYPAGESIZE), Integer.valueOf(defaultPageSize()));
			if (Executions.getCurrent() != null) {
				for (final Map.Entry<String, String[]> entry : Executions.getCurrent().getParameterMap().entrySet()) {
					arg.put(entry.getKey(), entry.getValue().length > 0 ? entry.getValue()[0] : "");
				}
//				if (Executions.getCurrent().getAttribute(WebKeys.PORTLET_ID) != null) {
//					final String url = PortalUtil
//							.getCurrentURL((HttpServletRequest) Executions.getCurrent().getNativeRequest());
//					try {
//						final String qstr = Iterables.getLast(Splitter.on('?').split("?" + Strings.nullToEmpty(new URI(url).getQuery())));
//						final UriComponents components = UriComponentsBuilder.newInstance().query(qstr).build();
//						for (final Entry<String, List<String>> entry : components.getQueryParams().entrySet()) {
//							arg.put(entry.getKey(), entry.getValue().isEmpty() ? "" : entry.getValue().get(0));
//						}
//					} catch (final URISyntaxException e) {
//						throw new IllegalArgumentException(e);
//					}
//				}
			}
		}
		
		//System.out.println("arg is : " +arg);
		return arg;
	}
	
	@SuppressWarnings({ "unchecked" })
	public Map<Object, Object> argDeco() {
		return new AbstractMapDecorator(getArg()) {
			@Override
			public Object get(final Object key) {
				Object result = super.get(key);
				if (result instanceof Map.Entry) {
					result = ((Map.Entry<?, ?>) result).getKey();
				} else if (result instanceof ModelIntf) {
					result = ((ModelIntf) result).getId();
				}
				return result;
			}
		};
	}
	
	public int defaultPageSize() {
		return Integer.parseInt(SystemPropertyUtils.resolvePlaceholders("${conf.defaultpagesize:20}"));
	}
	
	public <C> JPAQuery<C> query() {
		return new JPAQuery<C>(em()).setHint("org.hibernate.cacheable", SystemPropertyUtils.resolvePlaceholders("${conf.defcacheable:true}"));
	}
	
	public String folderStoreFiles() {
		final String result = ctx().getEnvironment().getProperty("filestore.root")
				+ ctx().getEnvironment().getProperty("filestore.folder") + File.separator;
		return result.replace('/', File.separatorChar);
	}

	public String folderStore() {
		final String result = folderStoreFiles() + getClass().getSimpleName().toLowerCase() + File.separator;
		return result.replace('/', File.separatorChar);
	}

	public String folderUrl() {
		return "/" + ctx().getEnvironment().getProperty("filestore.folder") + "/"
				+ getClass().getSimpleName().toLowerCase() + "/";
	}
	
	public String fileFolder() {
		return System.getProperty("user.home") + File.separator + "dnportal" + File.separator
				+ getClass().getSimpleName().toLowerCase() + File.separator;
	}
	
    public ApplicationContext ctx(){
        System.out.println("get ctx");
        if(appContext==null){
            System.out.println("ctx null, get from current");
            appContext = WebApplicationContextUtils.getWebApplicationContext(WebApps.getCurrent().getServletContext());
        }
        return appContext;
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(appContext==null){
            appContext = applicationContext;
        }
    }

    public CoreObject<?> env() {
        assert env != null;
        return env;
    }

    public void setEnv() {
        if(env==null){
            CoreObject.env = this;
        }
    }
    
    public EntityManager em(){
        return ctx().getBean(EntityManager.class);
    }
    
    public PlatformTransactionManager transactionManager(){
        return ctx().getBean(PlatformTransactionManager.class);
    }
    
    public TransactionTemplate transactionTemplate(){
        return new TransactionTemplate(transactionManager());
    }
    
    public TransactionOperations transactionero() {
		final TransactionTemplate result = transactioner();
		result.setReadOnly(true);
		return result;
	}
	
	public final TransactionTemplate transactioner() {
		return new TransactionTemplate(transactionManager());
	}
	
    public ServletRegistrationBean dispatcherServlet(String path){
        ServletRegistrationBean srb = new ServletRegistrationBean(new DispatcherServlet(), path);
        srb.setLoadOnStartup(1);
        return srb;
    }

    public FilterRegistrationBean characterEncodingFilter() {
        FilterRegistrationBean rs = new FilterRegistrationBean(new CharacterEncodingFilter());
        rs.setMatchAfter(true);
        rs.addInitParameter("encoding", "UTF-8");
        rs.addInitParameter("forceEncoding", "true");
        return rs;
    }
    
    public void deleteModel(final ModelIntf obj, final boolean trangThaiOnly) {
		if (!obj.noId()) {
			transactioner().execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(final TransactionStatus arg0) {
					if (trangThaiOnly) {
						obj.setDaXoa(true);
						obj.setTrangThai(getCore().TT_DA_XOA);
						saveModel(obj);
					} else {
						em().remove(em().merge(obj));
					}
				}
			});
		}
	}
    
    public void saveModel(final ModelIntf obj) {
		if (obj.getClass().isAnnotationPresent(Entity.class)) {
			transactioner().execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(final TransactionStatus arg0) {
					if (obj.noId()) {
						em().persist(obj);
					} else {
						em().merge(obj);
					}
				}
			});
		}
	}
    
    public void doSave() {
		saveModel(this);
	}
    
    public void doDelete(final boolean trangThaiOnly) {
		deleteModel(this, trangThaiOnly);
	}
    
    public boolean checkInUse() {
		final boolean result = !noId() && inUse();
		if (result) {
			Clients.showNotification("Xóa không thành công. Dữ liệu đang được sử dụng!", "error", null, SystemPropertyUtils.resolvePlaceholders(PH_DEFNOTIFYPOS),
					defNotifyTime());
		}
		return result;
	}
    
    public int defNotifyTime() {
		return Integer.valueOf(SystemPropertyUtils.resolvePlaceholders("${conf.defnotifyduration:5000}"));
	}
    
    public String diffTime(final Date startTime) {
		final StringBuffer result = new StringBuffer();
		if (startTime != null) {
			final long diff = new Date().getTime() - startTime.getTime();
			final long diffMinutes = diff / (60 * 1000) % 60;
			final long diffHours = diff / (60 * 60 * 1000) % 24;
			final long diffDays = diff / (24 * 60 * 60 * 1000);
			if (diffDays == 0) {
				if (diffHours == 0) {
					result.append(diffMinutes).append(" phút");
				} else {
					result.append(diffHours).append(" giờ ").append(diffMinutes).append(" phút");
				}
			} else {
				result.append(diffDays).append(" ngày ").append(diffHours).append(" giờ");
			}
			result.append(" trước");
		}
		return result.toString();
	}
    
    @Command
	public void deleteTrangThai(final @BindingParam("notify") Object vmodel,
			final @BindingParam("attr") @Default(value = "*") String attrs) {
		
		Messagebox.show("Bạn muốn xóa dữ liệu này ?", "Xác nhận", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener<Event>() {

					@Override
					public void onEvent(final Event event) {
						if (event != null && Messagebox.ON_OK.equals(event.getName())) {
							deleteModel(CoreObject.this, true);
							Clients.showNotification("Xóa dữ liệu thành công !", "info", null, SystemPropertyUtils.resolvePlaceholders(PH_DEFNOTIFYPOS),
									defNotifyTime());
							BindUtils.postNotifyChange(null, null, vmodel, attrs);
						}
					}
				});
	}
    
    public <C> JPAQuery<C> find(final Class<C> clazz) {
		final String path = Character.toLowerCase(clazz.getSimpleName().charAt(0)) + clazz.getSimpleName().substring(1);
		EntityPath<?> ePath = null;
		try {
			final Class<?> qclass = Class.forName(clazz.getPackage().getName() + ".Q" + clazz.getSimpleName());
			try {
				ePath = (EntityPath<?>) qclass.getDeclaredField(path + "1").get(null);
			} catch (NoSuchFieldException ex) { 
				try {
					ePath = (EntityPath<?>) qclass.getDeclaredField(path).get(null);
				} catch (NoSuchFieldException ex2) {
					ePath = new EntityPathBase<C>(clazz, path);					
				}
			}
		} catch (IllegalAccessException e) {
			Logger.getAnonymousLogger().log(Level.INFO, e.getMessage(), e);			
		} catch (ClassNotFoundException e) {
			Logger.getAnonymousLogger().log(Level.INFO, e.getMessage(), e);
		}
		final JPAQuery<C> qry = this.<C>query().from(ePath);
		try {
			clazz.getMethod("isDaXoa");
			qry.where(Expressions.booleanPath(ePath, "daXoa").isFalse());
		} catch (NoSuchMethodException e) {
		}	
		return qry;
	}
    
    @SuppressWarnings("unchecked")
	public Class<T> type() {
		final Class<?>[] arguments = GenericTypeResolver.resolveTypeArguments(getClass(), CoreObject.class);
		return (Class<T>) (arguments == null || arguments.length == 0 ? getClass() : arguments[0]);
	}

	public <C> JPAQuery<C> where(final JPAQuery<C> qry, final String field, final Object value) {
		return qry
				.where(value == null ? Expressions.FALSE
						: Expressions
								.path(Object.class, (Path<?>) qry.getMetadata().getJoins().get(0).getTarget(), field)
								.eq(value));
	}
    
	public <C> JPAQuery<C> sort(final JPAQuery<C> qry, final String field, final boolean asc) {
		final ComparablePath<?> com = Expressions.comparablePath(Comparable.class,
				(Path<?>) qry.getMetadata().getJoins().get(0).getTarget(), field);
		return qry.orderBy(asc ? com.asc() : com.desc());
	}
	
	public void setCloseConfirm(boolean value) {
		if (value) {
			Clients.evalJavaScript(
					"window.onbeforeunload = function (evt) {" + "var message = 'Are you sure you want to leave?';"
							+ "console.log(evt);" + "if (typeof evt == 'undefined') {" + "evt = window.event;" + "}"
							+ "if (evt) {" + "evt.returnValue = message;" + "}" + "return message;" + "}");
		} else {
			Clients.evalJavaScript("window.onbeforeunload = function (evt) {}");
		}
	}
	
	@SuppressWarnings("unchecked")
	public <B, A extends SimpleQuery<?> & Fetchable<B>> A page(final A qry) {
		final int len = MapUtils.getIntValue(getArg(), SystemPropertyUtils.resolvePlaceholders(PH_KEYPAGESIZE));
		String kPage = SystemPropertyUtils.resolvePlaceholders(PH_KEYPAGE);
		int page = MapUtils.getIntValue(arg, kPage);
		long count;
		if (qry instanceof JPAQuery && !((JPAQuery<?>) qry).getMetadata().getGroupBy().isEmpty()) {
			final JPAQuery<?> jpa = (JPAQuery<?>) qry;
			Expression<?> pro = jpa.getMetadata().getProjection();
			if (pro == null) {
				pro = jpa.getMetadata().getJoins().get(0).getTarget();
			}
			count = jpa.select(Expressions.ZERO).fetch().size();
			jpa.select(pro);
		} else {
			count = qry.fetchCount();
		}
		if (count <= page * len) {
			arg.put(kPage, page = 0);
			BindUtils.postNotifyChange(null, null, arg, kPage);
		}
		return (A) qry.offset(page * len).limit(len);
	}
	
	public int activePage() {
		return MapUtils.getIntValue(getArg(), SystemPropertyUtils.resolvePlaceholders(PH_KEYPAGE), 1) - 1;
	}
	
	public <C> List<C> pageList(List<C> l) {
		String kPage = SystemPropertyUtils.resolvePlaceholders(PH_KEYPAGE);
		int page = MapUtils.getIntValue(getArg(), kPage, 0);
		int len = MapUtils.getIntValue(getArg(), SystemPropertyUtils.resolvePlaceholders(PH_KEYPAGESIZE), defaultPageSize());
		if (l.size() <= page * len) {
			getArg().put(kPage, page = 0);
			BindUtils.postNotifyChange(null, null, getArg(), kPage);
		}
		return l.subList(page * len, Math.min(page * len + len, l.size()));
	}
	
	@Command
	public void invokeGG(
			@BindingParam("notify") Object vmArgs,
			@BindingParam("attr") String attrs,
			@BindingParam("detach") final Window wdn){
		for (final String field : attrs.split("\\|")) {
			if (!field.isEmpty()) {
				BindUtils.postNotifyChange(null, null, vmArgs, field);
			}
		}		
		if (wdn != null) {
			wdn.detach();
		}
	}
	
	@Command
	public void invoke(@BindingParam("object") final Object obj,
			@BindingParam("method") @Default(value = "") final String ten,
			@BindingParam("arg") final Collection<?> arg1, @BindingParam("notify") final Object beanObject,
			@BindingParam("attr") @Default(value = "*") final String attrs,
			@BindingParam("detach") final Component compDetach, @BindingParam("noSelf") final boolean noSelf) {
		if (!Strings.isNullOrEmpty(ten)) {
			try {
				Object object = obj == null ? this : obj;
				Object[] arguments = arg1 == null ? new Object[0] : arg1.toArray();
				Class<?>[] parameterTypes = new Class[arguments.length];
	            for (int i = 0; i < arguments.length; i++) {
	                parameterTypes[i] = arguments[i].getClass();
	            }
	            object.getClass().getMethod(ten, parameterTypes).invoke(object, arguments);
			} catch (final IllegalAccessException e) {
				throw new IllegalArgumentException(e);
			} catch (NoSuchMethodException e) {
				throw new IllegalArgumentException(e);
			} catch (InvocationTargetException e) {
				throw new IllegalArgumentException(e);
			}
		}
		for (final String field : attrs.split("\\|")) {
			if (!field.isEmpty()) {
				BindUtils.postNotifyChange(null, null, beanObject == null ? this : beanObject, field);
			}
		}
		if (!noSelf && beanObject != null && beanObject != this) {
			BindUtils.postNotifyChange(null, null, this, "*");
		}
		if (compDetach != null) {
			compDetach.detach();
		}
	}
	
	@Override
	public Object getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean inUse() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean noId() {
		// TODO Auto-generated method stub
		return getId() == null || getId().equals(0l);
	}
	
	@Override
	public void setTrangThai(String deleted) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setDaXoa(boolean deleted) {
		// TODO Auto-generated method stub
	}
	
    // SERVICES
    public DanhMucService getDanhMucService() {
    	return new DanhMucService();
    }
    
    public LoaiMauService getLoaiMauService() {
    	return new LoaiMauService();
    }
    
    public NhomMauService getNhomMauService() {
    	return new NhomMauService();
    }
    
    public ThanhPhoService getThanhPhoService() {
    	return new ThanhPhoService();
    }
    public QuanHuyenService getQuanHuyenService() {
    	return new QuanHuyenService();
    }
    public ChuongTrinhHienMauService getChuongTrinhHienMauService() {
    	return new ChuongTrinhHienMauService();
    }
    public ChiTietChuongTrinhService getChiTietChuongTrinhService() {
    	return new ChiTietChuongTrinhService();
    }
    public NhanVienService getNhanVienService(){
    	return new NhanVienService();
    }
    public VaiTroService getVaiTroService(){
    	return new VaiTroService();
    }
    public BaiDangService getBaiDangService(){
    	return new BaiDangService();
    }
    public BinhLuanService getBinhLuanService(){
    	return new BinhLuanService();
    }
    public DangKyService getDangKyService(){
    	return new DangKyService();
    }
    public DanhMucTinTucService getDanhMucTinTucService(){
    	return new DanhMucTinTucService();
    }
    public TinTucService getTinTucService(){
    	return new TinTucService();
    }
    public KhoMauService getKhoMauService(){
    	return new KhoMauService();
    }
    public PhanHoiService getPhanHoiService(){
    	return new PhanHoiService();
    }
    public DeNghiService getDeNghiService(){
    	return new DeNghiService();
    }
}
