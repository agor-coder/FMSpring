package pl.gorzki.fmspring.fault.application;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@Value
@ConstructorBinding
@ConfigurationProperties("fmspring.faults")
public class FaultProperties {

    Duration abandonPeriod;
}
