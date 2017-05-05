package vn.cal.enums;

public enum LoaiThongBao {
	TBDENGHI("đề nghị"), TBDANGBAI("bài đăng");
	
	private String val;
	
	LoaiThongBao(String val){
		this.val = val;
	}
	
	public String val(){
		return val;
	}
}
