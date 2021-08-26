package gr.apt.persistence.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import gr.apt.persistence.enumeration.Job;
import lombok.Data;

import java.math.BigInteger;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonBasicInfoDto {
    private BigInteger id;
    private String fname;
    private String lname;
    private String email;
    private String username;
    private Job job;
}
