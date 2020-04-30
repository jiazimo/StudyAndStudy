package com.example.demo.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.ContentType;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;


public class ImportAndOutport{

  /**
     *	  检验文件是否有效
     * 
     * @param file
     * @throws Exception
     */
    public static void checkFile(MultipartFile file) throws Exception {
        // 判断文件是否存在
        if (null == file) {
            throw new FileNotFoundException("文件不存在！");
        }
        // 获得文件名
        String fileName = file.getOriginalFilename();
        // 判断文件是否是excel文件
        if (!fileName.endsWith("xls") && !fileName.endsWith("xlsx")) {
            throw new IOException(fileName + "不是excel文件");
        }
    }

    /**
     * 	获取workbook
     * 
     * @param file
     * @return
     */
    public static Workbook getWorkBook(MultipartFile file) {
        // 获得文件名
        String fileName = file.getOriginalFilename();
        // 创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            // 获取excel文件的io流
            InputStream is = file.getInputStream();
            // 根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith("xls")) {
                // 2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith("xlsx")) {
                // 2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {

        }
        return workbook;
    }

    /**
     *	 获取解析后的文件内容
     * 
     * @return
     * @throws Exception 
     */
    public static List<Users> readExcel(MultipartFile file) {
        List<Users> list = new ArrayList<Users>();
        try {
            checkFile(file);
            // 获得Workbook工作薄对象
            Workbook workbook = getWorkBook(file);
            int sheetSize = workbook.getNumberOfSheets();
            System.out.println("sheetSize"+sheetSize);
//            for (int i = 0; i < sheetSize; i++) {// 遍历sheet页
                // 获取第一个张表
                Sheet sheet = workbook.getSheetAt(0);
                int a = sheet.getFirstRowNum() + 1;
                int b = sheet.getLastRowNum();
                System.out.println(a);
                System.out.println(b);
                // 获取每行中的字段
                for (int j = a; j <= b; j++) {
                    Row row = sheet.getRow(j); // 获取行
                    if (row == null) {//略过空行
                        continue;
                    }else{
                    // 获取单元格中的值并存到对象中
                    Users user = new Users();
                    row.getCell(0).setCellType(CellType.STRING);
                    row.getCell(1).setCellType(CellType.STRING);
                    row.getCell(2).setCellType(CellType.STRING);
                    user.setFullname(row.getCell(0).getStringCellValue());
                    user.setPassword(row.getCell(1).getStringCellValue());
                    user.setUsername(row.getCell(2).getStringCellValue());
                    list.add(user);
                    }
                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static HSSFWorkbook getHSSFWorkbook(List<Users> list){


        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet();

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();


        style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式


        HSSFCell cell1 = row.createCell(0);
        cell1.setCellValue("fullname");
        cell1.setCellStyle(style);

        HSSFCell cell2 = row.createCell(1);
        cell2.setCellValue("password");
        cell2.setCellStyle(style);

        HSSFCell cell3 = row.createCell(2);
        cell3.setCellValue("username");
        cell3.setCellStyle(style);


        //创建内容
        for(int i=0;i<list.size();i++){
           //第一行是表头，第二行开始插数据
            row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(list.get(i).getFullname());
            row.createCell(1).setCellValue(list.get(i).getPassword());
            row.createCell(2).setCellValue(list.get(i).getUsername());

        }
        return wb;
    }


    public static void main(String[] args) throws Exception {
    	ImportAndOutport testController = new ImportAndOutport();
    	File pdfFile = new File("D://a.xls");
    	FileInputStream fileInputStream = new FileInputStream(pdfFile);
    	MultipartFile multipartFile = new MockMultipartFile(pdfFile.getName(), pdfFile.getName(),ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
    	List<Users> list = testController.readExcel(multipartFile);
    	System.out.println(list.get(0).toString()+""+list.get(1).toString()+""+list.get(2).toString());
//    	Users user = new Users();
//    	user.setFullname("fullname");
//    	user.setPassword("password");
//    	user.setUsername("username");
//    	List<Users> list = new ArrayList();
//    	list.add(user);
//    	HSSFWorkbook hssfWorkBook = TestController.getHSSFWorkbook(list);
//    	FileOutputStream fileOutput = new FileOutputStream("D:\\jiazimo.xls");
//    	hssfWorkBook.write(fileOutput);
    }


}
