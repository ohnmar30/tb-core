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

package org.openmrs.module.kenyacore.requirement;

/**
 * Interface for required components - things which are required for the EMR to start, e.g. concept dictionary
 */
public interface Requirement {

	/**
	 * Gets the display name of this requirement
	 * @return the name
	 */
	public String getName();

	/**
	 * Gets the required version
	 * @return the version
	 */
	public String getRequiredVersion();

	/**
	 * Gets the version found in the system
	 * @return the found version
	 */
	public String getFoundVersion();

	/**
	 * Determines whether the found version meets the required version
	 * @return true if found version is sufficient
	 */
	public boolean isSatisfied();
}