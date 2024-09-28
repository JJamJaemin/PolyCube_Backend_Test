package kr.co.polycube.backendtest.Controller;

import kr.co.polycube.backendtest.Service.LottoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/lottos")
public class LottoController {

    private final LottoService lottoService;

    public LottoController(LottoService lottoService) {
        this.lottoService = lottoService;
    }

    @PostMapping
    public ResponseEntity<Map<String, int[]>> generateLottoNumbers() {
        return ResponseEntity.ok(Map.of("numbers", lottoService.generateLottoNumbers()));
    }
}