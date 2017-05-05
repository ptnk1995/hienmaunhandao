package vn.cal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.model.QVaiTro;
import vn.cal.model.VaiTro;

public final class VaiTroService extends BasicService<VaiTro>{
	
	private Map<String, String> listDefaultAlias = new HashMap<>();

	public Map<String, String> getListDefaultAlias() {
		if (listDefaultAlias.isEmpty()) {
			listDefaultAlias.put(VaiTro.CANHAN, "Cá nhân");
			listDefaultAlias.put(VaiTro.COQUAN, "Cơ quan, Tổ chức");
			listDefaultAlias.put(VaiTro.QUANTRIVIEN, "Quản trị viên");
		}
		return listDefaultAlias;
	}
	public void bootstrap() {
		if (find(VaiTro.class).fetchCount() == 0) {
			for (String vai : VaiTro.VAITRO_DEFAULTS) {
				VaiTro vaiTro = new VaiTro(Labels.getLabel("vaitro." + vai), vai);
				vaiTro.save();
			}
		}
	}
	public JPAQuery<VaiTro> getTargetQuery() {
		bootstrap();
		String param = MapUtils.getString(argDeco(), "tukhoa", "").trim();
		String trangThai = MapUtils.getString(argDeco(), "trangthai", "");
		JPAQuery<VaiTro> q = find(VaiTro.class).where(QVaiTro.vaiTro.trangThai.ne(getCore().TT_DA_XOA));
		if (param != null && !param.isEmpty()) {
			String tukhoa = "%" + param + "%";
			q.where(QVaiTro.vaiTro.tenVaiTro.like(tukhoa));
		}
		if (!trangThai.isEmpty()) {
			q.where(QVaiTro.vaiTro.trangThai.eq(trangThai));
		}
		q.orderBy(QVaiTro.vaiTro.ngaySua.desc());
		return q;
	}
	public QVaiTro getEntityPath() {
		return QVaiTro.vaiTro;

	}
	public VaiTro findOrNewByAlias(String alias) {
		VaiTro find = find(VaiTro.class).where(QVaiTro.vaiTro.alias.eq(alias)).fetchFirst();
		return find == null ? new VaiTro() : find;
	}
}
