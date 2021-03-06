/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2010-2012 Ephesoft Inc. 
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

package com.ephesoft.dcma.gwt.admin.bm.client.view.tableinfo;

import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.tableinfo.TableInfoListPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView;
import com.ephesoft.dcma.gwt.core.client.ui.table.TableHeader.HeaderColumn;

/**
 * This class provides functionality to show table info list view.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class TableInfoListView extends View<TableInfoListPresenter> {

	/**
	 * To get Table Info List View.
	 * 
	 * @return
	 */
	public ListView getTableInfoListView() {
		return listView;
	}

	/**
	 * name HeaderColumn.
	 */
	public HeaderColumn name = new HeaderColumn(1, LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.NAME),
			BatchClassManagementConstants.FIFTEEN);

	/**
	 * startPattern HeaderColumn.
	 */
	public HeaderColumn startPattern = new HeaderColumn(2, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.START_PATTERN), BatchClassManagementConstants.EIGHTEEN);

	/**
	 * endPattern HeaderColumn.
	 */
	public HeaderColumn endPattern = new HeaderColumn(BatchClassManagementConstants.THREE, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.END_PATTERN), BatchClassManagementConstants.EIGHTEEN);

	/**
	 * tableExtractionAPI HeaderColumn.
	 */
	public HeaderColumn tableExtractionAPI = new HeaderColumn(BatchClassManagementConstants.FOUR, LocaleDictionary.get()
			.getConstantValue(BatchClassManagementConstants.TABLE_EXTRACTION_TECHNIQUE), BatchClassManagementConstants.TWENTY);

	/**
	 * widthOfMultiline HeaderColumn.
	 */
	public HeaderColumn widthOfMultiline = new HeaderColumn(BatchClassManagementConstants.FIVE, LocaleDictionary.get()
			.getConstantValue(BatchClassManagementConstants.WIDTH_OF_MULTILINE), BatchClassManagementConstants.EIGHTEEN);

	/**
	 * listView ListView.
	 */
	public ListView listView = new ListView();

	/**
	 * Constructor.
	 */
	public TableInfoListView() {
		super();
		listView.addHeaderColumns(name, startPattern, endPattern, tableExtractionAPI, widthOfMultiline);
	}
}
