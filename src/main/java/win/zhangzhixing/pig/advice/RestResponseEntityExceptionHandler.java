package win.zhangzhixing.pig.advice;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler {
    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<String> runtimeExceptionHandler(RuntimeException ex) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("errorMessage", ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity(jsonObject.toString(), headers, HttpStatus.OK);
    }
}
