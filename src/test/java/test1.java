import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class test1 {


//    @Autowired
//    private Service2 service2;

    public static List<Long> maximumEvenSplit(long finalSum) {
        List<Long> result = new ArrayList<>();
        if(finalSum%2 != 0){
            return result;
        }
        long current = 2;
        while(finalSum >= current){
            result.add(current);
            finalSum  -= current;
            current += 2;
        }
        Long last = result.get(result.size() - 1);
        result.remove(last);
        result.add(finalSum +last);
        return result;
    }

    public static void main(String[] args) {
        List<Long> list = maximumEvenSplit(28);
        System.out.println(list);
    }

    @Test
    public void test2(){
//        System.out.println(service2);
    }
}
