package com.ithl.demo.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

/**
 * @author hl
 */
public class ExcelListener extends AnalysisEventListener<DemoData> {

    // 一行一行读取excel内容
    @Override
    public void invoke(DemoData data, AnalysisContext context) {
        System.out.println("*****" + data);
    }

    // 读取表头
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头" + headMap);
    }

    // 读取完成以后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
    }
}
