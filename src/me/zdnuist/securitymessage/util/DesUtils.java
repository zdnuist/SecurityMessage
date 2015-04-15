package me.zdnuist.securitymessage.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DesUtils {
	// 算法名称
	private final static String DES = "DES";
	
	public final static int ENCRYPT_STATUE = -2; //加密状态
	public final static int NONE_ENCRYPT_STATUE = -1; //非加密状态
	
	public final static String ENCRYPT_PWD = "me@zdnu1st!#";

	public static void test() throws Exception {
		String data = "{                                     "
				+ "	\"sms\":[                                  "
				+ "		{                                    "
				+ "		 \"body\":\"短信内容\",                  "
				+ "		 \"address\":\"号码\",                   "
				+ "		 \"type\":\"1为收, 2为发\",              "
				+ "		 \"date\":\"时间\",                      "
				+ "		 \"person\":\"收件人|发件人\"             "
				+ "		 \"longt_lat\":\"经度&维度\"             "
				+ "		}                                    "
				+ "	                                         "
				+ "	]                                        "
				+ "}                                          ";
		byte[] key = generateKeystore();
		System.err.println(encrypt(data.getBytes(), key));
		System.err.println(new String(decrypt(encrypt(data.getBytes(), key),
				key)));

	}

	/**
	 * Description 根据键值进行加密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(DES);

		// 用密钥初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}

	/**
	 * Description 根据键值进行解密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(DES);

		// 用密钥初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}

	/**
	 * 生成密钥
	 * 
	 * @return
	 */
	private static byte[] generateKeystore() {
		KeyGenerator keyGenerator;
		try {
			keyGenerator = KeyGenerator.getInstance(DES);
			keyGenerator.init(56);
			SecretKey secretKey = keyGenerator.generateKey();
			return secretKey.getEncoded();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
