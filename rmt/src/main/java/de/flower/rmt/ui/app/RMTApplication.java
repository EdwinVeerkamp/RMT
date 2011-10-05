package de.flower.rmt.ui.app;

import de.flower.common.ui.LoggingSerializer;
import de.flower.rmt.ui.common.page.login.HomePageResolver;
import de.flower.rmt.ui.common.page.login.LoginPage;
import de.flower.rmt.ui.manager.ManagerHomePage;
import de.flower.rmt.ui.manager.page.teams.TeamsPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.serialize.ISerializer;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.stereotype.Component;

@Component(value = "wicketApplication")
public class RMTApplication extends WebApplication {

    @Override
    protected void init() {
        super.init();
        // add support for @SpringBean
        getComponentInstantiationListeners().add(getSpringComponentInjector());
        // google maps have problems when wicket tags are rendered in development mode, so strip those tags
        getMarkupSettings().setStripWicketTags(true);

        getDebugSettings().setDevelopmentUtilitiesEnabled(true);

        initSerializer();

        initBookmarkablePages();
    }

    private void initSerializer() {
        final ISerializer serializer = getFrameworkSettings().getSerializer();
        getFrameworkSettings().setSerializer(new LoggingSerializer(serializer));
    }

    protected SpringComponentInjector getSpringComponentInjector() {
        return new SpringComponentInjector(this);
    }

    private void initBookmarkablePages() {
        // TODO (flowerrrr - 12.06.11) determine correct order
        mountPackage("manager", ManagerHomePage.class);
        mountPage("manager", ManagerHomePage.class);
        mountPage("manager/teams", TeamsPage.class);
        // mountPage("manager/players", null);
        mountPage("login", LoginPage.class);
    }

    @Override
    public Class getHomePage() {
        return HomePageResolver.getHomePage();
    }

    @Override
    public RMTSession newSession(Request request, Response response) {
        return new RMTSession(request);
    }


}