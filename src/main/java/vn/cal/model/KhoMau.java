package vn.cal.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import vn.cal.core.Asset;

@Entity
@Table(name="kho_mau")
public class KhoMau extends Asset<KhoMau>{

	private NhomMau nhomMau;
	private float theTich;
	
	@OneToOne(fetch = FetchType.LAZY)
	public NhomMau getNhomMau() {
		return nhomMau;
	}
	public void setNhomMau(NhomMau nhomMau) {
		this.nhomMau = nhomMau;
	}
	public float getTheTich() {
		return theTich;
	}
	public void setTheTich(float theTich) {
		this.theTich = theTich;
	}
	
}
