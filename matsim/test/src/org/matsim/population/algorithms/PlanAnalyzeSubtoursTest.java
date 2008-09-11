/* *********************************************************************** *
 * project: org.matsim.*
 * PlanAnalyzeSubtoursTest.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
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

package org.matsim.population.algorithms;

import java.util.HashMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.matsim.basic.v01.IdImpl;
import org.matsim.gbl.Gbl;
import org.matsim.network.MatsimNetworkReader;
import org.matsim.network.NetworkLayer;
import org.matsim.population.MatsimPopulationReader;
import org.matsim.population.Person;
import org.matsim.population.Plan;
import org.matsim.population.Population;
import org.matsim.population.PopulationReader;
import org.matsim.testcases.MatsimTestCase;
import org.matsim.utils.misc.Time;

/**
 * Test class for {@link PlanAnalyzeSubtours}.
 * 
 * Contains illustrative examples for subtour analysis.
 * 
 * @author meisterk
 *
 */
public class PlanAnalyzeSubtoursTest extends MatsimTestCase {

	private Population population = null;
	private NetworkLayer network = null;

	private static final String CONFIGFILE = "test/scenarios/equil/config.xml";

	private static Logger log = Logger.getLogger(PlanAnalyzeSubtoursTest.class);

	protected void setUp() throws Exception {

		super.setUp();

		super.loadConfig(PlanAnalyzeSubtoursTest.CONFIGFILE);

		log.info("Reading network xml file...");
		network = (NetworkLayer)Gbl.getWorld().createLayer(NetworkLayer.LAYER_TYPE, null);
		new MatsimNetworkReader(network).readFile(Gbl.getConfig().network().getInputFile());
		log.info("Reading network xml file...done.");

		log.info("Reading plans xml file...");
		population = new Population(Population.NO_STREAMING);
		PopulationReader plansReader = new MatsimPopulationReader(population);
		plansReader.readFile(Gbl.getConfig().plans().getInputFile());
		population.printPlansCount();
		log.info("Reading plans xml file...done.");

	}

	public void testRun() throws Exception {

		// At first, we test a standard h-w-h plan with different locations from the equil-test scenario
		Person person = population.getPerson("2");
		Plan plan = person.getPlans().get(0);

		PlanAnalyzeSubtours testee = new PlanAnalyzeSubtours();
		testee.run(plan);
		assertEquals(1, testee.getNumSubtours());

		person = new Person(new IdImpl("1000"));

		// now let's test different types of activity plans
		HashMap<String, String> expectedSubtourIndexations = new HashMap<String, String>();
		HashMap<String, Integer> expectedNumSubtours = new HashMap<String, Integer>();
		
		String testedRoute = "1 2 1";
		expectedSubtourIndexations.put(testedRoute, "0 0");
		expectedNumSubtours.put(testedRoute, 1);
		
		testedRoute = "1 2 20 1";
		expectedSubtourIndexations.put(testedRoute, "0 0 0");
		expectedNumSubtours.put(testedRoute, 1);

		testedRoute = "1 2 1 2 1";
		expectedSubtourIndexations.put(testedRoute, "0 0 1 1");
		expectedNumSubtours.put(testedRoute, 2);

		testedRoute = "1 2 1 3 1";
		expectedSubtourIndexations.put(testedRoute, "0 0 1 1");
		expectedNumSubtours.put(testedRoute, 2);

		testedRoute = "1 2 2 1";
		expectedSubtourIndexations.put(testedRoute, "1 0 1");
		expectedNumSubtours.put(testedRoute, 2);
		
		testedRoute = "1 2 2 2 2 2 2 2 1";
		expectedSubtourIndexations.put(testedRoute, "6 0 1 2 3 4 5 6");
		expectedNumSubtours.put(testedRoute, 7);

		testedRoute = "1 2 3 2 1";
		expectedSubtourIndexations.put(testedRoute, "1 0 0 1");
		expectedNumSubtours.put(testedRoute, 2);

		testedRoute = "1 2 3 4 3 2 1";
		expectedSubtourIndexations.put(testedRoute, "2 1 0 0 1 2");
		expectedNumSubtours.put(testedRoute, 3);

		testedRoute = "1 2 14 2 14 2 1";
		expectedSubtourIndexations.put(testedRoute, "2 0 0 1 1 2");
		expectedNumSubtours.put(testedRoute, 3);

		testedRoute = "1 2 14 14 2 14 2 1";
		expectedSubtourIndexations.put(testedRoute, "3 1 0 1 2 2 3");
		expectedNumSubtours.put(testedRoute, 4);

		testedRoute = "1 2 3 4 3 2 5 4 5 1";
		expectedSubtourIndexations.put(testedRoute, "3 1 0 0 1 3 2 2 3");
		expectedNumSubtours.put(testedRoute, 4);

		testedRoute = "1 2 3 2 3 2 1 2 1";
		expectedSubtourIndexations.put(testedRoute, "2 0 0 1 1 2 3 3");
		expectedNumSubtours.put(testedRoute, 4);

		testedRoute = "1 1 1 1 1 2 1";
		expectedSubtourIndexations.put(testedRoute, "0 1 2 3 4 4");
		expectedNumSubtours.put(testedRoute, 5);

		testedRoute = "1 2 1 1";
		expectedSubtourIndexations.put(testedRoute, "0 0 1");
		expectedNumSubtours.put(testedRoute, 2);

		testedRoute = "1 2 3 4";
		expectedSubtourIndexations.put(
				testedRoute, 
				new String(
						Integer.toString(PlanAnalyzeSubtours.UNDEFINED) + " " + 
						Integer.toString(PlanAnalyzeSubtours.UNDEFINED) + " " + 
						Integer.toString(PlanAnalyzeSubtours.UNDEFINED)));
		expectedNumSubtours.put(testedRoute, 0);

		testedRoute = "1 2 2 3 2 2 2 1 4 1";
		expectedSubtourIndexations.put(testedRoute, "4 0 1 1 2 3 4 5 5");
		expectedNumSubtours.put(testedRoute, 6);
		
		testedRoute = "1 2 3 4 3 1";
		expectedSubtourIndexations.put(testedRoute, "1 1 0 0 1");
		expectedNumSubtours.put(testedRoute, 2);
		
		for (String linkString : expectedSubtourIndexations.keySet()) {

			log.info("Testing location sequence: " + linkString);

			plan = new Plan(person);

			String[] linkIdSequence = linkString.split(" ");
			for (int aa=0; aa < linkIdSequence.length; aa++) {
				plan.createAct(
						"actOnLink" + linkIdSequence[aa], 
						100.0, 
						100.0, 
						network.getLink(linkIdSequence[aa]), 
						Time.parseTime("10:00:00"), 
						Time.parseTime("10:00:00"), 
						Time.parseTime("00:00:00"), 
						false);
				if (aa != (linkIdSequence.length - 1)) {
					plan.createLeg(
							"car", 
							Time.parseTime("10:30:00"), 
							Time.parseTime("00:00:00"), 
							Time.parseTime("10:30:00"));
				}
			}
			testee.run(plan);
			
			String actualSubtourIndexation = new String("");
			for (int value : testee.getSubtourIndexation()) {
				actualSubtourIndexation += Integer.toString(value);
				actualSubtourIndexation += " ";
			}
			actualSubtourIndexation = actualSubtourIndexation.substring(0, actualSubtourIndexation.length() - 1);
			assertEquals(expectedSubtourIndexations.get(linkString), actualSubtourIndexation);
			
			assertEquals(expectedNumSubtours.get(linkString).intValue(), testee.getNumSubtours());
		}

	}

}
