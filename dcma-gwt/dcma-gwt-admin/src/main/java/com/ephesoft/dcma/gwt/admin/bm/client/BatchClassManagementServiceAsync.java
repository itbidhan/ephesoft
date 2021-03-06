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

package com.ephesoft.dcma.gwt.admin.bm.client;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.gwt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchFolderListDTO;
import com.ephesoft.dcma.gwt.core.shared.DocumentTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.EmailConfigurationDTO;
import com.ephesoft.dcma.gwt.core.shared.ImportBatchClassSuperConfig;
import com.ephesoft.dcma.gwt.core.shared.ImportBatchClassUserOptionDTO;
import com.ephesoft.dcma.gwt.core.shared.KVExtractionDTO;
import com.ephesoft.dcma.gwt.core.shared.ModuleDTO;
import com.ephesoft.dcma.gwt.core.shared.OutputDataCarrierDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.ephesoft.dcma.gwt.core.shared.RoleDTO;
import com.ephesoft.dcma.gwt.core.shared.SamplePatternDTO;
import com.ephesoft.dcma.gwt.core.shared.TableInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.TestTableResultDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BatchClassManagementServiceAsync extends DCMARemoteServiceAsync {

	void getAllBatchClasses(AsyncCallback<List<BatchClassDTO>> callback);

	void getBatchClass(String batchClassIdentifier, AsyncCallback<BatchClassDTO> callback);

	void updateBatchClass(BatchClassDTO batchClassDTO, AsyncCallback<BatchClassDTO> callback);

	void learnFileForBatchClass(String batchClassID, AsyncCallback<Void> callback);

	void sampleGeneration(List<String> batchClassIDList, AsyncCallback<Void> callback);

	void getAllTables(String driverName, String url, String userName, String password,
			AsyncCallback<Map<String, List<String>>> callback);

	void getAllColumnsForTable(String driverName, String url, String userName, String password, String tableName,
			AsyncCallback<Map<String, String>> callback);

	void getDocumentLevelFields(String documentName, String batchClassId, AsyncCallback<Map<String, String>> callback);

	void learnDataBase(final String batchClassId, final boolean createIndex, AsyncCallback<Void> callback);

	void copyBatchClass(BatchClassDTO batchClassDTO, AsyncCallback<Void> callback);

	void getBatchFolderList(AsyncCallback<BatchFolderListDTO> callback);

	void getBatchClasses(int firstResult, int maxResults, Order order, AsyncCallback<List<BatchClassDTO>> callback);

	void countAllBatchClassesExcludeDeleted(AsyncCallback<Integer> callback);

	void createUncFolder(String path, AsyncCallback<Void> callback);

	void getProjectFilesForDocumentType(String batchClassIdentifier, String documentTypeName, AsyncCallback<List<String>> callback);

	void testKVExtraction(BatchClassDTO batchClassDTO, KVExtractionDTO kvExtractionDTO, String testImageFilePath,
			boolean isTestAdvancedKV, AsyncCallback<List<OutputDataCarrierDTO>> callback);

	void getAllRoles(AsyncCallback<List<RoleDTO>> callback);

	void getAllBarcodeTypes(AsyncCallback<List<String>> callback);

	void deleteBatchClass(BatchClassDTO batchClassDTO, AsyncCallback<BatchClassDTO> callback);

	void matchBaseFolder(String uncFolder, AsyncCallback<String> callback);

	void copyDocument(DocumentTypeDTO documentTypeDTO, AsyncCallback<DocumentTypeDTO> asyncCallback);

	void getAdvancedKVImageUploadPath(String batchClassId, String docName, String imageName, AsyncCallback<String> asyncCallback);

	void testTablePattern(BatchClassDTO batchClassDTO, TableInfoDTO tableInfoDTO, AsyncCallback<List<TestTableResultDTO>> asyncCallback);

	void getImportBatchClassUIConfig(String workflowName, String zipSourcePath,
			AsyncCallback<ImportBatchClassSuperConfig> asyncCallback);

	void importBatchClass(ImportBatchClassUserOptionDTO userOptions, AsyncCallback<Boolean> asyncCallback);

	void getAllBatchClassesIncludingDeleted(AsyncCallback<List<BatchClassDTO>> asyncCallback);

	void deleteAttachedFolders(final String zipFileName, AsyncCallback<Void> asyncCallback);

	void getAllPrimaryKeysForTable(String driverName, String url, String userName, String password, String table, String tableType,
			AsyncCallback<List<String>> callback);

	void getBatchClassRowCount(AsyncCallback<String> callback);

	void isUserSuperAdmin(AsyncCallback<Boolean> callback);

	void getAllRolesOfUser(AsyncCallback<Set<String>> callback);

	void getAllPluginDetailDTOs(AsyncCallback<List<PluginDetailsDTO>> callback);

	void createAndDeployWorkflowJPDL(String workflowName, BatchClassDTO batchClassDTO, AsyncCallback<BatchClassDTO> callback);

	void isWorkflowContentEqual(ImportBatchClassUserOptionDTO userOptions, String workflowName,
			AsyncCallback<java.util.Map<String, Boolean>> asyncCallback);

	void testAdvancedKVExtraction(BatchClassDTO batchClassDTO, KVExtractionDTO kvExtractionDTO, String docName, String imageName,
			AsyncCallback<List<OutputDataCarrierDTO>> callback);

	void getUpdatedTestFileName(String batchClassIdentifier, String docName, String fileName, AsyncCallback<String> asyncCallback);

	void getSamplePatterns(AsyncCallback<SamplePatternDTO> callback);

	void getSpanList(String batchClassId, String docName, String hocrFileName, AsyncCallback<List<Span>> callback);

	void getAdvancedTEImageUploadPath(String batchClassId, String documentName, String imageName, AsyncCallback<String> asyncCallback);

	void getAllModules(AsyncCallback<List<ModuleDTO>> callback);

	void createNewModule(ModuleDTO moduleDTO, AsyncCallback<ModuleDTO> callback);

	void getAllPluginsNames(AsyncCallback<List<String>> callback);

	void validateEmailConfig(EmailConfigurationDTO emailConfigDTO, AsyncCallback<Boolean> asyncCallback);

	void getAllUnFinishedBatchInstancesCount(String batchClassIdentifier, AsyncCallback<Integer> callback);
	
	void checkCmisConnection(Collection<BatchClassPluginConfigDTO> pluginConfigDTOValues, AsyncCallback<Map<String, String>> callback);
	
	void getCmisConfiguration(Collection<BatchClassPluginConfigDTO> pluginConfigDTOValues, AsyncCallback<Map<String, String>> callback);
	
	void getAuthenticationURL(Collection<BatchClassPluginConfigDTO> pluginConfigDTOValues, AsyncCallback<String> callback);
	
	void getBoxProperties(AsyncCallback<Map<String, String>> asyncCallback);

	void getNewTicket(String APIKey, AsyncCallback<String> asyncCallback);

	void authenticateBox(AsyncCallback<Boolean> asyncCallback);

	void getAuthenticationToken(String APIKey, String ticket, AsyncCallback<String> asyncCallback);

}
