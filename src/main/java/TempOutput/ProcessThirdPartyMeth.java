package TempOutput;

import entity.ExternalEntity;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.SingleCollect;

import java.io.FileInputStream;


public class ProcessThirdPartyMeth {
    private XSSFSheet sheet;
    protected SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();

    /**
     * initialize excel data
     * @param filePath  excel file path
     * @param sheetName sheet name
     */
    public ProcessThirdPartyMeth(String filePath,String sheetName){
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            XSSFWorkbook sheets = new XSSFWorkbook(fileInputStream);
            //获取sheet
            sheet = sheets.getSheet(sheetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Acquire cell data according to row and column
     * @param row
     * @param column
     * @return
     */
    public String getExcelDataByIndex(int row,int column){
        XSSFRow row1 = sheet.getRow(row);
        return row1.getCell(column).toString();
    }

    /**
     * Convert excel data
     */
    public void convertExcelData(){
        //get row number
        int rows = sheet.getPhysicalNumberOfRows();
        for(int i=0;i<rows;i++){
            //column number
            XSSFRow row = sheet.getRow(i);
//            int columns = row.getPhysicalNumberOfCells();
            ExternalEntity externalEntity = new ExternalEntity(singleCollect.getCurrentIndex(), row.getCell(0).toString()+"."+row.getCell(1).toString(), row.getCell(1).toString(), row.getCell(2).toString().split(" "), row.getCell(3).toString());
//            for(int j=0;j<columns;j++){
//                String cell = row.getCell(j).toString();
//                System.out.println(cell);
//            }
            singleCollect.addEntity(externalEntity);
            singleCollect.addThirdPartyAPIs(externalEntity.getId(), externalEntity.getQualifiedName());
        }
    }
}
