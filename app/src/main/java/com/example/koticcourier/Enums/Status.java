package com.example.koticcourier.Enums;

import com.example.koticcourier.R;

import java.util.LinkedHashMap;
import java.util.Map;


public enum Status {

    OnConfirm(100,"Onay Bekliyor"),
    OnProgress(200,"Hazırlanıyor"),
    Success(300,"Teslim Edildi"),
    Complete(404,"Tamamlandı"),
    Decline(450,"Red edildi"),
    Canceled(400,"İptal Edildi");
    private int type;
    private String title;

    Status (int i,String t)
    {
        this.type = i;this.title = t;
    }

    public String getTitle(){
        return title;
    }

    public int getNumericType()
    {
        return type;
    }

    public static String getNameByCode(int code){
        for(Status e : Status.values()){
            if(code == e.type) return e.name();
        }
        return null;
    }
    private static final Map<Integer, Status> BY_CODE_MAP = new LinkedHashMap<> ();
    static {
        for (Status rae : Status.values()) {
            BY_CODE_MAP.put(rae.type, rae);
        }
    }
    public static Status forCode(int code) {
        return BY_CODE_MAP.get(code);
    }
}
