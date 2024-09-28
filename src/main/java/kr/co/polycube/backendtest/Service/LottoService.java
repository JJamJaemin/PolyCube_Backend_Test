package kr.co.polycube.backendtest.Service;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

@Service
public class LottoService {

    public int[] generateLottoNumbers() {
        Set<Integer> numbers = new TreeSet<>();
        Random random = new Random();

        //로또번호 범위 지정
        while (numbers.size() < 6) {
            numbers.add(random.nextInt(45) + 1);
        }

        return numbers.stream().mapToInt(Integer::intValue).toArray();
    }
}