package com.armin.utility.bl.otp;

import com.armin.utility.object.SystemException;

public interface IOtpService {

    String generateOtp(String key) throws SystemException;

    boolean validateOtp(String key, String inputCode);

}
