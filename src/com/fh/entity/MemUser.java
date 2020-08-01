package com.fh.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 功能描述：前台用户实体类, 实现序列化接口 为了Redis缓存
 *
 * @author Ajie
 * @date 2020年3月27日10:24:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemUser implements Serializable {

    /** 创建时间 */
    private String GMT_CREATE;
    /** 更新时间 */
    private String GMT_MODIFIED;
    /** 用户名 */
    private String USER_NAME;
    /** 登录密码 */
    private String LOGIN_PASSWORD;
    /** 安全密码 */
    private String SECURITY_PASSWORD;
    /** USDT钱包 */
    private double USDT_WALLET;
    /** 娱乐积分 */
    private double ENTERTAINMENT_SCORE;
    /** 分享基金 */
    private double SHARE_FUND;
    /** 推荐人数 */
    private int RECOMMENDED_NUMBER;
    /** 推荐人 */
    private String RECOMMENDER;
    /** 推荐路径 */
    private String RE_PATH;
    /** 代数 */
    private int ALGEBRA;
    /** 用户状态 0:正常、1：封号 */
    private int USER_STATE;
    /** 团队人数 */
    private int TEAM_NUMBER;
    /** 姓名 */
    private String NAME;
    /** 佣金点 */
    private double COMMISSION;
    /** 手机号 */
    private String PHONE;
    /** usdt收币地址 */
    private String USDT_IN_ADDRESS;
    /** 钱包收款二维码 */
    private String WALLET_QR_CODE;
    /** 是否为休息号 0:正常、1：休息 */
    private String IS_REST;
    /** USDT线上钱包地址 */
    private String USDT_ADDRESS;
    /** 用户级别 */
    private int USER_RANK;
    /** usdt线上钱包余额 */
    private double USDT_WALLET_ACTUAL;
    /** 密保问题 */
    private String SECURITY_QUESTION;
    /** 密保答案 */
    private String ANSWER;
    /** 邀请码 */
    private String INVITATION_CODE;
    /** 每天提现累积 */
    private int DAY_WITHDRAWALS_COUNT;
    /** ID */
    private String ACCOUNT_ID;

}
