/**
 * Copyright (c) 2000-2008 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.ipgeocoder.servlet;

import com.liferay.ipgeocoder.messaging.IPGeocoderMessageListener;
import com.liferay.portal.kernel.messaging.Destination;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.ParallelDestination;
import com.liferay.portal.kernel.util.PortalInitable;
import com.liferay.portal.kernel.util.PortalInitableUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * <a href="IPGeocoderServletContextListener.java.html"><b><i>View Source</i>
 * </b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class IPGeocoderServletContextListener
	implements PortalInitable, ServletContextListener {

	public void contextDestroyed(ServletContextEvent event) {
		_ipGeocoderDestination.unregister(_ipGeocoderMessageListener);

		MessageBusUtil.removeDestination(
			_ipGeocoderDestination.getName());

		MessageBusUtil.removeDestination(DestinationNames.IP_GEOCODER_RESPONSE);
	}

	public void contextInitialized(ServletContextEvent event) {
		PortalInitableUtil.init(this);
	}

	public void portalInit() {
		_ipGeocoderDestination = new ParallelDestination(
			DestinationNames.IP_GEOCODER);

		MessageBusUtil.addDestination(_ipGeocoderDestination);

		_ipGeocoderMessageListener = new IPGeocoderMessageListener();

		_ipGeocoderDestination.register(_ipGeocoderMessageListener);

		Destination ipGeocoderResponseDestination = new ParallelDestination(
			DestinationNames.IP_GEOCODER_RESPONSE);

		MessageBusUtil.addDestination(ipGeocoderResponseDestination);
	}

	private Destination _ipGeocoderDestination;
	private MessageListener _ipGeocoderMessageListener;

}