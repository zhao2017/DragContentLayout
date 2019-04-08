package test.com.zh.java_test.test_string;

/**
 * 创建日期：2019/4/8
 * 描述:测试replace
 *
 * @author: zhaoh
 */
public class TestReplace {
    public static void main(String[] args) {
        String str = "cbbbbbbbbbk";
        /**
         * 使用指定的字面值替换序列替换此字符串所有匹配字面值目标序列的子字符串。该替换从字符串的开头朝末尾执行，
         * target - 要被替换的 char 值序列
         * replacement - char 值的替换序列
         */
        printStr(str.replace("cbbbbbbbbb","a"));
    }

    public static void printStr(String str){
        System.out.println(str);
    }
}
