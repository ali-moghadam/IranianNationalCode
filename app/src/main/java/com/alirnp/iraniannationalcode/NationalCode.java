package com.alirnp.iraniannationalcode;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

class NationalCode {

    protected static boolean specialCasesThatAreNotNationalCode(String code) {
        String[] notNationalCode = {"1111111111", "2222222222", "3333333333", "4444444444", "5555555555", "6666666666", "7777777777", "8888888888", "9999999999", "0000000000"};
        List<String> listOfNotNationalCode = Arrays.asList(notNationalCode);
        return listOfNotNationalCode.contains(code);
    }

    protected static boolean validationNationalCode(String code) {

        int length = 10;

        // check length
        if (code.length() != length)
            return false;

        // check exceptions
        if (specialCasesThatAreNotNationalCode(code))
            return false;

        long nationalCode = Long.parseLong(code);
        byte[] arrayNationalCode = new byte[length];

        //extract digits from number
        for (int i = 0; i < length; i++) {
            arrayNationalCode[i] = (byte) (nationalCode % length);
            nationalCode = nationalCode / length;
        }

        // check the control digit
        int sum = 0;
        for (int i = 9; i > 0; i--)
            sum += arrayNationalCode[i] * (i + 1);

        int temp = sum % 11;
        if (temp < 2)
            return arrayNationalCode[0] == temp;
        else
            return arrayNationalCode[0] == 11 - temp;
    }

    protected static Long generateCode() {
        int code;
        int min = 111111111;
        int max = 999999999;
        Random r = new Random();
        code = r.nextInt((max - min) + 1) + min;

        int index = 2;
        int number, temp;
        int sum = 0;

        temp = code;
        while (temp > 0) {
            number = temp % 10;
            temp = temp / 10;
            sum += number * index;
            index++;
        }

        int securityCode;
        int left = sum % 11;

        if (left < 2)
            securityCode = left;
        else
            securityCode = 11 - left;

        String finalCode = String.valueOf(code) + securityCode;

        return Long.parseLong(finalCode);
    }
}
