package de.flower.common.ui.form;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author oblume
 */
public class TimeSelect extends DropDownChoice<LocalTime> {

    DateTimeFormatter formatter =  DateTimeFormat.shortTime();

    public TimeSelect(String id) {
        super(id);
        setChoices(getTimeChoices());
        setChoiceRenderer(new IChoiceRenderer<LocalTime>() {
            @Override
            public Object getDisplayValue(LocalTime time) {
                return formatter.print(time);
            }

            @Override
            public String getIdValue(LocalTime object, int index) {
                return "" + index;
            }
        });
    }

    private List<LocalTime> getTimeChoices() {
        List<LocalTime> list = new ArrayList<LocalTime>();
        LocalTime start = LocalTime.fromMillisOfDay(0);
        LocalTime end = start.minusMillis(1);
        int interval = 15;
        int millis = start.getMillisOfDay();
        while (millis <= end.getMillisOfDay()){
            list.add(LocalTime.fromMillisOfDay(millis));
            millis += interval * 60 * 1000;
        }
        return list;
    }
}
