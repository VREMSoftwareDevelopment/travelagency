var TravelAgency = (function() {
	return {
		tabs : function() {
			var detailsTab = $(".detailsTab").find(".tab-row");
			detailsTab.find("ul")
				.removeClass("ui-tabs-nav").addClass("ui-tabs-nav")
				.find(".selected")
					.removeClass("ui-tabs-selected").addClass("ui-tabs-selected")
					.removeClass("ui-state-active").addClass("ui-state-active");
			detailsTab.find("li")
				.removeClass("ui-state-default").addClass("ui-state-default")
				.removeClass("ui-corner-top").addClass("ui-corner-top");
			detailsTab.find("li")
				.mouseover(function() {
					$(this).addClass("ui-state-hover");
				})
				.mouseout(function() {
					$(this).removeClass("ui-state-hover");
				});
		},

		buttons : function() {
			$('.button').button();
			$('.button').removeClass("ui-state-focus");
		},

		phones: function() {
			$('.phone').attr('placeholder', '(###) ###-#### ext:####');
		},

		signInPanel : function() {
			var signInPanel = $(".signInPanel");
			signInPanel.find("input[type='submit']").removeClass("button").addClass("button");
			signInPanel.find("input[type='reset']").removeClass("button").addClass("button");
		},

		rowsOddEven : function() {
			$(".dataview tbody tr:odd")
				.removeClass("odd")
				.removeClass("even")
				.addClass("odd");

			$(".dataview tbody tr:even")
				.removeClass("odd")
				.removeClass("even")
				.addClass("even");
		},

		init: function() {
			TravelAgency.phones();
			TravelAgency.signInPanel();
			TravelAgency.tabs();
			TravelAgency.buttons();
			TravelAgency.rowsOddEven();
		}
	};
})();
