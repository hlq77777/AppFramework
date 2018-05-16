package com.android.kingwong.appframework.util.ValidateUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by KingWong on 2017/9/28.
 * Luhm校验规则：16位银行卡号（19位通用）:
 * 1.将未带校验位的 15（或18）位卡号从右依次编号 1 到 15（18），位于奇数位号上的数字乘以 2。
 * 2.将奇位乘积的个十位全部相加，再加上所有偶数位上的数字。
 * 3.将加法和加上校验位能被 10 整除。
 */

public class BankCardValidate {
    //开头6位
    //private final static String strBin = "10,18,30,35,37,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,58,60,62,65,68,69,84,87,88,94,95,98,99";
    public final static String SUCCESS = "true";
    private final static String BAD_LENGTH = "银行卡号长度必须在16到19之间";
    private final static String NOT_NUMBER = "银行卡必须全部为数字";
    private final static String ILLEGAL_NUMBER = "银行卡不符合规则";

    public static String luhmCheck(String bankno) {
        if (bankno.length() < 16 || bankno.length() > 19) {
            return BAD_LENGTH;
        }

        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher match = pattern.matcher(bankno);
        if (match.matches() == false) {
            return NOT_NUMBER;
        }

//        if (strBin.indexOf(bankno.substring(0, 2)) == -1) {
//            return "银行卡号开头6位不符合规范";
//        }

        int lastNum = Integer.parseInt(bankno.substring(bankno.length() - 1,
                bankno.length()));// 取出最后一位（与luhm进行比较）

        String first15Num = bankno.substring(0, bankno.length() - 1);// 前15或18位
        char[] newArr = new char[first15Num.length()]; // 倒叙装入newArr
        char[] tempArr = first15Num.toCharArray();
        for (int i = 0; i < tempArr.length; i++) {
            newArr[tempArr.length - 1 - i] = tempArr[i];
        }

        int[] arrSingleNum = new int[newArr.length]; // 奇数位*2的积 <9
        int[] arrSingleNum2 = new int[newArr.length];// 奇数位*2的积 >9
        int[] arrDoubleNum = new int[newArr.length]; // 偶数位数组

        for (int j = 0; j < newArr.length; j++) {
            if ((j + 1) % 2 == 1) {// 奇数位
                if ((int) (newArr[j] - 48) * 2 < 9)
                    arrSingleNum[j] = (int) (newArr[j] - 48) * 2;
                else
                    arrSingleNum2[j] = (int) (newArr[j] - 48) * 2;
            } else
                // 偶数位
                arrDoubleNum[j] = (int) (newArr[j] - 48);
        }

        int[] arrSingleNumChild = new int[newArr.length]; // 奇数位*2 >9
        // 的分割之后的数组个位数
        int[] arrSingleNum2Child = new int[newArr.length];// 奇数位*2 >9
        // 的分割之后的数组十位数

        for (int h = 0; h < arrSingleNum2.length; h++) {
            arrSingleNumChild[h] = (arrSingleNum2[h]) % 10;
            arrSingleNum2Child[h] = (arrSingleNum2[h]) / 10;
        }

        int sumSingleNum = 0; // 奇数位*2 < 9 的数组之和
        int sumDoubleNum = 0; // 偶数位数组之和
        int sumSingleNumChild = 0; // 奇数位*2 >9 的分割之后的数组个位数之和
        int sumSingleNum2Child = 0; // 奇数位*2 >9 的分割之后的数组十位数之和
        int sumTotal = 0;
        for (int m = 0; m < arrSingleNum.length; m++) {
            sumSingleNum = sumSingleNum + arrSingleNum[m];
        }

        for (int n = 0; n < arrDoubleNum.length; n++) {
            sumDoubleNum = sumDoubleNum + arrDoubleNum[n];
        }

        for (int p = 0; p < arrSingleNumChild.length; p++) {
            sumSingleNumChild = sumSingleNumChild + arrSingleNumChild[p];
            sumSingleNum2Child = sumSingleNum2Child + arrSingleNum2Child[p];
        }

        sumTotal = sumSingleNum + sumDoubleNum + sumSingleNumChild + sumSingleNum2Child;

        // 计算Luhm值
        int k = sumTotal % 10 == 0 ? 10 : sumTotal % 10;
        int luhm = 10 - k;

        if (lastNum == luhm) {
            return SUCCESS;// 验证通过
        } else {
            return ILLEGAL_NUMBER;
        }
    }

    /**
     * 当你输入信用卡号码的时候，有没有担心输错了而造成损失呢？其实可以不必这么担心，
     * 因为并不是一个随便的信用卡号码都是合法的，它必须通过Luhn算法来验证通过。该校验的过程：
     * 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，则将其减去9），再求和。
     * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。例如，卡号是：5432123456788881
     * 则奇数、偶数位（用红色标出）分布：5432123456788881奇数位和=35
     * 偶数位乘以2（有些要减去9）的结果：16261577，求和=35。最后35+35=70可以被10整除，认定校验通过。
     * 请编写一个程序，从键盘输入卡号，然后判断是否校验通过。通过显示：“成功”，否则显示“失败”。比如，用户输入：356827027232780
     * 程序输出：成功
     * <p>
     * 判断是否是银行卡号
     *
     * @param cardNo
     * @return
     * @author WJ
     */
    public static boolean checkBankCard(String cardNo) {
        char bit = getBankCardCheckCode(cardNo
                .substring(0, cardNo.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardNo.charAt(cardNo.length() - 1) == bit;

    }

    private static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null
                || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

}
