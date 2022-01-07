package com.example.seaker.business.services;

import com.example.seaker.MainActivity;
import com.example.seaker.business.BusinessFacade;
import com.example.seaker.business.ErrorType;
import com.example.seaker.database.DTOs.UserDTO;
import com.example.seaker.database.repositories.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService {
    private static UserService instance=null;
    private UserRepository userRepository;

    public UserService() {
        userRepository=new UserRepository();
    }

    public static UserService getInstance(){
        if(instance==null) instance=new UserService();
        return instance;
    }

    public void addUser(UserDTO user) {
        userRepository.add(user);
    }

    public ErrorType validateFields(UserDTO user) {

//        boolean missing = false;

        if (user.getName().equals("")) return ErrorType.NameMissing;
        else {
            if (containsNumbers(user.getName())) {
//                ((MainActivity) getActivity()).onButtonShowPopupWindowClick(getView(), "Name can't contain numbers!");
                return ErrorType.NameHasNumbers;
            }

            if (containsSpecialCharacters(user.getName())) {
//                ((MainActivity) getActivity()).onButtonShowPopupWindowClick(getView(), "Name can't contain special characters!");
                return ErrorType.NameHasSpecialCharacters;
            }
        }
        if (user.getEmail().equals("")) return ErrorType.EmailMissing;
        else {
            if (!isValidEmailAddress(user.getEmail())) {
//                ((MainActivity) getActivity()).onButtonShowPopupWindowClick(getView(), "Invalid email format!");
                return ErrorType.EmailNotValid;
            }
        }

        if (user.getPassword().equals("")) return ErrorType.PasswordMissing;
        if (user.getType().equals("")) return ErrorType.UserTypeNotSpecified;

//        if (missing) {
//            ((MainActivity) getActivity()).onButtonShowPopupWindowClick(getView(), "Required fields missing!");
//            return false;
//        }
//
//        return true;
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
}
