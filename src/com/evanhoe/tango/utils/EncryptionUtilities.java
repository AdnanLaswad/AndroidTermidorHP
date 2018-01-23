package com.evanhoe.tango.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.apache.local.commons.codec.binary.Base64;


public class EncryptionUtilities
{
    private String rngAlgorithm = "SHA1PRNG";
    private String algorithm = "DESede";
    private String cipherTransformation = algorithm + "/ECB/PKCS5Padding";
    private String saltKeys[] = { "Foxtrot", "Limbo", "Pachanga", "Tango", "Waltz" };
    private final String salt = saltKeys[0].substring(0,0+2) +
                                saltKeys[4].substring(3,3+2) +
                                saltKeys[2].substring(2,2+4) +
                                saltKeys[1].substring(1,1+3) +
                                saltKeys[3].substring(2,2+2);


    private Cipher getCipher ( int encryptionMode )
        throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, NoSuchProviderException
    {
        Cipher myCipher;
        SecretKey myKey;
        KeyGenerator keygen;
        SecureRandom secureRandom;

        secureRandom = SecureRandom.getInstance ( rngAlgorithm, "Crypto" );
        secureRandom.setSeed ( salt.getBytes() );

        keygen = KeyGenerator.getInstance ( algorithm );
        keygen.init ( secureRandom );
        myKey = keygen.generateKey();

        myCipher = Cipher.getInstance ( cipherTransformation );
        myCipher.init ( encryptionMode, myKey );

        return ( myCipher );
    }

    public String encrypt ( String stringToEncrypt )
    {
        String returnString = null;

        try
        {
            Cipher encryptCipher = getCipher ( Cipher.ENCRYPT_MODE );
            byte[] encryptedBytes = encryptCipher.doFinal ( stringToEncrypt.getBytes() );

            returnString = Base64.encodeBase64String ( encryptedBytes );
        }
        catch ( NoSuchAlgorithmException nsae )
        {
            nsae.printStackTrace();
        }
        catch ( IllegalBlockSizeException ibse )
        {
            ibse.printStackTrace();
        }
        catch ( NoSuchPaddingException nspe )
        {
            nspe.printStackTrace();
        }
        catch ( BadPaddingException bpe )
        {
            bpe.printStackTrace();
        }
        catch ( InvalidKeyException ike )
        {
            ike.printStackTrace();
        }
        catch ( NoSuchProviderException nspe2 )
        {
            nspe2.printStackTrace();
        }

        return ( returnString );
    }

    public String decrypt ( String stringToDecrypt )
    {
        String returnString = null;

        try
        {
            Cipher decryptCipher = getCipher ( Cipher.DECRYPT_MODE );
            byte[] decryptedBytes = decryptCipher.doFinal ( Base64.decodeBase64 ( stringToDecrypt ) );

            returnString = new String ( decryptedBytes );
        }
        catch ( NoSuchAlgorithmException nsae )
        {
            nsae.printStackTrace();
        }
        catch ( IllegalBlockSizeException ibse )
        {
            ibse.printStackTrace();
        }
        catch ( NoSuchPaddingException nspe )
        {
            nspe.printStackTrace();
        }
        catch ( BadPaddingException bpe )
        {
            bpe.printStackTrace();
        }
        catch ( InvalidKeyException ike )
        {
            ike.printStackTrace();
        }
        catch ( NoSuchProviderException nspe2 )
        {
            nspe2.printStackTrace();
        }

        return ( returnString );
    }
}
