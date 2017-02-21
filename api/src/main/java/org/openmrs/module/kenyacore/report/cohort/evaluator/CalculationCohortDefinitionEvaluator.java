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

package org.openmrs.module.kenyacore.report.cohort.evaluator;

import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.CalculationUtils;
import org.openmrs.module.kenyacore.report.cohort.definition.CalculationCohortDefinition;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.evaluator.CohortDefinitionEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;

import java.util.Date;
import java.util.Set;

/**
 * Evaluator for calculation based cohorts
 */
@Handler(supports = CalculationCohortDefinition.class)
public class CalculationCohortDefinitionEvaluator implements CohortDefinitionEvaluator {
	
	/**
	 * @see org.openmrs.module.reporting.cohort.definition.evaluator.CohortDefinitionEvaluator#evaluate(org.openmrs.module.reporting.cohort.definition.CohortDefinition,
	 *      org.openmrs.module.reporting.evaluation.EvaluationContext)
	 */
	@Override
	public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {
		CalculationResultMap map = doCalculation(cohortDefinition, context);

		CalculationCohortDefinition cd = (CalculationCohortDefinition) cohortDefinition;
		Set<Integer> passing = CalculationUtils.patientsThatPass(map, cd.getWithResult());

		return new EvaluatedCohort(new Cohort(passing), cohortDefinition, context);
	}

	/**
	 * Performs the calculation
	 * @param cohortDefinition the cohort definition
	 * @param context the evaluation context
	 * @return the calculation results
	 */
	protected CalculationResultMap doCalculation(CohortDefinition cohortDefinition, EvaluationContext context) {
		CalculationCohortDefinition cd = (CalculationCohortDefinition) cohortDefinition;

		// Use date from cohort definition, or from ${date} or ${endDate} or now
		Date onDate = cd.getOnDate();
		if (onDate == null) {
			onDate = (Date) context.getParameterValue("date");
			if (onDate == null) {
				onDate = (Date) context.getParameterValue("endDate");
				if (onDate == null) {
					onDate = new Date();
				}
			}
		}

		PatientCalculationService pcs = Context.getService(PatientCalculationService.class);
		PatientCalculationContext calcContext = pcs.createCalculationContext();
		calcContext.setNow(onDate);

		Cohort cohort = context.getBaseCohort();
		if (cohort == null) {
			cohort = Context.getPatientSetService().getAllPatients();
		}

		return pcs.evaluate(cohort.getMemberIds(), cd.getCalculation(), cd.getCalculationParameters(), calcContext);
	}
}