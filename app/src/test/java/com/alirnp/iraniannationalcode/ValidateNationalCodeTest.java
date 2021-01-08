package com.alirnp.iraniannationalcode;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ValidateNationalCodeTest {

    @Deprecated  //this method have a bug :)
    private static boolean validateCodeOLD(long code) {

        int len = (int) Math.log10(code) + 1;
        int sum = 0;
        int securityCode;
        long number;
        long temp;
        int lastNumber = 0;

        if (len == 10) {

            int index = 1;
            temp = code;

            while (temp > 0) {
                number = temp % 10;
                temp = temp / 10;

                if (index == 1) {
                    lastNumber = (int) number;
                } else {
                    sum += number * (index);
                }
                index++;
            }

            int left = sum % 11;

            securityCode = (left < 2) ? left : 11 - left;

            return securityCode == lastNumber;
        }


        return false;
    }

    @Test
    public void validation1() {
        assertTrue(NationalCode.validationNationalCode("4369134021"));

    }

    @Test
    public void validation2() {
        assertFalse(NationalCode.validationNationalCode("6268103009"));
    }

    @Test
    public void validation3() {
        assertFalse(NationalCode.validationNationalCode("1233627066"));
    }

}