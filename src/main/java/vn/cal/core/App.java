package vn.cal.core;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Configuration
@Controller
public class App extends CoreObject<Object> {

    public static App instance;
    
    public App(){
        super();
        setEnv();
        instance = this;
    }
    
    @RequestMapping(value = "/dangnhap")
    public String dangnhap() {	
        return "forward:/WEB-INF/zul/login.zul";
    }
    
    @RequestMapping(value = "/c/{path:.+$}")
    public String signup(@PathVariable String path) {
    	//System.out.println("path "+path);
        return "forward:/frontend/index_2.zul?resource=" + path+"&file=/frontend/"+path+".zul" ;
    }
    
    @RequestMapping(value = "/c/{path:.+$}?{id:\\d+}")
    public String signup(@PathVariable String path, @PathVariable String id) {
    	//System.out.println("path "+path);
        return "forward:/frontend/index_2.zul?resource=" + path+"&file=/frontend/"+path+".zul?id=" +id ;
    }
    
    @RequestMapping(value = "/{path:.+$}")
    public String index(@PathVariable String path) {
        System.out.println("path - gg :"+path);
        return "forward:/WEB-INF/zul/home1.zul?resource=" + path + "&action=lietke&file=/WEB-INF/zul/" + path + "/list.zul";
    }
    
    @RequestMapping(value = "/cp/{path:.+$}")
	public String cp(@PathVariable String path) {
    	System.out.println("cp");
		return "forward:/WEB-INF/zul/home1.zul?resource=" + path + "&action=lietke&file=/WEB-INF/zul/" + path
				+ "/list.zul";
	}
    
    @RequestMapping(value = "/{path:.+$}/them")
	public String actionThem(@PathVariable String path) {
		return "forward:/WEB-INF/zul/home1.zul?resource=" + path + "&action=them&file=/WEB-INF/zul/nguoidung/them.zul";
	}
    
    @RequestMapping(value = "/{path:.+$}/xem/{id:.+$}")
	public String actionXem(@PathVariable String path, @PathVariable Long id) {
		return "forward:/WEB-INF/zul/home1.zul?resource=" + path + "&action=xem&file=/WEB-INF/zul/nguoidung/xem.zul&id=" + id;
	}
	
	@RequestMapping(value = "/{path:.+$}/sua/{id:.+$}")
	public String actionSua(@PathVariable String path, @PathVariable Long id) {
		return "forward:/WEB-INF/zul/home1.zul?resource=" + path + "&action=sua&file=/WEB-INF/zul/nguoidung/sua.zul&id=" + id;
	}
	
	@RequestMapping(value = "/thongke/{path:.+$}")
	public String thongke(@PathVariable String path) {
		return "forward:/WEB-INF/zul/home1.zul?resource=thongke&action=" + path + "&file=/WEB-INF/zul/thongke/" + path + ".zul";
	}
	@RequestMapping(value = "/baidang/{path:.+$}")
	public String baidang(@PathVariable String path) {
		return "forward:/WEB-INF/zul/home1.zul?resource=baidang&action=" + path + "&file=/WEB-INF/zul/baidang/" + path + ".zul";
	}
}
