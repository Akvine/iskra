package ru.akvine.iskra.events.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ru.akvine.iskra.events.LoadMetadataEvent;
import ru.akvine.iskra.services.MetadataLoaderService;

@Component
@RequiredArgsConstructor
public class MetadataLoaderListener implements ApplicationListener<LoadMetadataEvent> {
    private final MetadataLoaderService metadataLoaderService;

    @Override
    public void onApplicationEvent(LoadMetadataEvent event) {
        metadataLoaderService.loadOrList(event.getPlanUuid(), event.getUserUuid());
    }
}
