package com.example.testing;


import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class user {
    public String email,userid;

 public user() {
 }

 public user(String userid, String email){
     this.userid=userid;
     this.email=email;
 }

    public String getEmail() {
        return email;
    }


    public String getUserid() {
        return userid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result= new HashMap<>();
        result.put("userid", userid);
        result.put("email", email);
        return result;


    }

}