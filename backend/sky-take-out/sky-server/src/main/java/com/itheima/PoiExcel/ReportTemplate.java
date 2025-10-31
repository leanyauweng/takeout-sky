package com.itheima.PoiExcel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;

public class ReportTemplate {
    public static void main(String[] args) throws Exception {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet1");

        // 设置列宽（0-6列）
        for (int i = 0; i <= 6; i++) {
            sheet.setColumnWidth(i, 15 * 256);
        }

        // ------------------- 样式定义 -------------------
        // 标题样式
        CellStyle titleStyle = wb.createCellStyle();
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBold(true);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 保持白底，不设置填充颜色
        titleStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 小标题样式
        CellStyle subTitleStyle = wb.createCellStyle();
        Font subFont = wb.createFont();
        subFont.setBold(true);
        subTitleStyle.setFont(subFont);
        subTitleStyle.setAlignment(HorizontalAlignment.CENTER);
        subTitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        subTitleStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        subTitleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 表头样式
        CellStyle headerStyle = wb.createCellStyle();
        Font headFont = wb.createFont();
        headFont.setBold(true);
        headerStyle.setFont(headFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // 普通单元格样式
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        // ------------------- 标题 -------------------
        Row row0 = sheet.createRow(0);
        row0.setHeightInPoints(30);
        Cell cell0 = row0.createCell(1);
        cell0.setCellValue("运营数据报表");
        cell0.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 6));

        // 创建空白日期行（B2:G2）
        Row row1 = sheet.createRow(1);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 6));

        // 为合并区域内的每个单元格应用边框样式
        for (int col = 1; col <= 6; col++) {
            Cell cell = row1.createCell(col);
            cell.setCellStyle(cellStyle);
        }

        // ------------------- 概览数据标题 -------------------
        Row row2 = sheet.createRow(2);
        Cell cell2 = row2.createCell(1);
        cell2.setCellValue("概览数据");
        cell2.setCellStyle(subTitleStyle);
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 6));

        // ------------------- 概览数据内容 -------------------
        Row row3 = sheet.createRow(3);
        row3.createCell(1).setCellValue("营业额");
        row3.createCell(2).setCellValue("");
        row3.createCell(3).setCellValue("订单完成率");
        row3.createCell(4).setCellValue("");
        row3.createCell(5).setCellValue("新增用户数");
        row3.createCell(6).setCellValue("");

        Row row4 = sheet.createRow(4);
        row4.createCell(1).setCellValue("有效订单");
        row4.createCell(2).setCellValue("");
        row4.createCell(3).setCellValue("平均客单价");
        row4.createCell(4).setCellValue("");
        row4.createCell(5).setCellValue("");
        row4.createCell(6).setCellValue("");

        // 设置样式（从 B 到 G）
        for (int i = 1; i <= 6; i++) {
            row3.getCell(i).setCellStyle(headerStyle);
            row4.getCell(i).setCellStyle(headerStyle);
        }

        // ------------------- 明细数据标题 -------------------
        Row row6 = sheet.createRow(5);
        Cell cell6 = row6.createCell(1);
        cell6.setCellValue("明细数据");
        cell6.setCellStyle(subTitleStyle);
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 1, 6));

        // ------------------- 明细表头 -------------------
        Row row7 = sheet.createRow(6);
        String[] headers = {"日期", "营业额", "有效订单", "订单完成率", "平均客单价", "新增用户数"};
        for (int i = 0; i < headers.length ; i++) {
            Cell c = row7.createCell(i+1);
            c.setCellValue(headers[i]);
            c.setCellStyle(headerStyle);
        }

        // ------------------- 预留数据行 -------------------
        for (int r = 7; r <= 36; r++) {
            Row dataRow = sheet.createRow(r);
            for (int c = 1; c <= headers.length ; c++) {
                Cell dataCell = dataRow.createCell(c);
                dataCell.setCellValue("");
                dataCell.setCellStyle(cellStyle);
            }
        }

        // ------------------- 写出文件 -------------------
        try (FileOutputStream fos = new FileOutputStream("运营数据报表.xlsx")) {
            wb.write(fos);
        }
        wb.close();

        System.out.println("✅ 模板生成成功：运营数据报表.xlsx");
    }
}
