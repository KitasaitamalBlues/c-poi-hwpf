package com.hua.graalvm;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.graalvm.nativeimage.IsolateThread;
import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleDemo {
    @CEntryPoint(name = "readDOC")
    static CCharPointer readDOC(IsolateThread thread, CCharPointer cCharPointer) {
        String filePath = CTypeConversion.toJavaString(cCharPointer);
        File file = new File(filePath);
        if (file.isDirectory()){
            System.out.println("应当传入文件地址");
            return CTypeConversion.toCString("{\"status\":1;\"msg\":\"应该传入文件地址，而非目录\"}").get();
        }
        if (!file.exists()){
            System.out.println("路径不存在");
            return CTypeConversion.toCString("{\"status\":2;\"msg\":\"路径不存在\"}").get();
        }
        ObjectMapper objectMapper = new ObjectMapper();
//        try (var is = SimpleDemo.class.getClassLoader().getResourceAsStream("人员信息表.doc")) {
        try (var is = new FileInputStream(file)) {
            HWPFDocument hwpfDocument = new HWPFDocument(is);
//            System.out.println();
            var range = hwpfDocument.getRange();
            var it = new TableIterator(range);
            /// 表格 行 列 字
            List<Map<Integer, Map<Integer, List<String>>>> tableCollection = new ArrayList<>();
            while (it.hasNext()) {
//                System.out.println();
                Map<Integer, Map<Integer, List<String>>> tableRowCollection = new HashMap<>();
                var table = it.next();
                for (int i = 0; i < table.numRows(); ++i) {
                    Map<Integer, List<String>> tableCellCollection = new HashMap<>();
                    var row = table.getRow(i);
                    for (int j = 0; j < row.numCells(); ++j) {
                        var cell = row.getCell(j);
                        List<String> cellParagraphCollection = new ArrayList<>();
                        for (int k = 0; k < cell.numParagraphs(); ++k) {
                            Paragraph paragraph = cell.getParagraph(k);
                            String s = paragraph.text();
                            if (s != null && !s.isEmpty()) {
                                s = s.substring(0, s.length() - 1);
                                if (!s.isEmpty()) cellParagraphCollection.add(s);
                            }
//                            System.out.println(s+'\t');
                        }
                        tableCellCollection.put(j, cellParagraphCollection);
                    }
                    tableRowCollection.put(i, tableCellCollection);
                }
                tableCollection.add(tableRowCollection);
                System.out.println(objectMapper.writeValueAsString(tableCollection));
            }
            Map<String,Object> result = new HashMap<>();
            result.put("status",0);
            result.put("data",tableCollection);
            return CTypeConversion.toCString(objectMapper.writeValueAsString(result)).get();
        } catch (Throwable t) {
            System.out.println("异常兜底");
            return CTypeConversion.toCString("{\"status\":3;\"msg\":\"未知异常\"}").get();
        }
    }



    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        try(var is = SimpleDemo.class.getClassLoader().getResourceAsStream("人员信息表.doc")){
            HWPFDocument hwpfDocument = new HWPFDocument(is);
            System.out.println();
            var range = hwpfDocument.getRange();
            var it = new TableIterator(range);
            /// 表格 行 列 字
            List<Map<Integer,Map<Integer, List<String>>>> tableCollection = new ArrayList<>();
            while(it.hasNext()) {
                System.out.println();
                Map<Integer,Map<Integer,List<String>>> tableRowCollection = new HashMap<>();
                var table = it.next();
                for (int i = 0; i < table.numRows(); ++i){
                    Map<Integer,List<String>> tableCellCollection = new HashMap<>();
                    var row = table.getRow(i);
                    for (int j = 0; j < row.numCells(); ++j){
                        var cell = row.getCell(j);
                        List<String> cellParagraphCollection = new ArrayList<>();
                        for (int k = 0; k < cell.numParagraphs(); ++k){
                            Paragraph paragraph = cell.getParagraph(k);
                            String s = paragraph.text();
                            if (s!=null && !s.isEmpty()){
                                s = s.substring(0,s.length() - 1);
                                if(!s.isEmpty())cellParagraphCollection.add(s);
                            }
//                            System.out.println(s+'\t');
                            System.out.println(new String(s.getBytes(),"GBK")+' ');
                        }
                        tableCellCollection.put(j,cellParagraphCollection);
                    }
                    tableRowCollection.put(i,tableCellCollection);
                }
                tableCollection.add(tableRowCollection);
                ;
                System.out.println(objectMapper.writeValueAsString(tableCollection));
            }
        }catch (Throwable t){
            System.out.println("抛出异常");
            t.printStackTrace();
        }
    }
}
