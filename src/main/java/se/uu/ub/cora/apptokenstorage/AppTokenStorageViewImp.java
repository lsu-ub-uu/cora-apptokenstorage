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

import java.util.List;

import se.uu.ub.cora.apptokenverifier.AppTokenStorageView;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.storage.RecordStorage;

public class AppTokenStorageViewImp implements AppTokenStorageView {

	private RecordStorage recordStorage;

	public static AppTokenStorageViewImp usingRecordStorage(RecordStorage recordStorage) {
		return new AppTokenStorageViewImp(recordStorage);
	}

	AppTokenStorageViewImp(RecordStorage recordStorage) {
		this.recordStorage = recordStorage;
	}

	// @Override
	// public DataGroup getSearchTerm(String searchTermId) {
	// try {
	// return tryToGetSearchTerm(searchTermId);
	// } catch (Exception e) {
	// String message = "SearchTerm with id: " + searchTermId + " not found in storage.";
	// throw AppTokenStorageViewException.usingMessageAndException(message, e);
	// }
	// }

	private DataGroup tryToGetSearchTerm(String searchTermId) {
		return recordStorage.read(List.of("searchTerm"), searchTermId);
	}

	@Override
	public boolean userIdHasAppToken(String userId, String appToken) {
		// TODO Auto-generated method stub
		return false;
	}

	// @Override
	// public DataGroup getCollectIndexTerm(String collectIndexTermId) {
	// try {
	// return tryToGetCollectIndexTerm(collectIndexTermId);
	// } catch (Exception e) {
	// String message = "CollectIndexTerm with id: " + collectIndexTermId
	// + " not found in storage.";
	// throw AppTokenStorageViewException.usingMessageAndException(message, e);
	// }
	// }

	private DataGroup tryToGetCollectIndexTerm(String collectIndexTermId) {
		return recordStorage.read(List.of("collectIndexTerm"), collectIndexTermId);
	}

	public RecordStorage onlyForTestGetRecordStorage() {
		return recordStorage;
	}

}
