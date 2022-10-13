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
package se.uu.ub.cora.userstorage;

import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.spies.DataGroupSpy;
import se.uu.ub.cora.gatekeeper.user.User;
import se.uu.ub.cora.storage.spies.RecordStorageSpy;
import se.uu.ub.cora.userstorage.UserStorageViewImp;
import se.uu.ub.cora.userstorage.convert.DataGroupToUserSpy;
import se.uu.ub.cora.userstorage.spies.RecordTypeHandlerSpy;

public class UserStorageViewTest {
	private static final String APP_TOKEN = "someAppToken";
	private static final String USER_ID = "someUserId";
	private RecordStorageSpy recordStorage;
	private UserStorageViewImp appTokenStorage;
	private RecordTypeHandlerFactorySpy recordTypeHandlerFactory;
	private DataGroupToUserSpy dataGroupToUser;

	@BeforeMethod
	public void beforeMethod() {
		recordStorage = new RecordStorageSpy();
		recordTypeHandlerFactory = new RecordTypeHandlerFactorySpy();
		dataGroupToUser = new DataGroupToUserSpy();
		appTokenStorage = UserStorageViewImp.usingRecordStorageAndRecordTypeHandlerFactory(
				recordStorage, recordTypeHandlerFactory, dataGroupToUser);
	}

	@Test
	public void testInit() throws Exception {
		assertTrue(appTokenStorage instanceof UserStorageViewImp);
	}

	@Test
	public void testGetUserById_usingDependencies() throws Exception {
		appTokenStorage.getUserById(USER_ID);

		recordStorage.MCR.assertParameterAsEqual("read", 0, "types", List.of("recordType"));
		recordStorage.MCR.assertParameterAsEqual("read", 0, "id", "user");

		var userData = recordStorage.MCR.getReturnValue("read", 0);

		recordTypeHandlerFactory.MCR.assertParameters("factorUsingDataGroup", 0, userData);

		RecordTypeHandlerSpy userRecordTypeHandler = (RecordTypeHandlerSpy) recordTypeHandlerFactory.MCR
				.getReturnValue("factorUsingDataGroup", 0);
		var listOfUserTypes = userRecordTypeHandler.MCR
				.getReturnValue("getListOfImplementingRecordTypeIds", 0);
		recordStorage.MCR.assertParameters("read", 1, listOfUserTypes, USER_ID);
	}

	@Test
	public void testGetUserById_userContainsInfo() throws Exception {
		DataGroupSpy userDataGroup = new DataGroupSpy();
		userDataGroup.MRV.setReturnValues("getAllGroupsWithNameInData", List.of(),
				"userAppTokenGroup");

		recordStorage.MRV.setReturnValues("read", List.of(userDataGroup), Collections.emptyList(),
				USER_ID);

		User user = appTokenStorage.getUserById(USER_ID);

		dataGroupToUser.MCR.assertReturn("groupToUser", 0, user);
	}

}
