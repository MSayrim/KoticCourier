package com.example.koticcourier.Models;

public class User extends Base {

        private String phoneNumber;
        private String role;
        private boolean isOnline;

        public User(String role, boolean isOnline) {
                super();
                this.phoneNumber = phoneNumber;
                this.role = role;
                this.isOnline = isOnline;
        }

        public String getPhoneNumber() {
                return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
                this.phoneNumber = phoneNumber;
        }

        public String getRole() {
                return role;
        }

        public void setRole(String role) {
                this.role = role;
        }

        public boolean isOnline() {
                return isOnline;
        }

        public void setOnline(boolean online) {
                isOnline = online;
        }
}
