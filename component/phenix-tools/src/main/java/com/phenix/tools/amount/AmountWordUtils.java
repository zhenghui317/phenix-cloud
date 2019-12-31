package com.phenix.tools.amount;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * FileName: AmountWordUtils.java
 * Description: 账单金额转换为大写(工具类)
 *
 * @author zhenghui
 * @date 2019/2/25 17:37
 * @since JDK 1.8
 */
public class AmountWordUtils {

    private static String zero = "0";

    private static String doubleZero = "00";

    /**
     * 处理的最大数字达千万亿位 精确到分
     *
     * @param bigDecimal 账单金额
     * @return 金额的大写字符串
     * @throws Exception 抛出异常
     */
    public static String digitUppercase(BigDecimal bigDecimal) throws Exception {
        //标记是否为负数
        boolean isMinus = false;
        //如果是负数，取相反数再转换大写，返回时时加一个“负”字
        if (bigDecimal.compareTo(BigDecimal.ZERO) < 0) {
            bigDecimal = bigDecimal.negate();
            isMinus = true;
        }
        //标记是否不足一元
        boolean lessThanOne = false;
        //如果相反数不足一元，则加一，否则无法获取到整数部分
        if (bigDecimal.compareTo(BigDecimal.ONE) < 0) {
            bigDecimal = bigDecimal.add(BigDecimal.ONE);
            lessThanOne = true;
        }

        String[] digit = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};

        /*
         *      仟        佰        拾         ' '
         ' '    $4        $3        $2         $1
         万     $8        $7        $6         $5
         亿     $12       $11       $10        $9
         */

        //把钱数分成段,每四个一段,实际上得到的是一个二维数组
        String[] unit1 = {"", "拾", "佰", "仟"};
        //把钱数分成段,每四个一段,实际上得到的是一个二维数组
        String[] unit2 = {"元", "万", "亿", "万亿"};
        bigDecimal = bigDecimal.multiply(new BigDecimal(100));
        String strVal = String.valueOf(bigDecimal.toBigInteger());
        //整数部分
        String head;
        if (lessThanOne) {
            head = zero;
        } else {
            head = strVal.substring(0, strVal.length() - 2);
        }
        //小数部分
        String end = strVal.substring(strVal.length() - 2);
        String endMoney = "";
        StringBuilder headMoney = new StringBuilder();
        if (doubleZero.equals(end)) {
            endMoney = "整";
        } else {
            if (!zero.equals(end.substring(0, 1))) {
                endMoney += digit[Integer.valueOf(end.substring(0, 1))] + "角";
            } else if (zero.equals(end.substring(0, 1)) && !zero.equals(end.substring(1, 2))) {
                endMoney += "零角";
            }
            if (!zero.equals(end.substring(1, 2))) {
                endMoney += digit[Integer.valueOf(end.substring(1, 2))] + "分";
            }
        }
        char[] chars = head.toCharArray();
        //段位置是否已出现zero
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        //0连续出现标志
        boolean zeroKeepFlag = false;
        int vidxtemp = 0;
        for (int i = 0; i < chars.length; i++) {
            //段内位置  unit1
            int idx = (chars.length - 1 - i) % 4;
            //段位置 unit2
            int vidx = (chars.length - 1 - i) / 4;
            String s = digit[Integer.valueOf(String.valueOf(chars[i]))];
            if (!"零".equals(s)) {
                headMoney.append(s).append(unit1[idx]).append(unit2[vidx]);
                zeroKeepFlag = false;
            } else if (i == chars.length - 1 || map.get("zero" + vidx) != null) {
                headMoney.append("");
            } else {
                headMoney.append(s);
                zeroKeepFlag = true;
                //该段位已经出现0；
                map.put("zero" + vidx, true);
            }
            if (vidxtemp != vidx || i == chars.length - 1) {
                headMoney = new StringBuilder(headMoney.toString().replaceAll(unit2[vidx], ""));
                headMoney.append(unit2[vidx]);
            }
            if (zeroKeepFlag && (chars.length - 1 - i) % 4 == 0) {
                headMoney = new StringBuilder(headMoney.toString().replaceAll("零", ""));
            }
        }

        //如果不足一元，则在前面补充一个“零”字
        if (lessThanOne) {
            headMoney = new StringBuilder("零").append(headMoney);
        }
        //如果是负数，则在前面补充一个“负”字
        if (isMinus) {
            headMoney = new StringBuilder("负").append(headMoney);
        }
        return headMoney + endMoney;
    }
}

