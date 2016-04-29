package com.bojoy.bjsdk_mainland_new.support.http.config;

import java.io.File;

/**
 * Created by wutao on 2016/2/16.
 * 文件对象实体
 */
public class FileInput {

    public String key;
    public String filePath;
    public File file;

    public FileInput(String name, String filePath, File file) {
        this.key = name;
        this.filePath = filePath;
        this.file = file;
    }

    @Override
    public String toString() {
        return "FileInput{" +
                  "key='" + key + '\'' +
                  ", filename='" + filePath + '\'' +
                  ", file=" + file +
                  '}';
    }

}
