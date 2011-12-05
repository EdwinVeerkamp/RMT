/*
 * $Id: org.eclipse.jdt.ui.prefs 5004 2006-03-17 20:47:08 -0800 (Fri, 17 Mar 2006) eelco12 $
 * $Revision: 5004 $
 * $Date: 2006-03-17 20:47:08 -0800 (Fri, 17 Mar 2006) $
 *
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.contrib.gmap3.mapevent;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import wicket.contrib.gmap3.api.GLatLng;
import wicket.contrib.gmap3.overlay.GOverlay;

/**
 * See "CLICK" in the event section of <a
 * href="http://www.google.com/apis/maps/documentation/reference.html#GMap2"
 * >GMap2</a>.
 */
public abstract class ClickListener extends GMapEventListenerBehavior {

    private static final long serialVersionUID = -6265838789648579340L;

    @Override
    protected String getEvent() {
        return Event.CLICK.toString();
    }

    @Override
    protected void onEvent( final AjaxRequestTarget target ) {
        final Request request = RequestCycle.get().getRequest();

        GOverlay overlay = null;
        GLatLng latLng = null;

        final String markerParameter = request.getRequestParameters().getParameterValue( "argument0" ).toString();
        if ( markerParameter != null ) {
            for ( final GOverlay ovl : getGMap().getOverlays() ) {
                if ( ovl.getId().equals( markerParameter ) ) {
                    overlay = ovl;
                    break;
                }
            }
        }

        final String latLngParameter = request.getRequestParameters().getParameterValue( "latLng" ).toString();
        if ( latLngParameter != null ) {
            latLng = GLatLng.parse( latLngParameter );
        }

        onClick( target, latLng, overlay );
    }

    /**
     * Override this method to provide handling of a click on the map. See the
     * event section of <a href=
     * "http://www.google.com/apis/maps/documentation/reference.html#GMap2"
     * >GMap2</a>.
     *
     * @param latLng
     *            The clicked GLatLng. Might be null if a Marker was clicked.
     * @param overlay
     *            The clicked overlay. Might be null.
     * @param target
     *            The target that initiated the click.
     */
    protected abstract void onClick( AjaxRequestTarget target, GLatLng latLng, GOverlay overlay );
}