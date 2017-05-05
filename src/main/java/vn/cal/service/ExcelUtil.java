package vn.cal.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.zul.Filedownload;

import vn.cal.model.NhanVien;


public class ExcelUtil {
	
	private static ExcelUtil instance;

	public static ExcelUtil getInStance() {
		if (instance == null) {
			instance = new ExcelUtil();
		}
		return instance;
	}

	public static void exportThongKe(String title, String fileName, String sheetName,
			List<NhanVien> listResult) throws IOException {
		// New Workbook
		Workbook wb = new XSSFWorkbook();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Cell c = null;
			// New Sheet
			Sheet sheet1 = null;
			sheet1 = wb.createSheet(sheetName);
			sheet1.getPrintSetup().setLandscape(true);
			sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
			// Row and column indexes
			int idx = 0;
			// Generate column headings
			Row row;
			int countTitle = 0;
			CellStyle cellStyleDataRight = setBorderAndFont(wb, 12, false, 11, Font.BOLDWEIGHT_NORMAL, CellStyle.ALIGN_RIGHT);
			CellStyle cellStyleDataLeft = setBorderAndFont(wb, 12, false, 11, Font.BOLDWEIGHT_NORMAL, CellStyle.ALIGN_LEFT);
			CellStyle cellStyleDataCenter = setBorderAndFont(wb, 12, false, 11, Font.BOLDWEIGHT_NORMAL, CellStyle.ALIGN_CENTER);
			row = sheet1.createRow(idx);
			c = row.createCell(0);
			c.setCellValue(title);
			c.setCellStyle(setBorderAndFont(wb, 12, false, 11, Font.BOLDWEIGHT_BOLD, CellStyle.ALIGN_CENTER));
			sheet1.addMergedRegion(new CellRangeAddress(countTitle, countTitle + 1, 0, 5));
			for (idx = 1; idx <= 1; idx++) {
				row = sheet1.createRow(idx);
				c = row.createCell(1);
			}
			idx++;
			row = sheet1.createRow(idx);

			// set column width
			sheet1.setColumnWidth(0, 16 * 256);
			// Generate rows header of grid
			row = sheet1.createRow(idx);
			idx++;

			c = row.createCell(0);
			c.setCellValue("STT");
			c.setCellStyle(setBorderAndFont(wb, 12, false, 11, Font.BOLDWEIGHT_BOLD, CellStyle.ALIGN_CENTER));
			sheet1.setColumnWidth(0, 6 * 256);
			c = row.createCell(1);
			c.setCellValue("Họ và Tên");
			c.setCellStyle(setBorderAndFont(wb, 12, false, 11, Font.BOLDWEIGHT_BOLD, CellStyle.ALIGN_CENTER));
			sheet1.setColumnWidth(1, 20 * 256);
			c = row.createCell(2);
			c.setCellValue("Email");
			c.setCellStyle(setBorderAndFont(wb, 12, false, 11, Font.BOLDWEIGHT_BOLD, CellStyle.ALIGN_CENTER));
			sheet1.setColumnWidth(2, 30 * 256);
			c = row.createCell(3);
			c.setCellValue("Nhóm máu");
			c.setCellStyle(setBorderAndFont(wb, 12, false, 11, Font.BOLDWEIGHT_BOLD, CellStyle.ALIGN_CENTER));
			sheet1.setColumnWidth(3, 15 * 256);
			c = row.createCell(4);
			c.setCellValue("Ngày sinh");
			c.setCellStyle(setBorderAndFont(wb, 12, false, 11, Font.BOLDWEIGHT_BOLD, CellStyle.ALIGN_CENTER));
			sheet1.setColumnWidth(4, 20 * 256);
			c = row.createCell(5);
			c.setCellValue("Số điện thoại");
			c.setCellStyle(setBorderAndFont(wb, 12, false, 11, Font.BOLDWEIGHT_BOLD, CellStyle.ALIGN_CENTER));
			sheet1.setColumnWidth(5, 20 * 256);

			int i = 1;
			for (NhanVien map : listResult) {
				row = sheet1.createRow(idx);
				c = row.createCell(0);
				c.setCellValue(i);
				c.setCellStyle(cellStyleDataCenter);

				c = row.createCell(1);
				c.setCellValue(map.getHoVaTen());
				c.setCellStyle(cellStyleDataLeft);

				c = row.createCell(2);
				c.setCellValue(map.getEmail());
				c.setCellStyle(cellStyleDataLeft);

				c = row.createCell(3);
				if(map.getNhomMau() != null)
					c.setCellValue(map.getNhomMau().getTenNhom());
				else c.setCellValue("");
				c.setCellStyle(cellStyleDataCenter);

				c = row.createCell(4);
				if(map.getNgaySinh() != null)
					c.setCellValue(sdf.format(map.getNgaySinh()));
				else c.setCellValue("");
				c.setCellStyle(cellStyleDataCenter);

				c = row.createCell(5);
				c.setCellValue(map.getSoDienThoai());
				c.setCellStyle(cellStyleDataLeft);
				i++;
				idx++;
			}

			idx++;
			//createNoteRow(wb, sheet1, idx);
			idx++;

			ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
			wb.write(fileOut);
			Filedownload.save(new ByteArrayInputStream(fileOut.toByteArray()), "application/octet-stream",
					fileName + ".xlsx");
		} finally {
			//wb.close();
		}
	}

	private static CellStyle setBorderAndFont(Workbook wb, int i, boolean b, int j, Short weight, Short align) {
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		style.setAlignment(align);
		font.setBoldweight(weight);
		style.setFont(font);
		return style;
	}
	
}