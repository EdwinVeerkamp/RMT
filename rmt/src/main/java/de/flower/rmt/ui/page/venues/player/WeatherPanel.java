package de.flower.rmt.ui.page.venues.player;

import de.flower.common.ui.panel.BasePanel;
import de.flower.rmt.model.Venue;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author flowerrrr
 */
public class WeatherPanel extends BasePanel<String> {

    private static final String url = "http://www.wetteronline.de/cgi-bin/hpweather?PLZ={0}&FORMAT=long&MENU=dropdown&MAP=rainradar";

    public WeatherPanel(final IModel<Venue> model) {
        IModel<String> iframeSrcModel = getIframeSrcModel(model);
        setModel(iframeSrcModel);

        WebMarkupContainer iframe = new WebMarkupContainer("iframe");
        iframe.add(AttributeModifier.replace("src", iframeSrcModel));
        add(iframe);
    }

    @Override
    public boolean isVisible() {
        return getModelObject() != null;
    }

    private IModel<String> getIframeSrcModel(final IModel<Venue> model) {
        return new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                String address = model.getObject().getAddress();
                String zipCode = getZipCode(address);
                return (zipCode == null) ? null : MessageFormat.format(url, new Object[]{zipCode});
            }
        };
    }

    public static String getZipCode(String address) {
        if (address == null) {
            return null;
        }
        Pattern pattern = Pattern.compile("\\d{5}");
        Matcher m = pattern.matcher(address);
        if (m.find()) {
            return m.group();
        } else {
            return null;
        }
    }


}
