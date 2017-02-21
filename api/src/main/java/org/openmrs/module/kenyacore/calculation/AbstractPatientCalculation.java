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

package org.openmrs.module.kenyacore.calculation;

import org.openmrs.api.context.Context;
import org.openmrs.calculation.BaseCalculation;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.ResultUtil;

import java.util.Collection;
import java.util.Map;

/**
 * Abstract base class for patient calculations
 */
public abstract class AbstractPatientCalculation extends BaseCalculation implements PatientCalculation {

	/**
	 * Filters a calculation result map to reduce results to booleans
	 * @param results the result map
	 * @return the reduced result map
	 */
	protected static CalculationResultMap passing(CalculationResultMap results) {
		CalculationResultMap ret = new CalculationResultMap();
		for (Map.Entry<Integer, CalculationResult> e : results.entrySet()) {
			ret.put(e.getKey(), new BooleanResult(ResultUtil.isTrue(e.getValue()), null));
		}
		return ret;
	}

	/**
	 * Evaluates a given calculation on each patient
	 * @param calculation the calculation
	 * @param cohort the patient ids
	 * @param calculationContext the calculation context
	 * @return the calculation result map
	 */
	protected static CalculationResultMap calculate(PatientCalculation calculation, Collection<Integer> cohort, PatientCalculationContext calculationContext) {
		return Context.getService(PatientCalculationService.class).evaluate(cohort, calculation, calculationContext);
	}
}