package vn.cal.model;

import vn.cal.core.Asset;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.Window;

@Entity
@Table(name = "danhmuc")
public class DanhMuc extends Asset<DanhMuc> {
	public static transient final Logger LOG = LogManager.getLogger(DanhMuc.class.getName());

	private String name = "";
	private String description = "";

	@Column(length = 255)
	public String getName() {
		return name;
	} 

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Command
	public void saveDanhMuc(@BindingParam("list") final Object listObject, @BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) {
		LOG.info("save danh muc");
		saveAsShowNotification();
		wdn.detach();
		BindUtils.postNotifyChange(null, null, listObject, attr);
	}
}
