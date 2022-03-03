package com.poi.test;

import com.poi.util.WordUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fax
 * @date 2021-08-23 11:08
 */
public class TestWord {
    public static void main(String[] args) {

        String content = "马邦德：大风起兮云飞扬，安得猛士兮走四方。" +
                        "匪，任何时候都要剿，不剿不行，你想想，你带着老婆出了城，" +
                        "吃着火锅唱着歌，突然就被麻匪劫了，所以，没有麻匪的日子才是好日子！";

        Map<String, Object> params = new HashMap<>();
        params.put("title", "让子弹飞");
        params.put("content", content);
        params.put("header1", "序号");
        params.put("header2", "演员");
        params.put("header3", "角色");
        params.put("header4", "备注");
        params.put("header5", "介绍");

        List<String[]> table1 = new ArrayList<>();
        table1.add(new String[]{"1", "姜文", "张牧之", "张麻子", "绿林悍匪"});
        table1.add(new String[]{"2", "周润发", "黄四郎", "黄老爷", "鹅城一霸"});
        table1.add(new String[]{"2", "葛优", "马邦德", "汤师爷", "只想赚钱"});

        List<String[]> table2 = new ArrayList<>();
        table2.add(new String[]{"1", "姜文", "张牧之", "xxx"});
        table2.add(new String[]{"2", "周润发", "黄四郎", "xxx"});
        table2.add(new String[]{"3", "葛优", "马邦德", "xxx"});
        //饼图数据
        String[] title = new String[]{"1星", "2星", "3星", "4星", "5星"};
        Double[] value = new Double[]{0.3, 0.5, 5.9, 33.8, 56.2};
        // 柱状图、折线图 数据

        String[] quarter = new String[]{"第一季度", "第二季度", "第三季度", "第四季度"};
        Map<String, Number[]> map = new HashMap<>();
        map.put("姜文", new Integer[]{6688, 4399, 5327, 6379});
        map.put("周润发", new Integer[]{6799, 7499, 6429, 7379});
        map.put("葛优", new Integer[]{5899, 6599, 7665, 8573});
        System.out.println(System.getProperty("user.dir"));
        String inPath = System.getProperty("user.dir") + "\\poi_demo\\src\\main\\resources\\template\\Template.docx";
        String outPath = "D:\\word文件.docx";
        try (InputStream is = new FileInputStream(inPath);
             OutputStream os = new FileOutputStream(outPath);
             XWPFDocument document = new XWPFDocument(OPCPackage.open(is))) {
            WordUtils wordUtils = new WordUtils();
            // 替换段落中的参数
            wordUtils.replaceInPara(document, params);
            // 替换表格中的参数
            wordUtils.replaceInTable(document, new int[]{0}, params);
            // 给表格追加记录
            wordUtils.insertTableRow(document.getTableArray(0), table1);
            // 创建表格
            wordUtils.createTitle(document.createParagraph(), "二、表格二");

            wordUtils.createTable(document, table2);
            // 创建饼图
            wordUtils.createTitle(document.createParagraph(), "三、饼图");
            wordUtils.createPieChart(document, "豆瓣评分", title, value);

            wordUtils.createTitle(document.createParagraph(), "四、柱形图");
            // 创建柱状图
            wordUtils.createBarChart(document, "标题", null, null, quarter, map);
            wordUtils.createTitle(document.createParagraph(), "五、条形图");
            // 创建柱状图
            wordUtils.createLineChart(document, "标题", "季度", "数值", quarter, map);
            wordUtils.createTitle(document.createParagraph(), "六、混合图");
            // 创建柱状图
            wordUtils.createBarLineChart(document, "标题", null, null, quarter, map);
            document.write(os);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
