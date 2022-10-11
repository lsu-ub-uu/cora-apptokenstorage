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

import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.apptokenverifier.AppTokenStorageView;
import se.uu.ub.cora.apptokenverifier.AppTokenStorageViewInstanceProvider;
import se.uu.ub.cora.logger.LoggerFactory;
import se.uu.ub.cora.logger.LoggerProvider;
import se.uu.ub.cora.logger.spies.LoggerFactorySpy;
import se.uu.ub.cora.storage.RecordStorageProvider;
import se.uu.ub.cora.storage.spies.RecordStorageInstanceProviderSpy;

public class AppTokenStorageProviderTest {
	LoggerFactory loggerFactory = new LoggerFactorySpy();
	private RecordStorageInstanceProviderSpy recordStorageInstanceProvider;

	@BeforeMethod
	public void beforeMethod() {
		LoggerProvider.setLoggerFactory(loggerFactory);
		setUpRecordStorageProviderToReturnSpy();
	}

	private void setUpRecordStorageProviderToReturnSpy() {
		recordStorageInstanceProvider = new RecordStorageInstanceProviderSpy();
		RecordStorageProvider
				.onlyForTestSetRecordStorageInstanceProvider(recordStorageInstanceProvider);
	}

	@Test
	public void testPrivateConstructor() throws Exception {
		Constructor<AppTokenStorageProvider> constructor = AppTokenStorageProvider.class
				.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
	}

	@Test(expectedExceptions = InvocationTargetException.class)
	public void testPrivateConstructorInvoke() throws Exception {
		Constructor<AppTokenStorageProvider> constructor = AppTokenStorageProvider.class
				.getDeclaredConstructor();
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testGetStorageViewUsesRecordStorageFromRecordStorageProvider() throws Exception {
		AppTokenStorageViewImp searchStorageView = (AppTokenStorageViewImp) AppTokenStorageProvider
				.getStorageView();

		assertTrue(searchStorageView instanceof AppTokenStorageView);
		recordStorageInstanceProvider.MCR.assertReturn("getRecordStorage", 0,
				searchStorageView.onlyForTestGetRecordStorage());
	}

	@Test
	public void testMultipleCalls() throws Exception {
		AppTokenStorageViewImp searchStorageView1 = (AppTokenStorageViewImp) AppTokenStorageProvider
				.getStorageView();
		AppTokenStorageViewImp searchStorageView2 = (AppTokenStorageViewImp) AppTokenStorageProvider
				.getStorageView();

		recordStorageInstanceProvider.MCR.assertReturn("getRecordStorage", 0,
				searchStorageView1.onlyForTestGetRecordStorage());
		recordStorageInstanceProvider.MCR.assertReturn("getRecordStorage", 1,
				searchStorageView2.onlyForTestGetRecordStorage());
		assertNotSame(searchStorageView1, searchStorageView2);
	}

	@Test
	public void testDefaultSearchStorageViewInstanceProvider() throws Exception {
		AppTokenStorageViewInstanceProvider instanceProvider = AppTokenStorageProvider
				.onlyForTestGetSearchStorageViewInstanceProvider();
		assertTrue(instanceProvider instanceof AppTokenStorageViewInstanceProviderImp);

	}

	@Test
	public void testOnlyForTestSetSearchStorageViewInstanceProvider() throws Exception {
		AppTokenStorageViewInstanceProviderSpy instanceProvider = new AppTokenStorageViewInstanceProviderSpy();
		AppTokenStorageProvider.onlyForTestSetSearchStorageViewInstanceProvider(instanceProvider);

		AppTokenStorageView searchStorageView = AppTokenStorageProvider.getStorageView();

		instanceProvider.MCR.assertReturn("getStorageView", 0, searchStorageView);
	}

}
