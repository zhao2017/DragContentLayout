package test.com.zh.java_test.test_string;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import test.com.zh.java_test.model.SelectFillData;

/**
 * 创建日期：2019/5/6
 * 描述:
 *
 * @author: zhaoh
 */
public class TestSplit {

    private static List<String> list1 = new ArrayList<>();


    /**
     *
     * "[\"貌美如花|善解人意\",\"明眸善睐|貌美如花\"]"
     * String str = "\"[\"貌美如花|善解人意\",\"明眸善睐|貌美如花\"]\"";
     * String str2 = "\"[\"貌美如花|善解人意\",\"明眸善睐|貌美如花\"]\"";
     *
     * @param args
     */

    public static void main(String[] args) {
        // 这个相当于有多少道选词填空题
        List<SelectFillData> dataSource = getDataSource();
        // 创建这个集合用于存储哪一道题是多选哪一道是非多选题  集合的数量和dataSource的数量相同
        // 1 是多选  0 非多选
        List<String> mList = new ArrayList<>(dataSource.size());
        Gson gson = new Gson();

     /*   for (SelectFillData data : dataSource) {
            // 每道题的空数 用集合存放
            List<SelectFillData.ListTitleAnswerData> listTitleAnswerData = data.listTitleAnswerData;
            // 对答案集合里面的数据进行判断只要有一个答案是多选的说明就是多选题
            for (SelectFillData.ListTitleAnswerData bean:listTitleAnswerData){

            }
            mList.add("1");
            printStr("size==" + listTitleAnswerData.size());
        }*/
        for (int i = 0; i < dataSource.size(); i++) {
            SelectFillData selectFillData = dataSource.get(i);
            List<SelectFillData.ListTitleAnswerData> dataList = selectFillData.listTitleAnswerData;
            printStr("size==" + dataList.size());
            for (int j = 0; j < dataList.size(); j++) {
                SelectFillData.ListTitleAnswerData answerData = dataList.get(j);
                List<String> strings = formatStringToList(answerData.content);
                printStr("strings.size"+strings.size());
            }
        }


    }


    /**
     * 列表json字符串 转 list
     *
     * @param optionContent eg："[\"貌美如花|善解人意\",\"明眸善睐|貌美如花\"]"
     * @return
     */
    private static List<String> formatStringToList(String optionContent) {
        return new Gson().fromJson(optionContent, new ArrayList<>().getClass());
    }


    public static void printStr(String str) {
        System.out.println(str);
    }


    /**
     * 模仿正式库数据源创造数据源
     *
     * @return
     */

    public static List<SelectFillData> getDataSource() {
        List<SelectFillData> datas = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SelectFillData selectFillData = new SelectFillData();
            selectFillData.context = "大家好我是赵迪新建的选择填空题" + i;
            List<SelectFillData.ListTitleAnswerData> listTitleAnswerData = null;
            switch (i) {
                case 0:
                    listTitleAnswerData = new ArrayList<>();
                    for (int j = 0; j < 2; j++) {
                        SelectFillData.ListTitleAnswerData answerData = new SelectFillData.ListTitleAnswerData();
                        switch (j) {
                            case 0:
                                answerData.content = "[\"貌美如花|善解人意\",\"明眸善睐|貌美如花\"]";
                                break;
                            case 1:
                                answerData.content = "[\"貌美如花|善解人意\",\"明眸善睐|貌美如花\"]";
                                break;
                            default:
                                break;
                        }
                        listTitleAnswerData.add(answerData);
                    }
                    break;
                case 1:
                    listTitleAnswerData = new ArrayList<>();
                    for (int j = 0; j < 2; j++) {
                        SelectFillData.ListTitleAnswerData answerData = new SelectFillData.ListTitleAnswerData();
                        switch (j) {
                            case 0:
                                answerData.content = "[\"貌美如花|善解人意\",\"明眸善睐|貌美如花\"]";
                                break;
                            case 1:
                                answerData.content = "[\"貌美如花|善解人意\",\"明眸善睐|貌美如花\"]";
                                break;
                            default:
                                break;
                        }
                        listTitleAnswerData.add(answerData);
                    }
                    break;
                case 2:
                    listTitleAnswerData = new ArrayList<>();
                    for (int j = 0; j < 3; j++) {
                        SelectFillData.ListTitleAnswerData answerData = new SelectFillData.ListTitleAnswerData();
                        switch (j) {
                            case 0:
                                answerData.content = "[\"壹\"]";
                                break;
                            case 1:
                                answerData.content = "[\"贰\"]";
                                break;
                            case 2:
                                answerData.content = "[\"叁\"]";
                                break;
                            default:
                                break;
                        }
                        listTitleAnswerData.add(answerData);
                    }
                    break;
                default:
                    break;
            }
            selectFillData.listTitleAnswerData = listTitleAnswerData;
            datas.add(selectFillData);
        }
        return datas;
    }


}
