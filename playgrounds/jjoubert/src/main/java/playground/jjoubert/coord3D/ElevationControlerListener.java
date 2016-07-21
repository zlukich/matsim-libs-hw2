/* *********************************************************************** *
 * project: org.matsim.*
 * ElevationControlerListener.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2009 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

/**
 * 
 */
package playground.jjoubert.coord3D;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.events.ShutdownEvent;
import org.matsim.core.controler.events.StartupEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.controler.listener.ShutdownListener;
import org.matsim.core.controler.listener.StartupListener;
import org.matsim.core.utils.io.IOUtils;

import com.google.inject.Inject;

/**
 * Class to write the route choice proportions to file.
 * 
 * @author jwjoubert
 */
public class ElevationControlerListener implements StartupListener, IterationEndsListener, ShutdownListener {
	
	@Inject
	Scenario sc;
	
	@Inject
	ElevationEventHandler eventhandler;
	
	@Inject EventsManager events;
	
	@Inject
	public ElevationControlerListener() {
	}

	@Override
	public void notifyShutdown(ShutdownEvent event) {
	}

	@Override
	public void notifyIterationEnds(IterationEndsEvent event) {
		BufferedWriter bw = IOUtils.getAppendingBufferedWriter(event.getServices().getControlerIO().getOutputPath() + "/routeChoice.csv");
		try{
			bw.write(String.valueOf(event.getIteration()));
			Iterator<Integer[]> iterator = eventhandler.linkMap.values().iterator();
			while(iterator.hasNext()){
				Integer[] ia = iterator.next();
				bw.write(String.format(",%d,%d", ia[0], ia[1]));
			}
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot write route choice after iteration " + event.getIteration());
		} finally{
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Cannot close route choice writer after iteration " + event.getIteration());
			}
		}
	}

	@Override
	public void notifyStartup(StartupEvent event) {
		BufferedWriter bw = IOUtils.getBufferedWriter(event.getServices().getControlerIO().getOutputPath() + "/routeChoice.csv");
		try{
			bw.write("iter,a1,b1,a2,b2,a3,b3,a4,b4,a5,b5,a6,b6,a7,b7,a8,b8,a9,b9");
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot write route choice at startup");
		} finally{
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Cannot close route choice writer at startup.");
			}
		}
		
		this.events.addHandler(this.eventhandler);
	}

}
