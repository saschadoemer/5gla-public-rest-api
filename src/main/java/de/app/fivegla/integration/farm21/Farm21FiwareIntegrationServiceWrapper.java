package de.app.fivegla.integration.farm21;


import de.app.fivegla.api.FiwareDevicMeasurementeId;
import de.app.fivegla.api.FiwareDeviceId;
import de.app.fivegla.api.InstantFormat;
import de.app.fivegla.api.Manufacturer;
import de.app.fivegla.fiware.DeviceIntegrationService;
import de.app.fivegla.fiware.DeviceMeasurementIntegrationService;
import de.app.fivegla.fiware.api.enums.DeviceCategoryValues;
import de.app.fivegla.fiware.model.Device;
import de.app.fivegla.fiware.model.DeviceCategory;
import de.app.fivegla.fiware.model.DeviceMeasurement;
import de.app.fivegla.fiware.model.Location;
import de.app.fivegla.integration.farm21.model.Sensor;
import de.app.fivegla.integration.farm21.model.SensorData;
import de.app.fivegla.monitoring.FiwareEntityMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for integration with FIWARE.
 */
@Slf4j
@Service
public class Farm21FiwareIntegrationServiceWrapper {
    private final DeviceIntegrationService deviceIntegrationService;
    private final DeviceMeasurementIntegrationService deviceMeasurementIntegrationService;
    private final FiwareEntityMonitor fiwareEntityMonitor;

    public Farm21FiwareIntegrationServiceWrapper(DeviceIntegrationService deviceIntegrationService,
                                                 DeviceMeasurementIntegrationService deviceMeasurementIntegrationService,
                                                 FiwareEntityMonitor fiwareEntityMonitor) {
        this.deviceIntegrationService = deviceIntegrationService;
        this.deviceMeasurementIntegrationService = deviceMeasurementIntegrationService;
        this.fiwareEntityMonitor = fiwareEntityMonitor;
    }

    /**
     * Create Farm21 sensor data in FIWARE.
     *
     * @param sensor     the sensor
     * @param sensorData the sensor data to create
     */
    public void persist(Sensor sensor, List<SensorData> sensorData) {
        persist(sensor);
        fiwareEntityMonitor.sensorsSavedOrUpdated(Manufacturer.FARM21);
        sensorData.forEach(sd -> {
            var soilMoisture10 = createDefaultDeviceMeasurement(sensor, sd)
                    .controlledProperty("soilMoisture10")
                    .numValue(sd.getSoilMoisture10())
                    .build();
            log.info("Persisting soil moisture 10: {}", soilMoisture10);
            deviceMeasurementIntegrationService.persist(soilMoisture10);
            fiwareEntityMonitor.entitiesSavedOrUpdated(Manufacturer.FARM21);

            var soilMoisture20 = createDefaultDeviceMeasurement(sensor, sd)
                    .controlledProperty("soilMoisture20")
                    .numValue(sd.getSoilMoisture20())
                    .build();
            log.info("Persisting soil moisture 20: {}", soilMoisture20);
            deviceMeasurementIntegrationService.persist(soilMoisture20);
            fiwareEntityMonitor.entitiesSavedOrUpdated(Manufacturer.FARM21);

            var soilMoisture30 = createDefaultDeviceMeasurement(sensor, sd)
                    .controlledProperty("soilMoisture30")
                    .numValue(sd.getSoilMoisture30())
                    .build();
            log.info("Persisting soil moisture 30: {}", soilMoisture30);
            deviceMeasurementIntegrationService.persist(soilMoisture30);
            fiwareEntityMonitor.entitiesSavedOrUpdated(Manufacturer.FARM21);

            var tempNeg10 = createDefaultDeviceMeasurement(sensor, sd)
                    .controlledProperty("tempNeg10")
                    .numValue(sd.getTempNeg10())
                    .build();
            log.info("Persisting temp neg 10: {}", tempNeg10);
            deviceMeasurementIntegrationService.persist(tempNeg10);
            fiwareEntityMonitor.entitiesSavedOrUpdated(Manufacturer.FARM21);

            var humidity = createDefaultDeviceMeasurement(sensor, sd)
                    .controlledProperty("humidity")
                    .numValue(sd.getHumidity())
                    .build();
            log.info("Persisting humidity: {}", humidity);

            var tempPos10 = createDefaultDeviceMeasurement(sensor, sd)
                    .controlledProperty("tempPos10")
                    .numValue(sd.getTempPos10())
                    .build();
            log.info("Persisting temp pos 10: {}", tempPos10);
            deviceMeasurementIntegrationService.persist(tempPos10);
            fiwareEntityMonitor.entitiesSavedOrUpdated(Manufacturer.FARM21);

            var battery = createDefaultDeviceMeasurement(sensor, sd)
                    .controlledProperty("battery")
                    .numValue(sd.getBattery())
                    .build();
            log.info("Persisting battery: {}", battery);
            deviceMeasurementIntegrationService.persist(battery);
            fiwareEntityMonitor.entitiesSavedOrUpdated(Manufacturer.FARM21);

            var soilTemperature = createDefaultDeviceMeasurement(sensor, sd)
                    .controlledProperty("soilTemperature")
                    .numValue(sd.getSoilTemperature())
                    .build();
            log.info("Persisting soil temperature: {}", soilTemperature);
            deviceMeasurementIntegrationService.persist(soilTemperature);
            fiwareEntityMonitor.entitiesSavedOrUpdated(Manufacturer.FARM21);

            var airTemperature = createDefaultDeviceMeasurement(sensor, sd)
                    .controlledProperty("airTemperature")
                    .numValue(sd.getAirTemperature())
                    .build();
            log.info("Persisting air temperature: {}", airTemperature);
            deviceMeasurementIntegrationService.persist(airTemperature);
            fiwareEntityMonitor.entitiesSavedOrUpdated(Manufacturer.FARM21);

        });
    }

    private void persist(Sensor sensor) {
        var device = Device.builder()
                .id(FiwareDeviceId.create(Manufacturer.FARM21, String.valueOf(sensor.getId())))
                .deviceCategory(DeviceCategory.builder()
                        .value(List.of(DeviceCategoryValues.Farm21Sensor.getKey()))
                        .build())
                .build();
        deviceIntegrationService.persist(device);
    }

    private DeviceMeasurement.DeviceMeasurementBuilder createDefaultDeviceMeasurement(Sensor sensor, SensorData sensorData) {
        log.debug("Persisting sensor data for sensor: {}", sensor);
        log.debug("Persisting sensor data: {}", sensorData);
        return DeviceMeasurement.builder()
                .id(FiwareDevicMeasurementeId.create(Manufacturer.FARM21, String.valueOf(sensorData.getId())))
                .refDevice(FiwareDeviceId.create(Manufacturer.FARM21, String.valueOf(sensor.getId())))
                .dateObserved(InstantFormat.format(sensorData.getMeasuredAt()))
                .location(Location.builder()
                        .coordinates(List.of(sensorData.getLatitude(), sensorData.getLongitude()))
                        .build());
    }

}