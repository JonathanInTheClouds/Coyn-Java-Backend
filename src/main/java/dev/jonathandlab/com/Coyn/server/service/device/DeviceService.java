package dev.jonathandlab.com.Coyn.server.service.device;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import dev.jonathandlab.com.Coyn.server.model.entity.user.AppUserEntity;
import dev.jonathandlab.com.Coyn.server.model.entity.user.AppUserDeviceEntity;
import dev.jonathandlab.com.Coyn.server.repository.AppUserDeviceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua_parser.Client;
import ua_parser.Parser;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class DeviceService implements IDeviceService {

    private Parser userAgentParser;
    private HttpServletRequest request;
    private DatabaseReader databaseReader;
    private AppUserDeviceRepository appUserDeviceRepository;

    @Override
    public void verifyDevice(AppUserEntity user) {
        try {
            String ip = extractIp();
            String location = getIpLocation(ip);
            String deviceDetails = getDeviceDetails(request.getHeader("user-agent"));

            Optional<AppUserDeviceEntity> optionalAppUserDevice = findExistingDevice(user, deviceDetails, location);

            if (optionalAppUserDevice.isEmpty()) {
                // TODO: Unknown Device Notification
            } else {
                AppUserDeviceEntity appUserDeviceEntity = AppUserDeviceEntity.builder()
                        .appUser(user)
                        .deviceDetails(deviceDetails)
                        .location(location)
                        .lastLoggedIn(new Date())
                        .build();
                appUserDeviceRepository.save(appUserDeviceEntity);
            }
        } catch (IOException e) {
            // TODO: Log Device Confirmation Error
            e.printStackTrace();
        }
    }

    @Override
    public void createDevice(AppUserEntity user) {
        try {
            String ip = extractIp();
            String location = getIpLocation(ip);
            String deviceDetails = getDeviceDetails(request.getHeader("user-agent"));

            AppUserDeviceEntity appUserDeviceEntity = AppUserDeviceEntity.builder()
                    .appUser(user)
                    .deviceDetails(deviceDetails)
                    .location(location)
                    .lastLoggedIn(new Date())
                    .build();
            appUserDeviceRepository.save(appUserDeviceEntity);
        } catch (IOException e) {
            // TODO: Log Device Confirmation Error
            e.printStackTrace();
        }
    }

    private Optional<AppUserDeviceEntity> findExistingDevice(AppUserEntity user, String deviceDetails, String location) {
        Set<AppUserDeviceEntity> knownDevices = user.getAppUserDevices();
        for (AppUserDeviceEntity existingDevice : knownDevices) {
            if (existingDevice.getDeviceDetails().equals(deviceDetails) &&
            existingDevice.getLocation().equals(location)) {
                return Optional.of(existingDevice);
            }
        }
        return Optional.empty();
    }

    private String extractIp() {
        String clientIp;
        String clientXForwardedForIp = request.getHeader("x-forwarded-for");
        if (Objects.nonNull(clientXForwardedForIp)) {
            clientIp = parseXForwardedHeader(clientXForwardedForIp);
        } else {
            clientIp = request.getRemoteAddr();
        }

        return clientIp;
    }

    private String getDeviceDetails(String userAgent) {
        String deviceDetails = "UNKNOWN";

        Client client = userAgentParser.parse(userAgent);
        if (Objects.nonNull(client)) {
            deviceDetails = client.userAgent.family + " " + client.userAgent.major + "." + client.userAgent.minor +
                    " - " + client.os.family + " " + client.os.major + "." + client.os.minor;
        }

        return deviceDetails;
    }

    private String parseXForwardedHeader(String header) {
        return header.split(" *, *")[0];
    }

    private String getIpLocation(String ip) throws IOException {
        String location = "UNKNOWN";
        InetAddress ipAddress = InetAddress.getByName(ip);
        try {
            CityResponse cityResponse = databaseReader.city(ipAddress);
            if (Objects.nonNull(cityResponse) && Objects.nonNull(cityResponse.getCity())
                    && Objects.nonNull(cityResponse.getCity().getName()) && !cityResponse.getCity().getName().isEmpty()) {
                location = cityResponse.getCity().getName();
            }
            return location;
        } catch (GeoIp2Exception e) {
            return location;
        }
    }

}
