package com.fh.util;

import java.util.Arrays;

/**
 * 功能描述：以太币 不知道干嘛的
 * @author Ajie
 * @date 2019/11/8 0008
 */
public class Etheric {
	/*
	 * 哈希
	 */
	private String hash;
	/*
	 * 公钥
	 */
	private String publicKey;
	/*
	 * 私钥
	 */
	private String privateKey;
	/*
	 * 操作类型
	 */
	private String type;

	/*
	 * 钱包地址
	 */
	private String address;
	/*
	 * 转出钱包地址
	 */
	private String fromAddress;

	/*
	 * 目标钱包地址
	 */
	private String toAddress;
	/*
	 * 钱包数组
	 */
	private String[] addreses;
	/*
	 * gas价格
	 */
	private String gasPrice;

	/*
	 * gas数量
	 */
	private String gasLimit;
	/*
	 * 交易序号
	 */
	private String nonce;
	/*
	 * 验证随机数
	 */

	/*
	 * 转账金额
	 */
	private String value;
	/*
	 * 助记词
	 */

	/**
	 * 输入交易模块的信息
	 */
	private String data;

	/*
	 * 合约地址
	 */
	private String contractAddress;

	/*
	 * 秘码
	 */
	private String password;
	
	/*
	 * 秘钥文件
	 * */
	private byte[] keystore;
	
	/*
	 * 秘钥文件名
	 * */
	private String filename;
	/*
	 * 教秘状态
	 * */
	private boolean decryption;
	
	/*
	 * 文件名
	 * */
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public byte[] getKeystore() {
		return keystore;
	}

	public void setKeystore(byte[] keystore) {
		this.keystore = keystore;
	}

	public boolean getDecryption() {
		return decryption;
	}

	public void setDecryption(boolean decryption) {
		this.decryption = decryption;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String[] getAddreses() {
		return addreses;
	}

	public void setAddreses(String[] addreses) {
		this.addreses = addreses;
	}

	public String getGasPrice() {
		return gasPrice;
	}

	public void setGasPrice(String gasPrice) {
		this.gasPrice = gasPrice;
	}

	public String getGasLimit() {
		return gasLimit;
	}

	public void setGasLimit(String gasLimit) {
		this.gasLimit = gasLimit;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getContractAddress() {
		return contractAddress;
	}

	public void setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
	}

	@Override
	public String toString() {
		return "Etheric [hash=" + hash + ", publicKey=" + publicKey + ", privateKey=" + privateKey + ", type=" + type
				+ ", address=" + address + ", fromAddress=" + fromAddress + ", toAddress=" + toAddress + ", addreses="
				+ Arrays.toString(addreses) + ", gasPrice=" + gasPrice + ", gasLimit=" + gasLimit + ", nonce=" + nonce
				+ ", value=" + value + ", data=" + data + ", contractAddress=" + contractAddress + ", password="
				+ password + ", keystore=" + keystore + ", filename=" + filename + ", decryption=" + decryption + "]";
	}

}
