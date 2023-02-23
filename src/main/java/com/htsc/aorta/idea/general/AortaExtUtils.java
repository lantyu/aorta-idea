package com.htsc.aorta.idea.general;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AortaExtUtils {

    public static List<String> analyseDubboInterfaces(String toBeAnalyseText) {
        List<String> rList = new ArrayList<>();
        if (Strings.isNullOrEmpty(toBeAnalyseText)) {
            return rList;
        }
        Pattern regPattern = Pattern.compile("interface=\"(.+?)\"");
        Matcher matcher = regPattern.matcher(toBeAnalyseText);
        while (matcher.find()) {
            rList.add(matcher.group(1));
        }
        return rList;
    }

    public static String formatJsonArray(String text) {
        if (StringUtils.isNotEmpty(text)) {
            return text.replace(",", "\n");
        }
        return "";
    }
}
