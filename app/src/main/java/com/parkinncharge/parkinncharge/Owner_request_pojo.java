package com.parkinncharge.parkinncharge;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Owner_request_pojo {
    String name,phone;
    @ServerTimestamp
    Date register_date;
    @DocumentId
    String id;

    public Owner_request_pojo() {
    }

    public Owner_request_pojo(String name, String phone) {
        this.name = name;
        this.phone = phone;
        //this.register_date=registerDate;
        //this.id = id;
    }

    public Date getRegister_date() {
        return register_date;
    }

    public void setRegister_date(Date register_date) {
        this.register_date = register_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
