package charge.station.monitor.controller;

import charge.station.monitor.domain.ChargeInfo;
import charge.station.monitor.domain.Site;
import charge.station.monitor.dto.ChargeInfoRequestDto;
import charge.station.monitor.dto.ChargeInfoResponseDto;
import charge.station.monitor.dto.SiteRequestDto;
import charge.station.monitor.dto.SiteResponseDto;
import charge.station.monitor.service.ChargeInfoService;
import charge.station.monitor.service.SiteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("charge/*")
public class ChargeController {

    private final SiteService siteService;
    private final ChargeInfoService chargeInfoService;


    /**
     * 사이트 조회
     * @return
     */
    @GetMapping("/listSite")
    public ResponseEntity<SiteResponseDto> listSite(){
        List<Site> sites = siteService.selectAll();
        SiteResponseDto siteResponseDto = new SiteResponseDto();
        siteResponseDto.setMessage("사이트 반환 성공");
        siteResponseDto.setData(sites);

        return ResponseEntity.status(HttpStatus.OK).body(siteResponseDto);

    }


    /**
     * 사이트 추가
     * @param dto
     * @return
     */
    @PostMapping("/addSite")
    public ResponseEntity<Long> addSite(@RequestBody @Valid SiteRequestDto dto){
        Long id = siteService.addSite(dto);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }



    /**
     * 충전소 조회
     * @return
     */
    @GetMapping("/listCharge")
    public ResponseEntity<ChargeInfoResponseDto> listCharge(){
        List<ChargeInfo> chargeInfos = chargeInfoService.selectAll();
        ChargeInfoResponseDto chargeInfoResponseDto = new ChargeInfoResponseDto();
        chargeInfoResponseDto.setMessage("충전소 반환 성공");
        chargeInfoResponseDto.setData(chargeInfos);

        return ResponseEntity.status(HttpStatus.OK).body(chargeInfoResponseDto);

    }

    /**
     * 충전소 추가
     * @param dto
     * @return
     */
    @PostMapping("/addCharge")
    public ResponseEntity<Long> addChargeInfo(@RequestBody @Valid ChargeInfoRequestDto dto){
        Long id = chargeInfoService.addChargeInfo(dto);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }
}
