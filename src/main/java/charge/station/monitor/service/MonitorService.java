package charge.station.monitor.service;

import charge.station.monitor.domain.ChargeInfo;
import charge.station.monitor.domain.InOut;
import charge.station.monitor.domain.Monitor;
import charge.station.monitor.domain.Site;
import charge.station.monitor.dto.MonitorRequestDto;
import charge.station.monitor.repository.ChargeInfoRepository;
import charge.station.monitor.repository.InOutRepository;
import charge.station.monitor.repository.MonitorRepository;
import charge.station.monitor.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MonitorService {

    private final MonitorRepository monitorRepository;
    private final ChargeInfoRepository chargeInfoRepository;
    private final SiteRepository siteRepository;
    private final InOutRepository inOutRepository;
    private final PythonService pythonService;

    @Value("${upload.folder}")
    private String uploadFolder;


    /**
     * 이미지 저장 url 추출.
     */
    public String getPath(MonitorRequestDto monitorRequestDto) throws IOException {

        Long siteId = monitorRequestDto.getSiteId();
        int code = monitorRequestDto.getCode();
        MultipartFile file = monitorRequestDto.getImg();

        // 디렉토리 경로 설정
        String siteFolder = uploadFolder + File.separator + siteId;
        String codeFolder = siteFolder + File.separator + code;
        Path dirPath = Paths.get(codeFolder);

        // 디렉토리 생성
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        // 파일 저장 경로 설정
        String filename = file.getOriginalFilename();
        Path filePath = Paths.get(codeFolder + File.separator + filename);

        // 파일 저장
        Files.copy(file.getInputStream(), filePath);

        // 파일 URL 반환
        return "file:///" + filePath.toString().replace("\\", "/");
    }

    @Transactional
    public Long saveMonitor(MonitorRequestDto monitorRequestDto, String path){
        Site site = siteRepository.findById(monitorRequestDto.getSiteId()).get();
        ChargeInfo chargeInfo = chargeInfoRepository.findByCodeAndSite(monitorRequestDto.getCode(), site).get();
        Monitor pastMonitor = monitorRepository.findLatestByChargeInfoId(chargeInfo.getId());

        String currentNum = pythonService.getRecognizedNumber(path);// 이미지 저장경로와 파이썬 스크립트 실행.
        String pastNum = pastMonitor.getCurrentNum();//이전 번호
        String PofPNum = pastMonitor.getPastNum();//이전전 번호

        //날짜 설정.
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
        String date = now.format(formatter);

        if(currentNum.isEmpty() && pastNum.isEmpty() && PofPNum.isEmpty()){
            exitCar(currentNum,date,chargeInfo);
        }
        else if(currentNum.equals(pastNum) && !PofPNum.equals(pastNum)){
            //현재차량번호 입차처리
            entryCar(currentNum,date,chargeInfo);
        }else{
            //잘못 찍혔는지 확인 절차.
            boolean chk = compareStrings(currentNum, pastNum);
            if(chk){
                currentNum = pastNum;
            }
            else if(!PofPNum.equals(pastNum)){
                //현재차량번호 입차처리
                entryCar(currentNum,date,chargeInfo);
                //이전전 차량 번호 출차처리
                exitCar(PofPNum,date,chargeInfo);
            }
        }


        Monitor monitor = Monitor.builder()
                                .power(monitorRequestDto.getPower())
                                .imgUrl(monitorRequestDto.getImgUrl())
                                .chargeInfo(chargeInfo)
                                .currentNum(currentNum)
                                .pastNum(pastNum)
                                .localDate(date)
                                .build();
        monitorRepository.save(monitor);
        return monitor.getId();
    }

    //입차 처리 절차
    public void entryCar(String carNum, String date, ChargeInfo chargeInfo){
        InOut inOut =InOut.builder()
                .carNum(carNum)
                .inTime(date)
                .chargeInfo(chargeInfo)
                .build();
        String outTime = inOutRepository.findLatestOutTimeByCarNumAndChargeInfo(carNum, chargeInfo.getId());

        //차량 번호로 입력된 입차가 하나 이상인가??
        if(inOutRepository.countByCarNumAndChargeInfo(carNum, chargeInfo.getId()) >= 1) {
            //마지막 입차처리된 차량이 출차 처리가 되어있는가?
            if (!outTime.isEmpty()) {
                inOutRepository.save(inOut);
            }
        }else {
            //차량 번호로 입차된 차량이 하나도 없는가??
            inOutRepository.save(inOut);
        }

    }

    //출차 처리 절차
    public void exitCar(String carNum, String date, ChargeInfo chargeInfo){
        //차량번호로 입차된 차량이 있는가??
        if(inOutRepository.countByCarNumAndChargeInfo(carNum, chargeInfo.getId()) >= 1){
            //마지막으로 입차된 차량이 아직 출차를 안하였는가?
            if(inOutRepository.findLatestOutTimeByCarNumAndChargeInfo(carNum, chargeInfo.getId()).isEmpty()){
                //변경감지를 이용하여 출차 시간 변경.
                InOut inOut = inOutRepository.findLatestByCarNumAndChargeInfo(carNum, chargeInfo.getId());
                inOut.updateOutTime(date);
            }
        }
    }



    //번호판 비교후 3자리 이상 차이가 나는지 확인 메서드.
    public boolean compareStrings(String A, String B) {
        // 길이가 다르면 false 반환
        if (A.length() != B.length()) {
            return false;
        }

        int differenceCount = 0;

        // 각 자리 비교
        for (int i = 0; i < A.length(); i++) {
            if (A.charAt(i) != B.charAt(i)) {
                differenceCount++;
            }
        }

        // 2개 이하로 다르면 B 값을 반환
        return differenceCount <= 2;
    }
}
