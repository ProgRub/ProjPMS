package com.example.seaker.business.services;

import com.example.seaker.business.ErrorType;
import com.example.seaker.database.DTOs.UserDTO;
import com.example.seaker.database.repositories.UserRepository;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService {
    private static UserService instance = null;
    private UserRepository userRepository;
    private String selectedRole;
    private long loggedInUserId;
    private String loggedInUsername;

    public UserService() {
        userRepository = new UserRepository();
    }

    public static UserService getInstance() {
        if (instance == null) instance = new UserService();
        return instance;
    }

    public void addUser(UserDTO user) {
        userRepository.add(user);
    }

    public ErrorType validateFields(UserDTO user) {
        if (user.getName().equals("")) return ErrorType.NameMissing;
        else {
            if (containsNumbers(user.getName())) {
                return ErrorType.NameHasNumbers;
            }

            if (containsSpecialCharacters(user.getName())) {
                return ErrorType.NameHasSpecialCharacters;
            }
        }
        if (user.getEmail().equals("")) return ErrorType.EmailMissing;
        else {
            if (!isValidEmailAddress(user.getEmail())) {
                return ErrorType.EmailNotValid;
            }
        }

        if (user.getPassword().equals("")) return ErrorType.PasswordMissing;
        if (user.getType().equals("")) return ErrorType.UserTypeNotSpecified;

        return ErrorType.NoError;
    }

    private boolean containsNumbers(String string) {
        return string.matches(".*\\d.*");
    }

    private boolean containsSpecialCharacters(String string) {
        Pattern p = Pattern.compile("[^A-zÀ-ú0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }

    private static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public Iterable<UserDTO> getTeamMembers() {
        ArrayList<UserDTO> teamMembers = new ArrayList<>();
        for (UserDTO user : userRepository.getAll()) {
            if (user.getType().equals("TeamMember")) teamMembers.add(user);
        }
        return teamMembers;
    }

    public Iterable<UserDTO> getCompanyManagers() {
        ArrayList<UserDTO> companyManagers = new ArrayList<>();
        for (UserDTO user : userRepository.getAll()) {
            if (user.getType().equals("CompanyManager")) companyManagers.add(user);
        }
        return companyManagers;
    }

    public UserDTO getUserByID(long id) {
        return userRepository.getById(id);
    }

    public Iterable<UserDTO> getUsers() {
        return userRepository.getAll();
    }

    public void deleteUser(long id) {
        userRepository.removeById(id);
    }

    public ErrorType verifyLogin(UserDTO loginCredentials) {
        if (loginCredentials.getEmail().equals("")) return ErrorType.EmailMissing;
        if(loginCredentials.getPassword().equals("")) return ErrorType.PasswordMissing;
        if(!isValidEmailAddress(loginCredentials.getEmail())) return ErrorType.EmailNotValid;
        if(!userRepository.loginAdminCompanyManager(loginCredentials)) return ErrorType.WrongLoginData;
        for (UserDTO user:getUsers()) {
            if(user.getEmail().equals(loginCredentials.getEmail()))
                loggedInUserId=user.getId();
                loggedInUsername =user.getName();
        }

        return ErrorType.NoError;
    }

    public String getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(String selectedRole) {
        this.selectedRole = selectedRole;
    }

    public long getLoggedInUserId() {
        return loggedInUserId;
    }

    public void setLoggedInUser(long userId) {
                loggedInUserId=userId;
    }

    public UserDTO getLoggedInUser() {
        for (UserDTO user:userRepository.getAll()) {
            if(user.getId()==loggedInUserId)
                return user;
        }
        return null;
    }

    public void setLoggedInUserName(String userName) {
        this.loggedInUsername =userName;
    }

    public String getLoggedInUsername() {
        return loggedInUsername;
    }

}
