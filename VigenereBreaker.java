import java.util.*;
import edu.duke.*;

public class VigenereBreaker {
    public HashMap<String,HashSet<String>> languages = new HashMap<String,HashSet<String>>();

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
            keys = tryKeyLength(encrypted, i, mostCommonCharIn(dict));
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
        StringBuilder keySB = new StringBuilder();
        for (int k : keys) {
            keySB.append(k);
        }
        System.out.println("the keys are " + keySB);
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

    public char mostCommonCharIn (HashSet<String> dict)  {
        HashMap<Character, Integer> freqs = new HashMap<Character, Integer>();
        int maxCount = 0;
        Character result = null;
        for(String word : dict) {
            for(int i  = 0; i < word.length(); i++) {
                Character ch = word.charAt(i);
                if(!freqs.containsKey(ch)) {
                    freqs.put(ch, 1);
                } else {
                    freqs.put(ch, freqs.get(ch) + 1);
                }
                if(freqs.get(ch) > maxCount) {
                    maxCount = freqs.get(ch);
                    result = ch;
                }
            }
        }
        return result;
    }

    public String breakForAllLangs(String encrypted, HashMap<String,HashSet<String>> languages) {
        String result = new String();
        int max = 0;
        String lang = new String();

        for(String language : languages.keySet()) {
            HashSet<String> currDict = languages.get(language);
            String decrypted =  breakForLanguage(encrypted, currDict);
            int freqs = countWords(decrypted, currDict);
            System.out.println("trying with language: " + language);
            if(freqs > max) {
                max = freqs;
                result = decrypted;
                lang = language;
            }
        }

        System.out.println("Found language is " + lang);
        System.out.println("Max words found is " + max);
        return result;
    }

    public void breakVigenereUnknownLang () {
        FileResource fr = new FileResource();
        String fileString = fr.asString(); // file should be encrypted already
        String result = new String();

        HashSet<String> dictionaryWords = new HashSet<String>();
        String [] labels = {"Danish","Dutch","English","French","German","Italian","Portuguese","Spanish"};
        for(String s : labels) {
            FileResource dictFr = new FileResource("dictionaries/"+ s);
            dictionaryWords = readDictionary(dictFr);
            // add dict content to class var languages
            languages.put(s,dictionaryWords);
        }
        result = breakForAllLangs(fileString, languages);
        System.out.println("The decrypted message is ");
        System.out.println(result.substring(0, 200));
    }
}
