package gr.apt.persistence.dto;

import com.fasterxml.jackson.annotation.*;
import gr.apt.persistence.entity.superclass.AbstractEntity;
import gr.apt.persistence.enumeration.Job;
import gr.apt.persistence.enumeration.YesOrNo;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Collection;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonDto extends AbstractEntity {
    private BigInteger id;
    private String fname;
    private String lname;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;
    private String email;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfEmployment;
    private Job job;
    private Integer numberOfLeaves;
    private Integer usedLeaves;

    private Integer remainingLeaves;

    private YesOrNo isActive;

    private Collection<RoleDto> roles;

}
