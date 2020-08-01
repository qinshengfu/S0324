import com.fh.util.LotteryUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LotteryTest {

    static final int TIME = 100000;

    public static void iteratorMap(Map<Integer, Integer> map, List<Double> list){
        for(Map.Entry<Integer, Integer> entry : map.entrySet()){
            int index = entry.getKey();
            int time  = entry.getValue();
            Result result = new Result(index, TIME, time, list.get(index));
            System.out.println(result);
        }
    }

    public static void main(String[] args) {
        //构造概率集合
        List<Double> list = new ArrayList<Double>();
        list.add(40d);
        list.add(30d);
        list.add(28d);
        list.add(0.1d);
        list.add(1d);
        list.add(0.395d);
        list.add(0.005d);
        list.add(0d);
        System.out.println(list);


        LotteryUtil ll = new LotteryUtil(list);
        double sumProbability = ll.getMaxElement();

        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for(int i = 0; i < TIME; i++){
            // 开始抽奖
            int index = ll.randomColunmIndex();
            // 统计中奖次数
            if(map.containsKey(index)){
                map.put(index, map.get(index) + 1);
            }else{
                map.put(index, 1);
            }
        }

        for(int i = 0; i < list.size(); i++){
            double probability = list.get(i) / sumProbability;
            list.set(i, probability);
        }
        iteratorMap(map, list);

    }

}