package net.yao.dto.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiModuleDTO {

    private Long id;

    private Long projectId;

    private String name;

    private Date gmtCreate;

    private List<ApiDTO> list;

    private Date gmtModified;
}
