package com.nicbit.traquer.stryker.Models;

import android.graphics.Bitmap;

import java.io.File;

public class SelectedImage {

    public File file;
    public Bitmap bitmap;
    public String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
