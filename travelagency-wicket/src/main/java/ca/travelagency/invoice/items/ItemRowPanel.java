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
package ca.travelagency.invoice.items;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ContextRelativeResource;

import ca.travelagency.components.decorators.BlockUIDecorator;
import ca.travelagency.components.formdetail.DeletePanel;
import ca.travelagency.components.formdetail.EditPanel;
import ca.travelagency.invoice.InvoiceItem;
import ca.travelagency.invoice.SalesAmounts;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.utils.DateUtils;

public class ItemRowPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public static final String DELETE_BUTTON = "deleteButton";
	public static final String EDIT_BUTTON = "editButton";

	public static final String MOVE_UP = "moveUp";
	public static final String MOVE_DOWN = "moveDown";
	public static final String MOVE_LABEL = "label";

	public ItemRowPanel(final String id, IModel<InvoiceItem> model, final ItemsPanel itemsPanel) {
		super(id, model);
		setOutputMarkupId(true);

		add(new Label(InvoiceItem.Properties.description.name()));
		add(new Label(InvoiceItem.Properties.supplier.name()));
		add(new Label(InvoiceItem.Properties.commissionAsString.name()));
		add(new Label(InvoiceItem.Properties.salesAmounts.name() + Condition.SEPARATOR + SalesAmounts.Properties.commissionAsString));
		add(new Label(InvoiceItem.Properties.taxOnCommissionAsString.name()));
		add(new Label(InvoiceItem.Properties.priceAsString.name()));
		add(new Label(InvoiceItem.Properties.taxAsString.name()));
		add(new Label(InvoiceItem.Properties.cancelBeforeDeparture.name()));
		add(new Label(InvoiceItem.Properties.changeBeforeDeparture.name()));
		add(new Label(InvoiceItem.Properties.changeAfterDeparture.name()));
		add(new Label(InvoiceItem.Properties.qty.name()));
		add(new Label(InvoiceItem.Properties.salesAmounts.name() + Condition.SEPARATOR + SalesAmounts.Properties.saleAsString.name()));
		add(new Label(InvoiceItem.Properties.bookingNumber.name()));
		add(DateLabel.forDateStyle(InvoiceItem.Properties.bookingDate.name(), DateUtils.DATE_STYLE));
		add(new Label(InvoiceItem.Properties.commissionStatus.name()));

        add(makeMoveLink(MOVE_UP, itemsPanel)
        	.setVisible(getInvoiceItem().getIndex() > 0));

        add(makeMoveLink(MOVE_DOWN, itemsPanel)
        	.setVisible(getInvoiceItem().getIndex() < itemsPanel.getSize()-1));

		add(new DeletePanel<InvoiceItem>(DELETE_BUTTON, getInvoiceItem(), itemsPanel) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				itemsPanel.delete(target, getInvoiceItem());
			}
		}.setVisible(isVisible(model, itemsPanel)));

        add(new EditPanel(EDIT_BUTTON) {
    		private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
    			ItemFormPanel itemFormPanel = new ItemFormPanel(id, getInvoiceItem(), itemsPanel);
    			ItemRowPanel.this.replaceWith(itemFormPanel);
    			target.add(itemFormPanel);
			}
        }.setVisible(isVisible(model, itemsPanel)));

	}

	private boolean isVisible(IModel<InvoiceItem> model, ItemsPanel itemsPanel) {
		return itemsPanel.isEditable() && !model.getObject().isCommissionVerified();
	}

	private IndicatingAjaxLink<Void> makeMoveLink(final String id, final ItemsPanel itemsPanel) {
		IndicatingAjaxLink<Void> link = new IndicatingAjaxLink<Void>(id) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				if (MOVE_UP.equals(id)) {
					itemsPanel.moveUp(target, getInvoiceItem());
				} else {
					itemsPanel.moveDown(target, getInvoiceItem());
				}
			}
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.getAjaxCallListeners().add(new BlockUIDecorator());
			}
		};

		ContextRelativeResource contextRelativeResource =
			new ContextRelativeResource("images/" + (MOVE_UP.equals(id) ? "move_up.png" : "move_down.png"));
		link.add(new Image(MOVE_LABEL, contextRelativeResource));
		return link;
	}

	private InvoiceItem getInvoiceItem() {
		return (InvoiceItem) getDefaultModelObject();
	}
}
