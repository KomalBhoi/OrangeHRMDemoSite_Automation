package com.qa.OrangeHRMDemoSite.Utils;

import org.apache.commons.compress.archivers.dump.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class readExcelData {
    public static String TESTDATA_SHEET_PATH="src/main/java/com/qa/OrangeHRMDemoSite/testData/EmployeeData.xlsx";

    static Workbook book;
    static Sheet sheet;

    public static Object[][] getTestData(String sheetName){
        FileInputStream file = null;
        try{
            file =new FileInputStream(TESTDATA_SHEET_PATH);
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            book = WorkbookFactory.create(file);
        }catch(InvalidFormatException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sheet =book.getSheet(sheetName);
        int totalRows = sheet.getLastRowNum();
        int totalCols = sheet.getRow(0).getLastCellNum();

        Object[][] data =new Object[totalRows][totalCols];
        DataFormatter formatter = new DataFormatter();

        // Date format to standardize Excel dates
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");

        for(int i=0;i< totalRows;i++){
            for(int k=0;k<totalCols;k++){
                Cell cell = sheet.getRow(i+1).getCell(k);
                // This ensures numbers like 9878954567 are read as plain text
                //data[i][k] =formatter.formatCellValue(cell);

                if(cell == null){
                    data[i][k]="";
                    continue;
                }

                switch (cell.getCellType()){
                    case STRING:
                        // Dropdown or text cell → Excel stores dropdown text as plain string
                        data[i][k]=cell.getStringCellValue().trim();
                        break;

                    case NUMERIC:
                        // Check if it's a date cell
                        if(DateUtil.isCellDateFormatted(cell)){
                            Date dateValue = cell.getDateCellValue();
                            data[i][k] = sdf.format(dateValue);
                        } else{
                            // Just a number, format safely
                            data[i][k]=formatter.formatCellValue(cell);
                        }
                        break;

                    case BOOLEAN:
                        data[i][k]=cell.getBooleanCellValue();
                        break;

                    case FORMULA:
                        // Evaluate formulas and format result
                        data[i][k]=formatter.formatCellValue(cell);
                        break;

                    default:
                        data[i][k]="";
                        break;
                }
            }
        }
        return data;
    }
}
