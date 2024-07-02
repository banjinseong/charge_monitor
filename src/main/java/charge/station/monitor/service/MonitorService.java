package charge.station.monitor.service;

import charge.station.monitor.domain.ChargeInfo;
import charge.station.monitor.domain.Monitor;
import charge.station.monitor.domain.Site;
import charge.station.monitor.dto.MonitorRequestDto;
import charge.station.monitor.repository.ChargeInfoRepository;
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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MonitorService {

    private final MonitorRepository monitorRepository;
    private final ChargeInfoRepository chargeInfoRepository;
    private final SiteRepository siteRepository;
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


    public Long saveMonitor(MonitorRequestDto monitorRequestDto){
        Site site = siteRepository.findById(monitorRequestDto.getSiteId()).get();
        ChargeInfo chargeInfo = chargeInfoRepository.findByCodeAndSite(monitorRequestDto.getCode(), site).get();
        Monitor monitor = Monitor.builder()
                                .power(monitorRequestDto.getPower())
                                .imgUrl(monitorRequestDto.getImgUrl())
                                .chargeInfo(chargeInfo)
                                .build();
        monitorRepository.save(monitor);
        return monitor.getId();
    }

}
