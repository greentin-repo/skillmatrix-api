package com.greentin.enovation.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ExcelUtil {

	public static boolean IsExcelFormatValid(Sheet sheet, DataFormatter formatter) {
		System.out.println("Inside validate Excel");
		boolean flag = false;
		Row header = sheet.getRow(0);
		List<String> headersFromExcel = new ArrayList<>();
		List<String> mandatoryHeaders = new ArrayList<>();

		mandatoryHeaders.addAll(Arrays.asList(EnovationConstants.EMPLOYEE_ID,EnovationConstants.FIRST_NAME, EnovationConstants.LAST_NAME,
				EnovationConstants.EMAIL_ID,EnovationConstants.MOBILE_NUMBER,EnovationConstants.DEPARTMENT, EnovationConstants.DESIGNATION,
				EnovationConstants.LINE,EnovationConstants.DOJ,EnovationConstants.EMP_LEVEL));
		for (int k = header.getFirstCellNum(); k < header.getLastCellNum() + 1; k++) {
			Cell cellHeader = header.getCell(k);
			if (cellHeader != null) {
				headersFromExcel.add(cellHeader.toString());
			}
		}
		System.out.println("Excel Columns ==> " + headersFromExcel);
		System.out.println("Mandatory Columns  ==> " + mandatoryHeaders);
		flag = true;

		if (headersFromExcel.containsAll(mandatoryHeaders)) {
			flag = true;
		}

		return flag;
	}

	public static boolean IsExcelBlank(DataFormatter formatter, Sheet sheet) {
		boolean flag = true;
		for (Row row : sheet) {
			if (row.getRowNum() > 0) {
				for (int k = row.getFirstCellNum(); k < row.getLastCellNum() + 1; k++) {
					boolean cellFlag = validateCellData(formatter, row, k);
					if (cellFlag) {
						String cellData = formatter.formatCellValue(row.getCell(k));
						if (cellData != null && !cellData.isEmpty()) {
							flag = false;
							break;
						}
					}
				}
			}
		}
		return flag;
	}

	public static boolean validateCellData(DataFormatter formatter, Row row, int k) {
		boolean flag = false;
		if (!formatter.formatCellValue(row.getCell(k)).equals("")
				&& formatter.formatCellValue(row.getCell(k)).trim().length() != 0) {
			flag = true;
		}
		return flag;
	}

	
	public static boolean excelRowChecker(DataFormatter formatter, Row row) {
		boolean flag = false;

		for (int k = row.getFirstCellNum(); k < row.getLastCellNum() + 1; k++) {
			boolean cellFlag = validateCellData(formatter, row, k);
			if (cellFlag) {
				String cellData = formatter.formatCellValue(row.getCell(k));
				if (cellData != null && !cellData.isEmpty()) {
					flag = true;
					break;
				}
			}
		}
		return flag;
		

	}

}
