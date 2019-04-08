package com.example.customlibrary.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class ChosenMediaFile implements Parcelable {
    private String fileName;
    private String mimeType;
    private int width;
    private int height;
    private String uri;
    private String extType;
    private int isSelected;
    private long duration;
    private String album;

    public ChosenMediaFile() {

    }

    protected ChosenMediaFile(Parcel in) {
        this.width=in.readInt();
        this.height=in.readInt();
        this.uri=in.readString();
        this.extType=in.readString();
        this.mimeType=in.readString();
        this.isSelected=in.readInt();
        this.fileName=in.readString();
        this.duration=in.readLong();
        this.album=in.readString();


    }

    public static final Creator<ChosenMediaFile> CREATOR = new Creator<ChosenMediaFile>() {
        @Override
        public ChosenMediaFile createFromParcel(Parcel in) {
            return new ChosenMediaFile(in);
        }

        @Override
        public ChosenMediaFile[] newArray(int size) {
            return new ChosenMediaFile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(uri);
        dest.writeString(extType);
        dest.writeString(mimeType);
        dest.writeInt(isSelected);
        dest.writeString(fileName);
        dest.writeLong(duration);
        dest.writeString(album);
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getExtType() {
        return extType;
    }

    public void setExtType(String extType) {
        this.extType = extType;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
