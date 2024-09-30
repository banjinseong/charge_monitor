package charge.station.monitor.controller;

import charge.station.monitor.dto.MonitorRequestDto;
import charge.station.monitor.service.MonitorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;

@RestController
@RequiredArgsConstructor
@RequestMapping("monitor/*")
public class MonitorController {

    private final MonitorService monitorService;

    @PostMapping("stillCut")
    public ResponseEntity<Long> stillCut(@RequestBody @Valid MonitorRequestDto monitorRequestDto) throws IOException {
        String path = monitorService.getPath(monitorRequestDto);
        monitorRequestDto.setImgUrl(path);
        Long id = monitorService.saveMonitor(monitorRequestDto, path);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    @PostMapping("/imgConnection")
    public ResponseEntity<?> saveImg(@RequestBody byte[] image, HttpServletRequest request) {

        // 요청 헤더에서 사용할 값 가져오기 (예: "Image-Name" 헤더)
        String imageNameHeader = request.getHeader("Camera-ID");

        // 현재 날짜와 시간을 yyyy_MM_dd_HH_mm 형식으로 포맷
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        String formattedDateTime = now.format(formatter) + ".jpeg";
//
        // 저장할 디렉토리 경로 (예: 서버의 "uploads" 디렉토리)
        String uploadDir = "/app/monitor/고천/" + imageNameHeader+"/";

        // 디렉토리 확인 및 생성
        File directory = new File(uploadDir);

        if (!directory.exists()) {
            directory.mkdirs();  // 디렉토리 생성
        }
        // 바이너리 데이터를 하나씩 출력


        // 파일 저장
        String filePath = uploadDir + formattedDateTime;
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            bos.write(image);
            bos.flush();
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to save image: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
// 헤더 값 출력
        System.out.println("Request Headers:");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            System.out.println(headerName + ": " + headerValue);
        }
        // 응답
        return new ResponseEntity<>("File uploaded successfully: " + filePath, HttpStatus.OK);
    }
}
