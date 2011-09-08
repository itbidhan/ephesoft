/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2010-2011 Ephesoft Inc. 
* 
* This program is free software; you can redistribute it and/or modify it under 
* the terms of the GNU Affero General Public License version 3 as published by the 
* Free Software Foundation with the addition of the following permission added 
* to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK 
* IN WHICH THE COPYRIGHT IS OWNED BY EPHESOFT, EPHESOFT DISCLAIMS THE WARRANTY 
* OF NON INFRINGEMENT OF THIRD PARTY RIGHTS. 
* 
* This program is distributed in the hope that it will be useful, but WITHOUT 
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
* FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more 
* details. 
* 
* You should have received a copy of the GNU Affero General Public License along with 
* this program; if not, see http://www.gnu.org/licenses or write to the Free 
* Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
* 02110-1301 USA. 
* 
* You can contact Ephesoft, Inc. headquarters at 111 Academy Way, 
* Irvine, CA 92617, USA. or at email address info@ephesoft.com. 
* 
* The interactive user interfaces in modified source and object code versions 
* of this program must display Appropriate Legal Notices, as required under 
* Section 5 of the GNU Affero General Public License version 3. 
* 
* In accordance with Section 7(b) of the GNU Affero General Public License version 3, 
* these Appropriate Legal Notices must retain the display of the "Ephesoft" logo. 
* If the display of the logo is not reasonably feasible for 
* technical reasons, the Appropriate Legal Notices must display the words 
* "Powered by Ephesoft". 
********************************************************************************/ 

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin;

import java.util.Collection;

import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.PluginView;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginConfigDTO;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;

public class PluginViewPresenter extends AbstractBatchClassPresenter<PluginView> implements ValueChangeHandler<String> {

	private final PluginDetailPresenter pluginDetailPresenter;
	private final EditPluginPresenter editPluginPresenter;
	private final PluginDataPresenter pluginDataPresenter;

	public PluginViewPresenter(BatchClassManagementController controller, PluginView view) {
		super(controller, view);
		this.pluginDataPresenter = new PluginDataPresenter(controller, view.getPluginDataView());
		this.pluginDetailPresenter = new PluginDetailPresenter(controller, view.getPluginDetailView());
		this.editPluginPresenter = new EditPluginPresenter(controller, view.getEditPluginView());
	}

	public PluginDetailPresenter getPluginDetailPresenter() {
		return pluginDetailPresenter;
	}

	public EditPluginPresenter getEditPluginPresenter() {
		return editPluginPresenter;
	}

	public PluginDataPresenter getPluginDataPresenter() {
		return pluginDataPresenter;
	}

	@Override
	public void bind() {
		if (controller.getSelectedPlugin() != null) {
			this.pluginDataPresenter.bind();
			this.pluginDetailPresenter.bind();
			this.editPluginPresenter.bind();
			view.getPluginViewVerticalPanel().setVisible(Boolean.TRUE);
			view.getPluginConfigVerticalPanel().setVisible(Boolean.FALSE);
			Collection<BatchClassPluginConfigDTO> values = controller.getSelectedPlugin().getBatchClassPluginConfigs();
			if (values != null && values.isEmpty()) {
				view.getEditButton().setVisible(false);
				view.getNoResuleLabel().setVisible(true);
			} else {
				view.getEditButton().setVisible(true);
				view.getNoResuleLabel().setVisible(false);
			}
			view.getEditButton().addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					view.getPluginViewVerticalPanel().setVisible(Boolean.FALSE);
					view.getPluginConfigVerticalPanel().setVisible(Boolean.TRUE);
				}
			});
			// rpc calls
		}
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onValueChange(ValueChangeEvent<String> arg0) {
		// TODO Auto-generated method stub

	}

	public void showPluginViewDetail() {
		view.getPluginViewVerticalPanel().setVisible(Boolean.TRUE);
		view.getPluginConfigVerticalPanel().setVisible(Boolean.FALSE);
	}
}
