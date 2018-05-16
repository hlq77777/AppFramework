package com.android.kingwong.appframework.util.CodeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by KingWong on 2017/9/30.
 * 生成sign密钥
 */

public class CreateCodeUtil {

    public static String GetSign(TreeMap<String, String> map){
        String str = "";
        List<Map.Entry<String, String>> infoIds = new ArrayList<>(map.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return (o1.getKey().compareTo(o2.getKey()));
            }
        });
        for (int i = 0; i < infoIds.size(); i++){
            str += infoIds.get(i).getKey();
            str += infoIds.get(i).getValue();
        }
        return MD5Util.string2PHPMD5(str + "ba785c8d347b5841f261ef594b56aeb3");
    }
}
