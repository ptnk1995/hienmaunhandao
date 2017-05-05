package vn.cal.service;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;

import vn.cal.model.NhanVien;

public class HomeService extends BasicService<Object>{
	
	private String trangThaiDangKy;
	private String trangThaiDeNghi;
	
    public String getTrangThaiDangKy() {
		return trangThaiDangKy;
	}

	public void setTrangThaiDangKy(String trangThaiDangKy) {
		this.trangThaiDangKy = trangThaiDangKy;
		System.out.println("trangThaiDangKy"+trangThaiDangKy);
	}

	public String getTrangThaiDeNghi() {
		return trangThaiDeNghi;
	}

	public void setTrangThaiDeNghi(String trangThaiDeNghi) {
		this.trangThaiDeNghi = trangThaiDeNghi;
		System.out.println("trangThaiDeNghi"+trangThaiDeNghi);
	}

	@Command
    public void saveRegister(@BindingParam("vm") final Object vm, @BindingParam("mk2") final String matKhau2){
    	System.out.println("@@@");
    	NhanVien nv = (NhanVien)vm;
    	if (matKhau2 != null && !matKhau2.isEmpty()) {
    		System.out.println("updatePassword");
			nv.updatePassword(matKhau2);
		}
    	nv.save();
    	Executions.sendRedirect("/");
    	showNotification("Đăng ký thành công!", "", "success");
    }
    
    @Command
    public void load(){
    	System.out.println("FFF");
    }

}
