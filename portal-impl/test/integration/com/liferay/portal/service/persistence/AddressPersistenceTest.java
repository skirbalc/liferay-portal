/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.service.persistence;

import com.liferay.portal.NoSuchAddressException;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.model.Address;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.persistence.PersistenceExecutionTestListener;
import com.liferay.portal.test.ExecutionTestListeners;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import java.util.List;

/**
 * @author Brian Wing Shun Chan
 */
@ExecutionTestListeners(listeners =  {
	PersistenceExecutionTestListener.class})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class AddressPersistenceTest {
	@Before
	public void setUp() throws Exception {
		_persistence = (AddressPersistence)PortalBeanLocatorUtil.locate(AddressPersistence.class.getName());
	}

	@Test
	public void testCreate() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Address address = _persistence.create(pk);

		Assert.assertNotNull(address);

		Assert.assertEquals(address.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		Address newAddress = addAddress();

		_persistence.remove(newAddress);

		Address existingAddress = _persistence.fetchByPrimaryKey(newAddress.getPrimaryKey());

		Assert.assertNull(existingAddress);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addAddress();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Address newAddress = _persistence.create(pk);

		newAddress.setCompanyId(ServiceTestUtil.nextLong());

		newAddress.setUserId(ServiceTestUtil.nextLong());

		newAddress.setUserName(ServiceTestUtil.randomString());

		newAddress.setCreateDate(ServiceTestUtil.nextDate());

		newAddress.setModifiedDate(ServiceTestUtil.nextDate());

		newAddress.setClassNameId(ServiceTestUtil.nextLong());

		newAddress.setClassPK(ServiceTestUtil.nextLong());

		newAddress.setStreet1(ServiceTestUtil.randomString());

		newAddress.setStreet2(ServiceTestUtil.randomString());

		newAddress.setStreet3(ServiceTestUtil.randomString());

		newAddress.setCity(ServiceTestUtil.randomString());

		newAddress.setZip(ServiceTestUtil.randomString());

		newAddress.setRegionId(ServiceTestUtil.nextLong());

		newAddress.setCountryId(ServiceTestUtil.nextLong());

		newAddress.setTypeId(ServiceTestUtil.nextInt());

		newAddress.setMailing(ServiceTestUtil.randomBoolean());

		newAddress.setPrimary(ServiceTestUtil.randomBoolean());

		_persistence.update(newAddress, false);

		Address existingAddress = _persistence.findByPrimaryKey(newAddress.getPrimaryKey());

		Assert.assertEquals(existingAddress.getAddressId(),
			newAddress.getAddressId());
		Assert.assertEquals(existingAddress.getCompanyId(),
			newAddress.getCompanyId());
		Assert.assertEquals(existingAddress.getUserId(), newAddress.getUserId());
		Assert.assertEquals(existingAddress.getUserName(),
			newAddress.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingAddress.getCreateDate()),
			Time.getShortTimestamp(newAddress.getCreateDate()));
		Assert.assertEquals(Time.getShortTimestamp(
				existingAddress.getModifiedDate()),
			Time.getShortTimestamp(newAddress.getModifiedDate()));
		Assert.assertEquals(existingAddress.getClassNameId(),
			newAddress.getClassNameId());
		Assert.assertEquals(existingAddress.getClassPK(),
			newAddress.getClassPK());
		Assert.assertEquals(existingAddress.getStreet1(),
			newAddress.getStreet1());
		Assert.assertEquals(existingAddress.getStreet2(),
			newAddress.getStreet2());
		Assert.assertEquals(existingAddress.getStreet3(),
			newAddress.getStreet3());
		Assert.assertEquals(existingAddress.getCity(), newAddress.getCity());
		Assert.assertEquals(existingAddress.getZip(), newAddress.getZip());
		Assert.assertEquals(existingAddress.getRegionId(),
			newAddress.getRegionId());
		Assert.assertEquals(existingAddress.getCountryId(),
			newAddress.getCountryId());
		Assert.assertEquals(existingAddress.getTypeId(), newAddress.getTypeId());
		Assert.assertEquals(existingAddress.getMailing(),
			newAddress.getMailing());
		Assert.assertEquals(existingAddress.getPrimary(),
			newAddress.getPrimary());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		Address newAddress = addAddress();

		Address existingAddress = _persistence.findByPrimaryKey(newAddress.getPrimaryKey());

		Assert.assertEquals(existingAddress, newAddress);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchAddressException");
		}
		catch (NoSuchAddressException nsee) {
		}
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		Address newAddress = addAddress();

		Address existingAddress = _persistence.fetchByPrimaryKey(newAddress.getPrimaryKey());

		Assert.assertEquals(existingAddress, newAddress);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Address missingAddress = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingAddress);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		Address newAddress = addAddress();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Address.class,
				Address.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("addressId",
				newAddress.getAddressId()));

		List<Address> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Address existingAddress = result.get(0);

		Assert.assertEquals(existingAddress, newAddress);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Address.class,
				Address.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("addressId",
				ServiceTestUtil.nextLong()));

		List<Address> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		Address newAddress = addAddress();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Address.class,
				Address.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("addressId"));

		Object newAddressId = newAddress.getAddressId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("addressId",
				new Object[] { newAddressId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingAddressId = result.get(0);

		Assert.assertEquals(existingAddressId, newAddressId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Address.class,
				Address.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("addressId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("addressId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	protected Address addAddress() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		Address address = _persistence.create(pk);

		address.setCompanyId(ServiceTestUtil.nextLong());

		address.setUserId(ServiceTestUtil.nextLong());

		address.setUserName(ServiceTestUtil.randomString());

		address.setCreateDate(ServiceTestUtil.nextDate());

		address.setModifiedDate(ServiceTestUtil.nextDate());

		address.setClassNameId(ServiceTestUtil.nextLong());

		address.setClassPK(ServiceTestUtil.nextLong());

		address.setStreet1(ServiceTestUtil.randomString());

		address.setStreet2(ServiceTestUtil.randomString());

		address.setStreet3(ServiceTestUtil.randomString());

		address.setCity(ServiceTestUtil.randomString());

		address.setZip(ServiceTestUtil.randomString());

		address.setRegionId(ServiceTestUtil.nextLong());

		address.setCountryId(ServiceTestUtil.nextLong());

		address.setTypeId(ServiceTestUtil.nextInt());

		address.setMailing(ServiceTestUtil.randomBoolean());

		address.setPrimary(ServiceTestUtil.randomBoolean());

		_persistence.update(address, false);

		return address;
	}

	private AddressPersistence _persistence;
}