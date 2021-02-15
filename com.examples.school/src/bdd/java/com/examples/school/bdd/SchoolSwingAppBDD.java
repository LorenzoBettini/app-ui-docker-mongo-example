package com.examples.school.bdd;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

/**
 * Communicates with a MongoDB server on localhost; start MongoDB with Docker with
 * 
 * <pre>
 * docker run -p 27017:27017 --rm mongo:4.4.3
 * </pre>
 * 
 * @author Lorenzo Bettini
 *
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/bdd/resources", monochrome = true)
public class SchoolSwingAppBDD {

	public static int mongoPort =
		Integer.parseInt(System.getProperty("mongo.port", "27017"));

	@BeforeClass
	public static void setUpOnce() {
		FailOnThreadViolationRepaintManager.install();
	}

}
