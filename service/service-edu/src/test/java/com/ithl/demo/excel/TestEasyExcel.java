package com.ithl.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hl
 */
public class TestEasyExcel {

    public static void main(String[] args) {
        // 实现写操作
        String fileName = "G:\\write.xlsx";

        // 调用easyexcel  文件名称   实体类class
        EasyExcel.write(fileName, DemoData.class).sheet("学生列表").doWrite(getDemoData());
    }

    private static List<DemoData> getDemoData() {
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData demoData = new DemoData();
            demoData.setSno(i);
            demoData.setSname("lucy" + i);
            list.add(demoData);
        }
        return list;
    }
}
