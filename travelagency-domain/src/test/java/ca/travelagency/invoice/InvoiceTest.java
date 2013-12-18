/**
 *    Copyright (C) 2010 - 2014 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package ca.travelagency.invoice;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.travelagency.utils.MoneyUtils;


public class InvoiceTest {

	private Invoice fixture;

	@Before
	public void setUp() {
		fixture = InvoiceHelper.makeInvoice();
	}

	@Test
	public void testUniqueDestinationsAreAdded() {
		// setup
		InvoiceDestination destination1 = InvoiceHelper.makeDestination();
		InvoiceDestination destination2 = InvoiceHelper.makeDestination();
		InvoiceDestination destination3 = InvoiceHelper.makeDestination();
		// execute
		fixture.addInvoiceDestination(destination1);
		fixture.addInvoiceDestination(destination2);
		fixture.addInvoiceDestination(destination3);
		// validate
		SortedSet<InvoiceDestination> invoiceDestinations = fixture.getInvoiceDestinations();
		Assert.assertEquals(3, invoiceDestinations.size());
		Assert.assertTrue(invoiceDestinations.contains(destination1));
		Assert.assertTrue(invoiceDestinations.contains(destination2));
		Assert.assertTrue(invoiceDestinations.contains(destination3));
	}

	@Test
	public void testOnlyUniqueDestinationsAreAdded() {
		// setup
		InvoiceDestination destination1 = InvoiceHelper.makeDestination();
		InvoiceDestination destination2 = InvoiceHelper.makeDestination();
		destination2.setDeparturePlace(destination1.getDeparturePlace());
		destination2.setDepartureDate(destination1.getDepartureDate());
		destination2.setId((Long) destination1.getId());
		// execute
		fixture.addInvoiceDestination(destination1);
		fixture.addInvoiceDestination(destination2);
		// validate
		SortedSet<InvoiceDestination> invoiceDestinations = fixture.getInvoiceDestinations();
		Assert.assertEquals(1, invoiceDestinations.size());
		Assert.assertTrue(invoiceDestinations.contains(destination1));
	}

	@Test
	public void testOnlyUniqueTravelersAreAdded() {
		// setup
		InvoiceTraveler traveler1 = InvoiceHelper.makeTraveler();
		InvoiceTraveler traveler2 = InvoiceHelper.makeTraveler();
		traveler2.setSalutation(traveler1.getSalutation());
		traveler2.setFirstName(traveler1.getFirstName());
		traveler2.setLastName(traveler1.getLastName());
		traveler2.setId((Long) traveler1.getId());
		// execute
		fixture.addInvoiceTraveler(traveler1);
		fixture.addInvoiceTraveler(traveler2);
		// validate
		Assert.assertEquals(1, fixture.getInvoiceTravelers().size());
		Assert.assertTrue(fixture.getInvoiceTravelers().contains(traveler1));
	}

	@Test
	public void testUniqueTravelersAreAdded() {
		// setup
		InvoiceTraveler traveler1 = InvoiceHelper.makeTraveler();
		InvoiceTraveler traveler2 = InvoiceHelper.makeTraveler();
		InvoiceTraveler traveler3 = InvoiceHelper.makeTraveler();
		// execute
		fixture.addInvoiceTraveler(traveler1);
		fixture.addInvoiceTraveler(traveler2);
		fixture.addInvoiceTraveler(traveler3);
		// validate
		SortedSet<InvoiceTraveler> invoiceTravelers = fixture.getInvoiceTravelers();
		Assert.assertEquals(3, invoiceTravelers.size());
		Assert.assertTrue(invoiceTravelers.contains(traveler1));
		Assert.assertTrue(invoiceTravelers.contains(traveler2));
		Assert.assertTrue(invoiceTravelers.contains(traveler3));
	}

	@Test
	public void testInvoiceTotalPaid() {
		InvoicePayment payment1 = InvoiceHelper.makePayment();
		InvoicePayment payment2 = InvoiceHelper.makePayment();
		InvoicePayment payment3 = InvoiceHelper.makePayment();

		fixture.addInvoicePayment(payment1);
		fixture.addInvoicePayment(payment2);
		fixture.addInvoicePayment(payment3);

		BigDecimal expected = MoneyUtils.ZERO_VALUE;
		expected = expected.add(payment1.getAmount());
		expected = expected.add(payment2.getAmount());
		expected = expected.add(payment3.getAmount());

		Assert.assertEquals(expected, fixture.getSalesAmounts().getPaid());
		Assert.assertEquals(MoneyUtils.format(expected), fixture.getSalesAmounts().getPaidAsString());
	}

	@Test
	public void testInvoiceTotalTax() {
		InvoiceItem item1 = InvoiceHelper.makeItem();
		InvoiceItem item2 = InvoiceHelper.makeItem();
		InvoiceItem item3 = InvoiceHelper.makeItem();

		fixture.addInvoiceItem(item1);
		fixture.addInvoiceItem(item2);
		fixture.addInvoiceItem(item3);

		BigDecimal expected = MoneyUtils.ZERO_VALUE;
		expected = expected.add(item1.getSalesAmounts().getTax());
		expected = expected.add(item2.getSalesAmounts().getTax());
		expected = expected.add(item3.getSalesAmounts().getTax());

		Assert.assertEquals(expected, fixture.getSalesAmounts().getTax());
		Assert.assertEquals(MoneyUtils.format(expected), fixture.getSalesAmounts().getTaxAsString());
	}

	@Test
	public void testInvoiceTotalPrice() {
		InvoiceItem item1 = InvoiceHelper.makeItem();
		InvoiceItem item2 = InvoiceHelper.makeItem();
		InvoiceItem item3 = InvoiceHelper.makeItem();

		fixture.addInvoiceItem(item1);
		fixture.addInvoiceItem(item2);
		fixture.addInvoiceItem(item3);

		BigDecimal expected = MoneyUtils.ZERO_VALUE;
		expected = expected.add(item1.getSalesAmounts().getPrice());
		expected = expected.add(item2.getSalesAmounts().getPrice());
		expected = expected.add(item3.getSalesAmounts().getPrice());

		Assert.assertEquals(expected, fixture.getSalesAmounts().getPrice());
		Assert.assertEquals(MoneyUtils.format(expected), fixture.getSalesAmounts().getPriceAsString());
	}

	@Test
	public void testInvoiceTotalSale() {
		InvoiceItem item1 = InvoiceHelper.makeItem();
		InvoiceItem item2 = InvoiceHelper.makeItem();
		InvoiceItem item3 = InvoiceHelper.makeItem();

		fixture.addInvoiceItem(item1);
		fixture.addInvoiceItem(item2);
		fixture.addInvoiceItem(item3);

		BigDecimal expected = fixture.getSalesAmounts().getPrice().add(fixture.getSalesAmounts().getTax());

		Assert.assertEquals(expected, fixture.getSalesAmounts().getSale());
		Assert.assertEquals(MoneyUtils.format(expected), fixture.getSalesAmounts().getSaleAsString());
	}

	@Test
	public void testInvoiceTotalDue() {
		InvoicePayment payment = InvoiceHelper.makePayment();
		fixture.addInvoicePayment(payment);
		InvoiceItem item = InvoiceHelper.makeItem();
		fixture.addInvoiceItem(item);

		BigDecimal expected = MoneyUtils.round(fixture.getSalesAmounts().getSale().subtract(fixture.getSalesAmounts().getPaid()));

		Assert.assertEquals(expected, fixture.getSalesAmounts().getDue());
		Assert.assertEquals(MoneyUtils.format(expected), fixture.getSalesAmounts().getDueAsString());
	}

	@Test
	public void testInvoiceTotalCost() {
		InvoiceItem item1 = InvoiceHelper.makeItem();
		InvoiceItem item2 = InvoiceHelper.makeItem();
		InvoiceItem item3 = InvoiceHelper.makeItem();

		fixture.addInvoiceItem(item1);
		fixture.addInvoiceItem(item2);
		fixture.addInvoiceItem(item3);

		BigDecimal expected = MoneyUtils.ZERO_VALUE;
		expected = expected.add(item1.getSalesAmounts().getCost());
		expected = expected.add(item2.getSalesAmounts().getCost());
		expected = expected.add(item3.getSalesAmounts().getCost());

		Assert.assertEquals(expected, fixture.getSalesAmounts().getCost());
		Assert.assertEquals(MoneyUtils.format(expected), fixture.getSalesAmounts().getCostAsString());
	}

	@Test
	public void testInvoiceTotalTaxOnCommission() {
		InvoiceItem item1 = InvoiceHelper.makeItem();
		InvoiceItem item2 = InvoiceHelper.makeItem();
		InvoiceItem item3 = InvoiceHelper.makeItem();

		fixture.addInvoiceItem(item1);
		fixture.addInvoiceItem(item2);
		fixture.addInvoiceItem(item3);

		BigDecimal expected = MoneyUtils.ZERO_VALUE;
		expected = expected.add(item1.getSalesAmounts().getTaxOnCommission());
		expected = expected.add(item2.getSalesAmounts().getTaxOnCommission());
		expected = expected.add(item3.getSalesAmounts().getTaxOnCommission());

		Assert.assertEquals(expected, fixture.getSalesAmounts().getTaxOnCommission());
		Assert.assertEquals(MoneyUtils.format(expected), fixture.getSalesAmounts().getTaxOnCommissionAsString());
	}

	@Test
	public void testInvoiceTotalCommission() {
		InvoiceItem item1 = InvoiceHelper.makeItem();
		InvoiceItem item2 = InvoiceHelper.makeItem();
		InvoiceItem item3 = InvoiceHelper.makeItem();

		fixture.addInvoiceItem(item1);
		fixture.addInvoiceItem(item2);
		fixture.addInvoiceItem(item3);

		BigDecimal expected = MoneyUtils.ZERO_VALUE;
		expected = expected.add(item1.getSalesAmounts().getCommission());
		expected = expected.add(item2.getSalesAmounts().getCommission());
		expected = expected.add(item3.getSalesAmounts().getCommission());

		Assert.assertEquals(expected, fixture.getSalesAmounts().getCommission());
		Assert.assertEquals(MoneyUtils.format(expected), fixture.getSalesAmounts().getCommissionAsString());
	}

	@Test
	public void testInvoiceName() {
		fixture.setId(1L);
		Assert.assertEquals("0000000001", fixture.getName());
		fixture.setId(1111L);
		Assert.assertEquals("0000001111", fixture.getName());
		fixture.setId(99119911L);
		Assert.assertEquals("0099119911", fixture.getName());
		fixture.setId(1111111111L);
		Assert.assertEquals("1111111111", fixture.getName());

	}

	@Test
	public void testAddingItemsSetsIndex() {
		// setup
		InvoiceItem item1 = InvoiceHelper.makeItem();
		InvoiceItem item2 = InvoiceHelper.makeItem();
		InvoiceItem item3 = InvoiceHelper.makeItem();
		// execute
		fixture.addInvoiceItem(item1);
		fixture.addInvoiceItem(item2);
		fixture.addInvoiceItem(item3);
		// validate
		Assert.assertEquals(0, item1.getIndex().intValue());
		Assert.assertEquals(1, item2.getIndex().intValue());
		Assert.assertEquals(2, item3.getIndex().intValue());

		List<InvoiceItem> items = fixture.getInvoiceItems();
		Assert.assertEquals(3, items.size());
		Assert.assertEquals(items.get(0), item1);
		Assert.assertEquals(items.get(1), item2);
		Assert.assertEquals(items.get(2), item3);
	}

	@Test
	public void testDeletingItemsResetsIndex() {
		// setup
		InvoiceItem item1 = InvoiceHelper.makeItem();
		InvoiceItem item2 = InvoiceHelper.makeItem();
		InvoiceItem item3 = InvoiceHelper.makeItem();
		fixture.addInvoiceItem(item1);
		fixture.addInvoiceItem(item2);
		fixture.addInvoiceItem(item3);
		// execute
		fixture.removeInvoiceItem(item1);
		// validate
		Assert.assertNull(item1.getInvoice());
		Assert.assertEquals(0, item2.getIndex().intValue());
		Assert.assertEquals(1, item3.getIndex().intValue());

		List<InvoiceItem> items = fixture.getInvoiceItems();
		Assert.assertEquals(2, items.size());
		Assert.assertEquals(items.get(0), item2);
		Assert.assertEquals(items.get(1), item3);
		Assert.assertFalse(items.contains(item1));
	}

	@Test
	public void testAddingItemSetInvoiceOnItem() {
		// setup
		InvoiceItem item = InvoiceHelper.makeItem();
		// execute
		fixture.addInvoiceItem(item);
		// validate
		Assert.assertEquals(fixture, item.getInvoice());
		Assert.assertEquals(1, fixture.getInvoiceItems().size());
		Assert.assertEquals(item, fixture.getInvoiceItems().get(0));
	}

	@Test
	public void testDeletingItemRemovesInvoiceFromItem() {
		// setup
		InvoiceItem item = InvoiceHelper.makeItem();
		fixture.addInvoiceItem(item);
		// execute
		fixture.removeInvoiceItem(item);
		// validate
		Assert.assertNull(item.getInvoice());
		Assert.assertEquals(0, fixture.getInvoiceItems().size());
	}

	@Test
	public void testNoCommissionReceived() {
		// setup
		fixture.addInvoiceItem(InvoiceHelper.makeItem());
		fixture.addInvoiceItem(InvoiceHelper.makeItem());
		fixture.addInvoiceItem(InvoiceHelper.makeItem());
		// execute & validate
		Assert.assertEquals(MoneyUtils.ZERO_VALUE, fixture.getSalesAmounts().getCommissionReceived());
	}

	@Test
	public void testCommissionReceived() {
		// setup
		fixture.addInvoiceItem(makeItemWithCommissionReceived());
		fixture.addInvoiceItem(makeItemWithCommissionReceived());
		fixture.addInvoiceItem(makeItemWithCommissionReceived());
		// execute & validate
		Assert.assertEquals(fixture.getSalesAmounts().getCommission(), fixture.getSalesAmounts().getCommissionReceived());
	}

	@Test
	public void testPartialCommissionReceived() {
		// setup
		fixture.addInvoiceItem(makeItemWithCommissionReceived());
		fixture.addInvoiceItem(InvoiceHelper.makeItem());
		fixture.addInvoiceItem(makeItemWithCommissionReceived());
		// execute & validate
		Assert.assertFalse(fixture.getSalesAmounts().getCommission().equals(fixture.getSalesAmounts().getCommissionReceived()));
	}

	@Test
	public void testNoCommissionVerified() {
		// setup
		fixture.addInvoiceItem(InvoiceHelper.makeItem());
		fixture.addInvoiceItem(InvoiceHelper.makeItem());
		fixture.addInvoiceItem(InvoiceHelper.makeItem());
		// execute & validate
		Assert.assertEquals(MoneyUtils.ZERO_VALUE, fixture.getSalesAmounts().getCommissionVerified());
	}

	@Test
	public void testCommissionVerified() {
		// setup
		fixture.addInvoiceItem(makeItemWithCommissionVerified());
		fixture.addInvoiceItem(makeItemWithCommissionVerified());
		fixture.addInvoiceItem(makeItemWithCommissionVerified());
		// execute & validate
		Assert.assertEquals(fixture.getSalesAmounts().getCommission(), fixture.getSalesAmounts().getCommissionVerified());
	}

	@Test
	public void testPartialCommissionVerified() {
		// setup
		fixture.addInvoiceItem(makeItemWithCommissionVerified());
		fixture.addInvoiceItem(InvoiceHelper.makeItem());
		fixture.addInvoiceItem(makeItemWithCommissionVerified());
		// execute & validate
		Assert.assertFalse(fixture.getSalesAmounts().getCommission().equals(fixture.getSalesAmounts().getCommissionVerified()));
	}

	private InvoiceItem makeItemWithCommissionReceived() {
		InvoiceItem item = InvoiceHelper.makeItem();
		item.setCommissionStatus(CommissionStatus.Received);
		return item;
	}

	private InvoiceItem makeItemWithCommissionVerified() {
		InvoiceItem item = InvoiceHelper.makeItem();
		item.setCommissionStatus(CommissionStatus.Verified);
		return item;
	}

	@Test
	public void testIsActiveStatus() {
		Invoice invoice = InvoiceHelper.makeInvoice();
		Assert.assertTrue(invoice.isActive());

		invoice.setStatus(InvoiceStatus.Voided);
		Assert.assertFalse(invoice.isActive());

		invoice.setStatus(InvoiceStatus.Closed);
		Assert.assertFalse(invoice.isActive());
	}

	@Test
	public void testIsVoidedStatus() {
		Invoice invoice = InvoiceHelper.makeInvoice();
		Assert.assertFalse(invoice.isVoided());

		invoice.setStatus(InvoiceStatus.Voided);
		Assert.assertTrue(invoice.isVoided());

		invoice.setStatus(InvoiceStatus.Closed);
		Assert.assertFalse(invoice.isVoided());
	}

	@Test
	public void testMoveUpItem() {
		// setup
		InvoiceItem item1 = InvoiceHelper.makeItem();
		InvoiceItem item2 = InvoiceHelper.makeItem();
		InvoiceItem item3 = InvoiceHelper.makeItem();
		fixture.addInvoiceItem(item1);
		fixture.addInvoiceItem(item2);
		fixture.addInvoiceItem(item3);
		// execute
		fixture.moveUpItem(item3);
		// validate
		Assert.assertEquals(item1, fixture.getInvoiceItems().get(0));
		Assert.assertEquals(item2, fixture.getInvoiceItems().get(2));
		Assert.assertEquals(item3, fixture.getInvoiceItems().get(1));
	}

	@Test
	public void testMoveUpFirstItem() {
		// setup
		InvoiceItem item1 = InvoiceHelper.makeItem();
		InvoiceItem item2 = InvoiceHelper.makeItem();
		InvoiceItem item3 = InvoiceHelper.makeItem();
		fixture.addInvoiceItem(item1);
		fixture.addInvoiceItem(item2);
		fixture.addInvoiceItem(item3);
		// execute
		fixture.moveUpItem(item1);
		// validate
		Assert.assertEquals(item1, fixture.getInvoiceItems().get(0));
		Assert.assertEquals(item2, fixture.getInvoiceItems().get(1));
		Assert.assertEquals(item3, fixture.getInvoiceItems().get(2));
	}

	@Test
	public void testMoveDownItem() {
		// setup
		InvoiceItem item1 = InvoiceHelper.makeItem();
		InvoiceItem item2 = InvoiceHelper.makeItem();
		InvoiceItem item3 = InvoiceHelper.makeItem();
		fixture.addInvoiceItem(item1);
		fixture.addInvoiceItem(item2);
		fixture.addInvoiceItem(item3);
		// execute
		fixture.moveDownItem(item1);
		// validate
		Assert.assertEquals(item1, fixture.getInvoiceItems().get(1));
		Assert.assertEquals(item2, fixture.getInvoiceItems().get(0));
		Assert.assertEquals(item3, fixture.getInvoiceItems().get(2));
	}

	@Test
	public void testMoveDownLastItem() {
		// setup
		InvoiceItem item1 = InvoiceHelper.makeItem();
		InvoiceItem item2 = InvoiceHelper.makeItem();
		InvoiceItem item3 = InvoiceHelper.makeItem();
		fixture.addInvoiceItem(item1);
		fixture.addInvoiceItem(item2);
		fixture.addInvoiceItem(item3);
		// execute
		fixture.moveDownItem(item3);
		// validate
		Assert.assertEquals(item1, fixture.getInvoiceItems().get(0));
		Assert.assertEquals(item2, fixture.getInvoiceItems().get(1));
		Assert.assertEquals(item3, fixture.getInvoiceItems().get(2));
	}

	@Test
	public void testStatusIsClosedWhenCommissionStatusVerified() {
		// setup
		fixture.addInvoiceItem(makeItemWithCommissionVerified());
		// execute
		fixture.closeInvoice();
		// validate
		Assert.assertEquals(InvoiceStatus.Closed, fixture.getStatus());
	}

	@Test
	public void testStatusIsNotClosedWhenCommissionStatusReceived() {
		// setup
		fixture.addInvoiceItem(makeItemWithCommissionReceived());
		// execute
		fixture.closeInvoice();
		// validate
		Assert.assertEquals(InvoiceStatus.Active, fixture.getStatus());
	}

	@Test
	public void testStatusIsNotClosedWhenCommissionStatusNone() {
		// setup
		fixture.addInvoiceItem(InvoiceHelper.makeItem());
		// execute
		fixture.closeInvoice();
		// validate
		Assert.assertEquals(InvoiceStatus.Active, fixture.getStatus());
	}

	@Test
	public void testNoItemsDoesNotChangesStatusToClosed() {
		// execute
		fixture.closeInvoice();
		// validate
		Assert.assertEquals(InvoiceStatus.Active, fixture.getStatus());
	}

	@Test
	public void testVoidedStatusDoesNotChangesStatusToClosed() {
		// setup
		fixture.addInvoiceItem(makeItemWithCommissionVerified());
		fixture.setStatus(InvoiceStatus.Voided);
		// execute
		fixture.closeInvoice();
		// validate
		Assert.assertEquals(InvoiceStatus.Voided, fixture.getStatus());
	}

	@Test
	public void testInvoiceNotes() {
		// setup
		InvoiceNote note1 = InvoiceHelper.makeNote(); note1.setPrivateNote(true);
		InvoiceNote note2 = InvoiceHelper.makeNote();
		// execute
		fixture.addInvoiceNote(note1);
		fixture.addInvoiceNote(note2);
		// validate
		SortedSet<InvoiceNote> invoiceNotes = fixture.getInvoiceNotes();
		Assert.assertEquals(2, invoiceNotes.size());
		Assert.assertTrue(invoiceNotes.contains(note1));
		Assert.assertTrue(invoiceNotes.contains(note2));
	}

	@Test
	public void testPrivateInvoiceNotes() {
		// setup
		InvoiceNote note1 = InvoiceHelper.makeNote(); note1.setPrivateNote(true);
		InvoiceNote note2 = InvoiceHelper.makeNote();
		// execute
		fixture.addInvoiceNote(note1);
		fixture.addInvoiceNote(note2);
		// validate
		Collection<InvoiceNote> invoiceNotes = fixture.getPublicInvoiceNotes();
		Assert.assertEquals(1, invoiceNotes.size());
		Assert.assertTrue(invoiceNotes.contains(note2));
	}

	@Test
	public void testAddingPaymentSetInvoiceOnPayment() {
		// setup
		InvoicePayment payment = InvoiceHelper.makePayment();
		// execute
		fixture.addInvoicePayment(payment);
		// validate
		Assert.assertEquals(fixture, payment.getInvoice());
		Assert.assertEquals(1, fixture.getInvoicePayments().size());
		Assert.assertEquals(payment, fixture.getInvoicePayments().first());
	}

	@Test
	public void testDeletingPaymentRemovesInvoiceFromPayment() {
		// setup
		InvoicePayment payment = InvoiceHelper.makePayment();
		fixture.addInvoicePayment(payment);
		// execute
		fixture.removeInvoicePayment(payment);
		// validate
		Assert.assertNull(payment.getInvoice());
		Assert.assertEquals(0, fixture.getInvoicePayments().size());
	}

}
