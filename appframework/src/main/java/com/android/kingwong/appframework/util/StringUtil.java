package com.android.kingwong.appframework.util;

import android.text.TextUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by KingWong on 2017/9/4.
 * 字符串格式处理
 */

public class StringUtil {

    public static String getNotNullString(Object stringObject, String defaultString) {
        return stringObject == null ? defaultString : stringObject.toString();
    }

    public static boolean mailAddressVerify(String mailAddress) {
        if(mailAddress.trim().length()==0){
            return false;
        }
        String emailExp = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(emailExp);
        return p.matcher(mailAddress).matches();
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
		/*
		移动：134、135、136、137、138、139、147、148、150、151、152、157、158、159、172、178、182、183、184、187、188、198
		联通：130、131、132、145、146、155、156、166、171、175、176、185、186
		电信：133、149、153、173、174、177、180、181、189、199
		虚拟运营商: 170[1700/1701/1702(电信)、1703/1705/1706(移动)、1704/1707/1708/1709(联通)]
		总结起来就是第一位必定为1，第二位为34578，其他位置的可以为0-9。
		130-139这十个前三位已经全部开通，后面8位每一位都是0-9之间的任意数；
        14开头的目前只有145、146、147、148、149三位，后面8位每一位都是0-9之间的任意数；
        15开头的除了154以外第三位可以随意取，后面8位每一位都是0-9之间的任意数；
        17开头的除了179以外第三位可以随意取，后面8位每一位都是0-9之间的任意数；
        180-189这十个前三位已经全部开通，后面8位每一位都是0-9之间的任意数；
		*/
        String telRegex = "^1(3[0-9]|4[5-9]|5[^4]|7[0-8]|8[0-9])\\d{8}$";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    /**
     * 验证手机运营商
     * 返回0位移动， 1为联通， 2为电信，默认为移动
     */
    public static int checkPhone(String mobiles){
        String[] yidong = {"134", "135", "136", "137", "138" ,"139","147","148","150","151", "152", "157", "158", "159",
                "172", "178", "182", "183", "184", "187", "188", "198", "1703", "1705", "1706"};
        String[] liantong = {"130", "131", "132", "145", "146", "155", "156", "166", "171", "175", "176", "185", "186",
                "1704", "1707", "1708", "1709"};
        String[] dianxin = {"133", "149", "153", "173", "174", "177", "180", "181", "189", "199", "1700", "1701", "1702"};
        for (String str : yidong) {
            if (mobiles.contains(str)) {
                return 0;
            }
        }
        for (String str : liantong) {
            if (mobiles.contains(str)) {
                return 1;
            }
        }
        for (String str : dianxin) {
            if (mobiles.contains(str)) {
                return 2;
            }
        }
        return 0;
    }

    public static boolean isAreaNum(String num){
        String regex = "^(010|02\\d|0[3-9]\\d{2})$";
        if (TextUtils.isEmpty(num)) return false;
        else return num.matches(regex);
    }

    public static int getDotStringToInt(String number){
        if(number.indexOf(".") < 0){
            return Integer.parseInt(number);
        }else{
            String intNumber = number.substring(0, number.indexOf("."));
            return Integer.parseInt(intNumber);
        }
    }

    public static String getDotStringToString(String number){
        String intNumber = number.substring(0, number.indexOf("."));
        return intNumber;
    }

    public static String getDotStringfromDouble(Double mDouble){
        String number = String.valueOf(mDouble);
        String intNumber = number.substring(0, number.indexOf("."));
        return intNumber;
    }

    public static String getDotStringfromFloat(Float mFloat){
        String number = String.valueOf(mFloat);
        String intNumber = number.substring(0, number.indexOf("."));
        return intNumber;
    }

    /**
     * 用指定字符分隔格式化字符串
     * <br/>（如电话号为1391235678 指定startIndex为3，step为4，separator为'-'经过此处理后的结果为 <br/> 139-1234-5678）
     * @param source 需要分隔的字符串
     * @param startIndex  开始分隔的索引号
     * @param step 步长
     * @param separator 指定的分隔符
     * @return 返回分隔格式化处理后的结果字符串
     */
    public static String separateString(String source, int startIndex, int step, char separator) {
        int times = 0;
        StringBuilder tmpBuilder = new StringBuilder(source);
        for(int i = 0; i < tmpBuilder.length(); i++){
            if(i == startIndex + step * times + times){//if(i == 3 || i == 8){
                if(separator != tmpBuilder.charAt(i)){
                    tmpBuilder.insert(i, separator);
                }
                times++;
            }else {
                if(separator == tmpBuilder.charAt(i)){
                    tmpBuilder.deleteCharAt(i);
                    i = -1;
                    times = 0;
                }
            }
        }
        return tmpBuilder.toString();
    }

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     *
     * 去除字符串中空格制表符换行
     */
    public static String replaceBlank(String src) {
        String dest = "";
        if (src != null) {
            Pattern pattern = Pattern.compile("\t|\r|\n|\\s*");
            Matcher matcher = pattern.matcher(src);
            dest = matcher.replaceAll("");
        }
        return dest;
    }

    public static String stringTo2(double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(value);
    }

}
