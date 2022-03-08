package org.example.notifier;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractEventNotifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


/**
 * 自定义告警
 * */
@Slf4j
//@Component
//@SuppressWarnings("all")
public class DaShuaiNotifier extends AbstractEventNotifier {
    public DaShuaiNotifier(InstanceRepository repository) {
        super(repository);
    }

    /**
     *
     * @param event
     * @param instance
     * @return 实现对事件的通知
     */
    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> {
            if (event instanceof InstanceStatusChangedEvent){
                log.info("Instance Status Change:[{}],[{}],[{}]",
                        instance.getRegistration().getName(),event.getInstance(),
                        ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus());
            }else {
                log.info("Instance Info : [{}],[{}],[{}]",
                        instance.getRegistration().getName(),event.getInstance(),
                        event.getType());
            }
        });
    }
}
