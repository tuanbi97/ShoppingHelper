package com.tuanbi97.miniproject1;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by User on 6/19/2017.
 */
public final class Data {
    public static ArrayList<CustomPlace> listTV;
    public static ArrayList<CustomPlace> listBottle;
    public static void createData(){
        listTV = new ArrayList<CustomPlace>();
        listTV.add(new CustomPlace("Dien may Thien Hoa",10.777846, 106.680603));
        listTV.add(new CustomPlace("Dien may Hoang Kim", 10.774387, 106.665408));
        listTV.add(new CustomPlace("Dien may xanh", 10.785751, 106.667022));
        listTV.add(new CustomPlace("Dien may Cho Lon", 10.786320, 106.666367));
        listTV.add(new CustomPlace("May Tinh Phong Vu", 10.773324, 106.689546));
        listTV.add(new CustomPlace("Dien may Gia Thanh", 10.753959, 106.676519));
        listTV.add(new CustomPlace("Supermarket Electric", 10.758116, 106.688882));
        listTV.add(new CustomPlace("Dien may Anh Ngan", 10.773606, 106.702617));
        listBottle = new ArrayList<CustomPlace>();
        listBottle.add(new CustomPlace("Dai ly Bia nuoc ngot Han Nghi", 10.756781, 106.678480));
        listBottle.add(new CustomPlace("Nuoc Giai Khat DELTA", 10.771574, 106.691721));
        listBottle.add(new CustomPlace("Giai Khat Ngoc Mai", 10.757316, 106.703870));
        listBottle.add(new CustomPlace("Bia nuoc ngot Quang Thinh", 10.764452, 106.694494));
        listBottle.add(new CustomPlace("Nuoc Giai Khat TRIBECO", 10.783873, 106.682874));
        listBottle.add(new CustomPlace("Giai Khat Chuong Duong", 10.755592, 106.688340));
        listBottle.add(new CustomPlace("Bia Nuoc Ngot Phuong Mai", 10.764839, 106.705803));
    }
}
