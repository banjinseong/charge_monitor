package charge.station.monitor.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class PythonService {

    public String getRecognizedNumber(String imagePath) {
        String result = "";
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "c:/monitor/script.py", imagePath);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            process.waitFor();

            result = output.toString().trim(); // 결과 문자열을 가져옴

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
