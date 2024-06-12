package com.example.littleBank.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChangePwRequest {

    private Long id_cliente;
    private String oldPassword;
    private String newPassword;

}
