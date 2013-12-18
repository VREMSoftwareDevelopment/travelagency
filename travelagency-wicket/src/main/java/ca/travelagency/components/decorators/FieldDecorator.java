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
package ca.travelagency.components.decorators;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.IAjaxRegionMarkupIdProvider;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FeedbackMessages;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.request.Response;
import org.apache.wicket.util.string.Strings;

public class FieldDecorator extends Behavior implements IAjaxRegionMarkupIdProvider {
	private static final long serialVersionUID = 1L;

	private enum DisplayLabel {
		None,
		Inline,
		Above
	}

	private DisplayLabel displayLabel;

	public static FieldDecorator doNotDisplayLabel() {
		return new FieldDecorator(DisplayLabel.None);
	}

	public static FieldDecorator inlineDisplayLabel() {
		return new FieldDecorator(DisplayLabel.Inline);
	}

	public FieldDecorator() {
		this(DisplayLabel.Above);
	}

	private FieldDecorator(DisplayLabel displayLabel) {
		this.displayLabel = displayLabel;
	}

	@Override
	public void bind(Component component) {
		component.setOutputMarkupId(true);
	}

	@Override
	public void beforeRender(Component component) {
		FormComponent<?> formComponent = (FormComponent<?>) component;
		Response response = component.getResponse();

		response.write("<span id=\"");
		response.write(getAjaxRegionMarkupId(component));
		response.write("\">");

		String label = (formComponent.getLabel() != null) ? formComponent.getLabel().getObject() : null;
		if (!DisplayLabel.None.equals(displayLabel) && label != null) {
			response.write("<label for=\"");
			response.write(formComponent.getMarkupId());
			response.write("\" class=\"label");
			if (DisplayLabel.Inline.equals(displayLabel)) {
				response.write("Inline");
			}
			if (!formComponent.isValid()) {
				response.write(" error");
			}
			response.write("\">");
			response.write(Strings.escapeMarkup(label));
			if (formComponent.isRequired()) {
				response.write("<span class=\"required\">*</span>");
			}
			response.write("</label>");
		}

		super.beforeRender(component);
	}

	@Override
	public void afterRender(Component component) {
		Response response = component.getResponse();
		if (component.hasFeedbackMessage()) {
			response.write("<ul class=\"feedbackPanel\">");
			FeedbackMessages feedbackMessages = component.getFeedbackMessages();
			for (FeedbackMessage message : feedbackMessages) {
				response.write("<li class=\"feedbackPanel");
				response.write(message.getLevelAsString().toUpperCase());
				response.write("\">");
				response.write(Strings.escapeMarkup(message.getMessage().toString()));
				response.write("</li>");
				message.markRendered();
			}
			response.write("</ul>");
		}
		response.write("</span>");

		super.afterRender(component);
	}

	@Override
	public void onComponentTag(Component component, ComponentTag tag) {
		FormComponent<?> formComponent = (FormComponent<?>) component;
		if (formComponent.isValid()) {
			return;
		}
		String htmlClass = tag.getAttribute("class");
		if (htmlClass == null) {
			tag.put("class", "error");
		} else {
			tag.put("class", "error " + htmlClass);
		}
	}

	@Override
	public String getAjaxRegionMarkupId(Component component) {
		return component.getMarkupId() + "_fd";
	}
}
