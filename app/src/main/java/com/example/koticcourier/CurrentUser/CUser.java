package com.example.koticcourier.CurrentUser;

public class CUser {
    private String userPhone;
    private int userID;
    private String Role;
    private static CUser currentCUser = null;

    public static CUser CurrentUser()
    {
        // To ensure only one instance is created
        if (currentCUser == null)
        {
            currentCUser = new CUser();
        }
        return currentCUser;
    }

    public static CUser getCurrentUser() {
        return currentCUser;
    }

    public static void setCurrentUser(CUser currentCUser) {
        CUser.currentCUser = currentCUser;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
