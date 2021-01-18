import edu.duke.*;
import java.util.*;

public class Tester {
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
}