package edu.uoc.epcsd.user.domain.service;

import edu.uoc.epcsd.user.domain.UserSession;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import javax.xml.bind.DatatypeConverter;


@Log4j2
@Service
public class TokenServiceImpl implements TokenService {

    private final String secretKey;

    public TokenServiceImpl(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String encrypt(UserSession session) throws GeneralSecurityException {
        SecretKeySpec aesKey = new SecretKeySpec(secretKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(session.getString().getBytes());
         return DatatypeConverter.printBase64Binary(encrypted);
    }

    public JSONObject decrypt(String token) throws GeneralSecurityException {
        SecretKeySpec aesKey = new SecretKeySpec(secretKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] decryptedBytes = cipher.doFinal(DatatypeConverter.parseBase64Binary(token));
        String decryptedString = new String(decryptedBytes);
        return new JSONObject(decryptedString);
    }
}
