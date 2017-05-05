package vn.cal.core;

import java.util.Date;
import java.util.logging.Logger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Default;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;

import com.querydsl.core.annotations.QueryInit;

import vn.cal.model.NhanVien;


@MappedSuperclass
public class Model<T extends Model<T>> extends BaseObject<T> {
    
    private transient long instanceTime;
    private Long id;    
    private Date ngayTao;   
    private Date ngaySua;
	private boolean publishStatus = true;
    private NhanVien nguoiSua;
	private NhanVien nguoiTao;
    private boolean daXoa;
    private String trangThai = "ap_dung";

	@Id
    @GeneratedValue
    public Long getId() {
        if(id==null){
            id = Long.valueOf(0);
        }
        return id;
    }
    
    public void setId(Long id) {
        if(id!=null && id.longValue()==0l){
            id = null;
        }
        this.id = id;
    }
    
    @SuppressWarnings("deprecation")
	@org.hibernate.annotations.Index(name = "ngayTao")
    public Date getNgayTao() {
        return ngayTao;
    }
    
    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }
    
private boolean checkApDung;
	
	@Transient
	public boolean isCheckApDung() {
		checkApDung = false;
		if (getCore().TT_AP_DUNG.equals(getTrangThai())) {
			checkApDung = true;
		}
		return checkApDung;
	}

	public void setCheckApDung(final boolean _isCheckApDung) {
		if (_isCheckApDung) {
			setTrangThai(getCore().TT_AP_DUNG);
		} else {
			setTrangThai(getCore().TT_KHONG_AP_DUNG);
		}
		this.checkApDung = _isCheckApDung;
	}
    
    @SuppressWarnings("deprecation")
	@org.hibernate.annotations.Index(name = "ngaySua")
    public Date getNgaySua() {
        return ngaySua;
    }
    
    public void setNgaySua(Date ngaySua) {
        this.ngaySua = ngaySua;
    }
    
    @QueryInit("*.*.*.*")
	@ManyToOne
	public NhanVien getNguoiSua() {
		return nguoiSua;
	}

	public void setNguoiSua(NhanVien nguoiSua) {
		this.nguoiSua = nguoiSua;
	}

	@QueryInit("*.*.*.*")
	@ManyToOne
	public NhanVien getNguoiTao() {
		return nguoiTao;
	}

	public void setNguoiTao(NhanVien nguoiTao) {
		this.nguoiTao = nguoiTao;
	}
    
    @SuppressWarnings("deprecation")
	@org.hibernate.annotations.Index(name = "daXoa")
    public boolean isDaXoa() {
        return daXoa;
    }
    
    public void setDaXoa(boolean daXoa) {
        this.daXoa = daXoa;
        if(daXoa){
            setTrangThai(TT_DA_XOA);
        }
    }
    
    public String getTrangThai() {
        return trangThai;
    }
    
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
    
    public boolean isPublishStatus() {
		return publishStatus;
	}
    
    public void setPublishStatus(boolean publishStatus) {
		this.publishStatus = publishStatus;
	}
    
    @Transient
	public String getTrangThaiText() {
		return getCore().getListTrangThai().get(getTrangThai());
	}
	
	@Transient
	public String getPublishStatusText() {
		String status = "Không áp dụng";
		if(isPublishStatus()) {
			status = "Áp dụng";
		}
		return status;
	}
	
	@Transient
	public String getTrangThaiSoanText() {
		return getCore().getTrangThaiSoanList().get(getTrangThaiSoan());
	}
	
	@Transient
	public String getTrangThaiSoan() {
		return trangThai;
	}
	
    public boolean noId(){
        return getId()==null || getId().equals(0l);
    }
    
    @PostLoad
    private void loaded() {
        if (instanceTime == 0) {
            instanceTime = System.currentTimeMillis();
        }
    }
    
    @Transient
    public boolean isLoaded() {
        return instanceTime != 0;
    }
    
    @Transient
    public long getInstanceTime() {
        return instanceTime;
    }   
    
    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj==null || obj.getClass()!=this.getClass()){
            return false;
        }
        Model<?> other = (Model<?>) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        return new HashCodeBuilder((int) (getId()%2==0?getId()+1:getId()), prime).toHashCode();
    }
    
    @Override
    public String toString() {
        return super.toString() + " : "+ getId();
    }
   
    public void save(){
        setNgaySua(new Date());
        setNguoiSua(fetchNhanVien(true));
        if(noId()){
            setNgayTao(getNgaySua());
            setNguoiTao(getNguoiSua());
        }
        loaded();
        saveModel(this);
    }
    
	public void saveAsShowNotification() {
		transactioner().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {
				Logger.getGlobal().info(getId() + "");
				boolean noid = noId();
				doSave();
				if (noid) {
					showNotification("Đã lưu thành công!", "", "success");
				} else {
					showNotification("Đã cập nhật thành công!", "", "success");
				}
				Logger.getGlobal().info(getId() + "");
			}
		});
	}

	public void saveNotShowNotification() {
		doSave();
	}
    
    public void saveModel(final Model<T> obj) {
        if(obj.getClass().isAnnotationPresent(Entity.class)){
            transactionTemplate().execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    if(obj.noId()){
                        em().persist(obj);
                    } else {
                        em().merge(obj);
                    }
                }
            });
        }
    }
    
    @Command
	public void deleteTrangThaiConfirmAndNotify(
			final @BindingParam("notify") Object beanObject,
			final @BindingParam("attr") @Default(value = "*") String attr) {
		if (!checkInUse()) {
			Messagebox.show("Bạn muốn xóa mục này?", "Xác nhận",
				Messagebox.CANCEL | Messagebox.OK, Messagebox.QUESTION, new EventListener<Event>() {
				
				@Override
				public void onEvent(final Event event) {
					if (Messagebox.ON_OK.equals(event.getName())) {
						doDelete(true);
						showNotification("Đã xóa thành công!", "", "success");
						if (beanObject != null) {
							BindUtils.postNotifyChange(null, null, beanObject, attr);
							if (beanObject != this) {
								BindUtils.postNotifyChange(null, null, this, "*");
							}
						}
					}
				}
			});
		}
	}
}
