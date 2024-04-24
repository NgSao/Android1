package com.nguyensao.myapplication;

public class User {
    static private int id;
    static private String namewelcome = "admin";
    static private String emailUser;
    static private String phoneUser;
    static private String passUser;
    static private String adressUser;

    public static String getAdressUser() {
        return adressUser;
    }

    public static void setAdressUser(String adressUser) {
        User.adressUser = adressUser;
    }

    public static String getEmailUser() {
        return emailUser;
    }

    public static void setEmailUser(String emailUser) {
        User.emailUser = emailUser;
    }

    public static String getPhoneUser() {
        return phoneUser;
    }

    public static void setPhoneUser(String phoneUser) {
        User.phoneUser = phoneUser;
    }
    public static String getPassUser() {
        return passUser;
    }

    public static void setPassUser(String passUser) {
        User.passUser = passUser;
    }




    private String username;

    public static String getNamewelcome() {
        return namewelcome;
    }

    public static void setNamewelcome(String namewelcome) {
        User.namewelcome = namewelcome;
    }

    private  String password;
    private String photo;
    private String numphone;
    private  String email;

    public User(String username, String password, String photo,String numphone ,String email) {
        this.username = username;
        this.password = password;
        this.photo = photo;
        this.numphone = numphone;
        this.email = email;
    }
    public static int getId() {
        return id;
    }
    public static void setId(int id) {
        User.id = id;
    }

    public  String getEmail() {
        return email;
    }

    public  void  setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getNumphone() {
        return numphone;
    }

    public void setNumphone(String numphone) {
        this.numphone = numphone;
    }




}
