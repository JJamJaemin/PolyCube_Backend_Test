package kr.co.polycube.backendtest.Batch;

import kr.co.polycube.backendtest.Model.Lotto;
import kr.co.polycube.backendtest.Model.Winner;
import kr.co.polycube.backendtest.Repository.LottoRepository;
import kr.co.polycube.backendtest.Repository.WinnerRepository;
import kr.co.polycube.backendtest.Service.LottoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LottoWinnerCheckBatch {

    private final LottoRepository lottoRepository;
    private final WinnerRepository winnerRepository;
    private final LottoService lottoService;

    public LottoWinnerCheckBatch(LottoRepository lottoRepository, WinnerRepository winnerRepository, LottoService lottoService) {
        this.lottoRepository = lottoRepository;
        this.winnerRepository = winnerRepository;
        this.lottoService = lottoService;
    }
    //일요일 마다 실행
    @Scheduled(cron = "0 0 0 * * SUN")
    public void checkWinners() {
        int[] winningNumbers = lottoService.generateLottoNumbers();
        List<Lotto> allLottos = lottoRepository.findAll();

        for (Lotto lotto : allLottos) {
            int matchCount = countMatches(lotto, winningNumbers);
            int rank = getRank(matchCount);

            if (rank > 0) {
                Winner winner = new Winner();
                winner.setLotto(lotto);
                winner.setRank(rank);
                winnerRepository.save(winner);
            }
        }
    }
    // 일치하는 숫자 찾기
    private int countMatches(Lotto lotto, int[] winningNumbers) {
        int count = 0;
        int[] lottoNumbers = {lotto.getNumber1(), lotto.getNumber2(), lotto.getNumber3(),
                lotto.getNumber4(), lotto.getNumber5(), lotto.getNumber6()};

        for (int lottoNumber : lottoNumbers) {
            for (int winningNumber : winningNumbers) {
                if (lottoNumber == winningNumber) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }
    // 등수 매핑
    private int getRank(int matchCount) {
        switch (matchCount) {
            case 6: return 1;
            case 5: return 2;
            case 4: return 3;
            case 3: return 4;
            case 2: return 5;
            default: return 0;
        }
    }
}