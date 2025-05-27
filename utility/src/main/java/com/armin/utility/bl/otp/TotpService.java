package com.armin.utility.bl.otp;

import com.armin.utility.config.cloud.BaseApplicationProperties;
import org.apache.commons.codec.binary.Hex;
import org.kamranzafar.otp.OTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@Primary
@Service
public class TotpService implements IOtpService {

    private final BaseApplicationProperties applicationProperties;

    @Autowired
    public TotpService(BaseApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public String generateOtp(String key) {
        String base = String.valueOf(Instant.now().getEpochSecond() / 60);
        String hexKey = Hex.encodeHexString(key.getBytes());
        return OTP.generate("B" + hexKey, base, 5, "totp");
    }

    @Override
    public boolean validateOtp(String key, String inputCode) {
        long nowMinutes = Instant.now().getEpochSecond() / 60;
        String hexKey = Hex.encodeHexString(key.getBytes());
        for (int i = 0; i < applicationProperties.getIdentitySettings().getLockout().getMaxFailedAccessAttempts(); i++) {
            String otpCode = OTP.generate("B" + hexKey, String.valueOf(nowMinutes), 5, "totp");
            if (Objects.equals(otpCode, inputCode)) {
                return true;
            }
            nowMinutes--;
        }
        return false;
    }

}
