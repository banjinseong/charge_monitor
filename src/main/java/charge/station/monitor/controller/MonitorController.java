package charge.station.monitor.controller;

import charge.station.monitor.dto.MonitorRequestDto;
import charge.station.monitor.service.MonitorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("monitor/*")
public class MonitorController {

    private final MonitorService monitorService;

    @PostMapping("stillCut")
    public ResponseEntity<Long> stillCut(@RequestBody @Valid MonitorRequestDto monitorRequestDto) throws IOException {
        String path = monitorService.getPath(monitorRequestDto);
        monitorRequestDto.setImgUrl(path);
        Long id = monitorService.saveMonitor(monitorRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }
}
