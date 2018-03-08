package io.github.andyradionov.udacpopularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Andrey Radionov
 */

public class MovieYoutubeTrailer implements Parcelable {

    private String name;
    private String size;
    private String source;
    private String type;

    protected MovieYoutubeTrailer(Parcel in) {
        name = in.readString();
        size = in.readString();
        source = in.readString();
        type = in.readString();
    }

    public static final Creator<MovieYoutubeTrailer> CREATOR = new Creator<MovieYoutubeTrailer>() {
        @Override
        public MovieYoutubeTrailer createFromParcel(Parcel in) {
            return new MovieYoutubeTrailer(in);
        }

        @Override
        public MovieYoutubeTrailer[] newArray(int size) {
            return new MovieYoutubeTrailer[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(size);
        dest.writeString(source);
        dest.writeString(type);
    }
}
