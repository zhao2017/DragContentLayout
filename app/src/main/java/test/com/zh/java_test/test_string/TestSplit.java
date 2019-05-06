package test.com.zh.java_test.test_string;

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
     * String str = "\"[\"貌美如花|善解人意\",\"明眸善睐|貌美如花\"]\"";
     * String str2 = "\"[\"貌美如花|善解人意\",\"明眸善睐|貌美如花\"]\"";
     *
     * @param args
     */

    public static void main(String[] args) {

        List<SelectFillData> dataSource = getDataSource();

        for (SelectFillData data : dataSource) {
            List<SelectFillData.ListTitleAnswerData> listTitleAnswerData = data.listTitleAnswerData;
            printStr("size==" + listTitleAnswerData.size());
        }


    }

    public static void printStr(String str) {
        System.out.println(str);
    }

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
                                answerData.content = "\"[\"貌美如花|善解人意\",\"明眸善睐|貌美如花\"]\"";
                                break;
                            case 1:
                                answerData.content = "\"[\"貌美如花|善解人意\",\"明眸善睐|貌美如花\"]\"";
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
                                answerData.content = "\"[\"貌美如花|善解人意\",\"明眸善睐|貌美如花\"]\"";
                                break;
                            case 1:
                                answerData.content = "\"[\"貌美如花|善解人意\",\"明眸善睐|貌美如花\"]\"";
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
                                answerData.content = "\"[\"壹\"]\"";
                                break;
                            case 1:
                                answerData.content = "\"[\"贰\"]\"";
                                break;
                            case 2:
                                answerData.content = "\"[\"叁\"]\"";
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
