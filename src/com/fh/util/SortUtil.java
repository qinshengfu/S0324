package com.fh.util;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import java.util.*;

/**
 * 说明：list排序
 * 创建人：FH Q313596790
 * 修改时间：2017年11月24日
 */
public class SortUtil {


    /**
     * 对list进行时间排序
     *
     * @param sortList 需要排序的list
     */
    public static List<PageData> sortTime(List<PageData> sortList) {
        /*//按照时间降序排序
        Collections.sort(sortList, new Comparator<PageData>() {
            @Override
            public int compare(PageData o1, PageData o2) {
                Date dt1 = Tools.str2Date(o1.getString("GMT_CREATE"));
                Date dt2 = Tools.str2Date(o2.getString("GMT_CREATE"));
                if (dt1.getTime() > dt2.getTime()) {
                    return -1;
                } else if (dt1.getTime() < dt2.getTime()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });*/

        //按照创建时间降序排序
        sortList.sort((o1, o2) -> {
            Date dt1 = Tools.str2Date(o1.getString("GMT_CREATE"));
            Date dt2 = Tools.str2Date(o2.getString("GMT_CREATE"));
            return Long.compare(Objects.requireNonNull(dt2).getTime(), Objects.requireNonNull(dt1).getTime());
        });
        return sortList;
    }

    /**
     * 对list进行排序
     *
     * @param sortList  需要排序的list
     * @param param1    排序的参数名称
     * @param orderType 排序类型：正序-asc；倒序-desc
     */
    @SuppressWarnings("unchecked")
    public static List<Object> sort(List<Object> sortList, String param1, String orderType) {
        Comparator<Object> mycmp1 = ComparableComparator.getInstance();
        if ("desc".equals(orderType)) {
            mycmp1 = ComparatorUtils.reversedComparator(mycmp1); //逆序（默认为正序）
        }

        ArrayList<Object> sortFields = new ArrayList<Object>();
        sortFields.add(new BeanComparator(param1, mycmp1)); //主排序（第一排序）

        ComparatorChain multiSort = new ComparatorChain(sortFields);
        Collections.sort(sortList, multiSort);

        return sortList;
    }

    /**
     * 对list进行排序
     *
     * @param sortList  需要排序的list
     * @param param1    排序的参数名称:参数长度
     * @param param2    排序的参数名称:排序参数
     * @param orderType 排序类型：正序-asc；倒序-desc
     */
    @SuppressWarnings("unchecked")
    public static List<Object> sortParam2(List<Object> sortList, String param1, String param2, String orderType) {
        Comparator<Object> mycmp1 = ComparableComparator.getInstance();
        Comparator<Object> mycmp2 = ComparableComparator.getInstance();
        if ("desc".equals(orderType)) {
            mycmp1 = ComparatorUtils.reversedComparator(mycmp1); //逆序（默认为正序）
        }

        ArrayList<Object> sortFields = new ArrayList<Object>();
        sortFields.add(new BeanComparator(param1, mycmp1)); //主排序（第一排序）
        sortFields.add(new BeanComparator(param2, mycmp2)); //主排序（第一排序）

        ComparatorChain multiSort = new ComparatorChain(sortFields);
        Collections.sort(sortList, multiSort);

        return sortList;
    }

}
