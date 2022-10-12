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

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.apptokenverifier.AppTokenStorageView;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.spider.recordtype.RecordTypeHandler;
import se.uu.ub.cora.spider.recordtype.internal.RecordTypeHandlerFactory;
import se.uu.ub.cora.storage.RecordStorage;

public class AppTokenStorageViewImp implements AppTokenStorageView {
	private static final List<String> LIST_RECORD_TYPE = List.of("recordType");
	// protected static final String RECORD_TYPE = "recordType";
	// private static final String PARENT_ID = "parentId";

	private RecordStorage recordStorage;
	protected List<String> userRecordTypeNames = new ArrayList<>();
	private RecordTypeHandlerFactory recordTypeHandlerFactory;

	public static AppTokenStorageViewImp usingRecordStorageAndRecordTypeHandlerFactory(
			RecordStorage recordStorage, RecordTypeHandlerFactory recordTypeHandlerFactory) {
		return new AppTokenStorageViewImp(recordStorage, recordTypeHandlerFactory);
	}

	private AppTokenStorageViewImp(RecordStorage recordStorage,
			RecordTypeHandlerFactory recordTypeHandlerFactory) {
		this.recordStorage = recordStorage;
		this.recordTypeHandlerFactory = recordTypeHandlerFactory;
	}

	@Override
	public boolean userIdHasAppToken(String userId, String appToken) {
		var userDataRecordGroup = recordStorage.read(LIST_RECORD_TYPE, "user");
		RecordTypeHandler recordTypeHandler = recordTypeHandlerFactory
				.factorUsingDataGroup(userDataRecordGroup);
		var listOfUserTypes = recordTypeHandler.getListOfImplementingRecordTypeIds();
		recordStorage.read(listOfUserTypes, userId);

		// getAppTokensForUser()
		return false;
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

	private List<String> getAppTokensForUser(DataGroup user) {
		if (userExistsAndIsActive(user)) {
			return getAppTokensForActiveUser(user);
		}
		return new ArrayList<>();
	}

	private boolean userExistsAndIsActive(DataGroup user) {
		return user != null && userIsActive(user);
	}

	private List<String> getAppTokensForActiveUser(DataGroup user) {
		List<DataGroup> userAppTokenGroups = user.getAllGroupsWithNameInData("userAppTokenGroup");
		return getAppTokensForAppTokenGroups(userAppTokenGroups);
	}

	private List<String> getAppTokensForAppTokenGroups(List<DataGroup> userAppTokenGroups) {
		List<String> apptokens = new ArrayList<>(userAppTokenGroups.size());
		for (DataGroup userAppTokenGroup : userAppTokenGroups) {
			String appTokenId = extractAppTokenId(userAppTokenGroup);
			apptokens.add(getTokenFromStorage(appTokenId));
		}
		return apptokens;
	}

	private String extractAppTokenId(DataGroup userAppTokenGroup) {
		return userAppTokenGroup.getFirstGroupWithNameInData("appTokenLink")
				.getFirstAtomicValueWithNameInData("linkedRecordId");
	}

	private String getTokenFromStorage(String appTokenId) {
		return recordStorage.read(List.of("appToken"), appTokenId)
				.getFirstAtomicValueWithNameInData("token");
	}

	private boolean userIsActive(DataGroup user) {
		return "active".equals(user.getFirstAtomicValueWithNameInData("activeStatus"));
	}

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
