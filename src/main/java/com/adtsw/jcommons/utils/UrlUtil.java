package com.adtsw.jcommons.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class UrlUtil {

    public static Map<String, String> getQueryParams(String url) {
        Map<String, String> params = new HashMap<>();
        if(StringUtils.isNotEmpty(url)) {
            String[] splits = url.split("\\?");
            if(splits.length > 1) {
                String[] queryParams = splits[1].split("&");
                if(queryParams.length > 0) {
                    for (String queryParam : queryParams) {
                        String[] paramValue = queryParam.split("=");
                        params.put(paramValue[0], paramValue[1]);
                    }
                }
            }
        }
        return params;
    }

    public static List<String> getPathParams(String url) {
        List<String> params = new ArrayList<>();
        if(StringUtils.isNotEmpty(url)) {
            String[] splits = url.split("\\?");
            String urlPath = splits[0];
            String[] pathParams = urlPath.split("\\/");
            if(pathParams.length > 0) {
                for (String param : pathParams) {
                    params.add(param);
                }
            }
        }
        return params;
    }
}
