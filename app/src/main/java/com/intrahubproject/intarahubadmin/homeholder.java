package com.intrahubproject.intarahubadmin;

public class homeholder {

   private String username, imageuri, adminstatas;

  public homeholder(){

  }

    public homeholder(String username, String imageuri, String adminstatas) {
        this.username = username;
        this.imageuri = imageuri;
        this.adminstatas = adminstatas;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public String getAdminstatas() {
        return adminstatas;
    }

    public void setAdminstatas(String adminstatas) {
        this.adminstatas = adminstatas;
    }
}
