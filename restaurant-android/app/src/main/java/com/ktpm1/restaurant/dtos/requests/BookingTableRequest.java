package com.ktpm1.restaurant.dtos.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingTableRequest implements Parcelable {
    List<Long> tableIds;
    String startTime;
    int additionalTime = 0;
    boolean addCart;

    protected BookingTableRequest(Parcel in) {
        tableIds = new ArrayList<>();
        in.readList(tableIds, Long.class.getClassLoader());
        startTime = in.readString();
        additionalTime = in.readInt();
        addCart = in.readByte() != 0;
    }

    public static final Creator<BookingTableRequest> CREATOR = new Creator<BookingTableRequest>() {
        @Override
        public BookingTableRequest createFromParcel(Parcel in) {
            return new BookingTableRequest(in);
        }

        @Override
        public BookingTableRequest[] newArray(int size) {
            return new BookingTableRequest[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(tableIds);
        dest.writeString(startTime);
        dest.writeInt(additionalTime);
        dest.writeByte((byte) (addCart ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
