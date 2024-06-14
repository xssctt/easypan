package com.example.easypan.entity.po;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Maill {

    private Integer id;
    private String header;
    private String content;
    private String maill;


}
