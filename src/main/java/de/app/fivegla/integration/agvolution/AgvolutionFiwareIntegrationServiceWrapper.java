package de.app.fivegla.integration.agvolution;


import de.app.fivegla.api.FiwareDevicMeasurementeId;
import de.app.fivegla.api.FiwareDeviceId;
import de.app.fivegla.api.InstantFormat;
import de.app.fivegla.api.Manufacturer;
import de.app.fivegla.fiware.DeviceIntegrationService;
import de.app.fivegla.fiware.DeviceMeasurementIntegrationService;
import de.app.fivegla.fiware.model.Device;
import de.app.fivegla.fiware.model.DeviceCategory;
import de.app.fivegla.fiware.model.DeviceMeasurement;
import de.app.fivegla.fiware.model.Location;
import de.app.fivegla.integration.agvolution.model.SeriesEntry;
import de.app.fivegla.integration.agvolution.model.TimeSeriesEntry;
import de.app.fivegla.monitoring.FiwareEntityMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for integration with FIWARE.
 */
@Slf4j
@Service
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class AgvolutionFiwareIntegrationServiceWrapper {
    private final DeviceIntegrationService deviceIntegrationService;
    private final DeviceMeasurementIntegrationService deviceMeasurementIntegrationService;
    private final FiwareEntityMonitor fiwareEntityMonitor;

    public AgvolutionFiwareIntegrationServiceWrapper(DeviceIntegrationService deviceIntegrationService,
                                                     DeviceMeasurementIntegrationService deviceMeasurementIntegrationService,
                                                     FiwareEntityMonitor fiwareEntityMonitor) {
        this.deviceIntegrationService = deviceIntegrationService;
        this.deviceMeasurementIntegrationService = deviceMeasurementIntegrationService;
        this.fiwareEntityMonitor = fiwareEntityMonitor;
    }

    public void persist(SeriesEntry seriesEntry) {
        persist(seriesEntry.getDeviceId());
        seriesEntry.getTimeSeriesEntries().forEach(timeSeriesEntry -> {
            var deviceMeasurements = createDeviceMeasurements(seriesEntry, timeSeriesEntry);
            log.info("Persisting measurement for device: {}", seriesEntry.getDeviceId());
            deviceMeasurements.forEach(deviceMeasurement -> {
                log.info("Persisting measurement: {}", deviceMeasurement);
                deviceMeasurementIntegrationService.persist(deviceMeasurement);
                fiwareEntityMonitor.entitiesSavedOrUpdated(Manufacturer.AGVOLUTION);
            });
        });
    }
    
    private void persist(String deviceId) {
        var device = Device.builder()
                .id(FiwareDeviceId.create(Manufacturer.AGVOLUTION, deviceId))
                .deviceCategory(DeviceCategory.builder()
                        .value(List.of(Manufacturer.AGVOLUTION.key()))
                        .build())
                .build();
        deviceIntegrationService.persist(device);
        fiwareEntityMonitor.sensorsSavedOrUpdated(Manufacturer.AGVOLUTION);
    }

    private List<DeviceMeasurement> createDeviceMeasurements(SeriesEntry seriesEntry, TimeSeriesEntry timeSeriesEntry) {
        log.debug("Persisting data for device: {}", seriesEntry.getDeviceId());
        log.debug("Persisting data: {}", timeSeriesEntry);
        var deviceMeasurements = new ArrayList<DeviceMeasurement>();
        timeSeriesEntry.getValues().forEach(timeSeriesValue -> {
            var deviceMeasurement = DeviceMeasurement.builder()
                    .id(FiwareDevicMeasurementeId.create(Manufacturer.AGVOLUTION))
                    .refDevice(FiwareDeviceId.create(Manufacturer.AGVOLUTION, String.valueOf(seriesEntry.getDeviceId())))
                    .dateObserved(InstantFormat.format(timeSeriesValue.getTime()))
                    .location(Location.builder()
                            .coordinates(List.of(seriesEntry.getLatitude(), seriesEntry.getLongitude()))
                            .build())
                    .controlledProperty(timeSeriesEntry.getKey())
                    .numValue(timeSeriesValue.getValue())
                    .unit(timeSeriesEntry.getUnit())
                    .build();
            deviceMeasurements.add(deviceMeasurement);
        });
        return deviceMeasurements;
    }
}
