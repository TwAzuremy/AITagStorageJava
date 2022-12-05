package com.example.aitagstorage.tools;

import javax.servlet.http.HttpServletRequest;

public class PATH {
    public static String getUploadPath(HttpServletRequest request) {
        String path = request.getSession().getServletContext().getRealPath("/");
        return path.substring(0, path.indexOf("\\target")) + "\\uploadPic";
    }
}
