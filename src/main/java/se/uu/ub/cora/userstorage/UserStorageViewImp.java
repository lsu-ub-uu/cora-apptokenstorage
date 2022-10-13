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

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.gatekeeper.user.UserStorageView;
import se.uu.ub.cora.gatekeeper.user.User;
import se.uu.ub.cora.spider.recordtype.RecordTypeHandler;
import se.uu.ub.cora.spider.recordtype.internal.RecordTypeHandlerFactory;
import se.uu.ub.cora.storage.RecordStorage;
import se.uu.ub.cora.userstorage.convert.DataGroupToUser;

public class UserStorageViewImp implements UserStorageView {
	private static final List<String> LIST_RECORD_TYPE = List.of("recordType");
	// protected static final String RECORD_TYPE = "recordType";
	// private static final String PARENT_ID = "parentId";

	private RecordStorage recordStorage;
	protected List<String> userRecordTypeNames = new ArrayList<>();
	private RecordTypeHandlerFactory recordTypeHandlerFactory;

	private DataGroupToUser dataGroupToUser;

	public static UserStorageViewImp usingRecordStorageAndRecordTypeHandlerFactory(
			RecordStorage recordStorage, RecordTypeHandlerFactory recordTypeHandlerFactory,
			DataGroupToUser dataGroupToUser) {
		return new UserStorageViewImp(recordStorage, recordTypeHandlerFactory, dataGroupToUser);
	}

	private UserStorageViewImp(RecordStorage recordStorage,
			RecordTypeHandlerFactory recordTypeHandlerFactory, DataGroupToUser dataGroupToUser) {
		this.recordStorage = recordStorage;
		this.recordTypeHandlerFactory = recordTypeHandlerFactory;
		this.dataGroupToUser = dataGroupToUser;
	}

	@Override
	public User getUserById(String userId) {
		DataGroup userRecordType = recordStorage.read(LIST_RECORD_TYPE, "user");
		RecordTypeHandler recordTypeHandler = recordTypeHandlerFactory
				.factorUsingDataGroup(userRecordType);
		var listOfUserTypes = recordTypeHandler.getListOfImplementingRecordTypeIds();
		var userDataGroup = recordStorage.read(listOfUserTypes, userId);
		return dataGroupToUser.groupToUser(userDataGroup);
	}

	@Override
	public User getUserByIdFromLogin(String idFromLogin) {
		// TODO Auto-generated method stub
		return null;
	}

	// @Override
	// public boolean userIdHasAppToken(String userId, String appToken) {
	// // if (populatedDataUserIdHasAppToken(userId, appToken)) {
	// // return true;
	// // }
	// // // populateUserRecordTypeNamesFromStorage();
	// // return populatedDataUserIdHasAppToken(userId, appToken);
	// }

	// private boolean populatedDataUserIdHasAppToken(String userId, String appToken) {
	// List<String> appTokensForUser = findUserAndGetAppTokens(userId);
	// return apptokenFoundInList(appToken, appTokensForUser);
	// }

	// private boolean apptokenFoundInList(String appToken, List<String> tokenList) {
	// return tokenList.stream().anyMatch(token -> token.equals(appToken));
	// }

	// private List<String> findUserAndGetAppTokens(String userId) {
	//// DataGroup user = findUser(userId);
	// return getAppTokensForUser(user);
	// }

	// private DataGroup findUser(String userId) {
	// DataGroup user = null;
	// for (String userRecordTypeName : userRecordTypeNames) {
	// try {
	// user = recordStorage.read(List.of(userRecordTypeName), userId);
	// } catch (RecordNotFoundException e) {
	// // do nothing
	// }
	// }
	// return user;
	// }

	public RecordStorage onlyForTestGetRecordStorage() {
		return recordStorage;
	}
	// // from Security Storage
	// protected void populateUserRecordTypeNamesFromStorage() {
	// // recordStorage = RecordStorageInMemoryReadFromDisk
	// // .createRecordStorageOnDiskWithBasePath(basePath);
	// recordStorage = RecordStorageProvider.getRecordStorage();
	// populateUserRecordTypeNameList();
	// }
	//
	// private void populateUserRecordTypeNameList() {
	// Collection<DataGroup> recordTypes = recordStorage.readList(List.of(RECORD_TYPE),
	// DataGroupProvider.getDataGroupUsingNameInData("filter")).listOfDataGroups;
	//
	// for (DataGroup recordTypePossibleChild : recordTypes) {
	// addChildOfUserToUserRecordTypeNameList(recordTypePossibleChild);
	// }
	// }
	//
	// private void addChildOfUserToUserRecordTypeNameList(DataGroup recordTypePossibleChild) {
	// if (isChildOfUserRecordType(recordTypePossibleChild)) {
	// addChildToReadRecordList(recordTypePossibleChild);
	// }
	// }
	//
	// private boolean isChildOfUserRecordType(DataGroup recordTypePossibleChild) {
	// return recordHasParent(recordTypePossibleChild)
	// && userRecordTypeIsParentToRecord(recordTypePossibleChild);
	// }
	//
	// private void addChildToReadRecordList(DataGroup recordTypePossibleChild) {
	// String childRecordType = recordTypePossibleChild.getFirstGroupWithNameInData("recordInfo")
	// .getFirstAtomicValueWithNameInData("id");
	// userRecordTypeNames.add(childRecordType);
	// }
	//
	// protected boolean recordHasParent(DataGroup handledRecordTypeDataGroup) {
	// return handledRecordTypeDataGroup.containsChildWithNameInData(PARENT_ID);
	// }
	//
	// protected boolean userRecordTypeIsParentToRecord(DataGroup recordTypePossibleChild) {
	// DataGroup parent = recordTypePossibleChild.getFirstGroupWithNameInData(PARENT_ID);
	// return "user".equals(parent.getFirstAtomicValueWithNameInData("linkedRecordId"));
	// }

	public RecordTypeHandlerFactory onlyForTestGetRecordTypeHandlerFactory() {
		return recordTypeHandlerFactory;
	}

}
