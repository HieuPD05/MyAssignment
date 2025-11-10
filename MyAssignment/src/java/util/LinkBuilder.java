package util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class LinkBuilder {
    // basePath ví dụ: request.getContextPath()
    public static String resetPasswordLink(String basePath, String token) {
        String t = URLEncoder.encode(token, StandardCharsets.UTF_8);
        return basePath + "/admin/password/reset?token=" + t;
    }
}
