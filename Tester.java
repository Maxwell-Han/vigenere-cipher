import edu.duke.*;
import java.util.*;

public class Tester {
    // encrypt or decrypt from a stringified file
    public void testCaesarCipher () {
        int k = 4;
        CaesarCipher cc = new CaesarCipher(k);
        FileResource fr = new FileResource();
        String fileString = fr.asString();
        String encrypted = cc.encrypt(fileString);
        System.out.println("the regular file is " + fileString);
        System.out.println("the encrypted file is " + encrypted);
        String decrypted = cc.decrypt(encrypted);
        System.out.println("the decrypted file is " + decrypted);
    }

    // decrypt an encrypted string
    public void testCracker () {
        CaesarCracker cracker = new CaesarCracker();
        int k = 4;
        CaesarCipher cc = new CaesarCipher(k);
        FileResource fr = new FileResource();
        String fileString = fr.asString();
        String encrypted = cc.encrypt(fileString);
        String decryptedFromCracker = cracker.decrypt(encrypted);
        System.out.println("the cracker has decrypted the message to be " + decryptedFromCracker);
    }

    // encrypt and decrypt with vigenere cipher from stringified file
    public void testVigenere () {
        int[] keys = {17, 14, 12, 4};
        VigenereCipher vc = new VigenereCipher(keys);
        FileResource fr = new FileResource();
        String fileString = fr.asString();
        String encrypted = vc.encrypt(fileString);
        String decrypted = vc.decrypt(encrypted);
        System.out.println("the original message is " + fileString);
        System.out.println("the encrypted message is " + encrypted);
        System.out.println("the decrypted message is " + decrypted);
    }   

    public void testVigenereBreaker () {
        VigenereBreaker vb = new VigenereBreaker();
        String testString = "abcdefghijklm";
        String result = vb.sliceString(testString, 3, 4);
        System.out.println("testing sliceString result is "  + result);
    }

    public void testVBTryKeyLength () {
        VigenereBreaker vb = new VigenereBreaker();
        int[] keys = {5, 11, 20, 19, 4}; // flute is key
        VigenereCipher vc = new VigenereCipher(keys);

        FileResource fr = new FileResource();
        String fileString = fr.asString();
        int len = keys.length;
        int[] key = vb.tryKeyLength(fileString, len, 'e');
        for(int k : key) {
            System.out.println("found key " + k);
        }
    }
    
    public void practiceQuiz () {
        VigenereBreaker vb = new VigenereBreaker();
        FileResource fr = new FileResource("messages/secretmessage1.txt");
        int[] keys = vb.tryKeyLength(fr.asString(), 4, 'e');
        for(int k : keys) {
            System.out.println("found key is " + k);
        }
        
        vb.breakVigenere();
    }
    
    public void practiceQuiz2TestKeyLength () {
       VigenereBreaker vb = new VigenereBreaker();
       FileResource fr = new FileResource("messages/secretmessage2.txt");
       String fileString = fr.asString();
       int[] keys = vb.tryKeyLength(fileString, 38, 'e');
       VigenereCipher vc = new VigenereCipher(keys);
       String decrypted = vc.decrypt(fileString);
       FileResource dictionaryFile = new FileResource("dictionaries/English");
       HashSet dict = vb.readDictionary(dictionaryFile);
       int count = vb.countWords(decrypted, dict);
       System.out.println("found valid words: " + count);
    }
    
}