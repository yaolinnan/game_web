/**
 * MD5Util.java	  V1.0   2013-5-27 下午2:38:35
 *
 * Copyright Talkweb Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.yln.game.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public final class MD5Util {
	private static final String MD5_ALGORITHM = "MD5";

	private MD5Util() {
	}

	private static MessageDigest getMD5DigestAlgorithm() throws NoSuchAlgorithmException {
		return MessageDigest.getInstance(MD5_ALGORITHM);
	}

	/**
	 * 
	 * 功能描述：将明文字节加密为密文字节
	 * 
	 * @param source
	 *            明文字节
	 * @return
	 * @throws NoSuchAlgorithmException
	 * 
	 */
	public static byte[] getMD5Digest(byte source[]) throws NoSuchAlgorithmException {
		return getMD5DigestAlgorithm().digest(source);
	}

	/**
	 * 
	 * 功能描述：将明文字符串加密为密文字节
	 * 
	 * @param source
	 * @return
	 * @throws NoSuchAlgorithmException
	 * 
	 */
	public static byte[] getMD5Digest(String source) throws NoSuchAlgorithmException {
		return getMD5Digest(source.getBytes());
	}

	/**
	 * 
	 * 功能描述：用16进制编码将明文字节加密为密文字符串
	 * 
	 * @param source
	 * @return
	 * @throws NoSuchAlgorithmException
	 * 
	 */
	public static String getMD5DigestHex(byte source[]) throws NoSuchAlgorithmException {
		return new String(Hex.encodeHex(getMD5Digest(source)));
	}

	/**
	 * 
	 * 功能描述：用16进制编码将明文字符串加密为密文字符串
	 * 
	 * @param source
	 * @return
	 * @throws NoSuchAlgorithmException
	 * 
	 */
	public static String getMD5DigestHex(String source) throws NoSuchAlgorithmException {
		return new String(Hex.encodeHex(getMD5Digest(source)));
	}

	/**
	 * 
	 * 功能描述：用Base64编码将明文字节加密为密文字符串
	 * 
	 * @param source
	 * @return
	 * @throws NoSuchAlgorithmException
	 * 
	 */
	public static String getMD5DigestBase64(byte source[]) throws NoSuchAlgorithmException {
		return new String(Base64.encodeBase64(getMD5Digest(source)));
	}

	/**
	 * 
	 * 功能描述：用Base64编码将明文字符串加密为密文字符串
	 * 
	 * @param source
	 * @return
	 * @throws NoSuchAlgorithmException
	 * 
	 */
	public static String getMD5DigestBase64(String source) throws NoSuchAlgorithmException {
		return new String(Base64.encodeBase64(getMD5Digest(source)));
	}

	/**
	 * 
	 * 功能描述：
	 * 
	 * @param args
	 * @throws NoSuchAlgorithmException
	 * 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException {
		System.out.println(getMD5DigestHex("abc"));
		System.out.println(getMD5DigestBase64("abc"));
	}
}
