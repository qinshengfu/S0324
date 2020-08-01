package com.fh.util;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * 功能描述：红包算法
 * @author Ajie
 * @date 2020/4/2 0002
 */
public class RedEnvelope {
    private int remain;//金额，单位厘
    private int count;//个数
    private Precision precision;//精度
    private int max;//上限，单位厘
    private int min;//下限，单位厘
    private int[] redPool;
    private int index;

    public static void main(String[] args) {
        getRedPackage(1000 * 100, 10, 200 * 100, 1 * 10);
    }

    /**
     * 拆分红包
     *  @param money 总钱数 （分）
     * @param count 红包个数
     * @param max   最大的红包（分）
     * @param min   最小红包（分）
     */
    public static List<Double> getRedPackage(int money, int count, int max, int min) {
        RedEnvelope redEnvelope = RedEnvelope.newInstance(money * Precision.FEN.getPre(), count, Precision.FEN, max * Precision.FEN.getPre(), min * Precision.FEN.getPre());
        List<Double> redPackageList = new LinkedList<>();
        BigDecimal bigDecimal = new BigDecimal(money);
        BigDecimal bigDecimal1 = new BigDecimal(100);
        int conut = 0;
        for (int i = 0; i < count; i++) {
            int money1 = redEnvelope.getRed();
            BigDecimal bigDecimal2 = new BigDecimal(money1);
            BigDecimal bigDecimal3 = new BigDecimal(1000);
            double lastMoney = bigDecimal2.divide(bigDecimal3).doubleValue();
            if (lastMoney <= 0) {
                lastMoney = 0;
            }
            conut ++;
            redPackageList.add(lastMoney);
//            System.out.println("拆分红包：" + lastMoney + "元");
        }
//        System.out.println("总钱数为:" + bigDecimal.divide(bigDecimal1) + "元\t" +" 个数：" + conut );
        return redPackageList;
    }

    public int getRed() {
        return index < count ? redPool[index++] : 0;
    }

    public static RedEnvelope newInstance(int money, int count, Precision precision, int max, int min) {
        RedEnvelope redEnvelope = new RedEnvelope(money, count, precision, max, min);
        String msg;
        if ("".equals(msg = redEnvelope.validate())) {
            return redEnvelope;
        } else {
            throw new RuntimeException(msg);
        }
    }

    private RedEnvelope(int money, int count, Precision precision, int max, int min) {
        this.remain = money;
        this.count = count;
        this.precision = precision;
        this.max = max;
        this.min = min;
        init();
    }

    private void init() {
        redPool = new int[count];
        int remain_ = remain;
        for (int i = 0; i < count - 1; i++) {
            int max = getRealMax(remain_, count - i);
            int min = getRealMin(remain_, count - i);
            int money = ((int) (Math.random() * (max - min + precision.getPre())) + min)
                    / precision.getPre() * precision.getPre();//[min, realMax]
            remain_ -= money;
            redPool[i] = money;
        }
        redPool[count - 1] = remain_;
        randomPool();
    }

    private void randomPool() {
        for (int i = 0; i < count; i++) {
            int index = (int) (Math.random() * count);
            int temp = redPool[i];
            redPool[i] = redPool[index];
            redPool[index] = temp;
        }
    }

    private int getRealMax(int remain, int count) {
        int calMax = remain - ((count - 1) * min);
        return Math.min(calMax, max);
    }

    private int getRealMin(int remain, int count) {
        int calMin = remain - ((count - 1) * max);
        return Math.max(calMin, min);
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public Precision getPrecision() {
        return precision;
    }

    public void setPrecision(Precision precision) {
        this.precision = precision;
    }

    private String validate() {
        String msg = "";
        if (remain <= 0) {
            msg = "余额不能为0";
        } else if (remain % precision.getPre() != 0) {
            msg = "余额的精度不对";
        } else if (count <= 0) {
            msg = "红包个数必须为正数";
        } else if (max % precision.getPre() != 0) {
            msg = "上限的精度不对";
        } /*else if (max <= min) {
            msg = "上限必须大于下限";
        }*/ else if (min % precision.getPre() != 0) {
            msg = "下限的精度不对";
        } /*else if (min <= 0) {
            msg = "下限必须大于0";
        } else if (getRealMax(remain, count) < getRealMin(remain, count)) {
            msg = "上下限设置错误";
        }*/
        return msg;
    }

}

enum Precision {
    LI(1),
    FEN(10),
    JIAO(100),
    YUAN(1000);

    private int pre;

    private Precision(int pre) {
        this.pre = pre;
    }

    public int getPre() {
        return pre;
    }
}