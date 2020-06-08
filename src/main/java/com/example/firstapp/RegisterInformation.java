package com.example.firstapp;

public class RegisterInformation {
    public String FullName,UserName,Email,Password,ConfirmPassword;
public RegisterInformation()
{

}

    public RegisterInformation(String fullName, String userName, String email,String password, String confirmPassword) {
        FullName = fullName;
        UserName = userName;
        Email = email;
        Password = password;
        ConfirmPassword = confirmPassword;
    }
}
