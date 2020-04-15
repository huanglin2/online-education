package com.ithl.demo.excel;

import com.alibaba.excel.EasyExcel;

/**
 * @author hl
 */
public class TestEasyExcelRead {

    public static void main(String[] args) {
        String fileName = "G:\\write.xlsx";
        // 实现excel的读操作
        EasyExcel.read(fileName, DemoData.class, new ExcelListener()).sheet().doRead();
    }
}
