package kr.co.polycube.backendtest;

import kr.co.polycube.backendtest.Batch.LottoWinnerCheckBatch;
import kr.co.polycube.backendtest.Model.Lotto;
import kr.co.polycube.backendtest.Model.Winner;
import kr.co.polycube.backendtest.Repository.LottoRepository;
import kr.co.polycube.backendtest.Repository.WinnerRepository;
import kr.co.polycube.backendtest.Service.LottoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LottoWinnerCheckBatchIntegrationTest {

    @Autowired
    private LottoWinnerCheckBatch lottoWinnerCheckBatch;

    @Autowired
    private LottoRepository lottoRepository;

    @Autowired
    private WinnerRepository winnerRepository;

    @MockBean
    private LottoService lottoService;

    @BeforeEach
    void setUp() {
        winnerRepository.deleteAll();
        lottoRepository.deleteAll();

        // 테스트용 로또 번호 생성
        Lotto lotto1 = new Lotto();
        lotto1.setNumber1(1);
        lotto1.setNumber2(2);
        lotto1.setNumber3(3);
        lotto1.setNumber4(4);
        lotto1.setNumber5(5);
        lotto1.setNumber6(6);
        lottoRepository.save(lotto1);

        Lotto lotto2 = new Lotto();
        lotto2.setNumber1(10);
        lotto2.setNumber2(20);
        lotto2.setNumber3(30);
        lotto2.setNumber4(40);
        lotto2.setNumber5(41);
        lotto2.setNumber6(42);
        lottoRepository.save(lotto2);

        // 당첨 번호 설정
        when(lottoService.generateLottoNumbers()).thenReturn(new int[]{1, 2, 3, 4, 5, 6});
    }

    @Test
    void testCheckWinners() {
        System.out.println("배치 작업 시작 전 로또 수: " + lottoRepository.count());
        System.out.println("배치 작업 시작 전 당첨자 수: " + winnerRepository.count());

        // 배치 작업 실행
        lottoWinnerCheckBatch.checkWinners();

        List<Winner> winners = winnerRepository.findAll();
        System.out.println("배치 작업 후 당첨자 수: " + winners.size());

        assertFalse(winners.isEmpty(), "당첨자가 없습니다.");

        for (Winner winner : winners) {
            System.out.println("당첨자 정보 - ID: " + winner.getId() + ", 순위: " + winner.getRank() +
                    ", 로또 번호: " + winner.getLotto().getNumber1() + ", " + winner.getLotto().getNumber2() +
                    ", " + winner.getLotto().getNumber3() + ", " + winner.getLotto().getNumber4() +
                    ", " + winner.getLotto().getNumber5() + ", " + winner.getLotto().getNumber6());
        }

        // 1등 당첨자 확인
        Winner firstPrizeWinner = winners.stream()
                .filter(w -> w.getRank() == 1)
                .findFirst().orElse(null);
        assertNotNull(firstPrizeWinner, "1등 당첨자가 없습니다."); //firstprizewinner가 null이면 메시지 출력
        System.out.println("1등 당첨자 확인 - ID: " + firstPrizeWinner.getId());

        // 2등 당첨자가 없는지 확인
        assertTrue(winners.stream().noneMatch(w -> w.getRank() == 2), "2등 당첨자가 있습니다.");
        System.out.println("2등 당첨자 없음 확인");

        // Batch가 한 번만 실행되었는지 확인
        verify(lottoService, times(1)).generateLottoNumbers();
        System.out.println("배치 작업이 1회 실행되었음을 확인");
    }
}