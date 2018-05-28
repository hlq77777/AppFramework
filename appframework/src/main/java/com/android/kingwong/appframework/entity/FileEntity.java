package com.android.kingwong.appframework.entity;

/**
 * Created by KingWong on 2017/11/24.
 *
 */

public class FileEntity {
    private String file_path;
    private String file_key;

    public FileEntity(String file_key, String file_path){
        setFile_key(file_key);
        setFile_path(file_path);
    }

    public String getFile_path() {
        return file_path;
    }
    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getFile_key() {
        return file_key;
    }
    public void setFile_key(String file_key) {
        this.file_key = file_key;
    }
}
