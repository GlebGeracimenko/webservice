package com.gleb.webservices.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class StringHelper {
    public static Map<String, String> splitQueryParams(String urlPart) {
        Map<String, String> map = new HashMap<String, String>();
        for (String param : urlPart.split("&")) {
            if (param == null) {
                continue;
            }
            param = param.trim();
            String[] paramPair = param.split("=");
            if (paramPair.length != 2) {
                continue;
            }
            String key = paramPair[0];
            String value = paramPair[1];
            map.put(key, value);
        }
        return map;
    }

    public static String createNewId(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().replace("-", "");
    }

    public static boolean isEmpty(String id) {
        if (id == null)
            return true;
        return id.trim().equals("");
    }

    public static boolean isRequestParamEmpty(Object obj) {
        if (obj == null)
            return true;
        return obj.toString().trim().equals("");
    }
}
