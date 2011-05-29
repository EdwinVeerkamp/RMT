package de.flower.rmt.ui;

import de.flower.rmt.ui.player.pages.login.LoginPage;
import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component(value = "wicketApplication")
public class Application extends AuthenticatedWebApplication {

    private static final String DEFAULT_ENCODING = "UTF-8";

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected void init() {
        super.init();
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));

/*
        getMarkupSettings().setDefaultMarkupEncoding(DEFAULT_ENCODING);
        getRequestCycleSettings().setResponseRequestEncoding(DEFAULT_ENCODING);

        if (getConfigurationType().equals(WebApplication.DEPLOYMENT)) {
            getMarkupSettings().setStripWicketTags(true);
            getMarkupSettings().setStripComments(true);
            getMarkupSettings().setCompressWhitespace(true);
        }
*/

    }

    @Override
    protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
        return de.flower.rmt.ui.AuthenticatedWebSession.class;
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return LoginPage.class;
    }


    @Override
    public Class getHomePage() {
        return HomePage.class;
    }


}