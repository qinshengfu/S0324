package com.fh.util;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：负责传入一个大的集合，根据页号返回所需要的数据
 * @author Ajie
 * @date 2019/12/13 0013
 */
@Data
public class Pager<T> {
    private List<T> bigList; // 大的集合，外界传入
    private int curentPageIndex = 1; // 当前页号，外界传入
    private int countPerpage = 2; // 每页条数，外界可以设定
    private List<T> smallList; // 小的集合，返回
    private int pageCount; // 页数
    private int recordCount; // 记录条数
    private int prePageIndex; // 上一页序号
    private int nextPageIndex; // 下一页序号
    private boolean firstPage; // 是否第一页
    private boolean lastPage; // 是否最后一页

    public void setCurentPageIndex(int curentPageIndex) { // 每当页数改变，都会调用这个函数，筛选代码可以写在这里
        this.curentPageIndex = curentPageIndex;

        // 上一页，下一页确定
        prePageIndex = curentPageIndex - 1;
        nextPageIndex = curentPageIndex + 1;
        // 是否第一页，最后一页
        if (curentPageIndex == 1) {
            firstPage = true;
        } else {
            firstPage = false;
        }
        if (curentPageIndex == pageCount) {
            lastPage = true;
        } else {
            lastPage = false;
        }
        // 筛选工作
        smallList = new ArrayList<T>();
        for (int i = (curentPageIndex - 1) * countPerpage; i < curentPageIndex
                * countPerpage
                && i < recordCount; i++) {
            smallList.add(bigList.get(i));
        }
    }

    public int getCurentPageIndex() {
        return curentPageIndex;
    }

    public List<T> getBigList() {
        return bigList;
    }

    /**
     * 功能描述：传入完整的列表
     * @author Ajie
     * @date 2019/12/13 0013
     * @param bigList 大集合（所有数据）
     */
    public void setBigList(List<T> bigList) {
        this.bigList = bigList;
        // 计算条数
        recordCount = bigList.size();
        // 计算页数
        if (recordCount % countPerpage == 0) {
            pageCount = recordCount / countPerpage;
        } else {
            pageCount = recordCount / countPerpage + 1;
        }

        // 筛选工作
        smallList = new ArrayList<T>();
        for (int i = (curentPageIndex - 1) * countPerpage; i < curentPageIndex
                * countPerpage
                && i < recordCount; i++) {
            smallList.add(bigList.get(i));
        }

    }

}

