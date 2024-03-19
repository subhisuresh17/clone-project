package pl.rengreen.taskmanager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CallMessage {
    private String sdpOffer;
    private Long callerId;
    // Getter and setter methods
}
