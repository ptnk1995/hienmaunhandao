
package vn.cal.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Window;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.core.Model;

@Entity
@Table(name = "vaitro", uniqueConstraints = @UniqueConstraint(columnNames = {"tenVaiTro"}),
        indexes = {@Index(columnList = "alias"), @Index(columnList = "tenVaiTro")})
public class VaiTro extends Model<VaiTro> {

    public static transient final Logger log = Logger.getLogger(VaiTro.class.getName());
    
    public static final String QUANTRIVIEN = "quantrivien";
    public static final String CANHAN = "canhan";
    public static final String COQUAN = "coquan";
    
    public static final String[] VAITRO_DEFAULTS = { QUANTRIVIEN, CANHAN, COQUAN };
    
    private String tenVaiTro = "";
    private String alias = "";
    private Set<String> quyens = new HashSet<>();
    private Set<String> quyenEdits = quyens;
    
    public VaiTro(){
        super();
    }
    
    public VaiTro(String ten, String quyen){
        super();
        tenVaiTro = ten;
        setAlias(quyen.trim());
    }
    
    public String getTenVaiTro() {
        return tenVaiTro;
    }
    
    public void setTenVaiTro(String tenVaiTro) {
        this.tenVaiTro = tenVaiTro;
    }
    
    public String getAlias() {
        return alias;
    }
    
    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    @Transient
	public Set<String> getQuyenMacDinhs(String alias1) {
		Set<String> quyens1 = new HashSet<>();
		if (!alias1.isEmpty()) {
			if (QUANTRIVIEN.equals(alias1)) {
				
				quyens1.add(getCore().VAITROTHEM);
				quyens1.add(getCore().VAITROLIST);
				quyens1.add(getCore().VAITROSUA);
				quyens1.add(getCore().VAITROXOA);
				quyens1.add(getCore().VAITROXEM);
				
				quyens1.add(getCore().NGUOIDUNGTHEM);
				quyens1.add(getCore().NGUOIDUNGLIST);
				quyens1.add(getCore().NGUOIDUNGSUA);
				quyens1.add(getCore().NGUOIDUNGXOA);
				quyens1.add(getCore().NGUOIDUNGXEM);
				
				quyens1.add(getCore().CHUONGTRINHHIENMAUTHEM);
				quyens1.add(getCore().CHUONGTRINHHIENMAULIST);
				quyens1.add(getCore().CHUONGTRINHHIENMAUSUA);
				quyens1.add(getCore().CHUONGTRINHHIENMAUXOA);
				quyens1.add(getCore().CHUONGTRINHHIENMAUXEM);
				
				quyens1.add(getCore().NHOMMAUTHEM);
				quyens1.add(getCore().NHOMMAULIST);
				quyens1.add(getCore().NHOMMAUSUA);
				quyens1.add(getCore().NHOMMAUXOA);
				quyens1.add(getCore().NHOMMAUXEM);
				
				quyens1.add(getCore().LOAIMAUTHEM);
				quyens1.add(getCore().LOAIMAULIST);
				quyens1.add(getCore().LOAIMAUSUA);
				quyens1.add(getCore().LOAIMAUXOA);
				quyens1.add(getCore().LOAIMAUXEM);
				
				quyens1.add(getCore().THANHPHOTHEM);
				quyens1.add(getCore().THANHPHOLIST);
				quyens1.add(getCore().THANHPHOSUA);
				quyens1.add(getCore().THANHPHOXOA);
				quyens1.add(getCore().THANHPHOXEM);
				
				quyens1.add(getCore().QUANHUYENTHEM);
				quyens1.add(getCore().QUANHUYENLIST);
				quyens1.add(getCore().QUANHUYENSUA);
				quyens1.add(getCore().QUANHUYENXOA);
				quyens1.add(getCore().QUANHUYENXEM);
			}
			else if(COQUAN.equals(alias1)){
				quyens1.add(getCore().CHUONGTRINHHIENMAUTHEM);
				quyens1.add(getCore().CHUONGTRINHHIENMAULIST);
				quyens1.add(getCore().CHUONGTRINHHIENMAUSUA);
				quyens1.add(getCore().CHUONGTRINHHIENMAUXOA);
				quyens1.add(getCore().CHUONGTRINHHIENMAUXEM);
			}
			else if(CANHAN.equals(alias1)){
				quyens1.add(getCore().NGUOIDUNGLIST);
				quyens1.add(getCore().NGUOIDUNGSUA);
				quyens1.add(getCore().NGUOIDUNGXEM);
			}
		}
		return quyens1;
	}
    
    @Override
	public void save() {
		setTenVaiTro(getTenVaiTro().trim().replaceAll("\\s+", " "));
		setQuyens(quyenEdits);
		if (noId()) {
			showNotification("Đã lưu thành công!", "", "success");
		} else {
			showNotification("Đã cập nhật thành công!", "", "success");
		}
		doSave();
	}
    
    @Transient
	public Set<String> getQuyenAllMacDinhs() {
		Set<String> quyens1 = new HashSet<>();
		
		quyens1.add(getCore().VAITROTHEM);
		quyens1.add(getCore().VAITROLIST);
		quyens1.add(getCore().VAITROSUA);
		quyens1.add(getCore().VAITROXOA);
		quyens1.add(getCore().VAITROXEM);
		
		quyens1.add(getCore().NGUOIDUNGTHEM);
		quyens1.add(getCore().NGUOIDUNGLIST);
		quyens1.add(getCore().NGUOIDUNGSUA);
		quyens1.add(getCore().NGUOIDUNGXOA);
		quyens1.add(getCore().NGUOIDUNGXEM);
		
		quyens1.add(getCore().CHUONGTRINHHIENMAUTHEM);
		quyens1.add(getCore().CHUONGTRINHHIENMAULIST);
		quyens1.add(getCore().CHUONGTRINHHIENMAUSUA);
		quyens1.add(getCore().CHUONGTRINHHIENMAUXOA);
		quyens1.add(getCore().CHUONGTRINHHIENMAUXEM);
		
		quyens1.add(getCore().NHOMMAUTHEM);
		quyens1.add(getCore().NHOMMAULIST);
		quyens1.add(getCore().NHOMMAUSUA);
		quyens1.add(getCore().NHOMMAUXOA);
		quyens1.add(getCore().NHOMMAUXEM);
		
		quyens1.add(getCore().LOAIMAUTHEM);
		quyens1.add(getCore().LOAIMAULIST);
		quyens1.add(getCore().LOAIMAUSUA);
		quyens1.add(getCore().LOAIMAUXOA);
		quyens1.add(getCore().LOAIMAUXEM);
		
		quyens1.add(getCore().THANHPHOTHEM);
		quyens1.add(getCore().THANHPHOLIST);
		quyens1.add(getCore().THANHPHOSUA);
		quyens1.add(getCore().THANHPHOXOA);
		quyens1.add(getCore().THANHPHOXEM);
		
		quyens1.add(getCore().QUANHUYENTHEM);
		quyens1.add(getCore().QUANHUYENLIST);
		quyens1.add(getCore().QUANHUYENSUA);
		quyens1.add(getCore().QUANHUYENXOA);
		quyens1.add(getCore().QUANHUYENXEM);
		
		return quyens1;
    }
    
    @Transient
	public Set<String> getQuyenMacDinhs() {
		return getQuyenMacDinhs(getAlias());
	}
    
    @ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "vaitro_quyens")
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	public Set<String> getQuyens() {
		if (quyens.isEmpty()) {
			quyens.addAll(getQuyenMacDinhs());
		}
		return quyens;
	}

	public void setQuyens(final Set<String> dsChoPhep) {
		quyens = dsChoPhep;
	}

	@Override
	public String toString() {
		return super.toString() + " " + tenVaiTro;
	}

	@Transient
	public Set<TreeNode<String[]>> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(Set<TreeNode<String[]>> selectedItems_) {
		Set<TreeNode<String[]>> selectedItemsTmp = new HashSet<>();
		selectedItemsTmp.addAll(selectedItems);
		for (final TreeNode<String[]> node : selectedItems) {
			if (!selectedItems_.contains(node)) {
				quyenEdits.remove(node.getData()[1]);
				selectedItemsTmp.remove(node);
				
				// remove parent
				TreeNode<String[]> pNode = node.getParent();
				if(pNode != null && selectedItems_.contains(pNode)){
					quyenEdits.remove(pNode.getData()[1]);
					selectedItemsTmp.remove(pNode);
				}
				//remove all child
				if (node.getChildCount() > 0) {
					for (TreeNode<String[]> n : node.getChildren()) {
						quyenEdits.remove(n.getData()[1]);
						selectedItemsTmp.remove(n);
					}
				}
			}
		}
		for (final TreeNode<String[]> node : selectedItems_) {
			if (!selectedItems.contains(node)) {
				quyenEdits.add(node.getData()[1]);
				selectedItemsTmp.add(node);
				if (node.getChildCount() > 0) {
					for (TreeNode<String[]> n : node.getChildren()) {
						quyenEdits.add(n.getData()[1]);
						selectedItemsTmp.add(n);
					}
				}
			}
		}
		selectedItems = selectedItemsTmp;
		BindUtils.postNotifyChange(null, null, this, "quyenEdits");
		BindUtils.postNotifyChange(null, null, this, "selectedItems");
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

	public void setCheckApDung(boolean _isCheckApDung) {
		if (_isCheckApDung) {
			setTrangThai(getCore().TT_AP_DUNG);
		} else {
			setTrangThai(getCore().TT_KHONG_AP_DUNG);
		}
		this.checkApDung = _isCheckApDung;
	}
	
	@Transient
	public List<NhanVien> getListNhanVien() {
		JPAQuery<NhanVien> q = find(NhanVien.class).where(QNhanVien.nhanVien.trangThai.ne(getCore().TT_DA_XOA))
				.where(QNhanVien.nhanVien.vaiTros.contains(this));
		return q.fetch();
	}
    
    private Set<TreeNode<String[]>> selectedItems = new HashSet<>();
    
    @Transient
	@NotifyChange({ "selectedItems", "model", "*" })
	public DefaultTreeModel<String[]> getModel() {
		getQuyens();
		selectedItems.clear();
		
		HashSet<TreeNode<String[]>> openItems_ = new HashSet<>();
		
		TreeNode<String[]> rootNode = new DefaultTreeNode<>(new String[] {}, new ArrayList<DefaultTreeNode<String[]>>());
		
		DefaultTreeModel<String[]> model = new DefaultTreeModel<>(rootNode, true);
		
		model.setMultiple(true);
		
		Set<String> allQuyens = new HashSet<>();
		
		long q = find(VaiTro.class).fetchCount();
		
		if(q==0){
			for (String vaiTro : VAITRO_DEFAULTS) {
				allQuyens.addAll(getQuyenMacDinhs(vaiTro));
			}
		} else {
			allQuyens.addAll(getQuyenAllMacDinhs());
		}
				
		for (String resource : getCore().getRESOURCES()) {
			DefaultTreeNode<String[]> parentNode = new DefaultTreeNode<>(
					new String[] {
							Labels.getLabel("url." + resource + ".mota"),
							resource },
							new ArrayList<DefaultTreeNode<String[]>>());
			if (quyens.contains(resource)) {
				selectedItems.add(parentNode);
				openItems_.add(parentNode);
				model.setOpenObjects(openItems_);
			}
			for (String action : getCore().getACTIONS()) {
				String quyen = resource + Quyen.CACH + action;
				if (allQuyens.contains(quyen)) {
					DefaultTreeNode<String[]> childNode = new DefaultTreeNode<>(
							new String[] {
									Labels.getLabel("action." + action
											+ ".mota"), quyen },
											new ArrayList<DefaultTreeNode<String[]>>());
					if (quyens.contains(quyen)) {
						selectedItems.add(childNode);
						openItems_.add(childNode);
						openItems_.add(parentNode);
					}
					parentNode.add(childNode);
				}
			}
			rootNode.add(parentNode);
		}
		
		quyenEdits = new HashSet<>(quyens);
		model.setOpenObjects(openItems_);
		return model;
	}
	
	@Command
	public void saveVaiTro(@BindingParam("list") final Object listObject,
			@BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn) {
		setTenVaiTro(getTenVaiTro().trim().replaceAll("\\s+", " "));
		setQuyens(quyenEdits);
		saveAsShowNotification();
		wdn.detach();
		BindUtils.postNotifyChange(null, null, listObject, "targetQuery");
	}
	
}
