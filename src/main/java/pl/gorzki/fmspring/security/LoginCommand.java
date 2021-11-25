package pl.gorzki.fmspring.security;

import lombok.Data;

@Data
public class LoginCommand {
    String userName;
    String password;
}
