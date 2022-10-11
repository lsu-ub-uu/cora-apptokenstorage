/*
 * Copyright 2022 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.uu.ub.cora.apptokenstorage;

import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.storage.RecordStorage;
import se.uu.ub.cora.storage.spies.RecordStorageSpy;

public class AppTokenStorageViewTest {
	private RecordStorageSpy recordStorage;
	private AppTokenStorageViewImp appTokenStorage;

	@BeforeMethod
	public void beforeMethod() {
		recordStorage = new RecordStorageSpy();
		appTokenStorage = AppTokenStorageViewImp.usingRecordStorage(recordStorage);
	}

	@Test
	public void testInit() throws Exception {
		assertTrue(appTokenStorage instanceof AppTokenStorageViewImp);
	}

	@Test
	public void testOnlyForTestGetRecordStorage() throws Exception {
		RecordStorage recordStorage2 = appTokenStorage.onlyForTestGetRecordStorage();
		assertSame(recordStorage2, recordStorage);
	}

	// @Test
	// public void testGetSearchTermThrowsExceptionIfAnyExceptionIsThrownInRecordStorage()
	// throws Exception {
	// RuntimeException exceptionToThrow = new RuntimeException("Error fromSpy");
	// recordStorage.MRV.setAlwaysThrowException("read", exceptionToThrow);
	//
	// try {
	// searchStorage.getSearchTerm("someNotFoundId");
	// assertTrue(false);
	// } catch (Exception e) {
	// String errorMessage = "SearchTerm with id: someNotFoundId not found in storage.";
	// assertException(exceptionToThrow, e, errorMessage);
	// }
	// }

	// private void assertException(RuntimeException exceptionThrown, Exception catchedException,
	// String errorMessage) {
	// assertTrue(catchedException instanceof AppTokenStorageViewException);
	// assertEquals(catchedException.getMessage(), errorMessage);
	// assertSame(catchedException.getCause(), exceptionThrown);
	// }
	//
	// @Test
	// public void testGetSearchTerm() throws Exception {
	// DataGroup searchTerm = searchStorage.getSearchTerm("someSearchTermId");
	//
	// recordStorage.MCR.assertMethodWasCalled("read");
	// recordStorage.MCR.assertParameterAsEqual("read", 0, "types", List.of("searchTerm"));
	// recordStorage.MCR.assertParameter("read", 0, "id", "someSearchTermId");
	// recordStorage.MCR.assertReturn("read", 0, searchTerm);
	// }
	//
	// @Test
	// public void testGetCollectIndexTermThrowsExceptionIfAnyExceptionIsThrownInRecordStorage()
	// throws Exception {
	// RuntimeException exceptionToThrow = new RuntimeException("Error fromSpy");
	// recordStorage.MRV.setAlwaysThrowException("read", exceptionToThrow);
	//
	// try {
	// searchStorage.getCollectIndexTerm("someNotFoundId");
	// assertTrue(false);
	// } catch (Exception e) {
	// String errorMessage = "CollectIndexTerm with id: someNotFoundId not found in storage.";
	// assertException(exceptionToThrow, e, errorMessage);
	// }
	// }
	//
	// @Test
	// public void testGetCollectIndexTerm() throws Exception {
	//
	// DataGroup indexTerm = searchStorage.getCollectIndexTerm("someIndexTermId");
	//
	// recordStorage.MCR.assertMethodWasCalled("read");
	// recordStorage.MCR.assertParameterAsEqual("read", 0, "types", List.of("collectIndexTerm"));
	// recordStorage.MCR.assertParameter("read", 0, "id", "someIndexTermId");
	// recordStorage.MCR.assertReturn("read", 0, indexTerm);
	// }

}
