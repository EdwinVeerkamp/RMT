package de.flower.rmt.ui.app;

import de.flower.common.util.geo.LatLngEx;
import de.flower.rmt.model.Users;
import de.flower.rmt.service.security.ISecurityService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author oblume
 */
public class RMTSession extends WebSession {

    private Users user;

    private LatLngEx latLng;

    @SpringBean
    private ISecurityService securityService;

    public RMTSession(Request request) {
        super(request);
        Injector.get().inject(this);
        initUser();
        initLocation();
    }

    public static RMTSession get() {
        return (RMTSession) WebSession.get();
    }

    private void initUser() {
        if (user == null) {
            user = securityService.getCurrentUser();
        }
    }

    /**
     * Determine location based on IP address of browser.
     */
    private void initLocation() {
        // TODO (oblume - 24.07.11) replace with real implementation
        // latLng = iplocationtools.com/api?....
        // latLng = configurationService.getDefaultLocation();
        // latLng = user.getClub().getLatLng();
        latLng = new LatLngEx(48.133333, 11.566667); // München
    }

    // TODO (oblume - 14.08.11) check if this method is obsolete
    public Users getUser() {
        return user;
    }

    public LatLngEx getLatLng() {
        return latLng;
    }
}
