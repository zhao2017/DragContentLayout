package test.com.zh.dragcontentlayout.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Army
 * @version V_1.0.0
 * @date 2017/07/31
 * @description 跟String有关的工具类
 */
public class StringUtils {
    public static String unitFormat(int i) {
        String retStr;
        if (i >= 0 && i < 10) {
            retStr = "0" + i;
        } else {
            retStr = "" + i;
        }
        return retStr;
    }

    /**
     * 判断邮箱是否合法
     *
     * @param email
     * @return
     */
    public static boolean isEmailOrPhone(String email) {
        if (null == email || "".equals(email)) {
            return false;
        }
        // Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("(\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)|(1[3|4|5|7|8][0-9]\\d{8})");// 复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isEmpty(String str) {
        if (TextUtils.isEmpty(str) || "null".equalsIgnoreCase(str)) {
            return true;
        }
        return false;
    }

    /**
     * 把字符串里的多个换成换成一个
     */
    public static String replaceEnter(String content) {
        String str = content;
        try {
            str = str.replaceAll("[\\n]+", "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 把字符串里的回车和空格都替换成一个空格
     */
    public static String replaceEnterAndSpace(String content) {
        String str = new String(content);
        try {
            str = str.replaceAll("[\\n]+", " ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 移除字符串中的html
     */
    public static String RemoveHtml(String strHtml) {
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(strHtml);
        strHtml = m_html.replaceAll(""); //过滤html标签
        return strHtml.trim(); //返回文本字符串
    }


    /**
     * 判断字符串是否是汉字、字母、数字
     */
    public static boolean isChineseLetterNumber(String character) {
        return character.matches("[\u4e00-\u9fa5\\w\\s]+");
    }

    /**
     * 给一段文字的某一块设置颜色
     */
    public static SpannableString setTextStyle(String text, int start, int end, int color) {
        SpannableString s = new SpannableString(text);
        s.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }


    /**
     * 保留两位小数
     *
     * @param money
     * @return
     */
    public static String getMoneyToM2(String money) {
        if (StringUtils.isEmpty(money)) {
            return "0.00";
        }
        return new BigDecimal(money).setScale(2, RoundingMode.HALF_UP).toString();
    }


    /**
     * 获取金钱整数部分
     *
     * @param money
     * @return
     */
    public static String getMoneyInteger(String money) {
        String[] moneys = money.split("\\.");
        return moneys[0];
    }

    /**
     * 获取金钱小数部分
     *
     * @param money
     * @return
     */
    public static String getMoneyDecimal(String money) {
        String[] moneys = money.split("\\.");
        return moneys[1];
    }

    /**
     * 判断手机号是否输入正确
     */
    public static boolean isMobileRight(String mobiles) {
        String telRegex = "^(13[0-9]|14[579]|15[0-3,5-9]|16[67]|17[0135678]|18[0-9]|19[189])\\d{8}$";
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);
    }

    /**
     * 在图片的地址后面加上?imageView2/2/w/Width/h/Height
     *
     * @param url 原始地址
     */
    public static final int MINSIZE = 480;


    /**
     * 去掉img标签
     */
    private static final String IMG_LABEL = "<img ";
    private static StringBuilder builder = new StringBuilder();

    public static String replaceImageLabel(String content) {
        builder.delete(0, builder.length());
        if (StringUtils.isEmpty(content)) {
            return "";
        }
        if (content.contains(IMG_LABEL)) {
            builder.append(content);
            int start = builder.indexOf(IMG_LABEL);
            int end = builder.indexOf(">", start);
            builder.delete(start, end + 1);
            builder.insert(start, "[图片]");
            return replaceImageLabel(builder.toString());
        } else {
            return content;
        }
    }

    /**
     * 把表达式换成图片
     * [LaTeXI][/LaTeXI]
     */
    private static final String EXPRESSION_START = "[LaTeXI]";
    private static final String EXPRESSION_END = "[/LaTeXI]";
    private static final String EXPRESSION_URL = "http://gs.xueleyun.com//cgi-bin/mathtex.cgi?";
    private static final String EXPRESSION_IMAGE_LABEL_START = "<img src=\"";
    private static final String EXPRESSION_IMAGE_LABEL_END = "\">";

    private static final String UNDERLINE_START = "<span style=\"text-decoration: underline;\""; //包含下划线 外面添加
    private static final String DELETE_UNDERLINE_START = "style=\"text-decoration: underline;\""; //需要删除的文本
    private static final String UNDERLINE__END = "</span>";

    public static String replaceExpression(String content) {
        builder.delete(0, builder.length());
        if (StringUtils.isEmpty(content)) {
            return "";
        }
        if (content.contains(EXPRESSION_START) && content.contains(EXPRESSION_END)) {
            builder.append(content);
            int start = builder.indexOf(EXPRESSION_START);
            int end = builder.indexOf(EXPRESSION_END, start);
            if (start > -1 && end > -1) {
                String expression = builder.substring(start + EXPRESSION_START.length(), end);
                builder.delete(start, end + EXPRESSION_END.length());
                builder.insert(start, EXPRESSION_IMAGE_LABEL_START + EXPRESSION_URL + expression + EXPRESSION_IMAGE_LABEL_END);
                return replaceExpression(builder.toString());
            }
            return content;
        } else {
            return content;
        }
    }

    private static final String P_START_TAG = "<p>";
    private static final String P_START_TAG_WITH_SPACE = "<p >";
    private static final String P_END_TAG = "</p>";
    /**
     * 下标的标签
     */
    private static final String SUB_TAG = "<sub>";
    /**
     * 上标的标签
     */
    private static final String SUP_TAG = "<sup>";
    private static final String IMAGE_START_TAG = "<img ";
    private static final String UL_TAG = "</ul>";
    private static final String LI_TAG = "</li>";
    private static final String DIV_TAG = "</div>";
    private static final String SPAN_TAG = "</span>";
    private static final String STRONG_TAG = "</strong>";
    private static final String B_TAG = "</b>";
    private static final String CITE_TAG = "</cite>";
    private static final String DFN_TAG = "</dfn>";
    private static final String I_TAG = "</i>";
    private static final String BIG_TAG = "</big>";
    private static final String SMALL_TAG = "</small>";
    private static final String FONT_TAG = "</font>";
    private static final String BLOCKQUOTE_TAG = "</blockquote>";
    private static final String TT_TAG = "</tt>";
    private static final String A_TAG = "</a>";
    private static final String U_TAG = "</u>";
    private static final String DEL_TAG = "</del>";
    private static final String S_TAG = "</s>";
    private static final String STRIKE_TAG = "</strike>";
    private static final String H1_TAG = "<h1>";
    private static final String H2_TAG = "<h2>";
    private static final String H3_TAG = "<h3>";
    private static final String H4_TAG = "<h4>";
    private static final String H5_TAG = "<h5>";
    private static final String H6_TAG = "<h6>";
    private static final String BR_TAG = "<br ";
    private static final String IN_PUT = "<input ";



    public static String convertHtmlTag(String content) {
        if (StringUtils.isEmpty(content)) {
            return "";
        }
        content = content.replaceAll("\\n", "").replaceAll("&nbsp;", "\t")
                .replaceAll("<br>", "\n").replaceAll("<br/>", "\n")
                .replaceAll("<br />", "\n").replaceAll("&lt;", " < ")
                .replaceAll("&gt;", " > ");
        if (isContainHtmlTag(content.toLowerCase())) {
            return content.replaceAll("\\n", "<br>").replaceAll("\\t", "&nbsp;");
        }
        return htmlEncode(content).replaceAll("\\n", "<br>").replaceAll("\\t", "&nbsp;");
    }

    private static final String[] TAGS = {SUB_TAG, SUP_TAG, IMAGE_START_TAG, P_END_TAG, UL_TAG, LI_TAG,
            DIV_TAG, SPAN_TAG, STRONG_TAG, B_TAG, CITE_TAG, DFN_TAG, I_TAG, BIG_TAG, SMALL_TAG, FONT_TAG,
            BLOCKQUOTE_TAG, TT_TAG, A_TAG, U_TAG, DEL_TAG, S_TAG, STRIKE_TAG, H1_TAG, H2_TAG, H3_TAG, H4_TAG,
            H5_TAG, H6_TAG, BR_TAG,IN_PUT};

    private static boolean isContainHtmlTag(String content) {
        for (String tag : TAGS) {
            if (content.contains(tag)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 移除只在首尾存在的p标签
     */
    public static String remove_p_tag(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        if (!content.toLowerCase().startsWith(P_START_TAG) && !content.toLowerCase().startsWith(P_START_TAG_WITH_SPACE)) {
            return content;
        }
        content = content.replaceAll(P_START_TAG_WITH_SPACE, P_START_TAG);
        //如果包含多个p标签，则不移除
        if (content.toLowerCase().indexOf(P_START_TAG, P_START_TAG.length()) > 0) {
            return content;
        }
        return content.substring(P_START_TAG.length(), content.length() - P_END_TAG.length());
    }


    /**
     * 移除全部的p标签 <p></p> /n " "   <p >不处理
     */
    public static String removeAll_p_tag(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        if (!content.toLowerCase().startsWith(P_START_TAG) && !content.toLowerCase().startsWith(P_END_TAG)) {
            return content;
        }
        content = content.replaceAll(P_START_TAG, "");
        content = content.replaceAll(P_END_TAG, "");
        content = content.replaceAll("\n", "");
        return content;
    }


    /**
     * 处理文本中关于html的特殊字段{表达式  <img></>图片 <p></p>}
     */
    public static String dealHtmlToString(String content) {
        return replaceImageLabel(replaceExpression(convertHtmlTag(remove_p_tag(content))));
    }

    public static final String RGB = "rgb(";
    public static final String RIGHT_PARENTHESES = ")";

    /**
     * 把html代码里的rgb(255, 102, 0)这种颜色转化成十六进制的颜色
     */
    public static String convertRGB2HEX(StringBuilder html, int start) {
        if (TextUtils.isEmpty(html)) {
            return "";
        }
        int sIndex = html.indexOf(RGB, start);
        if (sIndex > -1) {
            int eIndex = html.indexOf(RIGHT_PARENTHESES, sIndex);
            if (eIndex > -1 && sIndex < eIndex) {
                String rgb = html.substring(sIndex + RGB.length(), eIndex).replaceAll(" ", "");
                String[] colors = rgb.split(",");
                if (colors.length == 3) {
                    html.delete(sIndex, eIndex);
                    html.insert(sIndex, "#" + convertDEC2HEX(colors[0]) + convertDEC2HEX(colors[1]) + convertDEC2HEX(colors[2]));
                }
                convertRGB2HEX(html, sIndex + RGB.length());
            } else {
                convertRGB2HEX(html, start + RGB.length());
            }
        }
        return html.toString();
    }

    /**
     * 十进制转十六进制
     */
    private static String convertDEC2HEX(String dec) {
        String hex = Integer.toHexString(Integer.parseInt(dec));
        if (hex.length() == 1) {
            return "0" + hex;
        }
        return hex;
    }

    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String replaceUnderline(String content) {
        builder.delete(0, builder.length());
        if (StringUtils.isEmpty(content)) {
            return "";
        }
        if (content.contains(UNDERLINE_START) && content.contains(UNDERLINE__END)) {
            builder.append(content);
            int start = builder.indexOf(UNDERLINE_START);
            int end = builder.indexOf(UNDERLINE__END, start);
            //在前面添加下划线标签
            if (start > -1 && end > -1) {
                builder.insert(start, "<u>");
                builder.insert(end + "<u>".length(), "</u>");
                //删除下划线style
                builder.delete(start + "<u>".length() + "<span ".length(), start + "<u>".length() + "<span ".length() + DELETE_UNDERLINE_START.length());
                return replaceUnderline(builder.toString());
            }
            return content;
        } else {
            return content;
        }
    }

    /**
     * @param voiceSecond 单位是s
     */
    public static String formatVoiceTime(long voiceSecond) {
        if (voiceSecond < 60) {
            return voiceSecond + "″";
        }
        if (voiceSecond % 60 == 0) {
            return (voiceSecond / 60) + "′";
        }
        long minute = voiceSecond / 60;
        long second = voiceSecond % 60;
        return String.format(Locale.getDefault(), "%d′%s″", minute, second);
    }

    public static boolean equals(String str1, String str2) {
        return str1 != null && str1.equals(str2);
    }

    /**
     * 陈处理尖括号
     *
     * @param s
     * @return
     */
    public static String htmlEncode(String s) {
        StringBuilder sb = new StringBuilder();
        char c;
        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            switch (c) {
                case '<':
                    sb.append("&lt;"); //$NON-NLS-1$
                    break;
                case '>':
                    sb.append("&gt;"); //$NON-NLS-1$
                    break;
                case '&':
                    sb.append("&amp;"); //$NON-NLS-1$
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }


    /**
     * 数字转化为汉字【只处理了百位以下的】
     *
     * @param num 12
     * @return 十二
     */
    public static String dealZhByNum(int num) {
        String[] units = {"", "十"};
        String[] numArr = {"", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

        String text = "";

        if (num > 0 && num < 10) {
            text = numArr[num];
        } else if (num >= 10 && num < 100) {
            int shi = num / 10;
            if (shi == 1) {
                text = units[1].concat(numArr[num / 10]);
            } else {
                text = numArr[num / 10].concat(units[1]).concat(numArr[num % 10]);
            }
        }

        return text;
    }

}
