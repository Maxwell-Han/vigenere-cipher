import java.util.*;
import edu.duke.*;

public class VigenereBreaker {
    public String sliceString(String message, int whichSlice, int totalSlices) {
        StringBuilder sb = new StringBuilder();
        for(int i = whichSlice; i < message.length(); i += totalSlices) {
            sb.append(message.charAt(i));
        }
        return sb.toString();
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        CaesarCracker cc = new CaesarCracker('e');
        int foundKey;
        for(int i  = 0; i < klength; i++) {
            foundKey = cc.getKey(sliceString(encrypted, i, klength));
            key[i] = foundKey;
        }
        return key;
    }

    public void breakVigenere () {
        FileResource fr = new FileResource();
        String fileString = fr.asString(); // file should be encrypted already
        int klen = 4;
        int[] keys = tryKeyLength(fileString, klen, 'e');
        VigenereCipher vCipher = new VigenereCipher(keys);
        String decrypted = vCipher.decrypt(fileString);
        System.out.println("the decrypted message from vigenere cipher is " + decrypted);
    }
    
    // reads from dictionary file which is one word per line
    public HashSet readDictionary (FileResource fr) {
        HashSet<String> set = new HashSet<String>();
        for(String line : fr.lines() ) {
            set.add(line.toLowerCase());
        }
        return set;
    }

    public int countWords (String message, HashSet dict) {
        ArrayList<String> words = new ArrayList<String>(Arrays.asList(message.split("\\W")));
        int count = 0;
        for(String word : words) {
            if(dict.contains(word.toLowerCase())) {
                count++;
            }
        }
        return count;
    }

    public String breakForLanguage(String encrypted, HashSet<String> dict) {
        int maxCount = 0;
        int[] keys = {};
        int kLen = 0;
        String decrypted = new String();

        for(int i = 1; i < 100; i++) {
            keys = tryKeyLength(encrypted, i, 'e');
            VigenereCipher vCipher = new VigenereCipher(keys);
            String currentDecrypted = vCipher.decrypt(encrypted);
            int currCount = countWords(currentDecrypted, dict);
            if(currCount > maxCount) {
                maxCount = currCount;
                decrypted = currentDecrypted;
                kLen = i;
            }
        }

        System.out.println("most words found: " + maxCount);
        System.out.println("key length: " + kLen);
        System.out.println("the keys are :");
        for (int k : keys) {
            System.out.println(k);
        }
        return decrypted;
    }

    public void breakVigenereUnknownLength () {
        FileResource fr = new FileResource();
        String fileString = fr.asString(); // file should be encrypted already
        FileResource dictionaryFile = new FileResource("dictionaries/English");
        HashSet dict = readDictionary(dictionaryFile);
        String result = breakForLanguage(fileString, dict);
        System.out.println("The decrypted message is ");
        System.out.println(result.substring(0, 200));
    }
}
