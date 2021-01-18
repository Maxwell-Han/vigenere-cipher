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

    public int countWords (String message, HashSet, dict) {
        ArrayList<String> words = new ArrayList<String>(Arrays.asList(message.split("\\W")));
        int count = 0;
        for(String word : words) {
            if(dict.contains(word.toLowerCase())) {
                count++;
            }
        }
        return count;
    }

    
}
