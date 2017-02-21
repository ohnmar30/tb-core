/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.kenyacore.program;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.Program;
import org.openmrs.module.kenyacore.test.StandardTestData;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.kenyacore.test.TestUtils;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link ProgramManager}
 */
public class ProgramManagerTest extends BaseModuleContextSensitiveTest {

	@Autowired
	private ProgramManager programManager;

	@Before
	public void setup() throws Exception {
		programManager.refresh();
	}

	@Test
	public void getAllPrograms() {
		Assert.assertNotNull(programManager.getAllProgramDescriptors());
	}

	@Test
	public void getPatientPrograms() {
		ProgramDescriptor hivProgram = programManager.getProgramDescriptor(MetadataUtils.existing(Program.class, StandardTestData._Program.HIV));
		Patient patient = TestUtils.getPatient(6);

		// Check with no enrollments
		Assert.assertEquals(0, programManager.getPatientPrograms(patient).size());

		// Check with non-active enrollment
		TestUtils.enrollInProgram(patient, hivProgram.getTarget(), TestUtils.date(2011, 1, 1), TestUtils.date(2011, 12, 1));
		Assert.assertThat(programManager.getPatientPrograms(patient), contains(hivProgram));

		// Check with active enrollment
		TestUtils.enrollInProgram(patient, hivProgram.getTarget(), TestUtils.date(2012, 1, 1));
		Assert.assertThat(programManager.getPatientPrograms(patient), contains(hivProgram));
	}
}