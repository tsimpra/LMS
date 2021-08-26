package gr.apt.persistence.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import gr.apt.persistence.entity.superclass.AbstractEntity;
import lombok.Data;

import java.math.BigInteger;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleDto extends AbstractEntity {
    private BigInteger id;
    private String role;
}
