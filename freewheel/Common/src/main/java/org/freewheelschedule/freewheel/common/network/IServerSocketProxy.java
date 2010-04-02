/*
 * Copyright (c) 2010. This file is copyright SJW Computer Consultancy Ltd. The code may be modified as per the GNU Public Licence.
 */

package org.freewheelschedule.freewheel.common.network;

import java.io.IOException;
import java.net.SocketTimeoutException;

public interface IServerSocketProxy {

	public ISocketProxy accept() throws IOException, SocketTimeoutException;
	public void close() throws IOException;
	
}
