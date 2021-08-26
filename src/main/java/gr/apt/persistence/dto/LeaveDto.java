package gr.apt.persistence.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import gr.apt.persistence.entity.superclass.AbstractEntity;
import gr.apt.persistence.enumeration.LeaveType;
import gr.apt.persistence.enumeration.YesOrNo;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeaveDto extends AbstractEntity {
    private BigInteger id;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    private Integer numberOfRequestedLeaves;

    private LeaveType type;

    private PersonBasicInfoDto personId;
}
