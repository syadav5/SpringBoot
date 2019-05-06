package com.service.stub.rest.RestStubService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
public class CommMessageController {

    public  String getEndpointsFile() {
        System.out.println("GETTER CALLED");

        return endpointsFile;
    }

    public  void setEndpointsFile(String endpointsFile) {
        System.out.println("SETTER CALLED");
        this.endpointsFile = endpointsFile;
    }

    @Value("${endpoints_properties_file}")
    private String endpointsFile;

    @Value("${response_type}")
    private String responseType;
    @Value("${stub_response_dir}")
    private String stubResponseDir;
    private String envDir;


    @Autowired
    private Environment env;
    @GetMapping(value = "/v1/getMessage",produces = "application/json")
    public String getMessage(){
        return "{\"msgTitle\":\"Hello World from Rest\"}";
    }

    @GetMapping(value = {"/{respType}","/"}, produces = "application/json;application/xml")
    public ResponseEntity home(@PathVariable String respType) {
        String msg = "Endpoints file is "+ endpointsFile;

        if(env!=null){
            envDir = env.getProperty("STUB_RESP_DIR_PATH");
        }
        if(respType!=null){
            try {
             msg =    readFileFromDirectory(respType);
            }
            catch(Exception ex){
                msg="OOPS! It appears that something is wrong with the response file. Please ensure that the stub response file with name "+respType+" exists at location "
                        +envDir;
            }
        }
        else{
            msg = "Going to return default non json response";
        }
        ResponseEntity<String> responseStatus = new ResponseEntity<String>(msg,HttpStatus.OK);
            return responseStatus;
    }

    private String readFileFromDirectory(String path) throws FileNotFoundException , IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(new File(envDir,path)));
        StringBuilder sb = new StringBuilder();
        String str;
        while((str=fileReader.readLine())!=null){
            sb.append(str);
        }
        return sb.toString();
    }
}
